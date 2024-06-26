package org.nobleson.respositories;

import org.nobleson.entities.AppUser;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

AppUser addUser(AppUser appUser) throws SQLException;
List<AppUser> getAllUsers() throws SQLException, ClassNotFoundException;

Optional<AppUser> getUserByID(Long userID) throws SQLException;

void deleteUser(Long userID) throws SQLException;

AppUser update(AppUser appUser) throws SQLException, ClassNotFoundException;



}
