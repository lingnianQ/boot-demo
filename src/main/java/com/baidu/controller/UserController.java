package com.baidu.controller;

import com.baidu.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
            userDir.mkdirs();
        }
    }

    /**
     * delUser
     *
     * @param request
     * @param response
     */
    @RequestMapping("/delUser")
    public void delUser(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        File file = new File(userDir, username + ".obj");
        if (!file.exists()) {
            return;
        }
        file.delete();
    }

    /**
     * userList
     *
     * @param request
     * @param response
     */
    @RequestMapping("/userList")
    public void userList(HttpServletRequest request, HttpServletResponse response) {

        List<User> userList = new ArrayList<>();
        File[] subs = userDir.listFiles(pathname -> pathname.getName().endsWith(".obj"));

        assert subs != null;
        for (File sub : subs) {
            try (FileInputStream fis = new FileInputStream(sub);
                 ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                User user = (User) ois.readObject();
                userList.add(user);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            response.setContentType("text/html;charset=utf-8");
            PrintWriter pw = response.getWriter();
            pw.println("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>用户列表</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\n" +
                    "<center>\n" +
                    "\n" +
                    "<h1>用户列表</h1>" +
                    "<table border=\"1\">" +
                    "<tr>" +
                    "<td>用户" + "</td>" +
                    "<td>密码" + "</td>" +
                    "<td>昵称" + "</td>" +
                    "<td>年龄" + "</td>" +
                    "<td>操作" + "</td>" +
                    "</tr>" + "\n");
            for (User user : userList) {
                pw.println("<tr>" +
                        "<td>" + user.getName() + "</td>" +
                        "<td>" + user.getPassword() + "</td>" +
                        "<td>" + user.getNickname() + "</td>" +
                        "<td>" + user.getAge() + "</td>" +
                        "<td>" + "<a href='/delUser?username=" + user.getName() + "'>删除</a>" + "</td>" +
                        "</tr>" + "\n");
            }
            pw.println("</table>" +
                    "</center>" +
                    "</body>\n" +
                    "</html>");

        } catch (IOException e) {
            e.printStackTrace();
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
                return;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            response.sendRedirect("/login/login_fail.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
