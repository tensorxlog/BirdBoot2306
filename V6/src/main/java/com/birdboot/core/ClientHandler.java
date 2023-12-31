package com.birdboot.core;

import com.birdboot.http.HttpServletRequest;

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
            String uri = request.getUri();
            System.out.println("uri is " + uri);

            File baseDir = new File(ClientHandler.class.getClassLoader().getResource(".").toURI());
            File staticDir = new File(baseDir, "static");
            File file = new File(staticDir, uri);

            int statusCode = 200;
            String statusInfo = "OK";
            if (!file.exists()) {
                file = new File(staticDir, "404.html");
                statusCode = 404;
                statusInfo = "NotFound";
            }

            println("HTTP/1.1 " + statusCode + " " + statusInfo);
            println("Content-Type: text/html");
            println("Content-Length: " + file.length());
            println("");

            OutputStream os = socket.getOutputStream();
            InputStream is = new FileInputStream(file);
            byte[] buf = new byte[1024 * 10];
            int d;
            while ((d = is.read(buf)) != -1) {
                os.write(buf, 0, d);
            }
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

    private void println(String line) throws IOException {
        OutputStream os = socket.getOutputStream();
        byte[] buf = line.getBytes(StandardCharsets.ISO_8859_1);
        os.write(buf);
        os.write(13);
        os.write(10);
    }

}
