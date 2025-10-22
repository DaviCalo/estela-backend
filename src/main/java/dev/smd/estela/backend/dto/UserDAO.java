package dev.smd.estela.backend.dto;

import static dev.smd.estela.backend.config.Config.DB_DRIVER;
import static dev.smd.estela.backend.config.Config.DB_PASSWORD;
import static dev.smd.estela.backend.config.Config.DB_URL;
import static dev.smd.estela.backend.config.Config.DB_USER;
import dev.smd.estela.backend.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

          public List<User> findAll() {
                    List<User> resultado = new ArrayList<>();
                    try {
                              Class.forName(DB_DRIVER);
                              Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                              Statement statement = connection.createStatement();
                              ResultSet resultSet = statement.executeQuery("SELECT * FROM games");
                              while (resultSet.next()) {
                                        User user = new User();

                                        resultado.add(user);
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

          public User getById(int userId) {
                    User user = null;
                    try {
                              Class.forName(DB_DRIVER);
                              Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                              PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM games WHERE game_id = ?");
                              preparedStatement.setInt(1, userId);
                              ResultSet resultSet = preparedStatement.executeQuery();
                              while (resultSet.next()) {
                                        user = new User();
                              }
                              resultSet.close();
                              preparedStatement.close();
                              connection.close();
                    } catch (ClassNotFoundException | SQLException ex) {
                              System.out.println(ex.getMessage());
                              return null;
                    }
                    return user;
          }

          public Boolean save(User newUser) {
                    boolean isSuccess = false;
                    try {
                              Class.forName(DB_DRIVER);
                              Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                              PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (name, email, password, administrator, nickname) VALUES (?, ?, ?, ?, ?)");
                              preparedStatement.setString(1, newUser.getName());
                              preparedStatement.setString(2, newUser.getUsername());
                              preparedStatement.setString(3, newUser.getPassword());
                              preparedStatement.setBoolean(4, newUser.isAdministrator());
                              preparedStatement.setString(5, newUser.getNickname());

                              isSuccess = (preparedStatement.executeUpdate() == 1);
                              preparedStatement.close();
                              connection.close();
                    } catch (ClassNotFoundException | SQLException ex) {
                              System.out.println(ex.getMessage());
                              return false;
                    }
                    return isSuccess;
          }

          public boolean update(User newUser) {
                    boolean isSuccess = false;
                    try {
                              Class.forName(DB_DRIVER);
                              Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                              PreparedStatement preparedStatement = connection.prepareStatement("UPDATE games SET price = ?, name = ?, characteristics = ?, description = ?, hard_drive_space = ?, graphics_card = ?, memory = ?, operating_system = ?, processor = ? WHERE game_id = ?");
                              preparedStatement.setString(1, newUser.getName());
                              preparedStatement.setString(2, newUser.getUsername());
                              preparedStatement.setString(3, newUser.getPassword());
                              preparedStatement.setBoolean(4, newUser.isAdministrator());
                              preparedStatement.setString(5, newUser.getNickname());
                              isSuccess = (preparedStatement.executeUpdate() == 1);
                              preparedStatement.close();
                              connection.close();
                    } catch (ClassNotFoundException | SQLException ex) {
                              System.out.println(ex.getMessage());
                              return false;
                    }
                    return isSuccess;
          }

          public boolean deleteById(int gameId) {
                    boolean isSuccess = false;
                    try {
                              Class.forName(DB_DRIVER);
                              Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                              PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM games WHERE game_id = ?");
                              preparedStatement.setInt(1, gameId);
                              isSuccess = (preparedStatement.executeUpdate() == 1);
                              preparedStatement.close();
                              connection.close();
                    } catch (ClassNotFoundException | SQLException ex) {
                              System.out.println(ex.getMessage());
                              return false;
                    }
                    return isSuccess;
          }

          public User login(String userName, String password) {
                    User user = null;
                    try {
                              Class.forName(DB_DRIVER);
                              Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                              PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email =? AND password =?");
                              preparedStatement.setString(1, userName);
                              preparedStatement.setString(2, password);
                              ResultSet resultSet = preparedStatement.executeQuery();
                              while (resultSet.next()) {
                                        user = new User();
                                        user.setUsername(resultSet.getString("email"));
                                        user.setPassword(resultSet.getString("password"));
                                        user.setId(resultSet.getInt("user_id"));
                              }
                              resultSet.close();
                              preparedStatement.close();
                              connection.close();
                    } catch (ClassNotFoundException | SQLException ex) {
                              System.out.println(ex.getMessage());
                              return null;
                    }
                    System.out.println(user.toString());
                    return user;
          }

          public User findByUsername(String username) {
                    User user = null;
                    try {
                              Class.forName(DB_DRIVER);
                              Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                              PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM games WHERE email = ?");
                              preparedStatement.setString(1, username);
                              ResultSet resultSet = preparedStatement.executeQuery();
                              while (resultSet.next()) {
                                        user = new User();
                                        user.setId(resultSet.getLong("user_id"));
                              }
                              resultSet.close();
                              preparedStatement.close();
                              connection.close();
                    } catch (ClassNotFoundException | SQLException ex) {
                              System.out.println(ex.getMessage());
                              return null;
                    }
                    return user;
          }
}
