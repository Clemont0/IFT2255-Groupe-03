package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Thread(() -> ServerApp.startServer()).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        displayMenu();
    }


    public static void displayMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n---------------------- Bienvenue sur MaVille! -----------------------");
            System.out.println("1. Se connecter");
            System.out.println("2. S'inscrire");
            System.out.println(("3. Quitter"));
            System.out.print("Veuillez choisir une option : ");

            if (scanner.hasNextInt()) {
                int choix = scanner.nextInt();
                scanner.nextLine();

                if (choix == 1) {
                    System.out.print("Veuillez entrer votre email : ");
                    String courriel = scanner.nextLine();
                    System.out.print("Veuillez entrer votre mot de passe : ");
                    String mdp = scanner.nextLine();

                    System.out.println("Tentative de connexion...");
                    MaVille.seConnecter(courriel, mdp, null);
                } else if (choix == 2) {
                    System.out.println("Pas implementer");
                }else if ( choix == 3) {
                    System.out.println("Au revoir");
                    ServerApp.stopServer();
                    break;
                } else {
                    System.out.println("Option invalide. Veuillez réessayer.");
                }
            } else {

                System.out.println("Option invalide. Veuillez entrer un nombre valide.");
                scanner.nextLine(); }
        }

        scanner.close();
    }
}
