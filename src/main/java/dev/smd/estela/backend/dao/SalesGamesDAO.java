package dev.smd.estela.backend.dao;

import static dev.smd.estela.backend.config.Config.DB_DRIVER;
import static dev.smd.estela.backend.config.Config.DB_PASSWORD;
import static dev.smd.estela.backend.config.Config.DB_URL;
import static dev.smd.estela.backend.config.Config.DB_USER;
import dev.smd.estela.backend.model.SaleGame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesGamesDAO {

    public Boolean save(SaleGame item) {
        boolean isSuccess = false;
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO sales_games (sale_id, game_id, price) VALUES (?, ?, ?)");
            preparedStatement.setLong(1, item.getSaleId());
            preparedStatement.setLong(2, item.getGameId());
            preparedStatement.setBigDecimal(3, item.getPrice());

            isSuccess = (preparedStatement.executeUpdate() == 1);
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return isSuccess;
        }
        return isSuccess;
    }

    public List<SaleGame> findBySaleId(Long saleId) {
        List<SaleGame> resultado = new ArrayList<>();
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT sale_id, game_id, price FROM sales_games WHERE sale_id = ?");
            preparedStatement.setLong(1, saleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                SaleGame item = new SaleGame();
                item.setSaleId(resultSet.getLong("sale_id"));
                item.setGameId(resultSet.getLong("game_id"));
                item.setPrice(resultSet.getBigDecimal("price"));
                resultado.add(item);
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
}