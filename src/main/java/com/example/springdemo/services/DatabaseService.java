package com.example.springdemo.services;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
public class DatabaseService {
        protected SQLServerDataSource getDataSource() {
                SQLServerDataSource ds = new SQLServerDataSource();
                ds.setUser("sa");
                ds.setPassword("123");
                ds.setServerName("LAPTOP-52IKI0LN\\SQLEXPRESS");
                ds.setPortNumber(1433);
                ds.setDatabaseName("JavaPlayLogin");
                ds.setTrustServerCertificate(true);

                return ds;
        }
}
