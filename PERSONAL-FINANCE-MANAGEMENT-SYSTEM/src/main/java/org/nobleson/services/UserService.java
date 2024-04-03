package org.nobleson.services;

import org.nobleson.entities.AppUser;
import org.nobleson.respositories.UserRepo;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepo userRepo ;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }



    public void addUser(AppUser user) throws SQLException {
        userRepo.addUser(user);
    }

    public Optional<AppUser> getUserByID(Long userID) throws SQLException {
        return userRepo.getUserByID(userID);
    }

    public List<AppUser> getAllUsers() throws SQLException{
        return userRepo.getAllUsers();
    }

    public void deleteUser(Long userID) throws SQLException{
        userRepo.deleteUser(userID);
    }

    public void updateUser(AppUser user) throws SQLException{
        userRepo.update(user);
    }
}
