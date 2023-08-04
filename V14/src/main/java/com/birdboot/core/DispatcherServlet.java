package com.birdboot.core;

import com.birdboot.http.HttpServletRequest;
import com.birdboot.http.HttpServletResponse;
import com.birdboot.controller.UserController;

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
        String uri = request.getRequestURI();
        System.out.println("uri is " + uri);

        if ("/regUser".equals(uri)) {
            UserController controller = new UserController();
            controller.reg(request, response);
        } else {
            File file = new File(staticDir, uri);

            response.setContentFile(file);
            response.setStatusCode(200);
            response.setStatusReason("OK");

            response.addHeader("Server", "BirdWebServer");

            if (!file.isFile()) {
                File errorFile404 = new File(staticDir, "404.html");
                response.setContentFile(errorFile404);
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
            }
        }
    }
}
