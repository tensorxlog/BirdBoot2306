package com.birdboot.controller;

import com.birdboot.entity.Article;
import com.birdboot.http.HttpServletRequest;
import com.birdboot.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ArticleController {
    private static File articleDir;

    static {
        articleDir = new File("./articledir");
        if (!articleDir.exists()) {
            articleDir.mkdirs();
        }
    }

    public void writeArticle(HttpServletRequest request, HttpServletResponse response) {
        //获取文章信息
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String content = request.getParameter("content");

        if (title == null || title.isEmpty() || author == null || author.isEmpty() || content == null || content.isEmpty()) {
            response.sendRedirect("article_info_error.html");
        } else {
            File file = new File(articleDir, title + ".obj");
            if (file.exists()) {
                response.sendRedirect("have_article.html");
            } else {
                Article article = new Article(title, author, content);
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));) {
                    oos.writeObject(article);
                    response.sendRedirect("submit_success.html");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
