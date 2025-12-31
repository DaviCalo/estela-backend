package dev.smd.estela.backend.dao;

import static dev.smd.estela.backend.config.Config.DB_DRIVER;
import static dev.smd.estela.backend.config.Config.DB_PASSWORD;
import static dev.smd.estela.backend.config.Config.DB_URL;
import static dev.smd.estela.backend.config.Config.DB_USER;
import dev.smd.estela.backend.dto.game.ReponseGameDetailsDTO;
import dev.smd.estela.backend.dto.game.ReponseGamePurchasedDTO;
import dev.smd.estela.backend.model.Game;
import java.math.BigDecimal;

import java.sql.*;
import java.time.LocalDateTime;
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
                    + "LEFT JOIN media m "
                    + "ON g.game_id = m.game_id AND m.media_type = 'COVER'";
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

    public Game getByIdWithIconAndCover(Long gameId) {
        Game game = new Game();
        String sql = "SELECT g.game_id, g.price, g.name, g.CreatedAt, "
                + "m_cover.url AS cover_url, m_icon.url AS icon_url "
                + "FROM games g "
                + "LEFT JOIN media m_cover ON g.game_id = m_cover.game_id AND m_cover.media_type = 'COVER' "
                + "LEFT JOIN media m_icon ON g.game_id = m_icon.game_id AND m_icon.media_type = 'ICON' "
                + "WHERE g.game_id = ?";
        try {
            Class.forName(DB_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, gameId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        game.setGameId(resultSet.getLong(1));
                        game.setPrice(resultSet.getBigDecimal(2));
                        game.setName(resultSet.getString(3));
                        game.setCreatedAt(resultSet.getObject(4, java.time.LocalDateTime.class));
                        game.setUrlCover(resultSet.getString(5));
                        game.setUrlIcon(resultSet.getString(6));
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return game;
    }

    public List<Game> findAllGamesDetails() {
        List<Game> resultado = new ArrayList<>();
        String sql = "SELECT g.game_id, g.price, g.name, g.description, g.createdAt, COUNT(sg.sale_id) AS quantidade_vendas "
                + "FROM games g "
                + "LEFT JOIN sales_games sg ON g.game_id = sg.game_id "
                + "GROUP BY g.game_id, g.price, g.name, g.createdAt "
                + "ORDER BY quantidade_vendas DESC";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Game game = new Game();
                game.setGameId(resultSet.getLong("game_id"));
                game.setPrice(resultSet.getBigDecimal("price"));
                game.setDescription(resultSet.getString("description"));
                game.setName(resultSet.getString("name"));
                game.setCreatedAt(resultSet.getObject("createdAt", java.time.LocalDateTime.class));
                game.setSold(resultSet.getInt("quantidade_vendas"));
                resultado.add(game);
            }

        } catch (SQLException ex) {
            System.out.println("Erro ao buscar jogos: " + ex.getMessage());
            return new ArrayList<>();
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

    public Game getByName(String gameName) {
        Game game = null;
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT game_id, price, name, characteristics, description, hard_drive_space, graphics_card, memory, operating_system, processor FROM games WHERE name = ?");
            preparedStatement.setString(1, gameName);
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
        String sql = "UPDATE games SET price = ?, name = ?, characteristics = ?, description = ?, "
                + "hard_drive_space = ?, graphics_card = ?, memory = ?, operating_system = ?, processor = ? "
                + "WHERE game_id = ?";
        try {
            Class.forName(DB_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setBigDecimal(1, game.getPrice());
                ps.setString(2, game.getName());
                ps.setString(3, game.getCharacteristics());
                ps.setString(4, game.getDescription());
                ps.setString(5, game.getHardDriveSpace());
                ps.setString(6, game.getGraphicsCard());
                ps.setString(7, game.getMemory());
                ps.setString(8, game.getOperatingSystem());
                ps.setString(9, game.getProcessor());
                ps.setLong(10, game.getGameId());

                return ps.executeUpdate() == 1;
            }
        } catch (Exception ex) {
            System.out.println("Erro update game: " + ex.getMessage());
            return false;
        }
    }

    public void clearCategories(Long gameId) {
        try {
            Class.forName(DB_DRIVER);
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement ps = conn.prepareStatement("DELETE FROM categories_games WHERE game_id = ?")) {
                ps.setLong(1, gameId);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("Erro limpar categorias: " + ex.getMessage());
        }
    }

    public void addCategory(Long gameId, Long categoryId) {
        try {
            Class.forName(DB_DRIVER);
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement ps = conn.prepareStatement("INSERT INTO categories_games (game_id, category_id) VALUES (?, ?)")) {
                ps.setLong(1, gameId);
                ps.setLong(2, categoryId);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("Erro add categoria: " + ex.getMessage());
        }
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

    public ReponseGameDetailsDTO getGameDetailsById(Long gameId) {
        ReponseGameDetailsDTO gameDetails = null;

        String sql = "SELECT g.game_id, g.name, g.price, g.description, g.characteristics, "
                + "g.createdAt, g.hard_drive_space, g.graphics_card, g.memory, "
                + "g.operating_system, g.processor, "
                + "m_cover.url AS url_cover, m_icon.url AS url_icon "
                + "FROM games g "
                + "LEFT JOIN media m_cover ON g.game_id = m_cover.game_id AND m_cover.media_type = 'COVER' "
                + "LEFT JOIN media m_icon ON g.game_id = m_icon.game_id AND m_icon.media_type = 'ICON' "
                + "WHERE g.game_id = ?";

        try {
            Class.forName(DB_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                preparedStatement.setLong(1, gameId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Long id = resultSet.getLong("game_id");
                        String name = resultSet.getString("name");
                        BigDecimal price = resultSet.getBigDecimal("price");
                        String description = resultSet.getString("description");
                        String characteristics = resultSet.getString("characteristics");
                        LocalDateTime createdAt = resultSet.getObject("createdAt", LocalDateTime.class);
                        String hardDriveSpace = resultSet.getString("hard_drive_space");
                        String graphicsCard = resultSet.getString("graphics_card");
                        String memory = resultSet.getString("memory");
                        String operatingSystem = resultSet.getString("operating_system");
                        String processor = resultSet.getString("processor");
                        String urlCover = resultSet.getString("url_cover");
                        String urlIcon = resultSet.getString("url_icon");

                        List<Long> categoryIds = new ArrayList<>();
                        String sqlCategories = "SELECT category_id FROM categories_games WHERE game_id = ?";

                        try (PreparedStatement psCats = connection.prepareStatement(sqlCategories)) {
                            psCats.setLong(1, id);
                            try (ResultSet rsCats = psCats.executeQuery()) {
                                while (rsCats.next()) {
                                    categoryIds.add(rsCats.getLong("category_id"));
                                }
                            }
                        }

                        List<String> urlsMedia = new ArrayList<>();
                        String sqlScreenshot = "SELECT url FROM media WHERE game_id = ? and media_type='SCREENSHOT'";

                        try (PreparedStatement psMedia = connection.prepareStatement(sqlScreenshot)) {
                            psMedia.setLong(1, id);
                            try (ResultSet rsMedia = psMedia.executeQuery()) {
                                while (rsMedia.next()) {
                                    urlsMedia.add(rsMedia.getString("url"));
                                }
                            }
                        }

                        gameDetails = new ReponseGameDetailsDTO(
                                id,
                                name,
                                price,
                                description,
                                urlIcon,
                                urlCover,
                                characteristics,
                                createdAt,
                                hardDriveSpace,
                                graphicsCard,
                                memory,
                                operatingSystem,
                                processor,
                                categoryIds,
                                urlsMedia
                        );
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return gameDetails;
    }
}
