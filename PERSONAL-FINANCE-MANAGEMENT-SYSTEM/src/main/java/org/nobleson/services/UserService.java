package org.nobleson.services;

import org.nobleson.entities.AppUser;
import org.nobleson.exceptions.ResourceNotFoundException;
import org.nobleson.respositories.UserRepo;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepo userRepo ;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }



    public AppUser addUser(AppUser user) throws SQLException {
        return userRepo.addUser(user);
    }

    public AppUser getUserByID(Long userID) throws SQLException {
        return userRepo.getUserByID(userID).orElseThrow((()-> new ResourceNotFoundException(
                "Employee by id " +userID+ "does not exist"
        )));
    }

    public List<AppUser> getAllUsers() throws SQLException, ClassNotFoundException {
        return userRepo.getAllUsers();
    }

    public void deleteUser(Long userID) throws SQLException{
        userRepo.deleteUser(userID);
    }

    public AppUser updateUser(AppUser user) throws SQLException, ClassNotFoundException {
        return userRepo.update(user);
    }
}
