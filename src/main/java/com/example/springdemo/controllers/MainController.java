package com.example.springdemo.controllers;

import com.example.springdemo.services.ArticleService;
import com.example.springdemo.services.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    // Initialize ArticleService to use in the controller
    private final ArticleService articleService = new ArticleService();


    /**
     * Handles GET requests for the home page ("/").
     * @param page the current page number (defaults to 1 if no parameter is provided).
     * @param model the Model object to add attributes to.
     * @param request the HttpServletRequest object to get request information.
     * @return the name of the view to render (in this case, "index").
     */
    @GetMapping("/")
    public String showIndex(@RequestParam(defaultValue = "1") int page, Model model, HttpServletRequest request) {
        // Set common model attributes
        setupModelAttributes(model, request, page, null, null, null);
        return "index";
    }


    /**
     * Handles GET requests for the article page ("/article").
     * @param params the map of parameters from the request.
     * @param model the Model object to add attributes to.
     * @param request the HttpServletRequest object to get request information.
     * @return the name of the view to render (in this case, "index").
     */
    @GetMapping("/article")
    public String showArticlePage(@RequestParam Map<String, Object> params, Model model, HttpServletRequest request) {
        // Extract parameters for filtering articles
        int page = Integer.parseInt(params.getOrDefault("page", "1").toString());
        String tagName = params.get("tagName") != null ? params.get("tagName").toString() : "";
        String date = params.get("date") != null ? params.get("date").toString() : "";
        String title = params.get("title") != null ? params.get("title").toString() : "";

        // Set model attributes for the articles page
        setupModelAttributes(model, request, page, tagName, date, title);
        model.addAttribute("param", params);
        if (!title.isEmpty()) {
            model.addAttribute("title", title);
        }

        return "index";
    }

    /**
     * Sets up common model attributes for both the home and article pages.
     * @param model the Model object to add attributes to.
     * @param request the HttpServletRequest object to get request information.
     * @param page the current page number.
     * @param tagName the tag name to filter articles by (optional).
     * @param date the date to filter articles by (optional).
     * @param title the title to filter articles by (optional).
     */
    private void setupModelAttributes(Model model, HttpServletRequest request, int page, String tagName, String date, String title) {
        UserInfoService.addUserInfoToModel(model, request);

        int pageSize = 5;
        List<Object> articles;
        int totalArticle;

        // Determine which articles to fetch based on provided filters
        if (tagName != null && !tagName.isEmpty()) {
            articles = articleService.getAllByTagName(tagName, pageSize, page);
            totalArticle = articleService.getTotalArticleByTag(tagName);
        } else if (date != null && !date.isEmpty()) {
            articles = articleService.getFromDate(date, pageSize, page);
            totalArticle = articleService.getTotalArticleByFromDate(date);
        } else if (title != null && !title.isEmpty()) {
            articles = articleService.getAllByTitle(title, pageSize, page);
            totalArticle = articleService.getTotalArticleByTitle(title);
        } else {
            articles = articleService.getAll(pageSize, page);
            totalArticle = articleService.getTotalArticle();
        }

        int totalPages = (int) Math.ceil((double) totalArticle / pageSize);
        // Fetch popular and remaining tags
        List<String> popularTags = articleService.getPopularTags();
        List<String> remainingTags = articleService.getRemainingTags();

        // Add attributes to the model
        model.addAttribute("articles", articles);
        model.addAttribute("totalArticle", totalArticle);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("popularTags", popularTags);
        model.addAttribute("remainingTags", remainingTags);
        model.addAttribute("rout", "/article");
    }

}
