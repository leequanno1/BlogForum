package com.example.springdemo.services;

import com.example.springdemo.security.EncoderUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class UserService extends DatabaseService {

    // Constants to represent different return states
    public static final int USERNAME_EXISTS = -1;

    public static final int EMAIL_EXISTS = -2;

    public static final int SUCCESS = 1;

    public static final int FAILURE = 0;

    /**
     * Checks if the given username already exists in the database.
     *
     * @param username Username of the user.
     * @return True if username exists, otherwise false.
     */
    public boolean isExitsUsername(String username) {
        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT Username FROM [User] WHERE Username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return true;
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }


    /**
     * Checks if the given email already exists in the database.
     *
     * @param email Email of the user.
     * @return True if email exists, otherwise false.
     */
    public boolean isExitsEmail(String email) {
        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT Username FROM [User] WHERE Email = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, email);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return true;
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }


    /**
     * Retrieves Username via UserID.
     *
     * @param userId UserID of the user.
     * @return Username of the User.
     */
    public String getUsernameByUserId(int userId) {
        String username = "";
        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT Username FROM [User] WHERE UserId = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        username = resultSet.getString("Username");
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return username;
    }


    /**
     * Retrieves the UserID associated with a username from the database.
     *
     * @param username Username of the user.
     * @param password Password of the user.
     * @return The UserID if authentication is successful, otherwise returns -1.
     */
    public int getUserIdByUsPw(String username, String password) {
        int userID = -1;

        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT UserId, Password FROM [User] WHERE Username = ? COLLATE Latin1_General_CS_AS";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String hashedPassword = resultSet.getString("password");
                        if (EncoderUtil.matches(password, hashedPassword)) {
                            userID = resultSet.getInt("UserId");
                        }
                    }
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return userID;
    }


    /**
     * Get userID via email of the user.
     *
     * @param email Email of the user.
     * @return UserID of the user.
     */
    public int getUserIdByEmail(String email) {
        int userID = -1;

        String query = "SELECT UserID FROM [User] WHERE Email = ?";
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userID = resultSet.getInt("UserID");
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return userID;
    }


    /**
     * Retrieves email information associated with a UserID from the database.
     *
     * @param userId The UserID of the user.
     * @return The email address of the user if UserID is valid, otherwise an empty string.
     */
    public String getEmailByUserId(int userId) {
        String email = "";

        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT Email FROM \"User\" WHERE UserID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        email = resultSet.getString("Email");

                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return email;
    }


    /**
     * Retrieves account information associated with a UserID from the database.
     *
     * @param userId The UserID of the user.
     * @return A map containing account information of the user if UserID is valid, otherwise an empty map.
     */
    public Map<String, Object> getAccountInfoByUserId(int userId) {
        Map<String, Object> userInfo = new HashMap<>();

        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT Username, DisplayName, AvatarURL FROM \"User\" WHERE UserID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        userInfo.put("Username", resultSet.getString("Username"));
                        userInfo.put("DisplayName", resultSet.getString("DisplayName"));
                        userInfo.put("AvatarURL", resultSet.getString("AvatarURL"));
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return userInfo;
    }


    /**
     * Retrieves all account information associated with a UserID from the database.
     *
     * @param userId The UserID of the user.
     * @return A map containing all account information of the user if UserID is valid, otherwise an empty map.
     */
    public Map<String, Object> getFullAccountInfoByUserId(int userId) {
        Map<String, Object> userInfo = new HashMap<>();

        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT UserID, Username, Email, Password, AvatarURL, Description FROM \"User\" WHERE UserID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        userInfo.put("UserID", resultSet.getInt("UserID"));
                        userInfo.put("Username", resultSet.getString("Username"));
                        userInfo.put("Email", resultSet.getString("Email"));
                        userInfo.put("Password", resultSet.getString("Password"));
                        userInfo.put("AvatarURL", resultSet.getString("AvatarURL"));
                        userInfo.put("Description", resultSet.getString("Description"));
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return userInfo;
    }


    /**
     * Retrieves all users being followed by the user with the given UserID.
     *
     * @param userId The UserID of the user.
     * @return A list of maps containing information of all users being followed by the user.
     */
    public List<Map<String, Object>> getAllFollowUserByUserID(int userId) {
        List<Map<String, Object>> usersInfo = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT F.FollowerID as UserID, U.Username AS Username, U.AvatarURL AS AvatarURL, U.DisplayName AS DisplayName " +
                    "FROM [Follow] F INNER JOIN [User] U ON F.FollowerID = U.UserID " +
                    "WHERE F.FollowedUserID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("UserID", resultSet.getInt("UserID"));
                        userInfo.put("Username", resultSet.getString("Username"));
                        userInfo.put("AvatarURL", resultSet.getString("AvatarURL"));
                        userInfo.put("DisplayName", resultSet.getString("DisplayName"));
                        usersInfo.add(userInfo);
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return usersInfo;
    }


    /**
     * Retrieves all users following the user with the given UserID.
     *
     * @param userId The UserID of the user.
     * @return A list of maps containing information of all users following the user.
     */
    public List<Map<String, Object>> getAllFollowingUserByUserID(int userId) {
        List<Map<String, Object>> usersInfo = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT F.FollowedUserID as UserID, U.Username AS Username, U.AvatarURL AS AvatarURL, U.DisplayName AS DisplayName " +
                    "FROM [Follow] F INNER JOIN [User] U ON F.FollowedUserID = U.UserID " +
                    "WHERE F.FollowerID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("UserID", resultSet.getInt("UserID"));
                        userInfo.put("Username", resultSet.getString("Username"));
                        userInfo.put("AvatarURL", resultSet.getString("AvatarURL"));
                        userInfo.put("DisplayName", resultSet.getString("DisplayName"));
                        usersInfo.add(userInfo);
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return usersInfo;
    }


    /**
     * Creates a new account for a user with the given username, password, and email.
     *
     * @param username Username of the user.
     * @param password Password of the user.
     * @param email    Email of the user.
     * @return SUCCESS if account is created successfully, USERNAME_EXISTS if username already exists,
     * EMAIL_EXISTS if email already exists, FAILURE if account creation fails.
     */
    public int createNewAccount(String username, String password, String email) {
        if (isExitsUsername(username)) {
            return USERNAME_EXISTS;
        }

        if (isExitsEmail(email)) {
            return EMAIL_EXISTS;
        }

        try (Connection connection = getDataSource().getConnection()) {
            String query = "INSERT INTO [User] ([Username], [Email], [Password], [DisplayName]) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, email);
                statement.setString(3, EncoderUtil.encode(password));
                statement.setString(4, username);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    return SUCCESS;
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return FAILURE;
    }


    /**
     * Changes the password of the user with the given UserID.
     *
     * @param email The email of the user.
     * @param newPassword The new password to be set.
     * @return SUCCESS if password is changed successfully, FAILURE if password change fails.
     */
    public int changePassword(String email, String newPassword) {
        try (Connection connection = getDataSource().getConnection()) {
            String query = "UPDATE [User] Set [Password] = ? WHERE [Email] = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, EncoderUtil.encode(newPassword));
                statement.setString(2, email);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    return 1;
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return -1;
    }

}