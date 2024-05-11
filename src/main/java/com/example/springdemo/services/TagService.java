package com.example.springdemo.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagService extends DatabaseService{

    private static final String GET_ALL =       "SELECT * FROM [Tag]";

    private static final String GET_SIMILAR =   "SELECT * FROM [Tag]\n" +
                                                "WHERE [Tag].TagName LIKE ?";

    private static final String ADD_TAG =       "INSERT INTO [Tag] ([TagName])\n" +
                                                "SELECT ?\n" +
                                                "WHERE NOT EXISTS (\n" +
                                                "    SELECT 1 FROM [Tag] WHERE [TagName] = ?\n" +
                                                ");";

    private static final String UPDATE_TAG =    "UPDATE [Tag]\n" +
                                                "SET [TagName] = ?\n" +
                                                "WHERE [TagName] = ?";

    public List<Object> getAll() {
        List<Object> res = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL)){
                getTagList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public List<Object> getSimilar(String name) {
        List<Object> res = new ArrayList<>();
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_SIMILAR)){
                preparedStatement.setString(1, name + "%");
                getTagList(res, preparedStatement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean addNewTag(String name) {
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_TAG)){
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, name);
                int rowInserted = preparedStatement.executeUpdate();
                if(rowInserted > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateTag(String currentName , String newName) {
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TAG)){
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, currentName);
                int rowInserted = preparedStatement.executeUpdate();
                if(rowInserted > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getTagList(List<Object> res, PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Map<String,Object> row = new HashMap<>();
            row.put("tagId", resultSet.getInt("TagID"));
            row.put("tagName", resultSet.getString("TagName"));
            res.add(row);
        }
    }
}
