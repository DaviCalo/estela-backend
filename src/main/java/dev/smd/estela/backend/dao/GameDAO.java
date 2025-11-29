package dev.smd.estela.backend.dao;

import static dev.smd.estela.backend.config.Config.DB_DRIVER;
import static dev.smd.estela.backend.config.Config.DB_PASSWORD;
import static dev.smd.estela.backend.config.Config.DB_URL;
import static dev.smd.estela.backend.config.Config.DB_USER;
import dev.smd.estela.backend.model.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

        public List<Game> findAll() {
                List<Game> resultado = new ArrayList<>();
                try {
                        Class.forName(DB_DRIVER);
                        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT game_id, price, name, characteristics, description, hard_drive_space, graphics_card, memory, operating_system, processor FROM games");
                        while (resultSet.next()) {
                                Game game = new Game();
                                game.setGameId(resultSet.getLong("game_id"));
                                game.setPrice(resultSet.getBigDecimal("price"));
                                game.setName(resultSet.getString("name"));
                                game.setCharacteristics(resultSet.getString("characteristics"));
                                game.setDescription(resultSet.getString("description"));
                                game.setHardDriveSpace(resultSet.getString("hard_drive_space"));
                                game.setGraphicsCard(resultSet.getString("graphics_card"));
                                game.setMemory(resultSet.getString("memory"));
                                game.setOperatingSystem(resultSet.getString("operating_system"));
                                game.setProcessor(resultSet.getString("processor"));
                                resultado.add(game);
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

        public List<Game> findAllGamesWithCover() {
                List<Game> resultado = new ArrayList<>();
                try {
                        Class.forName(DB_DRIVER);
                        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        Statement statement = connection.createStatement();
                        String sql = "SELECT g.game_id, g.price, g.name, m.url, g.CreatedAt "
                                  + "FROM games g "
                                  + "LEFT JOIN medias m "
                                  + "ON g.game_id = m.game_id AND m.media_type = 'cover'";
                        ResultSet resultSet = statement.executeQuery(sql);
                        while (resultSet.next()) {
                                Game game = new Game();
                                game.setGameId(resultSet.getLong(1));
                                game.setPrice(resultSet.getBigDecimal(2));
                                game.setName(resultSet.getString(3));
                                game.setUrlCover(resultSet.getString(4));
                               game.setCreatedAt(resultSet.getObject(5, java.time.LocalDateTime.class));
                                resultado.add(game);
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

        public Game getById(Long gameId) {
                Game game = null;
                try {
                        Class.forName(DB_DRIVER);
                        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        PreparedStatement preparedStatement = connection.prepareStatement("SELECT game_id, price, name, characteristics, description, hard_drive_space, graphics_card, memory, operating_system, processor FROM games WHERE game_id = ?");
                        preparedStatement.setLong(1, gameId);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                                game = new Game();
                                game.setGameId(resultSet.getLong("game_id"));
                                game.setPrice(resultSet.getBigDecimal("price"));
                                game.setName(resultSet.getString("name"));
                                game.setCharacteristics(resultSet.getString("characteristics"));
                                game.setDescription(resultSet.getString("description"));
                                game.setHardDriveSpace(resultSet.getString("hard_drive_space"));
                                game.setGraphicsCard(resultSet.getString("graphics_card"));
                                game.setMemory(resultSet.getString("memory"));
                                game.setOperatingSystem(resultSet.getString("operating_system"));
                                game.setProcessor(resultSet.getString("processor"));
                        }
                        resultSet.close();
                        preparedStatement.close();
                        connection.close();
                } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        return null;
                }
                return game;
        }

        public Boolean save(Game newGame) {
                boolean isSuccess = false;
                try {
                        Class.forName(DB_DRIVER);
                        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO games (price, name, characteristics, description, hard_drive_space, graphics_card, memory, operating_system, processor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        preparedStatement.setBigDecimal(1, newGame.getPrice());
                        preparedStatement.setString(2, newGame.getName());
                        preparedStatement.setString(3, newGame.getCharacteristics());
                        preparedStatement.setString(4, newGame.getDescription());
                        preparedStatement.setString(5, newGame.getHardDriveSpace());
                        preparedStatement.setString(6, newGame.getGraphicsCard());
                        preparedStatement.setString(7, newGame.getMemory());
                        preparedStatement.setString(8, newGame.getOperatingSystem());
                        preparedStatement.setString(9, newGame.getProcessor());

                        isSuccess = (preparedStatement.executeUpdate() == 1);
                        preparedStatement.close();
                        connection.close();
                } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        return isSuccess;
                }
                return isSuccess;
        }

        public boolean update(Game game) {
                boolean isSuccess = false;
                try {
                        Class.forName(DB_DRIVER);
                        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE games SET price = ?, name = ?, characteristics = ?, description = ?, hard_drive_space = ?, graphics_card = ?, memory = ?, operating_system = ?, processor = ? WHERE game_id = ?");
                        preparedStatement.setBigDecimal(1, game.getPrice());
                        preparedStatement.setString(2, game.getName());
                        preparedStatement.setString(3, game.getCharacteristics());
                        preparedStatement.setString(4, game.getDescription());
                        preparedStatement.setString(5, game.getHardDriveSpace());
                        preparedStatement.setString(6, game.getGraphicsCard());
                        preparedStatement.setString(7, game.getMemory());
                        preparedStatement.setString(8, game.getOperatingSystem());
                        preparedStatement.setString(9, game.getProcessor());
                        preparedStatement.setLong(10, game.getGameId());

                        isSuccess = (preparedStatement.executeUpdate() == 1);
                        preparedStatement.close();
                        connection.close();
                } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println(ex.getMessage());
                        return false;
                }
                return isSuccess;
        }

        public boolean deleteById(Long gameId) {
                boolean isSuccess = false;
                try {
                        Class.forName(DB_DRIVER);
                        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM games WHERE game_id = ?");
                        preparedStatement.setLong(1, gameId);
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
