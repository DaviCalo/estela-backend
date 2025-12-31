package dev.smd.estela.backend.dao;

import static dev.smd.estela.backend.config.Config.DB_DRIVER;
import static dev.smd.estela.backend.config.Config.DB_PASSWORD;
import static dev.smd.estela.backend.config.Config.DB_URL;
import static dev.smd.estela.backend.config.Config.DB_USER;
import dev.smd.estela.backend.model.CategoryGame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesGamesDAO {

    public void linkGameToCategory(Long gameId, Long categoryId) {
        String sql = "INSERT INTO categories_games (game_id, category_id) VALUES (?, ?)";
        try {
            Class.forName(DB_DRIVER);
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, gameId);
                ps.setLong(2, categoryId);
                ps.executeUpdate();
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Erro ao vincular categoria no DAO: " + ex.getMessage());
        }
    }

    public boolean delete(Long categorysId, Long gameId) {
        boolean isSuccess = false;
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM categories_games WHERE category_id = ? AND game_id = ?");
            preparedStatement.setLong(1, categorysId);
            preparedStatement.setLong(2, gameId);
            isSuccess = (preparedStatement.executeUpdate() == 1);
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return isSuccess;
    }

    public List<CategoryGame> findByCategorysId(Long categorysId) {
        List<CategoryGame> resultado = new ArrayList<>();
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT category_id, game_id FROM categories_games WHERE category_id = ?");
            preparedStatement.setLong(1, categorysId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CategoryGame item = new CategoryGame();
                item.setCategoryId(resultSet.getLong("category_id"));
                item.setGameId(resultSet.getLong("game_id"));
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
