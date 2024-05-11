package com.example.springdemo.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a Service class provide method to get API body.
 * */
public class TagService extends DatabaseService{

    /**
     * This is a SQL query to get all tags.
     * */
    private static final String GET_ALL =       "SELECT * FROM [Tag]";

    /**
     * This is a SQL query to get all tags have name similar to the provide name.
     * */
    private static final String GET_SIMILAR =   "SELECT * FROM [Tag]\n" +
                                                "WHERE [Tag].TagName LIKE ?";

    /**
     * This is a SQL query to add new tag by provide new name.
     * If new name already exit then doing nothing.
     * */
    private static final String ADD_TAG =       "INSERT INTO [Tag] ([TagName])\n" +
                                                "SELECT ?\n" +
                                                "WHERE NOT EXISTS (\n" +
                                                "    SELECT 1 FROM [Tag] WHERE [TagName] = ?\n" +
                                                ");";

    /**
     * This is a SQL query to update current tag name to new name.
     * If new name is already exit then don't change the current name.
     * */
    private static final String UPDATE_TAG =    "UPDATE [Tag]\n" +
                                                "SET [TagName] = CASE WHEN NOT EXISTS (SELECT 1 FROM [Tag] WHERE [TagName] = ?) THEN ?\n" +
                                                "              ELSE [TagName]\n" +
                                                "              END\n" +
                                                "WHERE [TagName] = ?";

    /**
     * This function return a list of all tag's name.
     * */
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

    /**
     * This function return a list of all tag have name similar the provided name.
     * @param name The provided name for searching purpose.
     * */
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

    /**
     * This function add a new tag's name to database.
     * Returning result if it inserted ok or not.
     * @param name Tag's name
     * */
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

    /**
     * This function update name for a current tag by giving them a new name.
     * Returning result if it updated ok or not.
     * @param currentName The current tag's name
     * @param newName The new tag's name
     * */
    public boolean updateTag(String currentName , String newName) {
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TAG)){
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, newName);
                preparedStatement.setString(3, currentName);
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

    /**
     * This function help get all tag value form the query to the "res" object.
     * @param res The result list.
     * @param preparedStatement The statement contain the SQL query.
     * */
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
