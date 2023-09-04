package com.library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Book {
    public static void main(String[] args) {
        try{
            ResultSet books = display();
            while(books.next()){
                System.out.println(books.getInt(1)+" "+books.getString(2));
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static ResultSet display() throws SQLException {
        ResultSet result = null;
        try {
            Statement statement = JDBC.main();
            result = statement.executeQuery("SELECT * FROM books");
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }
}
