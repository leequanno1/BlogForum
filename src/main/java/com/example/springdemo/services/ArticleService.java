package com.example.springdemo.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleService extends DatabaseService{

    private static final String GET_VOTE_VALUE =    "SELECT SUM(CASE WHEN VoteValue = 1 THEN 1 ELSE -1 END) AS TotalVotes\n" +
                                                    "FROM Vote\n" +
                                                    "WHERE ArticleID = ?\n" +
                                                    "GROUP BY ArticleID";

    private static final String GET_TAG_LIST =      "SELECT [Tag].* FROM [Tag]\n" +
                                                    "INNER JOIN [ArticleTag] ON [Tag].[TagID] = [ArticleTag].[TagID]\n" +
                                                    "WHERE [ArticleTag].[ArticleID] = ?";

    private static final String GET_ALL_TOP =       "SELECT * FROM (\n" +
                                                    "\tSELECT ar.[ArticleID], ar.[Title], ar.[CreatedAt], \n" +
                                                    "\tus.[UserID], us.[AvatarURL], us.[Username], us.[DisplayName], \n" +
                                                    "\tCOUNT(cmt.CommentID) as Comments,\n" +
                                                    "\tROW_NUMBER() OVER (ORDER BY ar.[ArticleID]) AS RowNum\n" +
                                                    "\tFROM [Article] as ar\n" +
                                                    "\tINNER JOIN [User] as us ON ar.[UserID] = us.[UserID]\n" +
                                                    "\tLEFT JOIN [Comment] as cmt ON ar.ArticleID = cmt.ArticleID\n";

    private static final String GET_ALL_BOTTOM =    "\tGROUP BY ar.[ArticleID], ar.[Title], ar.[CreatedAt], \n" +
                                                    "\tus.[UserID], us.[AvatarURL], us.[Username], us.[DisplayName]\n" +
                                                    ") AS RowConstrainedResult\n" +
                                                    "WHERE RowNum BETWEEN ? AND ?";

    private static final String GET_ALL =           GET_ALL_TOP + GET_ALL_BOTTOM;
    private static final String GET_BY_USERID =     GET_ALL_TOP +
                                                    "WHERE us.[UserID] = ?" +
                                                    GET_ALL_BOTTOM;
    private static final String GET_BY_TITLE =      GET_ALL_TOP +
                                                    "WHERE ar.[Title] LIKE ?" +
                                                    GET_ALL_BOTTOM;
    private static final String GET_BY_TAG_NAME =   GET_ALL_TOP +
                                                    "\tINNER JOIN [ArticleTag] AS att ON att.ArticleID = ar.ArticleID\n" +
                                                    "\tINNER JOIN [Tag] ON [Tag].TagID = att.TagID\n" +
                                                    "\tWHERE [Tag].TagName = ?" +
                                                    GET_ALL_BOTTOM;
    private static final String GET_FROM_DATE =     GET_ALL_TOP +
                                                    "WHERE ar.[CreatedAt] >= ?" +
                                                    GET_ALL_BOTTOM;
    private static final String GET_ALL_BOOKMARK =  GET_ALL_TOP +
                                                    "\tINNER JOIN [Bookmark] as bm ON bm.ArticleID = ar.ArticleID\n" +
                                                    "\tWHERE bm.UserID = ?" +
                                                    GET_ALL_BOTTOM;
    private static final String GET_BY_ARTICLE_ID = "SELECT ar.[ArticleID], ar.[Title], ar.[CreatedAt], ar.Content, \n" +
                                                    "us.[UserID], us.[AvatarURL], us.[Username], us.[DisplayName], \n" +
                                                    "(SELECT CASE \n" +
                                                    "\tWHEN EXISTS (SELECT 1 FROM Vote WHERE VoteUserID = ? AND ArticleID = ?) \n" +
                                                    "\tTHEN (SELECT VoteValue FROM Vote WHERE VoteUserID = ? AND ArticleID = ?)\n" +
                                                    "\tELSE NULL END\n" +
                                                    ") as VotedValue\n" +
                                                    "FROM [Article] as ar\n" +
                                                    "INNER JOIN [User] as us ON ar.[UserID] = us.[UserID]\n" +
                                                    "WHERE ar.[ArticleID] = ?";

    private int getVoteValue(int articleID) {
        int res = 0;
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_VOTE_VALUE)) {
                preparedStatement.setInt(1, articleID);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    res = resultSet.getInt("TotalVotes");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private List<Object> getTagList(int articleID) {
        List<Object> res = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_TAG_LIST)) {
                preparedStatement.setInt(1, articleID);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("tagID", resultSet.getInt("TagID"));
                    row.put("tagName", resultSet.getString("TagName"));
                    res.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void handleGetArticleList(List<Object> res, PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Map<String,Object> row = new HashMap<>();
            Map<String,Object> user = new HashMap<>();
            List<Object> tags = getTagList(resultSet.getInt("ArticleID"));
            row.put("articleID", resultSet.getInt("ArticleID"));
            row.put("title", resultSet.getString("Title"));
            row.put("createdAt", resultSet.getString("createdAt"));
            row.put("comments", resultSet.getString("comments"));
            row.put("votes", getVoteValue(resultSet.getInt("ArticleID")));
            user.put("userID", resultSet.getInt("userID"));
            user.put("avatarURL", resultSet.getString("avatarURL"));
            user.put("username", resultSet.getString("username"));
            user.put("displayName", resultSet.getString("displayName"));
            row.put("user", user);
            row.put("tags", tags);
            res.add(row);
        }
    }

    public List<Object> getAll(int quantity, int page) {
        List<Object> res = new ArrayList<>();
        try(Connection connection = getDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL)) {
                preparedStatement.setInt(1, quantity*(page-1) + 1);
                preparedStatement.setInt(2, quantity*(page-1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Object> getAllByUserID(int userID ,int quantity, int page) {
        List<Object> res = new ArrayList<>();
        try(Connection connection = getDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_USERID)) {
                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, quantity*(page-1) + 1);
                preparedStatement.setInt(3, quantity*(page-1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Object> getAllByTitle(String title ,int quantity, int page) {
        List<Object> res = new ArrayList<>();
        try(Connection connection = getDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_TITLE)) {
                preparedStatement.setString(1, "%" + title + "%");
                preparedStatement.setInt(2, quantity*(page-1) + 1);
                preparedStatement.setInt(3, quantity*(page-1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Object> getAllByTagName(String tagName ,int quantity, int page) {
        List<Object> res = new ArrayList<>();
        try(Connection connection = getDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_TAG_NAME)) {
                preparedStatement.setString(1, tagName);
                preparedStatement.setInt(2, quantity*(page-1) + 1);
                preparedStatement.setInt(3, quantity*(page-1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Object> getFromDate(String fromDate ,int quantity, int page) {
        List<Object> res = new ArrayList<>();
        try(Connection connection = getDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(GET_FROM_DATE)) {
                preparedStatement.setDate(1, Date.valueOf(fromDate));
                preparedStatement.setInt(2, quantity*(page-1) + 1);
                preparedStatement.setInt(3, quantity*(page-1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Object> getAllBookmark(int userID ,int quantity, int page) {
        List<Object> res = new ArrayList<>();
        try(Connection connection = getDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BOOKMARK)) {
                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, quantity*(page-1) + 1);
                preparedStatement.setInt(3, quantity*(page-1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Map<String, Object> getByArticleId(Integer userId, Integer articleId) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        List<Object> tags = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ARTICLE_ID)) {
                preparedStatement.setInt(1, userId == null? 0: userId);
                preparedStatement.setInt(2, articleId);
                preparedStatement.setInt(3, userId == null? 0: userId);
                preparedStatement.setInt(4, articleId);
                preparedStatement.setInt(5, articleId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    res.put("articleID",resultSet.getInt("ArticleID"));
                    res.put("title", resultSet.getString("Title"));
                    res.put("content", resultSet.getString("Content"));
                    res.put("createdAt", resultSet.getString("CreatedAt"));
                    res.put("votedValue",resultSet.getString("VotedValue") == null? null : resultSet.getBoolean("VotedValue"));
                    res.put("votes",getVoteValue(resultSet.getInt("ArticleID")));
                    user.put("userID",resultSet.getInt("ArticleID"));
                    user.put("avatarURL",resultSet.getString("AvatarURL"));
                    user.put("username",resultSet.getString("Username"));
                    user.put("displayName",resultSet.getString("DisplayName"));
                    res.put("user",user);
                    res.put("tags",getTagList(resultSet.getInt("ArticleID")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
