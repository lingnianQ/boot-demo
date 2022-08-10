package com.baidu.controller;

import com.baidu.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
            userDir.mkdir();
        }
    }

    @RequestMapping("/regUser")
    public void reg(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");

        int age = Integer.parseInt(ageStr);

        User user = new User(username, password, nickname, age);
        /*
         * File(File parent ,String child)
         */
        File file = new File(userDir, username + ".obj");

        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            response.sendRedirect("./reg_success.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(user);
    }
}
