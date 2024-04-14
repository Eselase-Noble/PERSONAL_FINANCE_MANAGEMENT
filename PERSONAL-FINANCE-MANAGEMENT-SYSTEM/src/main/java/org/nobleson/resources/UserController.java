package org.nobleson.resources;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.nobleson.connection.DatabaseConnection;
import org.nobleson.entities.AppUser;
import org.nobleson.exceptions.ResourceNotFoundException;
import org.nobleson.respositories.UserRepo;
import org.nobleson.respositories.UserRepository;
import org.nobleson.services.UserService;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void startServer() {
        try {
      //      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
            Class.forName("com.mysql.cj.jdbc.Driver");
            UserRepo userRepository = new UserRepo(DatabaseConnection.getConnection());
            userService = new UserService(userRepository);
            HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);
            server.createContext("/users", new UserHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
            System.out.println("Server running on port 8090");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private class UserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    handleGet(exchange);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "PUT":
                    handlePut(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange);
                    break;
                default:

                    sendResponse(exchange, 405, "Method Not Allowed");
            }
        }

        private void handleGet(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/users")) {
                try {
                    List<AppUser> users = userService.getAllUsers();
                    sendResponse(exchange, 200, new Gson().toJson(users));
                } catch (SQLException e) {
                    System.out.println("Internal Server Error");
                    sendResponse(exchange, 500, "Internal Server Error");
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (path.matches("/users/\\d+")) {
                String[] parts = path.split("/");
                long id = Long.parseLong(parts[2]);
                try {
                    AppUser user = userService.getUserByID(id);
                    sendResponse(exchange, 200, new Gson().toJson(user));
                } catch (SQLException e) {
                    sendResponse(exchange, 500, "Internal Server Error");
                } catch (ResourceNotFoundException e) {
                    sendResponse(exchange, 404, "User not found");
                }
            } else {
                sendResponse(exchange, 404, "Invalid endpoint");
            }
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            try {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String requestBody = br.readLine();
                AppUser newUser = new Gson().fromJson(requestBody, AppUser.class);
                AppUser createdUser = userService.addUser(newUser);
                sendResponse(exchange, 201, new Gson().toJson(createdUser));
            } catch (SQLException e) {
                sendResponse(exchange, 500, "Internal Server Error");
            }
        }

        private void handlePut(HttpExchange exchange) throws IOException {
            String[] parts = exchange.getRequestURI().getPath().split("/");
            long id = Long.parseLong(parts[2]);
            try {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String requestBody = br.readLine();
                AppUser updatedUser = new Gson().fromJson(requestBody, AppUser.class);
                updatedUser.setUserID(id);
                userService.updateUser(updatedUser);
                sendResponse(exchange, 200, new Gson().toJson(updatedUser));
            } catch (SQLException e) {
                sendResponse(exchange, 500, "Internal Server Error");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        private void handleDelete(HttpExchange exchange) throws IOException {
            String[] parts = exchange.getRequestURI().getPath().split("/");
            long id = Long.parseLong(parts[2]);
            try {
                userService.deleteUser(id);
                sendResponse(exchange, 200, "User deleted successfully");
            } catch (SQLException e) {
                sendResponse(exchange, 500, "Internal Server Error");
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    public static void main(String[] args) {
//        UserService userService = new UserService(new UserRepo()); // Assuming UserRepo is your UserRepository implementation
//        UserController userController = new UserController(userService);
//        userController.startServer();
    }
}
