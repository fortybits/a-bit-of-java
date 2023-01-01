package edu.bit.advanced.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Path;

// https://openjdk.org/jeps/408
public class SimpleServer {

    public static void main(String[] args) throws IOException {
        var socketAddress = new InetSocketAddress(8080);
        var server = SimpleFileServer.createFileServer(socketAddress, Path.of("/Users/naman/Desktop"),
                SimpleFileServer.OutputLevel.INFO);
        server.start();

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
        httpServer.createContext("/test", new MyHandler());
        httpServer.setExecutor(null); // creates a default executor
        httpServer.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}