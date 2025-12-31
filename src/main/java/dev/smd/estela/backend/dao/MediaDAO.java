package dev.smd.estela.backend.dao;

import static dev.smd.estela.backend.config.Config.DB_DRIVER;
import static dev.smd.estela.backend.config.Config.DB_PASSWORD;
import static dev.smd.estela.backend.config.Config.DB_URL;
import static dev.smd.estela.backend.config.Config.DB_USER;
import dev.smd.estela.backend.model.Media;
import dev.smd.estela.backend.model.MediaType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MediaDAO {

    public Boolean save(Media newMedia) {
        boolean isSuccess = false;
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO media (url, media_type, game_id) VALUES (?, ?, ?)");
            preparedStatement.setString(1, newMedia.getUrl());
            preparedStatement.setString(2, newMedia.getMediaType().name());
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
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM media WHERE id = ?");
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

    public boolean deleteByGameIdAndType(Long gameId, MediaType mediaType) {
        boolean isSuccess = false;
        String sql = "DELETE FROM media WHERE game_id = ? AND media_type = ?";
        try {
            Class.forName(DB_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, gameId);
                ps.setString(2, mediaType.name());
                isSuccess = (ps.executeUpdate() > 0);
            }
        } catch (Exception ex) {
            System.out.println("Erro ao deletar midia antiga: " + ex.getMessage());
        }
        return isSuccess;
    }

    public List<Media> findByGameId(Long gameId) {
        List<Media> resultado = new ArrayList<>();
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT media_id, url, media_type, game_id FROM media WHERE game_id = ?");
            preparedStatement.setLong(1, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Media media = new Media();
                media.setId(resultSet.getLong("media_id"));
                media.setUrl(resultSet.getString("url"));
                MediaType type = MediaType.valueOf(resultSet.getString("media_type"));
                media.setMediaType(type);
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
