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
    private String requestURI;
    private String queryString;
    private Map<String, String> parameters = new HashMap<>();

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

        parseURI(); //进一步解析URI

        System.out.println("method: " + method);
        System.out.println("uri: " + uri);
        System.out.println("protocol: " + protocol);
    }

    private void parseURI() {
        /*
            String requestURI
            String queryString
            Map parameters

            uri有两种情况:
            1:不含有参数的
              例如: /index.html
              直接将uri的值赋值给requestURI即可.

            2:含有参数的
              例如:/regUser?username=fancq&password=123456&nickname=chuanqi&age=22
              将uri中"?"左侧的请求部分赋值给requestURI
              将uri中"?"右侧的参数部分赋值给queryString
              将参数部分首先按照"&"拆分出每一组参数，再将每一组参数按照"="拆分为参数名与参数值
              并将参数名作为key，参数值作为value存入到parameters中。
                requestURI:/regUser
                queryString:username=&password=123456&nickname=chuanqi&age=22
                parameters:
                    key             value
                  username          ""
                  password          123456
                  nickname          chuanqi
                  age               22


           几种特殊情况
           1:表单中有空着不写的输入框
           例如:用户名不填
           /regUser?username=&password=123456&nickname=chuanqi&age=22

           2:表单中如果所有输入框没有指定name属性，此时表单提交则会忽略所有输入框
           /regUser?
         */
        String[] arr = uri.split("\\?");
        if (arr.length == 1) {
            requestURI = uri;
        } else {
            requestURI = arr[0];
            if (arr.length == 2) {
                queryString = arr[1];
                String[] pairs = queryString.split("&");
                for (String pair: pairs) {
                    String[] tempArr = pair.split("=", 2);
                    parameters.put(tempArr[0], tempArr[1]);
                }
            }
        }

        System.out.println("requestURI = " + requestURI);
        System.out.println("queryString = " + queryString);
        System.out.println("parameters = " + parameters);
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

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }
}
