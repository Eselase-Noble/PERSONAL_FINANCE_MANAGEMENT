package org.nobleson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.nobleson.connection.DatabaseConnection;
import org.nobleson.resources.UserController;
import org.nobleson.respositories.UserRepo;
import org.nobleson.services.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
//        HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);
//        server.createContext("/hello", new HelloHandler() );
//        server.setExecutor(null);
//        server.start();
//        System.out.println("Server started on port 8090");
        UserService userService = new UserService(new UserRepo(DatabaseConnection.getConnection())); // Assuming UserRepo is your UserRepository implementation
        UserController userController = new UserController(userService);
        userController.startServer();
    }

//    static class HelloHandler implements HttpHandler{
//
//        @Override
//        public void handle(HttpExchange httpExchange) throws IOException {
//        String response = "Welcome to Fundamental of REST API";
//        httpExchange.sendResponseHeaders(200, response.getBytes().length);
//            OutputStream os = httpExchange.getResponseBody();
//            os.write(response.getBytes());
//            os.close();
//        }
//    }
}