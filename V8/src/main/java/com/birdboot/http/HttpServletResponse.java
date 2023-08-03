package com.birdboot.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServletResponse {
    private Socket socket;
    private int statusCode;
    private String statusReason;
    private File contentFile;

    public void response() throws IOException {
        sendStatusLine();
        sendHeaders();
        sendContent();
    }

    private void sendStatusLine() throws IOException {
        println("HTTP/1.1 " + statusCode + " " + statusReason);
    }
    private void sendHeaders() throws IOException {
        println("Content-Type: text/html");
        println("Content-Length: " + contentFile.length());
        println("");
    }
    private void sendContent() throws IOException {
        OutputStream os = socket.getOutputStream();
        try (InputStream is = new FileInputStream(contentFile);) {
            byte[] buf = new byte[1024 * 10];
            int d;
            while ((d = is.read(buf)) != -1) {
                os.write(buf, 0, d);
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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
    }

    public HttpServletResponse(Socket socket) {
        this.socket = socket;
    }
}