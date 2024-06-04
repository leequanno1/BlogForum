package com.example.springdemo.controllers;

import com.example.springdemo.form.ForgotPasswordForm;
import com.example.springdemo.form.LoginForm;
import com.example.springdemo.form.ResetPasswordForm;
import com.example.springdemo.form.SignUpForm;
import com.example.springdemo.services.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    /**
     * userService is used to handel user information.
     */
    private final UserService userService = new UserService();

    /**
     * tokenService is used to handel token information.
     */
    private final TokenService tokenService = new TokenService();

    /**
     * emailService is used to handel email information.
     */
    private final EmailService emailService = new EmailService();

    private final ArticleService articleService = new ArticleService();

    /**
     * Handles GET requests to /login. Displays the login page.
     *
     * @param model The model to pass data to the view.
     * @return The login view template.
     */
    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }

        return "usertemplates/login";
    }


    /**
     * Handles GET requests to /signUp. Displays the sign-up page.
     *
     * @param model the model to pass data to the view
     * @return the sign-up view template
     */
    @GetMapping("/signUp")
    public String signup(Model model) {
        if (!model.containsAttribute("signUpForm")) {
            model.addAttribute("signUpForm", new SignUpForm());
        }

        return "usertemplates/sign_up";
    }


    /**
     * Handles GET requests to /forgotPassword. Displays the forgot password page.
     *
     * @param model the model to pass data to the view
     * @return the forgot password view template
     */
    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model) {
        if (!model.containsAttribute("forgotPasswordForm")) {
            model.addAttribute("forgotPasswordForm", new ForgotPasswordForm());
        }

        return "usertemplates/forgot_password";
    }


    /**
     * Handles GET requests to /resetPassword. Displays the reset password page.
     *
     * @param model the model to pass data to the view
     * @return the reset password view template
     */
    @GetMapping("/resetPassword")
    public String resetPassword(Model model) {
        if (!model.containsAttribute("resetPasswordForm")) {
            model.addAttribute("resetPasswordForm", new ResetPasswordForm());
        }


        return "usertemplates/reset_password";
    }


    /**
     * Handles POST requests to /loginProcess. Processes the login form submission.
     * Sets the username in the session and redirects to the home page.
     *
     * @param loginForm          The login form data.
     * @param request            The HTTP request object containing form data.
     * @param redirectAttributes Attributes for flash messages.
     * @return a redirect to the home page.
     */
    @PostMapping("/loginProcess")
    public String loginProcess(@Valid @ModelAttribute("loginForm") LoginForm loginForm, HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        String username, password;
        username = loginForm.getUsername();
        password = loginForm.getPassword();

        Integer userID = userService.getUserIdByUsPw(username, password);

        if (userID == -1) {
            redirectAttributes.addFlashAttribute("loginError", "Wrong username or password!");
            redirectAttributes.addFlashAttribute("loginForm", loginForm);
            return "redirect:/login";
        }

        Map<String, Object> userInfo = userService.getAccountInfoByUserId(userID);
        if(userInfo.get("AvatarURL") == null || ((String)userInfo.get("AvatarURL")).isEmpty()) {
            userInfo.replace("AvatarURL", "https://res.cloudinary.com/ddczpe6gq/image/upload/v1716959781/modfziyc6jesknmgizok.jpg");
        }
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", userInfo);

        Cookie sessionEmailCookie = new Cookie("SessionUserID", String.valueOf(userID));
        sessionEmailCookie.setPath("/");
        sessionEmailCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(sessionEmailCookie);

        return "redirect:/";
    }


    /**
     * Handles POST requests to /signUpProcess. Processes the sign-up form submission.
     * Redirects to the login page after successful sign-up.
     *
     * @param signUpForm         The sign-up form data.
     * @param request            The HTTP request object containing form data.
     * @param redirectAttributes Attributes for flash messages.
     * @return a redirect to the sign-up page.
     */
    @PostMapping("/signUpProcess")
    public String sinUpProgress(@Valid @ModelAttribute("signUpForm") SignUpForm signUpForm, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String username, password, tokenValue, email;
        username = signUpForm.getUsername();
        password = signUpForm.getPassword();
        email = signUpForm.getEmail();
        tokenValue = signUpForm.getTokenValue();

        int result = userService.createNewAccount(username, password, email);

        boolean resultCheckTokenValue = tokenService.isTokenValid(email, tokenValue);

        String signUpMessage = "";

        if (result == UserService.USERNAME_EXISTS) {
            signUpMessage = "Username already exists!";
        }

        if (result == UserService.EMAIL_EXISTS) {
            signUpMessage = "Email already exists";
        }

        if (!resultCheckTokenValue) {
            signUpMessage = "Invalid authentication code";
        }

        if (result == UserService.FAILURE) {
            signUpMessage = "Failed to create new account.";
        }

        if (result == UserService.SUCCESS) {
            tokenService.deleteToken(email);
            signUpMessage = "Create account successfully.";
        }

        redirectAttributes.addFlashAttribute("signUpMessage", signUpMessage);
        redirectAttributes.addFlashAttribute("signUpForm", signUpForm);

        return "redirect:/signUp";
    }


    /**
     * Handles POST requests to /forgotPassword. Processes the forgot password form submission.
     * Validates the token and sets a session cookie for the email.
     *
     * @param forgotPasswordForm The forgot password form data.
     * @param request            The HTTP request object.
     * @param response           The HTTP response object.
     * @param redirectAttributes Attributes for flash messages.
     * @return a redirect to the reset password page.
     */
    @PostMapping("/forgotPassword")
    public String forgotPassword(@Valid @ModelAttribute("forgotPasswordForm") ForgotPasswordForm forgotPasswordForm, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        String email =forgotPasswordForm.getEmail();
        String tokenValue = forgotPasswordForm.getTokenValue();
        int userID = userService.getUserIdByEmail(email);

        System.out.println("Email: " + email + " | Token value: " + tokenValue + " | UserID: " + userID);

        if (!tokenService.isTokenValid(email, tokenValue)) {
            redirectAttributes.addFlashAttribute("forgotPasswordMessage", "The authentication code is incorrect!");
            redirectAttributes.addFlashAttribute("forgotPasswordForm", forgotPasswordForm);
            return "redirect:/forgotPassword";
        }

        Cookie sessionEmailCookie = new Cookie("SessionEmail", email);
        sessionEmailCookie.setPath("/");
        sessionEmailCookie.setMaxAge(24 * 60 * 60);
        sessionEmailCookie.setHttpOnly(true);

        // Thêm cookie vào response
        response.addCookie(sessionEmailCookie);

        tokenService.deleteToken(email);

        return "redirect:/resetPassword";
    }

    /**
     * Retrieves the value of a cookie by its name.
     *
     * @param request The HTTP request object.
     * @param name    The name of the cookie.
     * @return The value of the cookie, or null if not found.
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


    /**
     * Handles POST requests to /resetPassword. Processes the reset password form submission.
     * Changes the user's password and redirects to the reset password page with a message.
     *
     * @param resetPasswordForm  The reset password form data.
     * @param request            The HTTP request object.
     * @param response           The HTTP response object.
     * @param redirectAttributes Attributes for flash messages.
     * @return a redirect to the reset password page.
     */
    @PostMapping("/resetPassword")
    public String resetPassword(@Valid @ModelAttribute("resetPasswordForm") ResetPasswordForm resetPasswordForm, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        String email = getCookieValue(request, "SessionEmail");
        String password = resetPasswordForm.getPassword();

        int result = userService.changePassword(email, password);

        System.out.println(email + " - " + password + " - " + result);

        if (result == -1) {
            redirectAttributes.addFlashAttribute("resetPasswordMessage", "Failed to update password.");
        } else {
            // Delete SessionEmail cookie
            Cookie cookie = new Cookie("SessionEmail", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            redirectAttributes.addFlashAttribute("resetPasswordMessage", "Update password successfully.");
        }

        return "redirect:/resetPassword";
    }



    /**
     * Handles POST requests to /logout. Logs out the user by invalidating the session and deleting cookies.
     *
     * @param request  The HTTP request object.
     * @param response The HTTP response object.
     * @return a redirect to the home page.
     */
    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        // Delete session
        request.getSession().invalidate();

        // Delete SessionUserID cookie
        Cookie sessionUserIDCookie = new Cookie("SessionUserID", "");
        sessionUserIDCookie.setMaxAge(0);
        sessionUserIDCookie.setPath("/");
        response.addCookie(sessionUserIDCookie);

        // Delete ClientUserID cookie
        Cookie clientUserIDCookie = new Cookie("ClientUserID", "");
        clientUserIDCookie.setMaxAge(0);
        clientUserIDCookie.setPath("/");
        response.addCookie(clientUserIDCookie);

        // Redirect to home page
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/user")
    public String getUserInfoView(Model model, HttpServletRequest request, @RequestParam Map<String, String> param){
        UserInfoService.addUserInfoToModel(model, request);
        String username;
        int page = 1;
        Map<String, Object> selectedUser;
        if(param.containsKey("user")) {
            username = param.get("user");
            // get user information by username
            selectedUser = userService.getAccountInfoByUsername(username);
            if (selectedUser.isEmpty()){
                return "redirect:/";
            }
        } else {
            return "redirect:/";
        }

        if(param.containsKey("page")) {
            page = Integer.parseInt(param.get("page"));
        }
        int followerId;
        Boolean isFollowed = null;
        try {
            followerId = Integer.parseInt(CookieService.getCookieValue(request,CookieService.cookieUserIdKey));
            isFollowed = userService.isFollowingUser(followerId, (int)selectedUser.get("UserID"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // check if this user is followed
        // get post list
        List<Object> postList = articleService.getAllByUserUsername(username,5,page);
        model.addAttribute("selectedUser", selectedUser);
        model.addAttribute("isFollowed", isFollowed);
        model.addAttribute("postList", postList);
        handlePageNav(model, username, page);
        return "user_info";
    }

    private void handlePageNav(Model model, String username, int page) {
        int pageSize = 5;
        int totalPages = (int) Math.ceil((double) articleService.getTotalArticleByUserName(username)/ pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("username", username);
        model.addAttribute("rout", "/user");
    }

}
