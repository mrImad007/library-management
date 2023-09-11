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
    private static final Logger logger = Logger.getLogger(Book.class.getName());
    public static void AddClient(){
        try {
            String name = null;
            String adress = null;
            String membership = null;
            int count = 1;

            while (name == null || name.trim().isEmpty()){
                name = JOptionPane.showInputDialog("Enter le nom du client");
            }

            while(adress == null || adress.trim().isEmpty()){
                adress = JOptionPane.showInputDialog("Enter l'adress du client");
            }

            while (membership == null || membership.trim().isEmpty()){
                membership = JOptionPane.showInputDialog("Enter un nouveau memberShip du client");
            }

            String check = "SELECT COUNT(*) AS count FROM `client` WHERE `membership` = ?";
            PreparedStatement checkingStatement = connection.prepareStatement(check);
            checkingStatement.setString(1,membership);
            ResultSet resultSet = checkingStatement.executeQuery();

            while (resultSet.next()){
                count = resultSet.getInt("count");
            }
            if (count > 0){
                JOptionPane.showMessageDialog(null,"client deja existant");
            }else{

            String query = "INSERT INTO `client` (`id`, `name`, `adress`, `membership`) VALUES (NULL ,? ,? ,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,adress);
            preparedStatement.setString(3,membership);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Client bien ajouté à la base de données");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void Display(){
        try {
            StringBuilder output = new StringBuilder();

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
                output.append(author).append("   ").append("\n");
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

        return preparedStatementId.executeQuery();
    }
}
