package dev.smd.estela.backend.dao;

import static dev.smd.estela.backend.config.Config.DB_DRIVER;
import static dev.smd.estela.backend.config.Config.DB_PASSWORD;
import static dev.smd.estela.backend.config.Config.DB_URL;
import static dev.smd.estela.backend.config.Config.DB_USER;
import dev.smd.estela.backend.model.Sale;

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
    
    public List<Sale> findByUserId(Long userId) {
        List<Sale> resultado = new ArrayList<>();
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT sale_id, data_sale, user_id FROM sales WHERE user_id = ?");
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Sale sale = new Sale();
                sale.setSaleId(resultSet.getLong("sale_id"));
                sale.setDataSale(resultSet.getTimestamp("data_sale"));
                sale.setUserId(resultSet.getLong("user_id"));
                resultado.add(sale);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return resultado;
    }

    public Boolean save(Sale newSale) {
        boolean isSuccess = false;
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO sales (data_sale, user_id) VALUES (?, ?)");
            preparedStatement.setTimestamp(1, newSale.getDataSale());
            preparedStatement.setLong(2, newSale.getUserId());

            isSuccess = (preparedStatement.executeUpdate() == 1);
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return isSuccess;
        }
        return isSuccess;
    }

    // Não é comum atualizar uma venda, mas o delete pode ser útil.
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
}