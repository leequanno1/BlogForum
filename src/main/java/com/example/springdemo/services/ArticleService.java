package com.example.springdemo.services;

import org.springframework.web.multipart.MultipartFile;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class ArticleService extends DatabaseService {

    /**
     * SQL query to get Vote value in an article by using article id
     */
    private static final String GET_VOTE_VALUE                  =   " SELECT SUM(CASE WHEN VoteValue = 1 THEN 1 ELSE -1 END) AS TotalVotes\n" +
                                                                    "FROM Vote\n" +
                                                                    "WHERE ArticleID = ?\n" +
                                                                    "GROUP BY ArticleID";

    /**
     * SQL query to get a list of tag in an article by using article id
     */
    private static final String GET_TAG_LIST                    =   "SELECT [Tag].* FROM [Tag]\n" +
                                                                    "INNER JOIN [ArticleTag] ON [Tag].[TagID] = [ArticleTag].[TagID]\n" +
                                                                    "WHERE [ArticleTag].[ArticleID] = ?";

    /**
     * Sub SQL query: the top part of GET_ALL query
     */
    private static final String GET_ALL_TOP                     =   "SELECT * FROM (\n" +
                                                                    "\tSELECT ar.[ArticleID], ar.[Title], ar.[CreatedAt], \n" +
                                                                    "\tus.[UserID], us.[AvatarURL], us.[Username], us.[DisplayName], \n" +
                                                                    "\tCOUNT(cmt.CommentID) as Comments,\n" +
                                                                    "\tROW_NUMBER() OVER (ORDER BY ar.[CreatedAt] DESC) AS RowNum\n" +
                                                                    "\tFROM [Article] as ar\n" +
                                                                    "\tINNER JOIN [User] as us ON ar.[UserID] = us.[UserID]\n" +
                                                                    "\tLEFT JOIN [Comment] as cmt ON ar.ArticleID = cmt.ArticleID\n";

    /**
     * Sub SQL query: the bottom part of GET_ALL query
     */
    private static final String GET_ALL_BOTTOM                  =   "\tGROUP BY ar.[ArticleID], ar.[Title], ar.[CreatedAt], \n" +
                                                                    "\tus.[UserID], us.[AvatarURL], us.[Username], us.[DisplayName]\n" +
                                                                    ") AS RowConstrainedResult\n" +
                                                                    "WHERE RowNum BETWEEN ? AND ?";

    /**
     * SQL query to get a list of all article of a page
     */
    private static final String GET_ALL                         =   GET_ALL_TOP + GET_ALL_BOTTOM;

    /**
     * SQL query to get a list of all article of a page by using user id
     */
    private static final String GET_BY_USERID                   =   GET_ALL_TOP +
                                                                    "WHERE us.[UserID] = ?" +
                                                                    GET_ALL_BOTTOM;

    private static final String GET_BY_USERNAME                   =    GET_ALL_TOP +
                                                                     "WHERE us.[UserName] = ?" +
                                                                     GET_ALL_BOTTOM;

    /**
     * SQL query to get a list of all article of a page by using article title
     */
    private static final String GET_BY_TITLE                    =   GET_ALL_TOP +
                                                                    "WHERE ar.[Title] LIKE ?" +
                                                                    GET_ALL_BOTTOM;

    /**
     * SQL query to get a list of all article of a page by using tag name
     */
    private static final String GET_BY_TAG_NAME                 =   GET_ALL_TOP +
                                                                    "\tINNER JOIN [ArticleTag] AS att ON att.ArticleID = ar.ArticleID\n" +
                                                                    "\tINNER JOIN [Tag] ON [Tag].TagID = att.TagID\n" +
                                                                    "\tWHERE [Tag].TagName = ?" +
                                                                    GET_ALL_BOTTOM;

    /**
     * SQL query to get a list of all article of a page from a specific date
     */
    private static final String GET_FROM_DATE                   =   GET_ALL_TOP +
                                                                    "WHERE ar.[CreatedAt] >= ?" +
                                                                    GET_ALL_BOTTOM;

    /**
     * SQL query to get a list of all article of a page bookmarked by a user
     */
    private static final String GET_ALL_BOOKMARK                =   GET_ALL_TOP +
                                                                    "\tINNER JOIN [Bookmark] as bm ON bm.ArticleID = ar.ArticleID\n" +
                                                                    "\tWHERE bm.UserID = ?" +
                                                                    GET_ALL_BOTTOM;

    /**
     * SQL query to get article information by using article id
     */
    private static final String GET_BY_ARTICLE_ID               =   "SELECT ar.[ArticleID], ar.[Title], ar.[CreatedAt], ar.Content, \n" +
                                                                    "us.[UserID], us.[AvatarURL], us.[Username], us.[DisplayName], \n" +
                                                                    "(SELECT CASE \n" +
                                                                    "\tWHEN EXISTS (SELECT 1 FROM Vote WHERE VoteUserID = ? AND ArticleID = ?) \n" +
                                                                    "\tTHEN (SELECT VoteValue FROM Vote WHERE VoteUserID = ? AND ArticleID = ?)\n" +
                                                                    "\tELSE NULL END\n" +
                                                                    ") as VotedValue\n" +
                                                                    "FROM [Article] as ar\n" +
                                                                    "INNER JOIN [User] as us ON ar.[UserID] = us.[UserID]\n" +
                                                                    "WHERE ar.[ArticleID] = ?";

    /**
     * SQL query to insert a new article into the Article table
     */
    private static final String INSERT_ARTICLE                  =   "INSERT INTO [Article]([UserID], [Title], [Content] ,[CreatedAt]) VALUES\n" +
                                                                    "(?,?,?, SYSDATETIME())";
    /**
     * SQL query to insert a new article-tag relationship into the ArticleTag table
     */
    private static final String INSERT_ARTICLETAG               =   "INSERT INTO [ArticleTag]([ArticleID],[TagID]) VALUES (?,?)";

    /**
     * SQL query to get the latest article ID for a specific user
     */
    private static final String GET_LASTEST                     =   "SELECT TOP(1) ArticleID FROM [Article]\n" +
                                                                    "WHERE UserID = ?" +
                                                                    "ORDER BY CreatedAt DESC";
    /**
     * SQL query to get the total number of articles
     */
    private static final String GET_TOTAL_ARTICLE               =   "SELECT COUNT(*) AS totalArticle FROM [Article]";

    private static final String GET_TOTAL_ARTICLE_BY_USERNAME   = "SELECT COUNT(atc.UserID) AS TotalArticle FROM [Article] atc\n" +
                                                                  "INNER JOIN [User] us ON atc.UserID = us.UserID\n" +
                                                                  "WHERE us.Username = ?";
    private static final String GET_TOTAL_ARTICLE_BY_USERID     = "SELECT COUNT(UserID) AS TotalArticle FROM [Article] \n" +
                                                                  "WHERE UserID = ?";

    /**
     * SQL query to get the total number of articles for a specific tag
     */
    private static final String GET_TOTAL_ARTICLE_BY_TAG        =   "SELECT COUNT(DISTINCT a.ArticleID) AS totalArticle " +
                                                                    "FROM Article a " +
                                                                    "JOIN ArticleTag at ON a.ArticleID = at.ArticleID " +
                                                                    "JOIN Tag t ON at.TagID = t.TagID " +
                                                                    "WHERE t.TagName = ?";

    /**
     * SQL query to get the total number of articles from a specific date onwards
     */
    private static final String GET_TOTAL_ARTICLE_BY_FROM_DATE  =   "SELECT COUNT(a.ArticleID) AS totalArticle " +
                                                                    "FROM Article a " +
                                                                    "WHERE a.[CreatedAt] >= ?";

    /**
     * SQL query to get the top 5 most popular tags
     */
    private static final String GET_POPULAR_TAGS                =   "SELECT TOP 5 t.TagName, COUNT(at.TagID) AS tagCount " +
                                                                    "FROM Tag t " +
                                                                    "JOIN ArticleTag at ON t.TagID = at.TagID " +
                                                                    "GROUP BY t.TagName " +
                                                                    "ORDER BY tagCount DESC";


    /**
     * SQL query to get the total number of articles with a specific title
     */
    private static final String GET_TOTAL_ARTICLE_BY_TITLE      =   "SELECT COUNT(a.ArticleID) AS totalArticle " +
                                                                    "FROM Article a " +
                                                                    "WHERE a.[Title] LIKE ?";

    /**
     * SQL query to get the remaining tags that are not in the top 5 most popular tags and appear in articles
     */
    private static final String GET_REMAINING_TAGS              =   "SELECT t.TagName " +
                                                                    "FROM Tag t " +
                                                                    "JOIN ArticleTag at ON t.TagID = at.TagID " +
                                                                    "GROUP BY t.TagName " +
                                                                    "HAVING COUNT(at.TagID) > 0 AND t.TagName NOT IN ( " +
                                                                    "    SELECT TOP 5 t.TagName " +
                                                                    "    FROM Tag t " +
                                                                    "    JOIN ArticleTag at ON t.TagID = at.TagID " +
                                                                    "    GROUP BY t.TagName " +
                                                                    "    ORDER BY COUNT(at.TagID) DESC " +
                                                                    ")";

    /**
     * SQL query to retrieve the next 3 articles based on the provided article ID
     */
    private static final String GET_NEXT_ARTICLE               =    "SELECT TOP 3 a.ArticleID,a.Title, a.UserID, u.Username, u.DisplayName " +
                                                                    "FROM Article a " +
                                                                    "JOIN [User] u ON a.UserID = u.UserID " +
                                                                    "WHERE a.ArticleID > ? " +
                                                                    "ORDER BY a.ArticleID";

    /**
     * SQL query to upvote an article or insert if the vote doesn't exist
     */
    private static final String UPVOTE_ARTICLE                 =    "IF EXISTS (SELECT 1 FROM Vote WHERE VoteUserID = ? AND ArticleID = ?) " +
                                                                    "BEGIN " +
                                                                    "    UPDATE Vote SET VoteValue = 1 WHERE VoteUserID = ? AND ArticleID = ? " +
                                                                    "END " +
                                                                    "ELSE " +
                                                                    "BEGIN " +
                                                                    "   INSERT INTO Vote (VoteUserID, ArticleID, VoteValue) VALUES (?, ?, 1) " +
                                                                    "END";


    /**
     * SQL query to downvote an article or insert if the vote doesn't exist
     */
    private static final String DOWNVOTE_ARTICLE               =    "IF EXISTS (SELECT 1 FROM Vote WHERE VoteUserID = ? AND ArticleID = ?) " +
                                                                    "BEGIN " +
                                                                    "    UPDATE Vote SET VoteValue = 0 WHERE VoteUserID = ? AND ArticleID = ? " +
                                                                    "END " +
                                                                    "ELSE " +
                                                                    "BEGIN " +
                                                                    "    INSERT INTO Vote (VoteUserID, ArticleID, VoteValue) VALUES (?, ?, 0) " +
                                                                    "END";

    /**
     * SQL query to bookmark an article
     */
    private static final String BOOKMARK_ARTICLE                =    "INSERT INTO Bookmark (UserID, ArticleID) VALUES (?, ?)";

    /**
     * SQL query to unbookmark an article
     */
    private static final String UNBOOKMARK_ARTICLE              =    "DELETE FROM Bookmark WHERE UserID = ? AND ArticleID = ?";

    /**
     * SQL query to check if a bookmark exists for a given user and article
     */
    private static final String CHECK_BOOKMARK_EXISTS           =     "SELECT COUNT(*) AS count FROM Bookmark WHERE UserID = ? AND ArticleID = ?";


    /**
     * Sub service to get the total number of articles.
     *
     * @return the total number of articles
     */
    public int getTotalArticle() {
        int totalArticle = 0;
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_ARTICLE)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    totalArticle = resultSet.getInt("totalArticle");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalArticle;
    }

    public int getTotalArticleByUserID(int userId) {
        int totalArticle = 0;
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_ARTICLE_BY_USERID)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    totalArticle = resultSet.getInt("TotalArticle");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalArticle;
    }

    /**
     * Sub service to get the total number of articles.
     * @return the total number of articles
     */
    public int getTotalArticleByUserName(String username) {
        int totalArticle = 0;
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_ARTICLE_BY_USERNAME)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    totalArticle = resultSet.getInt("TotalArticle");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalArticle;
    }


    /**
     * Sub service to get the total number article by tag.
     *
     * @return The total number acticle by tag.
     */
    public int getTotalArticleByTag(String tagName) {
        int totalArticle = 0;

        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_ARTICLE_BY_TAG)) {
                preparedStatement.setString(1, tagName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    totalArticle = resultSet.getInt("totalArticle");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalArticle;
    }


    /**
     * Sub service to get the total number article by from date.
     *
     * @return The total number acticle by from date.
     */
    public int getTotalArticleByFromDate(String date) {
        int totalArticle = 0;

        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_ARTICLE_BY_FROM_DATE)) {
                preparedStatement.setString(1, date);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    totalArticle = resultSet.getInt("totalArticle");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalArticle;
    }


    /**
     * Sub service to get the total number article by title.
     *
     * @return The total number acticle by title.
     */
    public int getTotalArticleByTitle(String title) {
        int totalArticle = 0;
        String searchTitle = "%" + title + "%";

        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_ARTICLE_BY_TITLE)) {
                preparedStatement.setString(1, searchTitle);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    totalArticle = resultSet.getInt("totalArticle");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalArticle;
    }


    /**
     * Sub service to get vote value of an article by using article ID
     *
     * @param articleID the article's id
     */
    private int getVoteValue(int articleID) {
        int res = 0;
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_VOTE_VALUE)) {
                preparedStatement.setInt(1, articleID);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    res = resultSet.getInt("TotalVotes");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Method to upvote an article
     *
     * @param userId    the ID of the user upvoting the article
     * @param articleId the ID of the article being upvoted
     * @return true if the upvote is successful, false otherwise
     */
    public boolean upVoteArticle(int userId, int articleId) {
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(UPVOTE_ARTICLE)) {
            ps.setInt(1, userId);
            ps.setInt(2, articleId);
            ps.setInt(3, userId);
            ps.setInt(4, articleId);
            ps.setInt(5, userId);
            ps.setInt(6, articleId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Method to downvote an article
     *
     * @param userId    the ID of the user downvoting the article
     * @param articleId the ID of the article being downvoted
     * @return true if the downvote is successful, false otherwise
     */
    public boolean downVoteArticle(int userId, int articleId) {
        try (Connection conn = getDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(DOWNVOTE_ARTICLE)) {
            ps.setInt(1, userId);
            ps.setInt(2, articleId);
            ps.setInt(3, userId);
            ps.setInt(4, articleId);
            ps.setInt(5, userId);
            ps.setInt(6, articleId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to bookmark an article
     *
     * @param userId    the ID of the user bookmarking the article
     * @param articleId the ID of the article being bookmarked
     * @return true if the bookmark is successful, false otherwise
     */
    public boolean bookmarkArticle(int userId, int articleId) {
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(BOOKMARK_ARTICLE)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, articleId);
                int result = preparedStatement.executeUpdate();
                return result > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Method to unbookmark an article
     *
     * @param userId    the ID of the user unbookmarking the article
     * @param articleId the ID of the article being unbookmarked
     * @return true if the unbookmark is successful, false otherwise
     */
    public boolean unbookmarkArticle(int userId, int articleId) {
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UNBOOKMARK_ARTICLE)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, articleId);
                int result = preparedStatement.executeUpdate();
                return result > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method to check if a bookmark exists for a given user and article
     *
     * @param userId    the ID of the user
     * @param articleId the ID of the article
     * @return true if the bookmark exists, false otherwise
     */
    public boolean isBookmarkExists(int userId, int articleId) {
        boolean exists = false;
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_BOOKMARK_EXISTS)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, articleId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    exists = resultSet.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }


    /**
     * Sub service to get tag list of an article by using article ID
     *
     * @param articleID the article's id
     */
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


    /**
     * Sub service to get popular tags.
     *
     * @return The popular tags.
     */
    public List<String> getPopularTags() {
        List<String> popularTags = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_POPULAR_TAGS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String tagName = resultSet.getString("TagName");
                popularTags.add(tagName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return popularTags;
    }


    /**
     * Sub service to get remaining tags excluding the top 5 popular tags.
     *
     * @return The remaining tags.
     */
    public List<String> getRemainingTags() {
        List<String> remainingTags = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_REMAINING_TAGS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String tagName = resultSet.getString("TagName");
                remainingTags.add(tagName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return remainingTags;
    }


    /**
     * Formats an input date string from one format to another.
     *
     * @param inputDate The input date string in the format "yyyy-MM-dd HH:mm:ss.S".
     * @return A formatted date string in the format "dd/MM/yyyy HH:mm",
     * or null if the input date string cannot be parsed.
     */
    public static String formatDateString(String inputDate) {
        // Input format pattern
        String inputFormat = "yyyy-MM-dd HH:mm:ss.S";
        // Output format pattern
        String outputFormat = "dd/MM/yyyy HH:mm";

        // Create SimpleDateFormat objects for input and output formats
        SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);

        try {
            // Parse the input string into a Date object
            Date date = inputDateFormat.parse(inputDate);
            // Format the Date object into the desired output format
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            // Print stack trace if parsing fails
            e.printStackTrace();
            return null; // or you can throw an exception or handle it differently
        }
    }


    /**
     * Sub service to handle get article list
     *
     * @param res               the result article list.
     * @param preparedStatement the statement contain the SQL query
     */
    private void handleGetArticleList(List<Object> res, PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<>();
            Map<String, Object> user = new HashMap<>();
            List<Object> tags = getTagList(resultSet.getInt("ArticleID"));

            row.put("articleID", resultSet.getInt("ArticleID"));
            row.put("title", resultSet.getString("Title"));
            row.put("createdAt", resultSet.getString("createdAt"));
            row.put("dateFormated", formatDateString(resultSet.getString("createdAt")));
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


    /**
     * Service to get the article list of a page
     *
     * @param quantity the maximum quantity of a page. Default value is: 6.
     * @param page     the current page of a list. Default value is: 1
     */
    public List<Object> getAll(int quantity, int page) {
        List<Object> res = new ArrayList<>();

        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL)) {
                preparedStatement.setInt(1, quantity * (page - 1) + 1);
                preparedStatement.setInt(2, quantity * (page - 1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Method to retrieve the next articles after a given article ID
     *
     * @param articleID the ID of the reference article
     * @return a list of next articles
     */
    public List<Map<String, Object>> getNextArticle(int articleID) {
        List<Map<String, Object>> articles = new ArrayList<>();

        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_NEXT_ARTICLE)) {
                preparedStatement.setInt(1, articleID);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Map<String, Object> article = new HashMap<>();
                    Map<String, Object> user = new HashMap<>();

                    article.put("articleID", resultSet.getInt("ArticleID"));
                    article.put("title", resultSet.getString("Title"));
                    article.put("votes", getVoteValue(resultSet.getInt("ArticleID")));

                    user.put("userID", resultSet.getInt("UserID"));
                    user.put("username", resultSet.getString("Username"));
                    user.put("displayName", resultSet.getString("DisplayName"));

                    article.put("user", user);
                    articles.add(article);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    /**
     * Service to get the article list of a page by using user id
     *
     * @param userID   the user's id
     * @param quantity the maximum quantity of a page. Default value is: 6.
     * @param page     the current page of a list. Default value is: 1.
     */
    public List<Object> getAllByUserID(int userID, int quantity, int page) {
        List<Object> res = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_USERID)) {
                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, quantity * (page - 1) + 1);
                preparedStatement.setInt(3, quantity * (page - 1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * Service to get the article list of a page by using user id
     * @param userID the user's id
     * @param quantity the maximum quantity of a page. Default value is: 6.
     * @param page the current page of a list. Default value is: 1.
     * */
    public List<Object> getAllByUserUsername(String username ,int quantity, int page) {
        List<Object> res = new ArrayList<>();
        try(Connection connection = getDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_USERNAME)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, quantity*(page-1) + 1);
                preparedStatement.setInt(3, quantity*(page-1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Service to get the article list of a page by using article's title.
     *
     * @param title    the article's title.
     * @param quantity the maximum quantity of a page. Default value is: 6.
     * @param page     the current page of a list. Default value is: 1
     */
    public List<Object> getAllByTitle(String title, int quantity, int page) {
        List<Object> res = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_TITLE)) {
                preparedStatement.setString(1, "%" + title + "%");
                preparedStatement.setInt(2, quantity * (page - 1) + 1);
                preparedStatement.setInt(3, quantity * (page - 1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Service to get the article list of a page by using tag name.
     *
     * @param tagName  the provided tag name.
     * @param quantity the maximum quantity of a page. Default value is: 6.
     * @param page     the current page of a list. Default value is: 1
     */
    public List<Object> getAllByTagName(String tagName, int quantity, int page) {
        List<Object> res = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_TAG_NAME)) {
                preparedStatement.setString(1, tagName);
                preparedStatement.setInt(2, quantity * (page - 1) + 1);
                preparedStatement.setInt(3, quantity * (page - 1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Service to get the article list of a page from specific date.
     *
     * @param fromDate the specific date.
     * @param quantity the maximum quantity of a page. Default value is: 6.
     * @param page     the current page of a list. Default value is: 1
     */
    public List<Object> getFromDate(String fromDate, int quantity, int page) {
        List<Object> res = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_FROM_DATE)) {
                preparedStatement.setTimestamp(1, Timestamp.valueOf(fromDate));
                preparedStatement.setInt(2, quantity * (page - 1) + 1);
                preparedStatement.setInt(3, quantity * (page - 1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Service to get the article list of a page by using bookmark
     *
     * @param userID   the user's id
     * @param quantity the maximum quantity of a page. Default value is: 6.
     * @param page     the current page of a list. Default value is: 1
     */
    public List<Object> getAllBookmark(int userID, int quantity, int page) {
        List<Object> res = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BOOKMARK)) {
                preparedStatement.setInt(1, userID);
                preparedStatement.setInt(2, quantity * (page - 1) + 1);
                preparedStatement.setInt(3, quantity * (page - 1) + quantity);
                handleGetArticleList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Service to get an article information by using article id
     *
     * @param userId    the user's id. This param allow null.
     * @param articleId the article's id, can't null.
     */
    public Map<String, Object> getByArticleId(Integer userId, Integer articleId) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        List<Object> tags = new ArrayList<>();
        UserService userService = new UserService();

        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ARTICLE_ID)) {
                preparedStatement.setInt(1, userId == null ? 0 : userId);
                preparedStatement.setInt(2, articleId);
                preparedStatement.setInt(3, userId == null ? 0 : userId);
                preparedStatement.setInt(4, articleId);
                preparedStatement.setInt(5, articleId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    res.put("articleID", resultSet.getInt("ArticleID"));
                    res.put("title", resultSet.getString("Title"));
                    res.put("content", resultSet.getString("Content"));
                    res.put("dateFormated", formatDateString(resultSet.getString("createdAt")));
                    res.put("votedValue", resultSet.getString("VotedValue") == null ? null : resultSet.getBoolean("VotedValue"));
                    res.put("votes", getVoteValue(resultSet.getInt("ArticleID")));
                    res.put("isBookMark", isBookmarkExists(userId, articleId));
                    res.put("isFollow", userService.isFollowingUser(userId, resultSet.getInt("UserID")));
                    user.put("userIdOfArticle", resultSet.getInt("UserID"));
                    user.put("avatarURL", resultSet.getString("AvatarURL"));
                    user.put("username", resultSet.getString("Username"));
                    user.put("displayName", resultSet.getString("DisplayName"));
                    res.put("user", user);
                    res.put("tags", getTagList(resultSet.getInt("ArticleID")));
                    res.put("createdAt", resultSet.getString("CreatedAt"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * This fuction handle add an article content with article image is a list of File.
     * Text article info will be saved in database and the image will be save in cloud.
     *
     * @param userId         number user id.
     * @param articelTitle   string article title.
     * @param articleContent string article content, this is the html tag will be render.
     * @param tags           the array string of tags.
     * @param images         the list MultipartFile of images.
     * @return true if succes otherwise return false.
     */
    public boolean handelAddNewArticle(Integer userId,
                                       String articleTitle,
                                       String articleContent,
                                       String[] tags,
                                       List<MultipartFile> images) {
        CloudsDiaryService cloudsDiaryService = new CloudsDiaryService();
        List<String> urlList = cloudsDiaryService.uploadImages(images);
        articleContent = replacePlaceholders(articleContent, urlList);
        int articleId = 0;
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ARTICLE)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, articleTitle);
                preparedStatement.setString(3, articleContent);
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_LASTEST)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    articleId = resultSet.getInt("ArticleID");
                }

            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ARTICLETAG)) {
                preparedStatement.setInt(1, articleId);
                for (String tag : tags) {
                    preparedStatement.setInt(2, Integer.parseInt(tag));
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * This fuction handle add an article content with article image is a string image data encode by base64.
     * Text article info will be saved in database and the image will be save in cloud.
     *
     * @param userId         number user id.
     * @param articelTitle   string article title.
     * @param articleContent string article content, this is the html tag will be render.
     * @param tags           the array string of tags.
     * @param images         the list MultipartFile of images.
     * @return true if succes otherwise return false.
     */
    public boolean handelAddNewArticleBase64(Integer userId,
                                             String articleTitle,
                                             String articleContent,
                                             String[] tags,
                                             List<String> images) {
        CloudsDiaryService cloudsDiaryService = new CloudsDiaryService();
        List<String> urlList = cloudsDiaryService.uploadImagesBase64(images);
        articleContent = replacePlaceholders(articleContent, urlList);
        int articleId = 0;
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ARTICLE)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, articleTitle);
                preparedStatement.setString(3, articleContent);
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_LASTEST)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    articleId = resultSet.getInt("ArticleID");
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ARTICLETAG)) {
                preparedStatement.setInt(1, articleId);
                for (String tag : tags) {
                    preparedStatement.setInt(2, Integer.parseInt(tag));
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * This function replace all "[?]" string into the value in the replacement list.
     *
     * @param original     the original string need to replace
     * @param replacements the List of String replacement
     * @return the destination string after replace.
     */
    private String replacePlaceholders(String original, List<String> replacements) {
        int index = 0;
        while (original.contains("[?]") && index < replacements.size()) {
            original = original.replaceFirst("\\[\\?\\]", replacements.get(index));
            index++;
        }
        return original;
    }


    /**
     * This function handle insert article into database.
     *
     * @param userId         the user's id
     * @param articleTitle   the article's title
     * @param articleContent the article's content
     * @return true if succes otherwise return false.
     */
    private boolean handleInsertArticle(int userId, String articleTitle, String articleContent) {
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ARTICLE)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, articleTitle);
                preparedStatement.setString(3, articleContent);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * This function handle get the last article id.
     *
     * @param userId the user's id that you want to find last article's id.
     * @return integer article's id, if not found return default value 0.
     */
    private int getLastArticleID(int userId) {
        int articleId = 0;
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_LASTEST)) {
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    articleId = resultSet.getInt("ArticleID");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articleId;
    }


    /**
     * This function handle insert article's tags into database.
     *
     * @param articleId integer article's id.
     * @param tags      string array of tags
     * @return true if succes otherwise return false.
     */
    private boolean handleInsertArticleTag(int articleId, String[] tags) {
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ARTICLETAG)) {
                preparedStatement.setInt(1, articleId);
                for (String tag : tags) {
                    preparedStatement.setInt(2, Integer.parseInt(tag));
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


}
