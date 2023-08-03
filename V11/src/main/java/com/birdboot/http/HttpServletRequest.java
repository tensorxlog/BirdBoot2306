package com.birdboot.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequest {
    private Socket socket;
    private String method;
    private String uri;
    private String protocol;
    private Map<String, String> headers = new HashMap<>();

    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;
        parseRequestLine();
        parseHeaders();
        parseContent();
    }

    private void parseRequestLine() throws IOException, EmptyRequestException {
        String line = readLine();
        if (line.isEmpty()) throw new EmptyRequestException();
        System.out.println("请求行: " + line);

        String[] tempArr = line.split(" ");

        method = tempArr[0];
        uri = tempArr[1];
        protocol = tempArr[2];

        System.out.println("method: " + method);
        System.out.println("uri: " + uri);
        System.out.println("protocol: " + protocol);
    }

    private void parseHeaders() throws IOException {
        String line = readLine();
        while (!(line = readLine()).isEmpty()) {
            System.out.println(line);
            String[] arr = line.split(": ");
            headers.put(arr[0], arr[1]);
        }
        System.out.println(headers);
    }

    private void parseContent() {}

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

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }
}
