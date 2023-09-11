package com.library;

import com.library.connection.JDBC;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static final Connection connection = JDBC.main();
    private static StringBuilder output = new StringBuilder();
    private static final Logger logger = Logger.getLogger(Book.class.getName());
    public static void AddClient(){

    }

    public static void Display(){
        try {
            output = new StringBuilder();

            String sql = "SELECT * FROM `client` ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            output.append("Nom\tAdress\tmembership\n");

            while (resultSet.next()) {
                String isbn = resultSet.getString(2);
                String name = resultSet.getString(3);
                String author = resultSet.getString(4);


                output.append(isbn).append("   ").append("\t");
                output.append(name).append("   ").append("\t");
                output.append(author).append("   ").append("\t");
            }

            if (output.length() > 0) {
                JOptionPane.showMessageDialog(null, output.toString(), "List des clients", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Aucun client n'est enregistré", "Book List", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Query failed");
            throw new RuntimeException(e);
        }
    }

    public static ResultSet checkMembership(String membership) throws SQLException {

        String queryIdClient = "SELECT id FROM client WHERE `membership` = ? ";
        PreparedStatement preparedStatementId = connection.prepareStatement(queryIdClient);
        preparedStatementId.setString(1, membership);
        ResultSet resultIdClient = preparedStatementId.executeQuery();

        return resultIdClient;
    }
}
