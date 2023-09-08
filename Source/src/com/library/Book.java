package com.library;

import com.library.connection.JDBC;


import javax.swing.*;
import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Book {
    private static ResultSet result = null;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(Book.class.getName());
    private static final Statement statement = null;
    private static final Connection connection = JDBC.main();
    private static StringBuilder output = new StringBuilder();


    public static void display() {
        try {
            String dispo = "disponible";
            output = new StringBuilder();

            String sql = "SELECT * FROM `book` WHERE `status` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, dispo);

            ResultSet resultSet = preparedStatement.executeQuery();
            output.append("ISBN\tTitre\tAuteur\tQuantité\n");

            while (resultSet.next()) {
                String isbn = resultSet.getString(1);
                String name = resultSet.getString(2);
                String author = resultSet.getString(3);
                int quantity = resultSet.getInt(4);

                output.append(isbn).append("   ").append("\t");
                output.append(name).append("   ").append("\t");
                output.append(author).append("   ").append("\t");
                output.append(quantity).append("   ").append("\n");
            }

            if (output.length() > 0) {
                JOptionPane.showMessageDialog(null, output.toString(), "List des livres disponible", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Aucun livre n'est disponible", "Book List", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Query failed");
            throw new RuntimeException(e);
        }
    }


    public static void displayByName(String name) {
        if (name == null || name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Merci d'entrer le titre du livre", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "SELECT * FROM book WHERE `name` = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet result = preparedStatement.executeQuery();

            StringBuilder output = new StringBuilder();

            while (result.next()) {
                output.append(result.getString(1)).append(" ");
                output.append(result.getString(2)).append("\n");
            }

            if (output.length() > 0) {
                JOptionPane.showMessageDialog(null, output.toString(), "List des livres disponible", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Livre indisponible", "Book List", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void displayByAuthor(String author) {
        if (author == null || author.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Merci d'entrer le nom de l'auteur", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "SELECT * FROM book WHERE `author` = ?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, author);
            ResultSet result = preparedStatement.executeQuery();

            StringBuilder output = new StringBuilder();

            while (result.next()) {
                output.append(result.getString(1)).append(" ");
                output.append(result.getString(2)).append("\n");
            }

            if (output.length() > 0) {
                JOptionPane.showMessageDialog(null, output.toString(), "Liste des livres de l'auteur", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Aucun livre trouvé de cet auteur", "Liste des livres", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }


    public static void displayByIsbn(int isbn) {
        try {
            result = statement.executeQuery("SELECT * FROM book WHERE `name` = '" + isbn + "'");
            while (result.next()) {
                output.append(result.getString(1)).append(" ");
                output.append(result.getString(2)).append("\n");
            }

            if (output.length() > 0) {
                JOptionPane.showMessageDialog(null, output.toString(), "List des livres disponible", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Livre indisponible", "Book List", JOptionPane.INFORMATION_MESSAGE);
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Merci d'enter le titre du livre", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void InsertBook() {
        try {
            String name = null;
            String author = null;
            int quantity = -1;

            while(name == null || name.trim().isEmpty()){
                name = JOptionPane.showInputDialog("Enter le titre du livre:");
                System.out.println(name);
            }

            while(author == null || author.trim().isEmpty()){
                author = JOptionPane.showInputDialog("Enter l'auteur du livre:");
            }

            while (quantity <= 0) {
                try {
                    quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter la quantité:"));

                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(null, "La quantité doit être supérieure à zéro.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Veuillez entrer une quantité valide.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                }
            }

            String status = (quantity > 0) ? "disponible" : "indisponible";

            JOptionPane.showMessageDialog(null, "Actuellement, ce livre est " + status, "Information", JOptionPane.INFORMATION_MESSAGE);

            String sql = "INSERT INTO `book` (`name`, `author`, `quantity`, `status`) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, status);

            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Livre ajouté avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Insertion failed", e);
            JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de l'ajout du livre.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void DeleteBook() {
        int isbn = -1;

        while (isbn == -1) {
            try {
                isbn = Integer.parseInt(JOptionPane.showInputDialog("Enter the ISBN of the book:\n"));

                String checkSql = "SELECT * FROM book WHERE isbn = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setInt(1, isbn);
                ResultSet resultSet = checkStatement.executeQuery();

                if (!resultSet.next()) {
                    JOptionPane.showMessageDialog(null, "Book not found", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    isbn = -1;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Query failed", e);
                JOptionPane.showMessageDialog(null, "An error occurred while checking the book.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        try {
            String deleteSql = "DELETE FROM book WHERE isbn = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setInt(1, isbn);
            int deletedRows = deleteStatement.executeUpdate();

            if (deletedRows > 0) {
                JOptionPane.showMessageDialog(null, "Book deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Book not found", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Delete failed", e);
            JOptionPane.showMessageDialog(null, "An error occurred while deleting the book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void UpdateBook(){
        System.out.println("Enter the isbn : ");
        int isbn = scanner.nextInt();
        System.out.println("Enter the name: ");
        String name = scanner.next();
        System.out.println("Enter the author: ");
        String author = scanner.next();
        System.out.println("Enter the quantity : ");
        int quantity = scanner.nextInt();
        System.out.println("Enter the status: ");
        String status = scanner.next();

        try{
            String sql = "UPDATE book SET name = '" + name + "', author = '" + author + "', quantity = '" + quantity + "', status = '" + status + "' WHERE isbn = " + isbn;
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.log(Level.WARNING,"failed");
            throw new RuntimeException(e);
        }
    }


}
