//package org.nobleson.resources;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sun.net.httpserver.HttpExchange;
//import com.sun.net.httpserver.HttpHandler;
//import com.sun.net.httpserver.HttpServer;
//import org.nobleson.connection.DatabaseConnection;
//import org.nobleson.entities.AppUser;
//import org.nobleson.respositories.UserRepo;
//import org.nobleson.services.UserService;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.InetSocketAddress;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//import java.io.OutputStream;
//
//
//
//public class UserController {
//    private final UserService userService;
//    private final ObjectMapper objectMapper;
//
//    public UserController(Connection connection) {
//        this.userService = new UserService(new UserRepo(connection));
//        this.objectMapper = new ObjectMapper();
//    }
//
//    @Override
//    public void handle(HttpExchange exchange) throws IOException {
//        String method = exchange.getRequestMethod();
//        switch (method) {
//            case "GET":
//                handleGet(exchange);
//                break;
//            case "POST":
//                handlePost(exchange);
//                break;
//            case "PUT":
//                handlePut(exchange);
//                break;
//            case "DELETE":
//                handleDelete(exchange);
//                break;
//            default:
//                sendResponse(exchange, HttpURLConnection.HTTP_BAD_METHOD, "Method Not Allowed");
//        }
//    }
//
//    private void handleGet(HttpExchange exchange) throws IOException {
//        try {
//            List<AppUser> users = userService.getAllUsers();
//            sendResponse(exchange, HttpURLConnection.HTTP_OK, objectMapper.writeValueAsString(users));
//        } catch (SQLException e) {
//            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal Server Error");
//        }
//    }
//
//    private void handlePost(HttpExchange exchange) throws IOException {
//        try {
//            InputStream requestBody = exchange.getRequestBody();
//            AppUser user = objectMapper.readValue(requestBody, AppUser.class);
//            userService.addUser(user);
//            sendResponse(exchange, HttpURLConnection.HTTP_OK, "User added successfully");
//        } catch (SQLException e) {
//            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal Server Error");
//        }
//    }
//
//    private void handlePut(HttpExchange exchange) throws IOException {
//        try {
//            String[] pathParts = exchange.getRequestURI().getPath().split("/");
//            long userId = Long.parseLong(pathParts[pathParts.length - 1]);
//            InputStream requestBody = exchange.getRequestBody();
//            AppUser user = objectMapper.readValue(requestBody, AppUser.class);
//            user.setUserID(userId);
//            userService.updateUser(user);
//            sendResponse(exchange, HttpURLConnection.HTTP_OK, "User updated successfully");
//        } catch (SQLException e) {
//            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal Server Error");
//        }
//    }
//
//    private void handleDelete(HttpExchange exchange) throws IOException {
//        try {
//            String[] pathParts = exchange.getRequestURI().getPath().split("/");
//            long userId = Long.parseLong(pathParts[pathParts.length - 1]);
//            userService.deleteUser(userId);
//            sendResponse(exchange, HttpURLConnection.HTTP_OK, "User deleted successfully");
//        } catch (SQLException e) {
//            sendResponse(exchange, HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal Server Error");
//        }
//    }
//
//    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
//        exchange.sendResponseHeaders(statusCode, response.length());
//        try (OutputStream os = exchange.getResponseBody()) {
//            os.write(response.getBytes());
//        }
//    }
//}
