package main.java.com.app;


import main.java.com.app.entities.Client;
import main.java.com.app.entities.Project;
import main.migrations.validation.Validator;


import java.sql.SQLException;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws SQLException, IllegalAccessException {

        Client client = new Client();
        client.setId(1);
        client.setName("adziaojdpzij");
        List<String> errors =  Validator.validate(client);
        if (errors.isEmpty()) {
            System.out.println("All fields are valid.");
        } else {
            for (String error : errors) {
                System.out.println(error);
            }
        }

//        List<Project> projects = new ProjectRepositoryImpl().getAll();
//        for (Project project : projects) {
//            System.out.println(project);
//        }
        while (true) {
            showMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    createNewProject();
                    break;
                case 2:
                    displayExistingProjects();
                    break;
                case 3:
                    calculateProjectCost();
                    break;
                case 4:
                    System.out.println("Quitting the application...");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Option non valide. Veuillez réessayer.");
                    break;
            }
        }
    }
    private static void showMainMenu() {
        System.out.println("=== Bienvenue dans l'application de gestion des projets de rénovation de cuisines ===");
        System.out.println("=== Menu Principal ===");
        System.out.println("1. Créer un nouveau projet");
        System.out.println("2. Afficher les projets existants");
        System.out.println("3. Calculer le coût d'un projet");
        System.out.println("4. Quitter");
        System.out.print("Choisissez une option : ");
    }

    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("Entrée invalide. Veuillez entrer un nombre : ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void createNewProject() {
        System.out.println("--- Création d'un Nouveau Projet ---");
        scanner.nextLine(); // Consume newline

        System.out.print("Entrez le nom du projet : ");
        String projectName = scanner.nextLine();

        System.out.print("Entrez la surface de la cuisine (en m²) : ");
        double surface = scanner.nextDouble();

        // Call methods to handle materials and labor, calculate costs, etc.

        System.out.println("Projet créé avec succès !");
    }

    private static void displayExistingProjects() {
        System.out.println("--- Affichage des projets existants ---");

        // Retrieve and display existing projects from the database or data source.

        System.out.println("Liste des projets : ");
        // Example placeholder
        System.out.println("1. Projet A - Client A - Coût Total: 5000 €");
        System.out.println("2. Projet B - Client B - Coût Total: 7500 €");
    }

    private static void calculateProjectCost() {
        System.out.println("--- Calcul du coût d'un projet ---");

        // Allow user to select a project and calculate costs.

        System.out.print("Sélectionnez un projet parmi la liste des projets existants : ");
        int projectId = getUserChoice();

        // Retrieve the selected project and perform cost calculations.

        System.out.println("Calcul du coût en cours...");
        // Example placeholder result
        System.out.println("Coût total calculé : 6000 €");
    }
}