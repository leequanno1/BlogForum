package com.example.springdemo.services;

import com.example.springdemo.security.EncoderUtil;

import java.sql.*;
import java.util.Date;

@SuppressWarnings("All")
public class TokenService extends DatabaseService {

    /**
     * Saves a token to the database for changing passwords.
     *
     * @param email Email of the user.
     * @param tokenValue Token value that is randomly generated.
     * @return Returns 1 if saved successfully, otherwise returns -1.
     */
    public int saveToken(String email, String tokenValue) {
        try (Connection connection = getDataSource().getConnection()) {
            // Xóa các token cũ có cùng email
            String deleteQuery = "DELETE FROM [Token] WHERE [Email] = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setString(1, email);
                deleteStatement.executeUpdate();
            }

            // Thêm token mới
            String insertQuery = "INSERT INTO [Token] ([Email], [TokenValue], [CreatedAt]) VALUES (?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, email);
                insertStatement.setString(2, EncoderUtil.encode(tokenValue));

                Timestamp currentTimestamp = new Timestamp(new Date().getTime());
                insertStatement.setTimestamp(3, currentTimestamp);

                int rowsAffected = insertStatement.executeUpdate();
                if (rowsAffected > 0) {
                    return 1;
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return -1;
    }



    /**
     * Checks if the token is valid.
     *
     * @param email Email of the user.
     * @param tokenValue Token value that is an authentication code for the user when they change the password.
     * @return Returns true if the token is valid, otherwise returns false.
     */
    public boolean isTokenValid(String email, String tokenValue) {
        try (Connection connection = getDataSource().getConnection()) {
            String query = "SELECT TokenValue, CreatedAt FROM [Token] WHERE [Email] = ? ";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, email);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Timestamp createdAt = resultSet.getTimestamp("CreatedAt");
                        String hashedTokenValue = resultSet.getString("TokenValue");

                        long currentTimeMillis = System.currentTimeMillis();
                        long tokenAgeMillis = currentTimeMillis - createdAt.getTime();
                        long tenMinutesMillis = 5 * 60 * 1000;

                        return (tokenAgeMillis < tenMinutesMillis) && EncoderUtil.matches(tokenValue, hashedTokenValue);
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }


    /**
     * Deletes the token of the user when it expires.
     *
     * @param email Email of the user.
     * @return Returns 1 if deleted successfully, otherwise returns -1.
     */
    public int deleteToken(String email) {
        try (Connection connection = getDataSource().getConnection()) {
            String query = "DELETE FROM [Token] WHERE [Email] = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, email);

                int rowsAffected = statement.executeUpdate();
                return rowsAffected;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return -1;
    }


}
