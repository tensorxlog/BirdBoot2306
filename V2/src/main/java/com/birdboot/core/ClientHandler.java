package com.birdboot.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream is = socket.getInputStream();
            int d;
            char pre = '0';
            char cur = '0';
            StringBuilder sb = new StringBuilder();
            while ((d = is.read()) != -1) {
                cur = (char) d;
                if (pre == 13 && cur == 10) {
                    break;
                }
                sb.append(cur);
                pre = cur;
            }
            String line = sb.toString().trim();
            System.out.println("请求行: " + line);

            String[] tempArr = line.split(" ");
            String method = tempArr[0];
            String uri = tempArr[1];
            String protocol = tempArr[2];

            System.out.println("method: " + method);
            System.out.println("uri: " + uri);
            System.out.println("protocol: " + protocol);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
