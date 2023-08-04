package com.birdboot.controller;

import com.birdboot.entity.User;
import com.birdboot.http.HttpServletRequest;
import com.birdboot.http.HttpServletResponse;

import java.io.*;

public class UserController {
    private static File userDir;

    static {
        userDir = new File("./users");
        if (!userDir.exists()) {
            userDir.mkdirs();
            System.out.println("Directory created.");
        }
    }

    public void reg(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("开始处理用户注册");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        System.out.println(username + ", " + password + ", " + nickname + ", " + ageStr);

        if (username == null || username.isEmpty() ||
            password == null || password.isEmpty() ||
            nickname == null || nickname.isEmpty() ||
            ageStr == null || ageStr.isEmpty() ||
            !ageStr.matches("[0-9]+")) {
            response.sendRedirect("reg_info_error.html");
            return;
        } else {
            File userFile = new File(userDir, username + ".obj");
            if (userFile.exists()) {
                response.sendRedirect("have_user.html");
                return;
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userFile));){
                User user = new User(username, password, nickname, Integer.parseInt(ageStr));
                oos.writeObject(user);
                response.sendRedirect("reg_success.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
