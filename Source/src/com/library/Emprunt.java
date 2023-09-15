package com.library;

import com.library.connection.JDBC;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Emprunt {
    private static final Connection connection = JDBC.main();
    public static void borrowBook()  {

        try {
            int idClient = 0;
            int idLib = 0;
            int isbn = -1;
            int period = 0;
            String membership = null;
            String usership = null;

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            while(membership == null || membership.trim().isEmpty()){
                membership = JOptionPane.showInputDialog("Entrer le membershit du client :");
            }

            while(usership == null || usership.trim().isEmpty()){
                usership = JOptionPane.showInputDialog("Entrer votre usership :");
            }

            while (isbn  <= 0) {
                try {
                    isbn = Integer.parseInt(JOptionPane.showInputDialog("Veuillez enter l'isbn':"));

                    if (isbn <= 0) {
                        JOptionPane.showMessageDialog(null, "Isbn invalide", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Veuillez entrer un isbn valide.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                }
            }

            boolean available = Book.doesBookExist(isbn);

            if (available){
                while (period  <= 0) {
                    try {
                        period = Integer.parseInt(JOptionPane.showInputDialog("Entrer la periode d'emprunt en jours :"));

                        if (period <= 0) {
                            JOptionPane.showMessageDialog(null, "periode invalide", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Veuillez entrer une periode valide.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    }
                }

                ResultSet resultIdClient = Client.checkMembership(membership);
                ResultSet resultIdLib = Bibliothequaire.checkUsership(usership);


                while (resultIdClient.next()){
                    idClient = resultIdClient.getInt(1);
                }

                while (resultIdLib.next()){
                    idLib = resultIdLib.getInt(1);
                }

                if (idClient>0 && idLib>0){
                    boolean check = Emprunt.checkIfClientExists(idClient);
                    if (!check){
                        String InsertQuery = "INSERT INTO" +
                                " `emprunt` (`isbn_book`, `client_id`, `bibliothequaire_id`, `pickup_date`, `return_date`, `period`)" +
                                "VALUES (?, ?, ?, ?, DATE_ADD(?, INTERVAL ? DAY), ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(InsertQuery);
                        preparedStatement.setInt(1,isbn);
                        preparedStatement.setInt(2,idClient);
                        preparedStatement.setInt(3,idLib);
                        preparedStatement.setString(4,dtf.format(now));
                        preparedStatement.setString(5,dtf.format(now));
                        preparedStatement.setInt(6,period);
                        preparedStatement.setInt(7,period);

                        preparedStatement.executeUpdate();

                        JOptionPane.showMessageDialog(null,"livre bien emprunté");
                    }else{
                        JOptionPane.showMessageDialog(null,"Ce client a déja emprunté un livre");
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Client ou user introuvable");
                }
            }else{
                JOptionPane.showMessageDialog(null,"Ce livre n'est pas disponible");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void borrowedBooks() {
        try {
            String query = "SELECT * FROM `book` INNER JOIN `emprunt` WHERE book.isbn = emprunt.isbn_book";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder output = new StringBuilder();

            output.append("ISBN\t\t\tTitre\t\t\t\t\t\tAuteur\n");

            while (resultSet.next()) {
                String isbn = resultSet.getString(1);
                String name = resultSet.getString(2);
                String author = resultSet.getString(3);


                output.append(isbn).append("   ").append("\t\t\t");
                output.append(name).append("   ").append("\t\t\t\t\t\t");
                output.append(author).append("   ").append("\n");
            }

            if (output.length() > 0) {
                JOptionPane.showMessageDialog(null, output.toString(), "List des livres empruntés", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Aucun livre n'est emprunté", "Book List", JOptionPane.INFORMATION_MESSAGE);
            }


        }catch (Exception e){
            throw new RuntimeException(e);
        }



    }

    public static void retunBook(){
        int idClient = 0;
        String membership = null;
        try {
            while (membership == null || membership.trim().isEmpty()){
                membership = JOptionPane.showInputDialog("Entrer le membership du client");
            }
            ResultSet client = Client.checkMembership(membership);
            while(client.next()){
                idClient = client.getInt(1);
            }
            if(idClient>0){
                String query = "DELETE FROM `emprunt` WHERE client_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, idClient);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(null,"Livre retourné ");
            }else{
                JOptionPane.showMessageDialog(null,"Client introuvable");
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean checkIfClientExists(int id) throws SQLException{
        String query = "SELECT COUNT(*) FROM `emprunt` WHERE `client_id` = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int clientCount = resultSet.getInt(1);
                    return clientCount > 0;
                }
            }
        }
        return false;
    }

    public static int BorrowingStatistics(){
        try{
            int totalBorrowed = 0;
            String query = "SELECT COUNT(*) AS total FROM `emprunt`";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                totalBorrowed = resultSet.getInt("total");
            }
            return totalBorrowed;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean checkBook(int isbn){
        try {
            int bookId = 0;
            String query = "SELECT * FROM `emprunt` WHERE isbn_book = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,isbn);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                bookId = resultSet.getInt(1);
            }
            if (bookId > 0){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
