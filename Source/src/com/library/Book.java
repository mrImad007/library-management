package com.library;

import com.library.connection.JDBC;


import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Book {
    private static ResultSet result;
    public static final String ANSI_YELLOW = "\u001B[41m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static final Scanner scanner = new Scanner(System.in);
    //private static String name;
    private static final Logger logger = Logger.getLogger(Book.class.getName());
    private static final Statement statement = JDBC.main();
    public static void main(String[] args) throws SQLException {
        try {
            while (result.next()){
                System.out.println(result.getString(2));
            }
        }catch (Exception e){
            throw new RuntimeException();
        }
        System.out.println(ANSI_YELLOW+"Bienvenue dans votre application"+ ANSI_RESET);

    }

    public static void display(){
        try {
            result = statement.executeQuery("SELECT * FROM `book`");
            while (result.next()){
                System.out.println(result.getString(2));
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Query failed");
        }
    }

    public static void displaySingle(String name) throws SQLException {
        if(name != null){
            result = statement.executeQuery("SELECT * FROM book WHERE `name` = '" + name + "'");
            while(result.next()){
                System.out.println(result.getInt(1)+" "+result.getString(2));
            }
        }else{
            System.out.println("merci de donner le nom du book ");
        }
    }



    public static void InsertBook(){
        //System.out.println("Enter the isbn : ");
        //int isbn = scanner.nextInt();
        System.out.println("Enter the name: ");
        String name = scanner.next();
        System.out.println("Enter the author: ");
        String author = scanner.next();
        System.out.println("Enter the quantity : ");
        int quantity = scanner.nextInt();
        System.out.println("Enter the status: ");
        String status = scanner.next();

        try{
            System.out.println(quantity);
            String sql = "INSERT INTO `book` (`name`, `author`, `quantity`, `status`) VALUES ('" + name + "', '" + author + "', '" + quantity + "', '" + status + "')";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.log(Level.WARNING,"failed");
            throw new RuntimeException(e);
        }

    }

    public static void DeleteBook() {
        System.out.println("Enter the isbn : ");
        int isbn = scanner.nextInt();

        try {
            String sql = "DELETE FROM book WHERE (`isbn` = "+ isbn +") ";
            statement.executeUpdate(sql);
        }catch (Exception e){
            logger.log(Level.WARNING,"delete failed");
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
