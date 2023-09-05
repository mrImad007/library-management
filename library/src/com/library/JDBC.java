package com.library;

import java.sql.*;
public class JDBC {
    public static Statement main() {
        String url = "jdbc:mysql://localhost:3306/library";
        String root = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url,root,password);
            Statement statement = connection.createStatement();
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
