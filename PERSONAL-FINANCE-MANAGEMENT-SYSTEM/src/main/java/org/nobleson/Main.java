package org.nobleson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8090), 0);
        server.createContext("/hello", new HelloHandler() );
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8090");

    }

    static class HelloHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Welcome to Fundamental of REST API";
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}