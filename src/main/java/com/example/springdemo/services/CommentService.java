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
     * This constance is a SQL query to get all
     * comments of an article by using article id
     * */
    private static final String GET_ALL =               "SELECT cm.CommentID, us.UserID, us.DisplayName, us.AvatarURL, cm.Content, cm.CreatedAt \n" +
                                                        "FROM [Comment] AS cm\n" +
                                                        "INNER JOIN [User] AS us \n" +
                                                        "ON cm.[UserID] = us.[UserID]\n" +
                                                        "WHERE cm.[ArticleID] = ?" +
                                                        "ORDER BY cm.[CreatedAt] DESC";

    /**
     * This constance is a SQL query to get comment's
     * quantity of an article by using article id
     * */
    private static final String GET_QUANTITY =          "SELECT COUNT([Comment].[CommentID]) AS Quantity \n" +
                                                        "FROM [Comment]\n" +
                                                        "WHERE [Comment].[ArticleID] = ?";

    /**
     * This constance is a SQL query to get the last comment
     * of an article by using article id and user id
     * */
    private static final String GET_LATEST_COMMENT =    "SELECT TOP(1) cm.CommentID, us.UserID, us.DisplayName, us.AvatarURL, cm.Content, cm.CreatedAt \n" +
                                                        "FROM [Comment] AS cm\n" +
                                                        "INNER JOIN [User] AS us \n" +
                                                        "ON cm.[UserID] = us.[UserID]\n" +
                                                        "WHERE cm.[ArticleID] = ? AND cm.[UserID] = ?\n" +
                                                        "ORDER BY cm.[CreatedAt] DESC";

    /**
     * This constance is a SQL insert query to add
     * a new comment
     * */
    private static final String CREATE_NEW_COMMENT =    "INSERT INTO [Comment]([UserID], [ArticleID], [Content], [CreatedAt])\n" +
                                                        "VALUES (?,?,?,SYSDATETIME())";


    /**
     * This constance is a SQL update query to update a
     * comment's content by using user id and comment id
     * */
    private static final String UPDATE_COMMENT =        "UPDATE [Comment]\n" +
                                                        "SET [Content] = ?, [CreatedAt] = SYSDATETIME()\n" +
                                                        "WHERE [UserID] = ? AND [CommentID] = ?";


    /**
     * This constance is a SQL delete query to delete
     * a comment by using user id and comment id
     * */
    private static final String DELETE_COMMENT =        "DELETE FROM [Comment] WHERE [CommentID] = ? AND [UserID] = ?";

    /**
     * This function return all comments in an article
     * by using article ID
     * @param articleId Article ID
     * */
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

    /**
     * This function return the comment's quantity of an
     * article by using article ID.
     * @param articleId Article ID
     * */
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

    /**
     * This function return the latest comment that create
     * by a user in an article by using user ID and article
     * ID.
     * @param userId User ID
     * @param articleId article ID
     * */
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

    /**
     * This function create a comment by using
     * user ID, comment ID and comment's content.
     * If delete success, return true.
     * Otherwise, return false.
     * @param userId User ID
     * @param articleId articleID
     * @param commentContent Comment's content
     * */
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

    /**
     * This function update a comment's content by using
     * user ID, comment ID and new comment's content.
     * If delete success, return true.
     * Otherwise, return false.
     * @param userId User ID
     * @param commentId Comment ID
     * @param newContent New comment's content
     * */
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

    /**
     * This function delete a comment by using comment ID and user ID.
     * If delete success, return true.
     * Otherwise, return false.
     * @param userId User ID
     * @param commentId Comment ID
     * */
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

}
