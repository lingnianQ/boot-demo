package com.baidu.controller;

import com.baidu.entity.Article;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author sytsnb@gmail.com
 * @Date 2022 2022/8/11 14:31
 */
@Controller
public class ArticleController {
    //文章保存目录
    private static File articleDir;

    static {
        articleDir = new File("./src/main/resources/templates/articles/");
        if (!articleDir.exists()) {
            articleDir.mkdirs();
        }
    }

    /**
     * writeArticle
     *
     * @param request
     * @param response
     */
    @RequestMapping("/writeArticle")
    public void writeArticle(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        File file = new File(articleDir, title + ".obj");

        if (title == null || title.isEmpty()) {
            try {
                response.sendRedirect("/article/article_fail.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        Article article = new Article(title, content);

        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(article);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            response.sendRedirect("/article/article_success.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}











