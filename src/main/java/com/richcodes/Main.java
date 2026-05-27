package com.richcodes;

import com.richcodes.models.HttpRequest;
import com.richcodes.models.HttpResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
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

        String requestLine  = bufferedReader.readLine();
         HttpRequest request = parseRequestLine(requestLine);

         System.out.println(request.method());
         System.out.println(request.path());
         System.out.println(request.version());

        while((line = bufferedReader.readLine()) != null) {
            if (line.isEmpty()) break;
            System.out.println("received line: " + line);
        }

        String body = "{ \"message\": \"Hello world o\" }";
         HttpResponse response = HttpResponse.builder()
                 .statusCode(200)
                 .statusText("OK")
                 .headers("Content-Type", "application/json")
                 .headers("Content-Length", String.valueOf(body.getBytes().length))
                 .body(body)
                 .build();

        output.write(response.toRawString().getBytes());
        output.flush();
        socket.close();
    }

    public static HttpRequest parseRequestLine(String line) {
        String[] parts = line.split(" ");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid request line: " + line);
        }
        return new HttpRequest(parts[0], parts[1], parts[2], new HashMap<>(), "");
    }

}
