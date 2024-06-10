package com.example.springdemo.controllers;

import com.example.springdemo.form.ChangePasswordForm;
import com.example.springdemo.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        List<Object> postList = articleService.getAllByUserID(userId,5,getPageFromParam(param));
        model.addAttribute("postList", postList);
        model.addAttribute("nav", "posts");
        handlePostsNav(model,userId,getPageFromParam(param));
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
        handleBookmarkNav(model,userId,getPageFromParam(param));
        return "personal";
    }

    /**
     * Handle render Personal Post view
     * @param model model to fill into view.
     * @param request the request param information.
     * @return personal view at followed posts nav.
     * */
    @GetMapping("/followedposts")
    public String showFollowedPosts (Model model, HttpServletRequest request) {
        UserInfoService.addUserInfoToModel(model,request);
        model.addAttribute("nav", "followedposts");
        return "personal";
    }

    /**
     * Handle render Personal Post view
     * @param model model to fill into view.
     * @param request the request param information.
     * @return personal view at followed user nav.
     * */
    @GetMapping("/followedusers")
    public String showFollowedUsers (Model model, HttpServletRequest request) {
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
     * @param request the request information.
     * @return personal view at follower nav.
     * */
    @GetMapping("/followers")
    public String showFollower (Model model,  HttpServletRequest request) {
        int userId = Integer.parseInt(CookieService.getCookieValue(request,CookieService.cookieUserIdKey));
        UserInfoService.addUserInfoToModel(model,request);
        UserService userService = new UserService();
        List<Map<String, Object>> userList = userService.getAllFollowUserByUserID(userId);
        model.addAttribute("userList", userList);
        model.addAttribute("nav", "follower");
        return "personal";
    }

    /**
     * Render modify personal information view
     * */
    @GetMapping("/modify")
    public String modifyPersonalInformation(Model model, HttpServletRequest request) {
        UserInfoService.addUserInfoToModel(model,request);

        return "modify_personal_information";
    }

    /**
     * Render change password view
     * */
    @GetMapping("/modify/password")
    public String changePassword(Model model, HttpServletRequest request) {
        UserInfoService.addUserInfoToModel(model,request);
        if(!model.containsAttribute("form")) {
            model.addAttribute("form", new ChangePasswordForm());
        }
        return "change_password";
    }

    /**
     * Handle submit change information
     * */
    @PostMapping("/modify/changeinfo")
    public String changeInfo(HttpServletRequest request, RedirectAttributes redirectAttributes,
                             @RequestParam Map<String, Object> param) {
        int userId = Integer.parseInt(CookieService.getCookieValue(request,CookieService.cookieUserIdKey));
        String image =  (String) param.get("base64Output");
        String avtURL =  (String) param.get("lastAvtURL");
        String displayName =  (String) param.get("displayName");
        String description =  (String) param.get("userDescription");
        if(displayName.isEmpty()){
            redirectAttributes.addFlashAttribute("displayNameMessage", true);
            return "redirect:/personal/modify";
        }
        if(!image.isEmpty()){
            CloudsDiaryService cloudsDiaryService = new CloudsDiaryService();
            avtURL = cloudsDiaryService.uploadImageBase64(image, UUID.randomUUID().toString());
        }
        UserService userService = new UserService();
        userService.changeInfo(userId,avtURL,displayName,description);
        Map<String, Object> userInfo = userService.getAccountInfoByUserId(userId);
        request.getSession().setAttribute("userInfo", userInfo);
        return "redirect:/personal";
    }

    /**
     * Handle submit change password.
     * */
    @PostMapping("/modify/passwordprocess")
    public String changePasswordProcess(HttpServletRequest request, @Valid @ModelAttribute ChangePasswordForm form, RedirectAttributes redirectAttributes) {
        if(form.getOldPassword().isEmpty()){
            redirectAttributes.addFlashAttribute("oldPasswordRequired", true);
        }
        if(form.getNewPassword().isEmpty()) {
            redirectAttributes.addFlashAttribute("newPasswordRequired", true);
        }
        UserService userService = new UserService();
        int userId = Integer.parseInt(CookieService.getCookieValue(request,CookieService.cookieUserIdKey));
        String username = userService.getUsernameByUserId(userId);
        if(form.getRepeatPassword().isEmpty()) {
            redirectAttributes.addFlashAttribute("repeatPasswordRequired", true);
        } else if(!form.getNewPassword().equals(form.getRepeatPassword())){
            redirectAttributes.addFlashAttribute("newPasswordMatched", true);
        } else if(userService.getUserIdByUsPw(username, form.getOldPassword()) == -1) {
            // hiện thông báo
            redirectAttributes.addFlashAttribute("oldPasswordMatched", true);
        } else {
            userService.changePasswordByUsername(username,form.getNewPassword());
            return "redirect:/personal/modify";
            // redirect
        }
        return "redirect:/personal/modify/password";
    }

    private void handlePostsNav(Model model, int userId , int page) {
        ArticleService articleService = new ArticleService();
        int pageSize = 5;
        int totalPages = (int) Math.ceil((double) articleService.getTotalArticleByUserID(userId)/ pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("rout", "/personal/posts");
    }

    private void handleBookmarkNav(Model model, int userId , int page) {
        ArticleService articleService = new ArticleService();
        int pageSize = 5;
        int totalPages = (int) Math.ceil((double) articleService.getTotalArticleByBookmark(userId)/ pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("rout", "/personal/bookmark");
    }
}
