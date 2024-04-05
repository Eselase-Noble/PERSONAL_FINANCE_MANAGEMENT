package org.nobleson.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.nobleson.entities.AppUser;
import org.nobleson.services.UserService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserResource implements HttpHandler {
   private final UserService userService;


    public UserResource(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();


        switch (method) {
            case "GET":
                try {
                    getAllUsers(exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "POST":
                try {
                    addUser(exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "PUT":
                try {
                    updateUser(exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "DELETE":
                try {
                    handleDeleteRequest(exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                handleMethodNotAllowed(exchange);
                break;
        }


    }

    private List<AppUser> getAllUsers(HttpExchange exchange) throws IOException, SQLException {
    String path = exchange.getRequestURI().getPath();
    if(path.equals("/api/user")){

        List<AppUser> appUsers = userService.getAllUsers();
        String response = appUsers.stream()
                .map(AppUser::toString)
                .collect(Collectors.joining("\n"));
        sendResponse(exchange, 200, response);
        return appUsers;
    }
    else {

        return null;
    }

    }

    private AppUser getUserByID(HttpExchange exchange, Long userID) throws SQLException, IOException {
        AppUser user = userService.getUserByID(userID);
        if (user != null){

        sendResponse(exchange, 200, user.toString());
        return user;
        }
        else {
            sendResponse(exchange, 404, "User not found");
            return null;
        }

    }

    private AppUser addUser(HttpExchange exchange) throws IOException, SQLException {
    ObjectMapper mapper = new ObjectMapper();
    InputStream requestBody = exchange.getRequestBody();
    AppUser user = mapper.readValue(requestBody, AppUser.class);

    AppUser newUser = userService.addUser(user);

    String response = "User created with ID: " + newUser.getUserID();

    sendResponse(exchange, 201, response);
    return newUser;

    }


    private AppUser updateUser(HttpExchange exchange) throws IOException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream requestBody = exchange.getRequestBody();
        AppUser user = mapper.readValue(requestBody, AppUser.class);

        AppUser updateUser = userService.getUserByID(user.getUserID());
        if (updateUser != null){
            AppUser appUser = userService.updateUser(updateUser);
            sendResponse(exchange, 200, "User updated: " + appUser.toString());
            return appUser;
        }
        else {
            sendResponse(exchange, 404, "User not found");
            return null;

        }

    }


    private void handleDeleteRequest(HttpExchange exchange) throws IOException, SQLException {
        String idStr = exchange.getRequestURI().getPath().split("/")[3];
        Long id = Long.parseLong(idStr);
        userService.deleteUser(id);
        sendResponse(exchange, 200, "User deleted");
    }



    private void handleMethodNotAllowed(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 405, "Method not allowed");
    }
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
