package com.example.springdemo.services;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserAccountService extends DatabaseService {
    private static final String SQL_GET_USER = "SELECT Username, Password FROM UserAccount WHERE Username = ? AND Password = ?";
    public boolean getUser(String username, String password) {
        try (Connection conn = getDataSource().getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(SQL_GET_USER)) {
                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
