package com.example.springdemo.services;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("ALL")
public class CommentService extends DatabaseService {
    /**
     *
     * */
    private static final String GET_ALL =               "SELECT cm.CommentID, us.UserID, us.DisplayName, us.AvatarURL, cm.Content, cm.CreatedAt \n" +
                                                        "FROM [Comment] AS cm\n" +
                                                        "INNER JOIN [User] AS us \n" +
                                                        "ON cm.[UserID] = us.[UserID]\n" +
                                                        "WHERE cm.[ArticleID] = ?";

    private static final String GET_QUANTITY =          "SELECT COUNT([Comment].[CommentID]) AS Quantity \n" +
                                                        "FROM [Comment]\n" +
                                                        "WHERE [Comment].[ArticleID] = ?";

    private static final String GET_LATEST_COMMENT =    "SELECT TOP(1) cm.CommentID, us.UserID, us.DisplayName, us.AvatarURL, cm.Content, cm.CreatedAt \n" +
                                                        "FROM [Comment] AS cm\n" +
                                                        "INNER JOIN [User] AS us \n" +
                                                        "ON cm.[UserID] = us.[UserID]\n" +
                                                        "WHERE cm.[ArticleID] = ? AND cm.[UserID] = ?\n" +
                                                        "ORDER BY cm.[CreatedAt] DESC";
    private static final String CREATE_NEW_COMMENT =    "INSERT INTO [Comment]([UserID], [ArticleID], [Content], [CreatedAt])\n" +
                                                        "VALUES (?,?,?,SYSDATETIME())";

    private static final String UPDATE_COMMENT =        "UPDATE [Comment]\n" +
                                                        "SET [Content] = ?, [CreatedAt] = SYSDATETIME()\n" +
                                                        "WHERE [UserID] = ? AND [CommentID] = ?";

    private static final String DELETE_COMMENT =        "DELETE FROM [Comment] WHERE [CommentID] = ? AND [UserID] = ?";

    public List<Object> getAll(int articleId) {
        List<Object> res = new ArrayList<>();
        try (Connection conn = getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(GET_ALL)) {
                statement.setInt(1, articleId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()){
                        Map<String, Object> comment = new HashMap<>();
                        Map<String, Object> user = new HashMap<>();
                        user.put("userId", resultSet.getInt("UserId"));
                        user.put("displayName", resultSet.getString("DisplayName"));
                        user.put("avatarUrl", resultSet.getString("AvatarUrl"));
                        comment.put("commentID", resultSet.getInt("CommentID"));
                        comment.put("content", resultSet.getString("Content"));
                        comment.put("createdAt", resultSet.getDate("CreatedAt"));
                        comment.put("user", user);
                        res.add(comment);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Map<String,Integer> getQuantity(int articleId) {
        Map<String, Integer> res = new HashMap<>();
        try (Connection conn = getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(GET_QUANTITY)) {
                statement.setInt(1, articleId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()){
                        res.put("quantity",resultSet.getInt("Quantity"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Map<String,Object> getLatestComment(int userId, int articleId) {
        Map<String, Object> comment = new HashMap<>();
        try (Connection conn = getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(GET_LATEST_COMMENT)) {
                statement.setInt(1, userId);
                statement.setInt(2, articleId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()){
                        Map<String, Object> user = new HashMap<>();
                        user.put("userId", resultSet.getInt("UserId"));
                        user.put("displayName", resultSet.getString("DisplayName"));
                        user.put("avatarUrl", resultSet.getString("AvatarUrl"));
                        comment.put("commentID", resultSet.getInt("CommentID"));
                        comment.put("content", resultSet.getString("Content"));
                        comment.put("createdAt", resultSet.getDate("CreatedAt"));
                        comment.put("user", user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comment;
    }

    public boolean createNewComment(int userId, int articleId, String commentContent) {
        try (Connection conn = getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(CREATE_NEW_COMMENT)) {
                statement.setInt(1, userId);
                statement.setInt(2, articleId);
                statement.setString(3,commentContent);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateComment(int userId, int commentId, String newContent) {
        try (Connection connection = getDataSource().getConnection()){
            try(PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COMMENT)){
                preparedStatement.setString(1, newContent);
                preparedStatement.setInt(2, userId);
                preparedStatement.setInt(3, commentId);

                int updatedRow = preparedStatement.executeUpdate();
                if(updatedRow > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteComment(int userId, int commentId) {
        try(Connection connection = getDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COMMENT)) {
                preparedStatement.setInt(1, commentId);
                preparedStatement.setInt(2, userId);

                int rowDeleted = preparedStatement.executeUpdate();
                if(rowDeleted > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Map<String, String> createMessage(String message) {
        Map<String, String> res = new HashMap<>();
        res.put("message", message);
        return res;
    }
}
