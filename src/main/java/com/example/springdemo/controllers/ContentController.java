package com.example.springdemo.controllers;

import com.example.springdemo.services.ArticleService;
import com.example.springdemo.services.CommentService;
import com.example.springdemo.services.UserInfoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/content")
public class ContentController {

    private final ArticleService articleService = new ArticleService();

    private final CommentService commentService = new CommentService();


    /**
     * Method to display content based on the provided parameters
     *
     * @param params request parameters
     * @param model  the model to be used in the view
     * @param request HTTP servlet request
     * @return the name of the view to be displayed (content)
     */
    @GetMapping("")
    public String showContent(@RequestParam Map<String, Object> params, Model model, HttpServletRequest request) {
        UserInfoService.addUserInfoToModel(model,request);

        int articleID = Integer.parseInt(params.get("id").toString());
        String sessionUserID = getCookieValue(request, "SessionUserID");
        int userID = sessionUserID != null  ? Integer.parseInt(sessionUserID) : 0;
        Map<String, Object> content = articleService.getByArticleId(userID, articleID);
        List<Object> comments = commentService.getAll(articleID);

        Map<String, Object> user = (Map<String, Object>) content.get("user");
        int userIdOfArticle = Integer.parseInt(user.get("userIdOfArticle").toString());
        List<Map<String, Object>> nextArticle = articleService.getNextArticle(articleID, userIdOfArticle);


        model.addAttribute("content", content);
        model.addAttribute("comments", comments);
        model.addAttribute("nextArticle", nextArticle);
        model.addAttribute("userID", userID);

        return "content";
    }


    /**
     * Method to retrieve the value of a cookie from the request
     *
     * @param request the HTTP servlet request
     * @param name    the name of the cookie
     * @return the value of the cookie if found, otherwise null
     */
    public String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
