package dev.smd.estela.backend.dao;

import static dev.smd.estela.backend.config.Config.DB_DRIVER;
import static dev.smd.estela.backend.config.Config.DB_PASSWORD;
import static dev.smd.estela.backend.config.Config.DB_URL;
import static dev.smd.estela.backend.config.Config.DB_USER;
import dev.smd.estela.backend.dto.sale.SalesReportDTO;
import dev.smd.estela.backend.model.Sale;
import java.math.BigDecimal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {

    public List<Sale> findAll() {
        List<Sale> resultado = new ArrayList<>();
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT sale_id, data_sale, user_id FROM sales");
            while (resultSet.next()) {
                Sale sale = new Sale();
                sale.setSaleId(resultSet.getLong("sale_id"));
                sale.setDataSale(resultSet.getTimestamp("data_sale"));
                sale.setUserId(resultSet.getLong("user_id"));
                resultado.add(sale);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return resultado;
    }

    public Sale getById(Long saleId) {
        Sale sale = null;
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT sale_id, data_sale, user_id FROM sales WHERE sale_id = ?");
            preparedStatement.setLong(1, saleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sale = new Sale();
                sale.setSaleId(resultSet.getLong("sale_id"));
                sale.setDataSale(resultSet.getTimestamp("data_sale"));
                sale.setUserId(resultSet.getLong("user_id"));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return sale;
    }

    public ArrayList<Sale> findByUserId(Long userId) {
        ArrayList<Sale> resultado = new ArrayList<>();
        try {
            Class.forName(DB_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement("SELECT sale_id, data_sale, user_id FROM sales WHERE user_id = ?")) {
                preparedStatement.setLong(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Sale sale = new Sale();
                        sale.setSaleId(resultSet.getLong("sale_id"));
                        sale.setDataSale(resultSet.getTimestamp("data_sale"));
                        sale.setUserId(resultSet.getLong("user_id"));
                        resultado.add(sale);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return resultado;
    }

    public Sale save(Sale sale) {
        Sale newSale = new Sale();
        String sql = "INSERT INTO sales (user_id) VALUES (?)";
        try {
            Class.forName(DB_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, sale.getUserId());
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 1) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            long idSale = generatedKeys.getLong(1);
                            newSale.setSaleId(idSale);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return newSale;
    }

    public boolean deleteById(Long saleId) {
        boolean isSuccess = false;
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM sales WHERE sale_id = ?");
            preparedStatement.setLong(1, saleId);
            isSuccess = (preparedStatement.executeUpdate() == 1);
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return isSuccess;
    }

    public List<SalesReportDTO> getSalesReport(String name, String start, String end) {
        List<SalesReportDTO> listOfSalesReport = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.user_id AS user_id, u.name AS user_name, s.sale_id, s.data_sale, SUM(g.price) AS total_price ");
        sql.append("FROM users u ");
        sql.append("JOIN sales s ON u.user_id = s.user_id ");
        sql.append("JOIN sales_games sg ON s.sale_id = sg.sale_id ");
        sql.append("JOIN games g ON sg.game_id = g.game_id ");
        sql.append("WHERE 1=1 ");

        if (name != null && !name.isEmpty()) {
            sql.append("AND LOWER(u.name) LIKE ? ");
        }
        if (start != null && !start.isEmpty()) {
            sql.append("AND s.data_sale >= ?::timestamp ");
        }
        if (end != null && !end.isEmpty()) {
            sql.append("AND s.data_sale <= ?::timestamp ");
        }

        sql.append("GROUP BY u.user_id, u.name, s.sale_id, s.data_sale ");
        sql.append("ORDER BY s.data_sale DESC");

        try {
            Class.forName(DB_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {

                int index = 1;

                if (name != null && !name.isEmpty()) {
                    preparedStatement.setString(index++, "%" + name.toLowerCase() + "%");
                }
                if (start != null && !start.isEmpty()) {
                    //'yyyy-MM-dd HH:mm:ss'
                    preparedStatement.setString(index++, start);
                }
                if (end != null && !end.isEmpty()) {
                    preparedStatement.setString(index++, end);
                }

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        SalesReportDTO salesReportDTO = new SalesReportDTO();
                        salesReportDTO.setSaleId(resultSet.getLong("sale_id"));
                        salesReportDTO.setUserId(resultSet.getLong("user_id"));
                        salesReportDTO.setUserName(resultSet.getString("user_name"));
                        java.sql.Timestamp ts = resultSet.getTimestamp("data_sale");
                        if (ts != null) {
                            salesReportDTO.setDataSale(ts.toLocalDateTime());
                        }
                        salesReportDTO.setTotalPrice(resultSet.getBigDecimal("total_price"));
                        listOfSalesReport.add(salesReportDTO);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Erro ao gerar relatório: " + ex.getMessage());
            return new ArrayList<>();
        }
        return listOfSalesReport;
    }
}
