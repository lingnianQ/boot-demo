package com.baidu.controller;

import com.baidu.entity.Article;
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

    @RequestMapping("/delArticle")
    public void delArticle(HttpServletRequest request, HttpServletResponse response) {
        String title = request.getParameter("title");
        File file = new File(articleDir, title + ".obj");
        if (!file.exists()) {
            return;
        }
        file.delete();
        try {
            response.sendRedirect("/articleList");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * articleList
     *
     * @param request
     * @param response
     */
    @RequestMapping("/articleList")
    public void articleList(HttpServletRequest request, HttpServletResponse response) {
        List<Article> articleList = new ArrayList<>();
        File[] subs = articleDir.listFiles(pathname -> pathname.getName().endsWith(".obj"));

        assert subs != null;
        for (File sub : subs) {
            try (
                    FileInputStream fis = new FileInputStream(sub);
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ) {
                Article article = (Article) ois.readObject();
                articleList.add(article);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            //setContentType() --声明文章类型
            response.setContentType("text/html;charset=utf-8");
            PrintWriter pw = response.getWriter();
            pw.println("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>文章列表</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\n" +
                    "<center>\n" +
                    "\n" +
                    "<h1>文章列表</h1>" +
                    "<table border=\"1\">" +
                    "<tr>" +
                    "<td>标题" + "</td>" +
                    "<td>作者" + "</td>" +
                    "<td>操作" + "</td>" +
                    "</tr>" + "\n");
            for (Article article : articleList) {
                pw.println("<tr>" +
                        "<td>" + article.getTitle() + "</td>" +
                        "<td>" + article.getAuthor() + "</td>" +
                        "<td>" + "<a href='/delArticle?title=" + article.getTitle() + "'>删除</a>" + "</td>" +
                        "</tr>" + "\n");
            }
            pw.println("</table>" +
                    "<a href='/index.html'>返回首页</a>\n" +
                    "<a href='/article/writeArticle.html'>发表文章</a>" +
                    "</center>" +
                    "</body>\n" +
                    "</html>");

        } catch (IOException e) {
            e.printStackTrace();
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
        String author = request.getParameter("author");
        String content = request.getParameter("content");

        Article article = new Article(title, author, content);
        File file = new File(articleDir, title + ".obj");

        if (file.exists() || title == null || title.trim().isEmpty() ||
                author == null || author.trim().isEmpty() ||
                content == null || content.trim().isEmpty()) {
            try {
                response.sendRedirect("/article/article_fail.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(article);
            response.sendRedirect("/article/article_success.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}












