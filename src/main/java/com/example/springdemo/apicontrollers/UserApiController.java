package com.example.springdemo.apicontrollers;

import com.example.springdemo.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@RestController
public class UserApiController {

    /***
     * Service to manipulate user information.
     */
    public final UserService userService = new UserService();


    /***
     * API endpoint for user authentication via username and password.
     * @param requestBody Object containing username and password.
     * @return ResponseEntity containing the UserID if authentication is successful, else returns UNAUTHORIZED status code.
     */
    @PostMapping("/api/user/validate")
    public ResponseEntity getUserIdByUsPw(@RequestBody Map<String, Object> requestBody) {
        String Username = (String) requestBody.get("Username");
        String Password = (String) requestBody.get("Password");

        int userID = userService.getUserIdByUsPw(Username, Password);
        if (userID == -1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserID is invalid");
        }

        return ResponseEntity.ok(userID);
    }


    /***
     * API endpoint for retrieving account information via UserID of User.
     * @param requestBody: Object containing UserID.
     * @return ResponseEntity: Response containing account information or UNAUTHORIZED status code if UserID is invalid.
     */
    @PostMapping("/api/user/display")
    public ResponseEntity getAccountInfoByUserId(@RequestBody Map<String, Object> requestBody) {
        int UserID = Integer.parseInt(requestBody.get("UserID").toString());

        Map<String, Object> userInfo = userService.getAccountInfoByUserId(UserID);
        if (userInfo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserID is invalid");
        }

        return ResponseEntity.ok(userInfo);
    }


    /***
     * API endpoint for retrieving full account information via UserID of User.
     * @param requestBody: Object containing UserID.
     * @return ResponseEntity: Response containing full account information or UNAUTHORIZED status code if UserID is invalid.
     */
    @PostMapping("/api/user/info")
    public ResponseEntity getFullAccountInfoByUserId(@RequestBody Map<String, Object> requestBody) {
        int UserID = Integer.parseInt(requestBody.get("UserID").toString());

        Map<String, Object> userInfo = userService.getFullAccountInfoByUserId(UserID);
        if (userInfo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserID is invalid");
        }

        return ResponseEntity.ok(userInfo);
    }


    /***
     * API endpoint for retrieving email information via UserID of User.
     * @param requestBody: Object containing UserID.
     * @return ResponseEntity: Response containing email information or UNAUTHORIZED status code if UserID is invalid.
     */
    @PostMapping("/api/user/email")
    public ResponseEntity getEmailByUserId(@RequestBody Map<String, Object> requestBody) {
        int UserID = Integer.parseInt(requestBody.get("UserID").toString());

        String email = userService.getEmailByUserId(UserID);
        if (email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserID is invalid");
        }

        return ResponseEntity.ok(email);
    }


    /***
     * API endpoint for retrieving all users being followed by the user via UserID.
     * @param requestBody: Object containing UserID.
     * @return ResponseEntity: Response containing followed users' information or 0 if no followed users found.
     */
    @PostMapping("/api/user/follow")
    public ResponseEntity getAllFollowUserByUserID(@RequestBody Map<String, Object> requestBody) {
        int UserID = Integer.parseInt(requestBody.get("UserID").toString());

        List<Map<String, Object>> usersInfo = userService.getAllFollowUserByUserID(UserID);
        if (usersInfo.isEmpty()) {
            return ResponseEntity.ok(0);
        }

        return ResponseEntity.ok(usersInfo);
    }


    /***
     * API endpoint for retrieving all users following the user via UserID.
     * @param requestBody: Object containing UserID.
     * @return ResponseEntity: Response containing users following the user or 0 if no followers found.
     */
    @PostMapping("/api/user/following")
    public ResponseEntity getAllFollowingUserByUserID(@RequestBody Map<String, Object> requestBody) {
        int UserID = Integer.parseInt(requestBody.get("UserID").toString());

        List<Map<String, Object>> usersInfo = userService.getAllFollowingUserByUserID(UserID);
        if (usersInfo.isEmpty()) {
            return ResponseEntity.ok(0);
        }

        return ResponseEntity.ok(usersInfo);
    }


    /***
     * API endpoint for creating a new user account.
     * @param requestBody: Object containing username, password, and email.
     * @return ResponseEntity: Response indicating success or failure of account creation.
     */
    @PutMapping("/api/user/create")
    public ResponseEntity createNewAccount(@RequestBody Map<String, Object> requestBody) {
        String Username, Password, Email;
        Username = requestBody.get("Username").toString();
        Password = requestBody.get("Password").toString();
        Email = requestBody.get("Email").toString();

        int result = userService.createNewAccount(Username, Password, Email);
        if (result == UserService.USERNAME_EXISTS) {
            return ResponseEntity.badRequest().body("Username already exists.");
        }
        if (result == UserService.EMAIL_EXISTS) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }
        if (result == UserService.FAILURE) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create new account.");
        }

        return ResponseEntity.ok("Create new account successfully");
    }


    /***
     * API endpoint for changing the password of a user.
     * @param requestBody: Object containing UserID and new password.
     * @return ResponseEntity: Response indicating success or failure of password change.
     */
    @PutMapping("/api/user/changePassword")
    public ResponseEntity changePassword(@RequestBody Map<String, Object> requestBody) {
        int UserID = Integer.parseInt(requestBody.get("UserID").toString());
        String NewPassword = requestBody.get("NewPassword").toString();

        int result = userService.changePassword(UserID, NewPassword);
        if (result == UserService.FAILURE) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update password.");
        }

        return ResponseEntity.ok("Update password successfully.");
    }


}
