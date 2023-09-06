package com.library;

import java.util.Scanner;
import com.library.Book;

public class Main {
    public static final String ANSI_YELLOW = "\u001B[41m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println(ANSI_YELLOW+"Bienvenue dans votre application"+ ANSI_RESET);
        System.out.println("menu \n 1 : liste des livres\n 2 : \n 3 : ");
        int choice = scanner.nextInt();

        switch (choice){
            case 1 : {
                Book.display();
            }
            default:
                System.out.println("Choix indisponnible");
        }
    }

}
