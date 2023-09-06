package com.library.dto;

import com.library.connection.JDBC;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Logger;

public interface Book {
    ResultSet result = null;
    Scanner scanner = new Scanner(System.in);
    Logger logger = Logger.getLogger(com.library.Book.class.getName());
    Statement statement = JDBC.main();


}
