package com.birdboot.core;

import com.birdboot.http.HttpServletRequest;
import com.birdboot.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;

public class DispatcherServlet {
    private static DispatcherServlet instance = new DispatcherServlet();
    private static File baseDir;
    private static File staticDir;

    static {
        try {
            baseDir = new File(ClientHandler.class.getClassLoader().getResource(".").toURI());
            staticDir = new File(baseDir, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DispatcherServlet() {}

    public static DispatcherServlet getInstance() {
        return instance;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getUri();
        System.out.println("uri is " + uri);
        File file = new File(staticDir, uri);

        response.setContentFile(file);
        response.setStatusCode(200);
        response.setStatusReason("OK");

        if (!file.isFile()) {
            response.setContentFile(new File(staticDir, "404.html"));
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
        }
    }
}
