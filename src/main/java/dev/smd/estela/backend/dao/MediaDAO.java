package dev.smd.estela.backend.dao;

import static dev.smd.estela.backend.config.Config.DB_DRIVER;
import static dev.smd.estela.backend.config.Config.DB_PASSWORD;
import static dev.smd.estela.backend.config.Config.DB_URL;
import static dev.smd.estela.backend.config.Config.DB_USER;
import dev.smd.estela.backend.model.Media;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MediaDAO {

    public Boolean save(Media newMedia) {
        boolean isSuccess = false;
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO medias (url, media_type, game_id) VALUES (?, ?, ?)");
            preparedStatement.setString(1, newMedia.getUrl());
            preparedStatement.setString(2, newMedia.getMediaType());
            preparedStatement.setLong(3, newMedia.getGameId());
            isSuccess = (preparedStatement.executeUpdate() == 1);
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return isSuccess;
        }
        return isSuccess;
    }

    public boolean deleteById(Long mediaId) {
        boolean isSuccess = false;
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM medias WHERE id = ?");
            preparedStatement.setLong(1, mediaId);
            isSuccess = (preparedStatement.executeUpdate() == 1);
            preparedStatement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return isSuccess;
    }

    public List<Media> findByGameId(Long gameId) {
        List<Media> resultado = new ArrayList<>();
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, url, media_type, game_id FROM medias WHERE game_id = ?");
            preparedStatement.setLong(1, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Media media = new Media();
                media.setId(resultSet.getLong("id"));
                media.setUrl(resultSet.getString("url"));
                media.setMediaType(resultSet.getString("media_type"));
                media.setGameId(resultSet.getLong("game_id"));
                resultado.add(media);
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