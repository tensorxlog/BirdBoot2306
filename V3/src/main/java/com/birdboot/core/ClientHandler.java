package com.birdboot.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            String line = readLine();
            System.out.println("请求行: " + line);

            String[] tempArr = line.split(" ");
            String method = tempArr[0];
            String uri = tempArr[1];
            String protocol = tempArr[2];

            System.out.println("method: " + method);
            System.out.println("uri: " + uri);
            System.out.println("protocol: " + protocol);

            Map<String, String> headers = new HashMap<>();
            while (!(line = readLine()).isEmpty()) {
                System.out.println(line);
                String[] arr = line.split(": ");
                headers.put(arr[0], arr[1]);
            }
            System.out.println(headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readLine() throws IOException { //被重用的代码出现的异常基本上都需要在方法上声明抛出，因为调用这段代码时处理异常的方式可能不同
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

        return line;
    }
}
