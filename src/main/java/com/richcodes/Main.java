package com.richcodes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException {

        try (var executor = Executors.newVirtualThreadPerTaskExecutor();
             ServerSocket serverSocket = new ServerSocket(4221);
        ) {
                serverSocket.setReuseAddress(true);
                while (true) {
                    Socket socket = serverSocket.accept(); // Wait for connection from client.
                    System.out.println("accepted new connection");
                    executor.submit(() -> {
                        try {
                            handleClientRequest(socket);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                }
        }
    }

     public static void handleClientRequest(Socket socket) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

         OutputStream output = socket.getOutputStream();

         PrintWriter printWriter = new PrintWriter(
                 output, true);

        String line;

        while((line = bufferedReader.readLine()) != null) {

            if (line.isEmpty()) break;
            System.out.println("received line: " + line);
        }
        String body = "{ \"message\": \"Hello\" }";
        String response =
                 "HTTP/1.1 200 OK\r\n" +
                         "Content-Type: application/json\r\n" +
                         "Content-Length: " + body.length() + "\r\n" +
                         "\r\n" +
                         body;

        output.write(response.getBytes());
        output.flush();
        socket.close();
    }
}
