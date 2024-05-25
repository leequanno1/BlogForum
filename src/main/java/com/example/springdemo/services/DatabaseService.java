package com.example.springdemo.services;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
abstract class DatabaseService {
    protected SQLServerDataSource getDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("sa");
        ds.setPassword("123");
        ds.setPortNumber(1433);
        ds.setDatabaseName("ForumDB");
        ds.setTrustServerCertificate(true);

        return ds;
    }
}