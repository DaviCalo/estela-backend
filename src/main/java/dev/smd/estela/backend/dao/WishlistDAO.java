package dev.smd.estela.backend.dao;

import static dev.smd.estela.backend.config.Config.DB_DRIVER;
import static dev.smd.estela.backend.config.Config.DB_PASSWORD;
import static dev.smd.estela.backend.config.Config.DB_URL;
import static dev.smd.estela.backend.config.Config.DB_USER;
import dev.smd.estela.backend.model.Wishlist; 

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {

    public Boolean save(Wishlist item) {
        boolean isSuccess = false;
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO wishlist (user_id, game_id) VALUES (?, ?)");
            preparedStatement.setLong(1, item.getUserId());
            preparedStatement.setLong(2, item.getGameId());

            isSuccess = (preparedStatement.executeUpdate() == 1);
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return isSuccess;
        }
        return isSuccess;
    }

    public boolean delete(Long userId, Long gameId) {
        boolean isSuccess = false;
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM wishlist WHERE user_id = ? AND game_id = ?");
            preparedStatement.setLong(1, userId);
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

    public List<Wishlist> findByUserId(Long userId) {
        List<Wishlist> resultado = new ArrayList<>();
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id, game_id FROM wishlist WHERE user_id = ?");
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Wishlist item = new Wishlist();
                item.setUserId(resultSet.getLong("user_id"));
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