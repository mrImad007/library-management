package com.library.dto;

public class BookDto {
    private static String name;
    private static String author;
    private static String status;
    private static int quantity ;
    private static int isbn;
    private static BookDto instance;
    private BookDto(){}

    public static BookDto getInstance(){
        if(instance==null){
            instance = new BookDto();
        }
        return instance;
    }
    private BookDto(int isbn,String title, String author, String status, int quantity){
        this.isbn = isbn;
        this.name = title;
        this.author = author;
        this.status = status;
        this.quantity = quantity;
    }


    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        BookDto.name = name;
    }

    public static String getAuthor() {
        return author;
    }

    public static void setAuthor(String author) {
        BookDto.author = author;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        BookDto.status = status;
    }

    public static int getQuantity() {
        return quantity;
    }

    public static void setQuantity(int quantity) {
        BookDto.quantity = quantity;
    }

    public static int getIsbn() {
        return isbn;
    }

    public static void setIsbn(int isbn) {
        BookDto.isbn = isbn;
    }

}
