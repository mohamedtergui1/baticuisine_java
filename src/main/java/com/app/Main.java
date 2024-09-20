package main.java.com.app;



import main.java.com.app.entities.Client;
import main.java.com.app.entities.Project;

import main.java.com.app.repository.client.ClientRepositoryImpl;
import main.java.com.app.service.ClientService;
import main.java.com.app.service.ProjectService;
import main.myframework.injector.DependencyInjector;


import java.sql.SQLException;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws SQLException, IllegalAccessException {

        List<Client> clients = new ClientRepositoryImpl().searchByName("a");
        for (Client client : clients) {
            System.out.println(client);
        }
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
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant\n" +
                "2. Ajouter un nouveau client");
        Client client = new Client();

            int choice = getUserChoice();
            scanner.nextLine();
            switch (choice)
            {
                case 1:
                     searchClient(client);
                     break;
                case 2:
            }


        createNewProject(client);


    }

    private static void searchClient(Client client) {
        System.out.println("chercher un client");
            System.out.print("entrez le nom de client:");
            String line = scanner.nextLine();
            List<Client> clients = DependencyInjector.createInstance(ClientService.class).SearchByName(line);
            for (Client c : clients) {
                System.out.println(c);
            }

            int choice = getUserChoice();
                scanner.nextLine();
                if(scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println(choice);
                    client  = DependencyInjector.createInstance(ClientService.class).getClient(choice);
                    System.out.println(client);
                }

    }

    private static void createNewProject(Client client) {
        Project project = new Project();
        project.setClient(client);
        System.out.println("--- Création d'un Nouveau Projet ---");



        System.out.print("Entrez le nom du projet : ");
        String projectName = scanner.nextLine();
        project.setProjectName(projectName);
        System.out.print("Entrez la surface de la cuisine (en m²) : ");
        double surface = scanner.nextDouble();
        project.setProfitMargin(surface);

        System.out.println(project);
        System.out.println("Projet créé avec succès !");
    }

    private static void displayExistingProjects() {
        System.out.println("--- Affichage des projets existants ---");
        System.out.println("Liste des projets : ");
        List<Project> projects  = DependencyInjector.createInstance(ProjectService.class).getAllProject();
        for (Project project : projects) {
            System.out.println(project);
        }
    }

    private static void calculateProjectCost() {
        System.out.println("--- Calcul du coût d'un projet ---");
        System.out.print("Sélectionnez un projet parmi la liste des projets existants : ");
        int projectId = getUserChoice();
        System.out.println("Calcul du coût en cours...");
        System.out.println("Coût total calculé : 6000 €");
    }
}