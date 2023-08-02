package com.birdboot.core;

import com.birdboot.http.HttpServletRequest;
import com.birdboot.http.HttpServletResponse;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            HttpServletRequest request = new HttpServletRequest(socket);
            HttpServletResponse response = new HttpServletResponse(socket);
            String uri = request.getUri();
            System.out.println("uri is " + uri);
            File baseDir = new File(ClientHandler.class.getClassLoader().getResource(".").toURI());
            File staticDir = new File(baseDir, "static");
            File file = new File(staticDir, uri);

            response.setContentFile(file);
            response.setStatusCode(200);
            response.setStatusReason("OK");

            if (!file.isFile()) {
                response.setContentFile(new File(staticDir, "404.html"));
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
            }

            response.response();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
