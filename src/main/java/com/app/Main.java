package main.java.com.app;



import main.java.com.app.entities.*;


import main.java.com.app.service.ClientService;
import main.java.com.app.service.LaborService;

import main.java.com.app.service.ProjectService;
import main.myframework.injector.DependencyInjector;


import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        ClientService clientService = DependencyInjector.createInstance(ClientService.class);
        Client client = clientService.getClient(1);
        client = clientService.updateClient(client);
        System.out.println(client);

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



    private static void createNewProject() {
        System.out.println("Souhaitez-vous chercher un client existant ou en ajouter un nouveau ?");
        System.out.println("1. Chercher un client existant\n" +
                "2. Ajouter un nouveau client");
        System.out.print("veuillez entrer un nombre : ");
        Client client = new Client();

            int choice = getUserChoice();
            scanner.nextLine();
            switch (choice)
            {
                case 1:
                    client = searchClient(client);
                     break;
                case 2:
                    client = createNewUser();
            }


        createNewProject(client);

    }

    private static Client createNewUser() {
        Client client = new Client();

        System.out.println("Enter client details:");

        // Get and validate name
        String name;
        do {
            System.out.print("Name (6-30 characters): ");
            name = scanner.nextLine();
            if (!isValid(name, 6, 30)) {
                System.out.println("Invalid name. Please try again.");
            }
        } while (!isValid(name, 6, 30));
        client.setName(name);

        // Get and validate address
        String address;
        do {
            System.out.print("Address (6-255 characters): ");
            address = scanner.nextLine();
            if (!isValid(address, 6, 255)) {
                System.out.println("Invalid address. Please try again.");
            }
        } while (!isValid(address, 6, 255));
        client.setAddress(address);

        // Get and validate phone number
        String phoneNumber;
        do {
            System.out.print("Phone number (9-13 characters): ");
            phoneNumber = scanner.nextLine();
            if (!isValid(phoneNumber, 9, 13)) {
                System.out.println("Invalid phone number. Please try again.");
            }
        } while (!isValid(phoneNumber, 9, 13));
        client.setPhoneNumber(phoneNumber);

        // Get isProfessional status
        System.out.print("Is the client a professional? (yes/no): ");
        String isProfessionalInput = scanner.nextLine();
        client.setProfessional(isProfessionalInput.equalsIgnoreCase("yes"));
        client = DependencyInjector.createInstance(ClientService.class).addClient(client);

        return client;
    }



    private static Client searchClient(Client client) {
        System.out.println("chercher un client");
            System.out.print("entrez le nom de client:");
            String line = scanner.nextLine();
            List<Client> clients = DependencyInjector.createInstance(ClientService.class).SearchByName(line);
            for (Client c : clients) {
                System.out.println(c);
            }
            System.out.print("veullez choisir un client:");
            int choice = getUserChoice();
                scanner.nextLine();
                if(scanner.hasNextInt()) {
                    choice = getUserChoice();
                    scanner.nextLine();

                    client  = DependencyInjector.createInstance(ClientService.class).getClient(choice);
                    System.out.println(client);
                }
                return client;
    }

    private static void createNewProject(Client client) {
        Project project = new Project();
        System.out.println("client qui vous avez selection");
        project.setClient(client);




        System.out.print("Entrez le nom du projet : ");
        String projectName = scanner.nextLine();
        project.setProjectName(projectName);
        System.out.print("Entrez la surface de la cuisine (en m²) : ");
        double surface = scanner.nextDouble();
        project.setProfitMargin(surface);
        project = DependencyInjector.createInstance(ProjectService.class).addProject(project);
        if( project != null) {
            System.out.println("Projet créé avec succès !");

        }
        else System.out.println("Project ne create pas");
    }

    private static void displayExistingProjects() {
        System.out.println("--- Affichage des projets existants ---");
        System.out.println("Liste des projets : ");
        displaALLProjects();
    }
    private static void displaALLProjects() {
        List<Project> projects  = DependencyInjector.createInstance(ProjectService.class).getAllProject();
        for (Project project : projects) {
            System.out.println(project);
        }
    }
    private static void calculateProjectCost() {
        System.out.println("--- Calcul du coût d'un projet ---");
        displaALLProjects();
        System.out.print("Sélectionnez un projet parmi la liste des projets existants : ");
        int projectId = getUserChoice();
        List<Labor> labors = DependencyInjector.createInstance(LaborService.class).getAllLabor();
        double sum  = 0;
        for (Labor l : labors) {
            sum += calculateCostPlusTva(l);
        }

        System.out.println("Calcul du coût en cours...");
        System.out.println("Coût total calculé : " + sum);
    }


    public static double calculateCostPlusTva(Component component) {
        return component.calculateCost() + component.calculateCost() * 0.01;
    }


    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("Entrée invalide. Veuillez entrer un nombre : ");
            scanner.next();
        }
        return scanner.nextInt();
    }
    private static boolean isValid(String value, int minLength, int maxLength) {
        return value != null && value.length() >= minLength && value.length() <= maxLength;
    }
}