package org.nobleson.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.nobleson.entities.AppUser;
import org.nobleson.services.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserResource {
    private final  UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }


    private void handlePost(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestHeaders().containsKey("Content-Type") || !exchange.getRequestHeaders().getFirst("Content-Type").equals("application/json")) {
            sendResponse(exchange, 400, "Bad Request: Content-Type header must be application/json");
            return;
        }

        try {
            String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody())).lines().collect(Collectors.joining("\n"));
            ObjectMapper objectMapper = new ObjectMapper();
            AppUser user = objectMapper.readValue(requestBody, AppUser.class);
            userService.addUser(user);
            sendResponse(exchange, 201, "User added successfully");
        } catch (IOException | SQLException e) {
            sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 4 && pathParts[3].equals("users")) {
            try {
                List<AppUser> users = userService.getAllUsers();
                ObjectMapper objectMapper = new ObjectMapper();
                String response = objectMapper.writeValueAsString(users);
                sendResponse(exchange, 200, response);
            } catch (SQLException e) {
                sendResponse(exchange, 500, "Internal Server Error");
            }
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestHeaders().containsKey("Content-Type") || !exchange.getRequestHeaders().getFirst("Content-Type").equals("application/json")) {
            sendResponse(exchange, 400, "Bad Request: Content-Type header must be application/json");
            return;
        }

        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 5 && pathParts[3].equals("users")) {
            try {
                Long userId = Long.parseLong(pathParts[4]);
                String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody())).lines().collect(Collectors.joining("\n"));
                ObjectMapper objectMapper = new ObjectMapper();
                AppUser user = objectMapper.readValue(requestBody, AppUser.class);
                user.setUserID(userId);
                userService.updateUser(user);
                sendResponse(exchange, 200, "User updated successfully");
            } catch (IOException | SQLException e) {
                sendResponse(exchange, 500, "Internal Server Error");
            }
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 5 && pathParts[3].equals("users")) {
            try {
                Long userId = Long.parseLong(pathParts[4]);
                userService.deleteUser(userId);
                sendResponse(exchange, 200, "User deleted successfully");
            } catch (NumberFormatException | SQLException e) {
                sendResponse(exchange, 500, "Internal Server Error");
            }
        } else {
            sendResponse(exchange, 404, "Not Found");
        }
    }

}
