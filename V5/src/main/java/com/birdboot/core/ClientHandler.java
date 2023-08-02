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
            File file = new File(staticDir, "index.html");

            OutputStream os = socket.getOutputStream();

            String line = "HTTP/1.1 200 OK";
            byte[] buf = line.getBytes(StandardCharsets.ISO_8859_1);
            os.write(buf);
            os.write(13);
            os.write(10);
            line = "Content-Type: text/html";
            buf = line.getBytes(StandardCharsets.ISO_8859_1);
            os.write(buf);
            os.write(13);
            os.write(10);
            line = "Content-Length: " + file.length();
            buf = line.getBytes(StandardCharsets.ISO_8859_1);
            os.write(buf);
            os.write(13);
            os.write(10);
            os.write(13);
            os.write(10);

            InputStream is = new FileInputStream(file);
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

}
