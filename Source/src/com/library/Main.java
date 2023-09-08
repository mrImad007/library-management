package com.library;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {
        while (true) {
            String menu = JOptionPane.showInputDialog("""
                    Bienvenue dans votre application\s
                    ******* Menu *******\s
                    1 : Afficher la liste des livres disponibles
                    2 : Gestion des livres\s
                    3 : Trouver un livre \s
                    4 : Emprunter un livre\s
                    5 : Retourner un livre
                    6 : Générer des rapports statiques
                    0 : Quitter""");

            try {
                int choice = Integer.parseInt(menu);

                switch (choice) {
                    case 0: {
                        JOptionPane.showMessageDialog(null, "Fin du programme\nÀ la prochaine :)");
                        return;
                    }
                    case 1: {
                        Book.display();
                        break;
                    }
                    case 2: {
                        boolean managementMenu = true;

                        while (managementMenu) {
                            String subMenu = JOptionPane.showInputDialog(
                                    """
                                            ******* Menu *******\s
                                            1 : Ajouter un livre\s
                                            2 : Modifier\s
                                            3 : Supprimer \s
                                            0 : Retour\s""");

                            int subChoice = Integer.parseInt(subMenu);

                            switch (subChoice) {
                                case 0: {
                                    managementMenu = false;
                                    break;
                                }
                                case 1: {
                                    try {
                                        Book.InsertBook();
                                        JOptionPane.showMessageDialog(null, "Nouveau livre ajouté à la bibliotheque :)");
                                        managementMenu = false;
                                    } catch (Exception e) {
                                        JOptionPane.showMessageDialog(null, "Une erreur s'est produite lors de l'ajout du livre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                                    }
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
                                            2 : recherche par auteur\s """);

                            int researchChoice = Integer.parseInt(subMenu);

                            switch (researchChoice) {
                                case 1: {
                                    try{
                                        String title = JOptionPane.showInputDialog("Enter le titre du livre :");
                                        Book.displayByName(title);
                                    }catch (Exception e){
                                        throw new RuntimeException(e);
                                    }
                                    break;
                                }
                                case 2: {
                                    try {
                                        String author = JOptionPane.showInputDialog("Entrer le nom de l'auteur :");
                                        Book.displayByAuthor(author);
                                    }catch (Exception e){
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                    }
                    case 4 : {
                        boolean brwMenu = true;

                        while (brwMenu) {
                            String borrowMenu = JOptionPane.showInputDialog(
                                    """
                                            ******* Menu *******\s
                                            1 : Emprunter pour l-un client deja existant\s
                                            2 : Ajouter un nouveau client\s""");

                            int borrowChoice = Integer.parseInt(borrowMenu);
                            switch (borrowChoice){

                            }
                        }
                    }
                    default:
                        System.out.println("Choix indisponible");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre valide.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
