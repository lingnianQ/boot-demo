package com.baidu.controller;

import com.baidu.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author sytsnb@gmail.com
 * @Date 2022 2022/8/10 11:55
 */

@Controller
public class UserController {
    //表示保存所有用户信息的目录users
    private static File userDir;

    static {
        userDir = new File("./src/main/resources/templates/users/");
        if (!userDir.exists()) {
            userDir.mkdir();
        }
    }

    /**
     * register
     *
     * @param request
     * @param response
     */
    @RequestMapping("/regUser")
    public void reg(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
//        Cookie cookie = new Cookie(username, password);
//        response.addCookie(cookie);
        /*
         * register -- if
         */
        if (username == null || username.isEmpty() ||
                password == null || password.isEmpty() ||
                nickname == null || nickname.isEmpty() ||
                ageStr == null || ageStr.isEmpty() || !ageStr.matches("[0-9]+")) {
            try {
                response.sendRedirect("/register/reg_info_error.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        int age = Integer.parseInt(ageStr);

        User user = new User(username, password, nickname, age);
        /*
         * File(File parent ,String child)
         */
        File file = new File(userDir, username + ".obj");
        /*
         * 判断用户（文件） 是否存在
         */
        if (file.exists()) {
            try {
                response.sendRedirect("/register/have_user.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response.sendRedirect("/register/reg_success.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(user);
    }

    /**
     * login
     *
     * @param request
     * @param response
     */
    @RequestMapping("/loginUser")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = new User(username, password);
        File file = new File(userDir, user.getName() + ".obj");

        if (!file.exists() || user.getName() == null || user.getPassword() == null
                || user.getName().isEmpty() || user.getPassword().isEmpty()
        ) {
            try {
                response.sendRedirect("/login/login_info_error.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try (
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            User userGet = (User) ois.readObject();
            if (userGet.getName().equals(user.getName())
                    && userGet.getPassword().equals(user.getPassword())) {
                response.sendRedirect("/login/login_success.html");
            } else {
                response.sendRedirect("/login/login_fail.html");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
