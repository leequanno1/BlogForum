package com.example.springdemo.apicontrollers;

import com.example.springdemo.services.EmailService;
import com.example.springdemo.services.TokenService;
import com.example.springdemo.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@RestController
public class UserApiController {

    /***
     * Service to manipulate user information.
     */
    private UserService userService = new UserService();

    /***
     * Service for sending emails.
     */
    private EmailService emailService = new EmailService();

    /***
     * Service for managing tokens.
     */
    private TokenService tokenService = new TokenService();


    /***
     * API endpoint for user authentication via username and password.
     *
     * @param requestBody Object containing username and password.
     * @param response HttpServletResponse to add a cookie.
     * @return ResponseEntity containing the UserID if authentication is successful, else returns UNAUTHORIZED status code.
     */
    @PostMapping("/api/user/validate")
    public ResponseEntity getUserIdByUsPw(@RequestBody Map<String, Object> requestBody, HttpServletResponse response) {
        String username = (String) requestBody.get("Username");
        String password = (String) requestBody.get("Password");

        int userID = userService.getUserIdByUsPw(username, password);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("UserID", userID);

        if (userID != -1) {
            Cookie cookie = new Cookie("ServerUserID", String.valueOf(userID));
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(30 * 24 * 60 * 60);
            response.addCookie(cookie);
            return ResponseEntity.ok(Map.of("UserID", userID));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Account creation failed"));
        }
    }


    /***
     * API endpoint for retrieving account information via UserID of User.
     *
     * @param requestBody Object containing UserID.
     * @return ResponseEntity containing account information or UNAUTHORIZED status code if UserID is invalid.
     */
    @PostMapping("/api/user/display")
    public ResponseEntity getAccountInfoByUserId(@RequestBody Map<String, Object> requestBody) {
        int userID = Integer.parseInt(requestBody.get("UserID").toString());

        Map<String, Object> userInfo = userService.getAccountInfoByUserId(userID);
        if (userInfo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserID is invalid");
        }

        return ResponseEntity.ok(userInfo);
    }


    /***
     * API endpoint for retrieving full account information via UserID of User.
     *
     * @param requestBody Object containing UserID.
     * @return ResponseEntity containing full account information or UNAUTHORIZED status code if UserID is invalid.
     */
    @PostMapping("/api/user/info")
    public ResponseEntity getFullAccountInfoByUserId(@RequestBody Map<String, Object> requestBody) {
        int userID = Integer.parseInt(requestBody.get("UserID").toString());

        Map<String, Object> userInfo = userService.getFullAccountInfoByUserId(userID);
        if (userInfo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserID is invalid");
        }

        return ResponseEntity.ok(userInfo);
    }


    /***
     * API endpoint for retrieving email information via UserID of User.
     *
     * @param requestBody Object containing UserID.
     * @return ResponseEntity containing email information or UNAUTHORIZED status code if UserID is invalid.
     */
    @PostMapping("/api/user/email")
    public ResponseEntity getEmailByUserId(@RequestBody Map<String, Object> requestBody) {
        int userID = Integer.parseInt(requestBody.get("UserID").toString());

        String email = userService.getEmailByUserId(userID);
        if (email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserID is invalid");
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("Email", email);

        return ResponseEntity.ok(userInfo);
    }


    /***
     * API endpoint for retrieving all users being followed by the user via UserID.
     *
     * @param requestBody Object containing UserID.
     * @return ResponseEntity containing followed users' information or 0 if no followed users found.
     */
    @PostMapping("/api/user/follow")
    public ResponseEntity getAllFollowUserByUserID(@RequestBody Map<String, Object> requestBody) {
        int userID = Integer.parseInt(requestBody.get("UserID").toString());

        List<Map<String, Object>> usersInfo = userService.getAllFollowUserByUserID(userID);
        if (usersInfo.isEmpty()) {
            return ResponseEntity.ok(0);
        }

        return ResponseEntity.ok(usersInfo);
    }


    /***
     * API endpoint for retrieving all users following the user via UserID.
     *
     * @param requestBody Object containing UserID.
     * @return ResponseEntity containing users following the user or 0 if no followers found.
     */
    @PostMapping("/api/user/following")
    public ResponseEntity getAllFollowingUserByUserID(@RequestBody Map<String, Object> requestBody) {
        int userID = Integer.parseInt(requestBody.get("UserID").toString());

        List<Map<String, Object>> usersInfo = userService.getAllFollowingUserByUserID(userID);
        if (usersInfo.isEmpty()) {
            return ResponseEntity.ok(0);
        }

        return ResponseEntity.ok(usersInfo);
    }


    /***
     * API endpoint for creating a new user account.
     *
     * @param requestBody: Object containing username, password, and email.
     * @return ResponseEntity: Response indicating success or failure of account creation.
     */
    @PutMapping("/api/user/create")
    public ResponseEntity<Map<String, String>> createNewAccount(@RequestBody Map<String, Object> requestBody) {
        String username, password, email;
        username = requestBody.get("Username").toString();
        password = requestBody.get("Password").toString();
        email = requestBody.get("Email").toString();

        int result = userService.createNewAccount(username, password, email);
        Map<String, String> response = new HashMap<>();

        if (result == UserService.USERNAME_EXISTS) {
            response.put("message", "Username already exists.");
            return ResponseEntity.badRequest().body(response);
        }
        if (result == UserService.EMAIL_EXISTS) {
            response.put("message", "Email already exists.");
            return ResponseEntity.badRequest().body(response);
        }
        if (result == UserService.FAILURE) {
            response.put("message", "Failed to create new account.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("message", "Create new account successfully");
        return ResponseEntity.ok(response);
    }


    /***
     * API endpoint for changing the password of a user.
     *
     * @param requestBody: Object containing UserID and new password.
     * @return ResponseEntity: Response indicating success or failure of password change.
     */
    @PutMapping("/api/user/changePassword")
    public ResponseEntity changePassword(@RequestBody Map<String, Object> requestBody) {
        String email = requestBody.get("Email").toString();
        String newPassword = requestBody.get("NewPassword").toString();

        int result = userService.changePassword(email, newPassword);
        tokenService.deleteToken(email);

        if (result == -1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to update password."));
        }

        return ResponseEntity.ok(Map.of("message", "Update password successfully."));
    }


    /***
     * API endpoint to validate email and send token value for forgot password.
     *
     * @param requestBody Object containing Email.
     * @return ResponseEntity indicating success or failure of sending token value.
     */
    @PostMapping("/api/user/sendTokenValueToForgotPassword")
    public ResponseEntity<?> validateEmail(@RequestBody Map<String, Object> requestBody) {
        String email = requestBody.get("Email").toString();

        if (!userService.isExitsEmail(email)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Email does not exit!"));
        }

        int result = -1;
        result = emailService.sendEmail(email);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Send authentication code successfully."));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Send authentication code failure."));
    }


    /***
     * API endpoint to validate authentication code.
     *
     * @param requestBody Object containing AuthenticationCode and Email.
     * @return ResponseEntity indicating success or failure of validating token value.
     */
    @PostMapping("api/user/validateTokenValue")
    public ResponseEntity validateTokenValue(@RequestBody Map<String, Object> requestBody) {
        String tokenValue =requestBody.get("TokenValue").toString();
        String email = requestBody.get("Email").toString();

        if (tokenService.isTokenValid(email, tokenValue)) {
            return ResponseEntity.ok(Map.of("message", "The token value email successfully"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Token value is invalid"));
    }


    /***
     * API endpoint to send token value to email for sign up.
     *
     * @param requestBody Object containing Email.
     * @return ResponseEntity indicating success or failure of sending token value.
     */
    @PostMapping("api/user/sendTokenValueToSignUp")
    public ResponseEntity sendTokenValueToEmail(@RequestBody Map<String, Object> requestBody) {
        String email = requestBody.get("Email").toString();

        if (userService.isExitsEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already exists!"));
        }

        int result = -1;
        result = emailService.sendEmail(email);
        if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Send authentication code successfully."));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Send authentication code failure."));
    }

}
