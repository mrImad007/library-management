package com.library;

import com.library.connection.JDBC;

import java.sql.*;

public class Bibliothequaire {
    private static final Connection connection = JDBC.main();

    public static void main(String[] args) {

    }

    public static ResultSet checkUsership (String usership) throws SQLException {
        String query = "SELECT id FROM bibliothequaire WHERE `usership` = ? ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,usership);
        ResultSet resultIdLib = preparedStatement.executeQuery();

        return resultIdLib;
    }
}
