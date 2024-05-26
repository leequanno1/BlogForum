package com.example.springdemo.controllers;

import com.example.springdemo.services.ArticleService;
import com.example.springdemo.services.UserService;
import com.example.springdemo.services.CookieService;
import com.example.springdemo.services.UserInfoService;
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
@RequestMapping("/personal")
public class PersonalController {

    /**
     * This function help get the 'page' param data.
     * If there is not 'page' param then the default page is 1.
     * @param param is a Map param.
     * @return page number.
     * */
    private int getPageFromParam(Map<String, Object> param) {
        int page = 1;
        if(param.containsKey("page")){
            page = Integer.parseInt(param.get("page").toString());
        }
        return page;
    }

    /**
     * The Posts page can be render in 2 URL: .../personal and .../personal/posts.
     * This function provide a method for render the Posts page in two URL.
     * @param model the render information.
     * @param param the request param.
     * @param request the request information.
     * */
    private String handleRenderPostPage(Model model, @RequestParam Map<String, Object> param, HttpServletRequest request) {
        int userId = Integer.parseInt(CookieService.getCookieValue(request,CookieService.cookieUserIdKey));
        UserInfoService.addUserInfoToModel(model,request);
        ArticleService articleService = new ArticleService();
        List<Object> postList = articleService.getAllByUserID(userId,6,getPageFromParam(param));
        model.addAttribute("postList", postList);
        model.addAttribute("nav", "posts");
        return "personal";
    }


    /**
     * Handle render Personal Post view
     * @param model model to fill into view.
     * @param param the request param information.
     * @return personal view at post nav.
     * */
    @GetMapping("")
    public String showPersonal (Model model, @RequestParam Map<String, Object> param, HttpServletRequest request) {
        return handleRenderPostPage(model, param, request);
    }

    /**
     * Handle render Personal Post view
     * @param model model to fill into view.
     * @param param the request param information.
     * @return personal view at post nav.
     * */
    @GetMapping("/posts")
    public String showPosts (Model model, @RequestParam Map<String, Object> param, HttpServletRequest request) {
        return handleRenderPostPage(model, param, request);
    }

    /**
     * Handle render Personal Post view
     * @param model model to fill into view.
     * @param param the request param information.
     * @return personal view at bookmark nav.
     * */
    @GetMapping("/bookmark")
    public String showBookmark (Model model, @RequestParam Map<String, Object> param, HttpServletRequest request) {
        int userId = Integer.parseInt(CookieService.getCookieValue(request,CookieService.cookieUserIdKey));
        UserInfoService.addUserInfoToModel(model,request);
        ArticleService articleService = new ArticleService();
        List<Object> postList = articleService.getAllBookmark(userId,6,getPageFromParam(param));
        model.addAttribute("postList", postList);
        model.addAttribute("nav", "bookmark");
        return "personal";
    }

    /**
     * Handle render Personal Post view
     * @param model model to fill into view.
     * @param param the request param information.
     * @return personal view at followed posts nav.
     * */
    @GetMapping("/followedposts")
    public String showFollowedPosts (Model model, @RequestParam Map<String, Object> param, HttpServletRequest request) {
        int userId = Integer.parseInt(CookieService.getCookieValue(request,CookieService.cookieUserIdKey));
        UserInfoService.addUserInfoToModel(model,request);
        model.addAttribute("nav", "followedposts");
        return "personal";
    }

    /**
     * Handle render Personal Post view
     * @param model model to fill into view.
     * @param param the request param information.
     * @return personal view at followed user nav.
     * */
    @GetMapping("/followedusers")
    public String showFollowedUsers (Model model, @RequestParam Map<String, Object> param, HttpServletRequest request) {
        int userId = Integer.parseInt(CookieService.getCookieValue(request,CookieService.cookieUserIdKey));
        UserInfoService.addUserInfoToModel(model,request);
        UserService userService = new UserService();
        List<Map<String, Object>> userList = userService.getAllFollowingUserByUserID(userId);
        model.addAttribute("userList", userList);
        model.addAttribute("nav", "followedusers");
        return "personal";
    }

    /**
     * Handle render Personal Post view
     * @param model model to fill into view.
     * @param param the request param information.
     * @return personal view at follower nav.
     * */
    @GetMapping("/followers")
    public String showFollower (Model model, @RequestParam Map<String, Object> param, HttpServletRequest request) {
        int userId = Integer.parseInt(CookieService.getCookieValue(request,CookieService.cookieUserIdKey));
        UserInfoService.addUserInfoToModel(model,request);
        UserService userService = new UserService();
        List<Map<String, Object>> userList = userService.getAllFollowUserByUserID(userId);
        model.addAttribute("userList", userList);
        model.addAttribute("nav", "follower");
        return "personal";
    }

}
