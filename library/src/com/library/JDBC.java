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
            /*ResultSet result = statement.executeQuery("SELECT * from Books");
            while (result.next()){
                System.out.println(result.getInt(1)+" "+result.getString(2)+" "+result.getString(3));
            }*/
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
