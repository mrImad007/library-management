package com.library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Book {
    private static ResultSet result = null;
    private static final Scanner scanner = new Scanner(System.in);
    //private static String name;
    private static final Logger logger = Logger.getLogger(Book.class.getName());
    private static final Statement statement = JDBC.main();
    public static void main(String[] args){

        try{
            ResultSet books = display();
            while(books.next()){
                System.out.println(books.getInt(1)+" "+books.getString(2));
            }
        }catch (Exception e){
            logger.log(Level.WARNING, "Connection with db failed");
        }
        DeleteBook();
    }

    public static ResultSet display(){
        try {
            result = statement.executeQuery("SELECT * FROM books");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Query failed");
        }
        return result;
    }

    public static void InsertBook(){
        System.out.println("Enter the id : ");
        int id = scanner.nextInt();
        System.out.println("Enter the name: ");
        String name = scanner.next();
        System.out.println("Enter the something: ");
        String som = scanner.next();
        System.out.println("Enter the haha: ");
        String haha = scanner.next();

        try{

            String sql = "INSERT INTO books (`id`, `name`, `something`, `hah`) VALUES (" + id + ", '" + name + "', '" + som + "', '" + haha + "')";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            logger.log(Level.WARNING,"failed");
        }

    }

    public static void DeleteBook() {
        System.out.println("Enter the id : ");
        int id = scanner.nextInt();

        try {
            String sql = "DELETE FROM books WHERE (`id` = "+ id +") ";
            statement.executeUpdate(sql);
        }catch (Exception e){
            logger.log(Level.WARNING,"delete failed");
        }

    }


}
