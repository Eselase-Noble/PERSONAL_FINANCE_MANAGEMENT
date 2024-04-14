package org.nobleson.respositories;

import org.nobleson.connection.DatabaseConnection;
import org.nobleson.entities.AppUser;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


//This class handles all the NECESSARY QUERIES
public class UserRepo implements UserRepository{

private Connection connection = DatabaseConnection.getConnection();

public UserRepo (Connection connection) throws SQLException, ClassNotFoundException {
    this.connection = connection;
}

    @Override
    public AppUser addUser(AppUser appUser) throws SQLException {
        String query = "INSERT INTO users (surname,othername ,username,  email, created_at, password,) VALUES (?, ?, ?, ?, ?, ?)";
        try(PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query)){
            statement.setString(1, appUser.getSurname());
            statement.setString(2, appUser.getOtherName());
            statement.setString(3, appUser.getUsername());
            statement.setString(4, appUser.getEmail());
            statement.setDate(5, appUser.getCreatedAt());
            statement.setString(6, appUser.getPassword());
            statement.executeUpdate();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return appUser;
    }

    @Override
    public List<AppUser> getAllUsers() throws SQLException, ClassNotFoundException {

    List<AppUser> users = new ArrayList<>();
    String query = "SELECT * FROM Users";
    try(PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query)){

        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            AppUser user = new AppUser();
            user.setSurname(resultSet.getString("surname"));
            user.setOtherName(resultSet.getString("otherName"));
            user.setUsername(resultSet.getString("username"));
            user.setEmail(resultSet.getString("email"));
            user.setCreatedAt(resultSet.getDate("created_at"));
            user.setPassword(resultSet.getString("password"));
            users.add(user);

        }
    }
        return users;
    }

    @Override
    public Optional <AppUser> getUserByID(Long userID) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query)) {
            statement.setLong(1, userID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                AppUser user = new AppUser();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setCreatedAt(resultSet.getDate("createdAt"));
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(Long userID) throws SQLException {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query)) {
            statement.setLong(1, userID);
            statement.executeUpdate();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AppUser update(AppUser appUser) throws SQLException, ClassNotFoundException {
        String query = "UPDATE users SET username = ?, password = ?, email = ? WHERE user_id = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query)) {
            statement.setString(1, appUser.getUsername());
            statement.setString(2, appUser.getPassword());
            statement.setString(3, appUser.getEmail());
            statement.setLong(4, appUser.getUserID());
            statement.executeUpdate();
        }

        return appUser;
    }
}
