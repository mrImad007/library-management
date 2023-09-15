package com.library;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static com.library.Book.getTotalBookCounts;

public class Main {
    public static void main(String[] args) {

        boolean auth = false;
        while(!auth){
            auth = Bibliothequaire.authentification();
        }

        while (true) {
            String menu = JOptionPane.showInputDialog("""
                    Bienvenue dans votre application\s
                    ******* Menu *******\s
                    1 : Afficher la liste des livres
                    2 : Gestion de la bibliotheque\s
                    3 : Trouver un livre \s
                    4 : Emprunter un livre\s
                    5 : Retourner un livre\s
                    6 : Générer des rapports statiques\s
                    7 : Supprimer un client\s
                    0 : Quitter""");

            try {
                int choice = Integer.parseInt(menu);

                switch (choice) {
                    case 0: {
                        JOptionPane.showMessageDialog(null, "Fin du programme\nÀ la prochaine :)");
                        return;
                    }
                    case 1: {
                        String showMenu = JOptionPane.showInputDialog("""
                                Bienvenue dans votre application\s
                                ******* Menu *******\s
                                1 : Livres disponibles\s
                                2 : Livres indisponibles
                                3 : Livres empruntés\s""");
                        int choiceShowMenu = Integer.parseInt(showMenu);

                        switch (choiceShowMenu){
                            case 1 : {
                                Book.display();
                                break;
                            }
                            case 2 : {
                                Book.displayIndisponible();
                                break;
                            }
                            case 3 : {
                                Emprunt.borrowedBooks();
                                break;
                            }
                        }

                        break;
                    }
                    case 2: {
                        boolean managementMenu = true;

                        while (managementMenu) {
                            String subMenu = JOptionPane.showInputDialog(
                                    """
                                            ******* Menu *******\s
                                            1 : Ajouter un livre\s
                                            2 : Modifier un livre\s
                                            3 : Supprimer livre\s
                                            4 : Marquer un livre perdu \s
                                            0 : Retour\s""");

                            int subChoice = Integer.parseInt(subMenu);

                            switch (subChoice) {
                                case 0: {
                                    managementMenu = false;
                                    break;
                                }
                                case 1: {
                                    Book.InsertBook();
                                    managementMenu = false;
                                    break;
                                }
                                case 2: {
                                    try {
                                        Book.UpdateBook();
                                        JOptionPane.showMessageDialog(null, "Nouveau livre ajouté à la bibliotheque :)");
                                        managementMenu = false;
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de l'ajout du livre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                                    }
                                    break;
                                }
                                case 3: {
                                    try {
                                        Book.DeleteBook();
                                        managementMenu = false;
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de l'ajout du livre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                                    }
                                    break;
                                }
                                case 4 : {
                                    Book.markLost();
                                    break;
                                }
                                default: {
                                    JOptionPane.showMessageDialog(null, "Choix indisponible", "Erreur", JOptionPane.ERROR_MESSAGE);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    case 3: {
                        boolean researchMenu = true;

                        while (researchMenu) {
                            String subMenu = JOptionPane.showInputDialog(
                                    """
                                            ******* Menu *******\s
                                            1 : Recherche par titre du livre\s
                                            2 : recherche par auteur""");

                            int researchChoice = Integer.parseInt(subMenu);

                            switch (researchChoice) {
                                case 1: {
                                    try{
                                        String title = JOptionPane.showInputDialog("Enter le titre du livre :");
                                        Book.displayByName(title);
                                    }catch (Exception e){
                                        throw new RuntimeException(e);
                                    }
                                    researchMenu = false;
                                    break;
                                }
                                case 2: {
                                    try {
                                        String author = JOptionPane.showInputDialog("Entrer le nom de l'auteur :");
                                        Book.displayByAuthor(author);
                                    }catch (Exception e){
                                        throw new RuntimeException(e);
                                    }
                                    researchMenu = false;
                                    break;
                                }
                                default:{
                                    JOptionPane.showMessageDialog(null,"Choix indiponible");
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    case 4 : {
                        boolean brwMenu = true;

                        while (brwMenu) {
                            String borrowMenu = JOptionPane.showInputDialog(
                                    """
                                            ******* Menu *******\s
                                            1 : Emprunter pour l-un client deja existant\s
                                            2 : Ajouter un nouveau client""");

                            int borrowChoice = Integer.parseInt(borrowMenu);
                            switch (borrowChoice){
                                case 1 : {
                                    Client.Display();
                                    Emprunt.borrowBook();
                                    brwMenu = false;
                                    break;
                                }
                                case 2 : {
                                    Client.AddClient();
                                    break;
                                }
                                default:{
                                    JOptionPane.showMessageDialog(null,"Choix indiponible");
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    case 5 : {
                        Emprunt.retunBook();
                        break;
                    }
                    case 6 : {
                        generateStatistics();
                        break;
                    }
                    case 7 : {
                        Client.deleteClient();
                        break;
                    }
                    default:
                        System.out.println("Choix indisponible");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre valide.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            }
        }


    }


    public static void generateStatistics(){
        try {
            Map<String, Integer> bookCounts = getTotalBookCounts();
            int disponibleCount = bookCounts.getOrDefault("disponibles", 0);
            int indisponibleCount = bookCounts.getOrDefault("indisponibles",0);
            int perdusCount = bookCounts.getOrDefault("perdus", 0);
            int borrowed = Emprunt.BorrowingStatistics();
            BufferedWriter writer = new BufferedWriter(new FileWriter("Statistiques.txt"));

            writer.write("*****************  les statistiques de votre bibliotheque  *****************\n\n");
            writer.newLine();
            writer.write("Livres disponibles: " + disponibleCount);
            writer.newLine();
            writer.write("Livres indisponibles: " + indisponibleCount);
            writer.newLine();
            writer.write("Livres perdus : " + perdusCount);
            writer.newLine();
            writer.write("Livres empruntés: " + borrowed);

            writer.close();

            JOptionPane.showMessageDialog(null,"Votre fichier des statistiques est généré par succes. (Statistiques.txt)");
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
