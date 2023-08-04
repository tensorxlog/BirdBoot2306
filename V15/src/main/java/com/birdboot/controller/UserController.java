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

    public void login(HttpServletRequest request, HttpServletResponse response) {
        //获取请求中的登录信息
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //判断该用户是否已存在.如果已经存在，比较密码是否正确
        File file = new File(userDir, username + ".obj");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));) {
                User user = (User) ois.readObject();
                String savePassword = user.getPassword();
                if (savePassword.equals(password)) {
                    response.sendRedirect("login_success.html");
                } else {
                    response.sendRedirect("login_fail.html");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("login_info_error.html");
        }
    }
}
