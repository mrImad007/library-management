package com.library;

import com.library.connection.JDBC;


import javax.swing.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Book {
    private static ResultSet result = null;
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
            output.append("ISBN\t\t\t\t\tTitre\t\t\t\t\tAuteur\t\t\tQuantité\n");

            while (resultSet.next()) {
                String isbn = resultSet.getString(1);
                String name = resultSet.getString(2);
                String author = resultSet.getString(3);
                int quantity = resultSet.getInt(4);

                output.append(isbn).append("   ").append("\t\t\t\t\t");
                output.append(name).append("   ").append("\t\t\t\t\t");
                output.append(author).append("   ").append("\t\t\t");
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


    public static boolean doesBookExist(int isbn) throws SQLException {
        String query = "SELECT COUNT(*) FROM book WHERE isbn = ? AND `status` = 'disponible'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, isbn);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int bookCount = resultSet.getInt(1);
                    return bookCount > 0;
                }
            }
        }
        return false;
    }

    public static void InsertBook() {
        try {
            String name = null;
            String author = null;
            int quantity = -1;

            while(name == null || name.trim().isEmpty()){
                name = JOptionPane.showInputDialog("Enter le titre du livre:");
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

    /*public static void UpdateBook(){
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
    }*/

    public static void UpdateBook() {
        try {
            String isbnInput = JOptionPane.showInputDialog("Enter the ISBN:");
            int isbn;

            try {
                isbn = Integer.parseInt(isbnInput);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid ISBN. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method if ISBN is invalid
            }

            String name = null;
            String author = null;
            int quantity = -1;
            String status = null;

            while (name == null || name.trim().isEmpty()) {
                name = JOptionPane.showInputDialog("Enter the name:");
            }

            while (author == null || author.trim().isEmpty()) {
                author = JOptionPane.showInputDialog("Enter the author:");
            }

            while (quantity <= 0) {
                try {
                    String quantityInput = JOptionPane.showInputDialog("Enter the quantity:");
                    quantity = Integer.parseInt(quantityInput);

                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(null, "Quantity must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid quantity. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            while (status == null || status.trim().isEmpty()) {
                status = JOptionPane.showInputDialog("Enter the status:");
            }

            String sql = "UPDATE book SET name = ?, author = ?, quantity = ?, status = ? WHERE isbn = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, status);
            preparedStatement.setInt(5, isbn);

            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Book updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Update failed", e);
            JOptionPane.showMessageDialog(null, "An error occurred while updating the book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static int Totalbooks(){
        try {
            int totalQuantity = 0;
            String query = "SELECT SUM(quantity) AS total_quantity FROM book";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                totalQuantity = resultSet.getInt("total_quantity");
            }
            return totalQuantity;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Integer> getTotalBookCounts() {
        try {
            Map<String, Integer> bookCounts = new HashMap<>();
            try {
                String disponiblesQuery = "SELECT SUM(quantity) AS total_disponible FROM book WHERE status = 'disponible'";
                PreparedStatement disponiblesStatement = connection.prepareStatement(disponiblesQuery);
                ResultSet disponiblesResultSet = disponiblesStatement.executeQuery();

                if (disponiblesResultSet.next()) {
                    int disponibleCount = disponiblesResultSet.getInt("total_disponible");
                    bookCounts.put("disponibles", disponibleCount);
                }

                String indisponibleQuery = "SELECT COUNT(*) AS total_indisponible FROM book WHERE status = 'indisponible'";
                PreparedStatement indisponibleStatement = connection.prepareStatement(indisponibleQuery);
                ResultSet indsponibleResultSet = indisponibleStatement.executeQuery();

                if (indsponibleResultSet.next()) {
                    int indiponibleCount = indsponibleResultSet.getInt("total_indisponible");
                    bookCounts.put("indisponibles", indiponibleCount);
                }

                String perdusQuery = "SELECT COUNT(*) AS total_perdu FROM book WHERE status = 'perdu'";
                PreparedStatement perdusStatement = connection.prepareStatement(perdusQuery);
                ResultSet perdusResultSet = perdusStatement.executeQuery();

                if (perdusResultSet.next()) {
                    int perdusCount = perdusResultSet.getInt("total_perdu");
                    bookCounts.put("perdus", perdusCount);
                }

                return bookCounts;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
