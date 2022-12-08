package org.Lup.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDao {

    private Connection connection;
    private String host = "localhost";
    private String port = "5432";
    private String db = "library";
    private String user = "postgres";
    private String password = "Ytp1288";

    protected AbstractDao(){
        try {
            String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, db);
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected Connection getConnection(){
        return connection;
    }

    protected void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
