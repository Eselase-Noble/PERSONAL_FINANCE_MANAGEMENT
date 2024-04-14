//package org.nobleson.resources;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sun.net.httpserver.HttpExchange;
//import com.sun.net.httpserver.HttpHandler;
//import com.sun.net.httpserver.HttpServer;
//import org.nobleson.entities.AppUser;
//import org.nobleson.exceptions.ResourceNotFoundException;
//import org.nobleson.services.UserService;
//
//import java.io.*;
//import java.net.InetSocketAddress;
//import java.nio.charset.StandardCharsets;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import com.google.gson.Gson;
//
//public class UserResource {
//
//
//   private final UserService userService;
//    private final Map<Long, AppUser> users = new HashMap<>();
//    private long nextId = 1;
//
//    public UserResource(UserService userService) {
//        this.userService = userService;
//        try {
//            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
//            server.createContext("/users", new UserHandler());
//            server.setExecutor(null); // creates a default executor
//            server.start();
//            System.out.println("Server running on port 8000");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    private class UserHandler implements HttpHandler {
//        private final UserService userService;
//
//        public UserHandler(UserService userService) {
//            this.userService = userService;
//        }
//
//        @Override
//        public void handle(HttpExchange exchange) throws IOException {
//            String method = exchange.getRequestMethod();
//            switch (method) {
//                case "GET":
//                    handleGet(exchange);
//                    break;
//                case "POST":
//                    handlePost(exchange);
//                    break;
//                case "PUT":
//                    handlePut(exchange);
//                    break;
//                case "DELETE":
//                    handleDelete(exchange);
//                    break;
//                default:
//                    sendResponse(exchange, 405, "Method Not Allowed");
//            }
//        }
//
//        private void handleGet(HttpExchange exchange) throws IOException {
//            String path = exchange.getRequestURI().getPath();
//            if (path.equals("/users")) {
//                try {
//                    List<AppUser> users = userService.getAllUsers();
//                    sendResponse(exchange, 200, new Gson().toJson(users));
//                } catch (SQLException e) {
//                    sendResponse(exchange, 500, "Internal Server Error");
//                }
//            } else if (path.matches("/users/\\d+")) {
//                String[] parts = path.split("/");
//                long id = Long.parseLong(parts[2]);
//                try {
//                    AppUser user = userService.getUserByID(id);
//                    sendResponse(exchange, 200, new Gson().toJson(user));
//                } catch (SQLException e) {
//                    sendResponse(exchange, 500, "Internal Server Error");
//                } catch (ResourceNotFoundException e) {
//                    sendResponse(exchange, 404, "User not found");
//                }
//            } else {
//                sendResponse(exchange, 404, "Invalid endpoint");
//            }
//        }
//
//        private void handlePost(HttpExchange exchange) throws IOException {
//            // Implement POST method here
//        }
//
//        private void handlePut(HttpExchange exchange) throws IOException {
//            String[] parts = exchange.getRequestURI().getPath().split("/");
//            long id = Long.parseLong(parts[2]);
//            try {
//                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
//                BufferedReader br = new BufferedReader(isr);
//                String requestBody = br.readLine();
//                AppUser updatedUser = new Gson().fromJson(requestBody, AppUser.class);
//                updatedUser.setUserID(id);
//                userService.updateUser(updatedUser);
//                sendResponse(exchange, 200, new Gson().toJson(updatedUser));
//            } catch (SQLException e) {
//                sendResponse(exchange, 500, "Internal Server Error");
//            }
//        }
//
//        private void handleDelete(HttpExchange exchange) throws IOException {
//            String[] parts = exchange.getRequestURI().getPath().split("/");
//            long id = Long.parseLong(parts[2]);
//            try {
//                userService.deleteUser(id);
//                sendResponse(exchange, 200, "User deleted successfully");
//            } catch (SQLException e) {
//                sendResponse(exchange, 500, "Internal Server Error");
//            }
//        }
//
//        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
//            exchange.getResponseHeaders().set("Content-Type", "application/json");
//            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
//            try (OutputStream os = exchange.getResponseBody()) {
//                os.write(response.getBytes());
//            }
//        }
//    }
//
//}
