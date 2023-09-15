package com.library.connection;

import java.sql.*;

public class JDBC {
    private static final String URL = "jdbc:mysql://localhost:3306/bibliotheque";
    private static final String ROOT = "root";
    private static final String PASSWORD = "";
    public static Connection main() {

        try {
            return DriverManager.getConnection(URL,ROOT,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
