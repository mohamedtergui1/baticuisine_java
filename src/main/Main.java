package main;

import main.java.com.app.entities.*;

import main.java.com.app.enums.Status;
import main.java.com.app.service.*;

import main.java.com.app.utils.ValidationUtils;
import main.myframework.injector.DependencyInjector;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

       //Client client =  DependencyInjector.createInstance(ClientService.class).getClient(2);
//        Project project =DependencyInjector.createInstance(ProjectService.class).getProject(3);
//        DependencyInjector.createInstance(ProjectService.class).updateProject(project);
//        for(Project project : client.getProjects()){
//            System.out.println(project);
//            for(Labor labor : project.getLabors()){
//                System.out.println(labor);
//            }
//            for(Material material : project.getMaterials()){
//                System.out.println(material);
//            }
//        }

//        Project project = DependencyInjector.createInstance(ProjectService.class).getProject(1);
//        displayProjectDetails(project);
//        System.out.println(project);
//        Project client = DependencyInjector.createInstance(ProjectService.class).updateProject(project);

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
                c.showAsMenu();
            }
            System.out.print("veullez choisir un client:");
            int choice = getUserChoice();
                scanner.nextLine();
                if(scanner.hasNextInt()) {
                    choice = getUserChoice();
                    scanner.nextLine();

                    client  = DependencyInjector.createInstance(ClientService.class).getClient(choice);

                }
                return client;
    }

    private static void createNewProject(Client client) {
        Project project = new Project();
        System.out.println("client qui vous avez selection");
        client.showAsMenu();
        project.setClient(client);

        System.out.print("Entrez le nom du projet : ");
        String projectName = scanner.nextLine();
        project.setProjectName(projectName);
        System.out.print("Entrez la surface de la cuisine (en m²) : ");
        double surface = scanner.nextDouble();
        scanner.nextLine();
        project.setSurfaceArea(surface);
        project = DependencyInjector.createInstance(ProjectService.class).addProject(project);
        if( project != null) {
            System.out.println("Projet créé avec succès !");
        }
        else System.out.println("Project ne create pas");
        int choice;
        do{
            System.out.println("1 : ajouter material");
            System.out.println("2 : ajouter main d ouvre");
            System.out.println("3 : quitter ");
            System.out.print("Choisissez une option : ");
            choice = scanner.nextInt();
            scanner.nextLine();
            if(choice == 1) {
                createNewMaterial(project);
            }
            else if(choice == 2) {
                createNewLabor(project);
            }
        }while(choice != 3);
        calculateProjectCost(project.getId());
    }

    public static void createNewMaterial(Project project) {
        Material material = new Material();
        System.out.print("--- Ajouter des matériaux ---\n");

        // Name
        System.out.print("Entrez le nom du matériau : ");
        material.setName(scanner.nextLine().trim());
        if (material.getName().isEmpty()) {
            System.out.println("Le nom du matériau ne peut pas être vide.");
            return;
        }

        // Quantity
        System.out.print("Entrez la quantité de ce matériau (en m²) : ");
        double quantity = scanner.nextDouble();
        if (quantity <= 0) {
            System.out.println("La quantité doit être supérieure à 0.");
            return;
        }
        material.setQuantity(quantity);

        // Unit Cost
        System.out.print("Entrez le coût unitaire de ce matériau (€/m²) : ");
        double unitCost = scanner.nextDouble();
        if (unitCost < 0) {
            System.out.println("Le coût unitaire ne peut pas être négatif.");
            return;
        }
        material.setUnitCost(unitCost);

        // Transport Cost
        System.out.print("Entrez le coût de transport de ce matériau (€) : ");
        double transportCost = scanner.nextDouble();
        if (transportCost < 0) {
            System.out.println("Le coût de transport ne peut pas être négatif.");
            return;
        }
        material.setTransportCost(transportCost);

        // Quality Coefficient
        System.out.print("Entrez le coefficient de qualité de ce matériau : ");
        double qualityCoefficient = scanner.nextDouble();
        if (qualityCoefficient < 0) {
            System.out.println("Le coefficient de qualité ne peut pas être négatif.");
            return;
        }
        material.setQualityCoefficient(qualityCoefficient); // Assuming you have this method

        scanner.nextLine(); // Consume the newline character
        material.setProject(project);

        material = DependencyInjector.createInstance(MaterialService.class).addMaterial(material);
        if (material != null) {
            System.out.println("Matériau ajouté avec succès !");
        } else {
            System.out.println("Quelque chose s'est mal passé, veuillez réessayer.");
        }
    }

    private static void createNewLabor(Project project) {
        Labor labor = new Labor();
        System.out.print("--- Ajout de la main-d'œuvre ---\n");

        // Labor Type
        System.out.print("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : ");
        labor.setName(scanner.nextLine().trim());
        if (labor.getName().isEmpty()) {
            System.out.println("Le type de main-d'œuvre ne peut pas être vide.");
            return;
        }

        // Hourly Rate
        System.out.print("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
        double hourlyRate = scanner.nextDouble();
        if (hourlyRate < 0) {
            System.out.println("Le taux horaire ne peut pas être négatif.");
            return;
        }
        labor.setHourlyRate(hourlyRate);

        // Hours Worked
        System.out.print("Entrez le nombre d'heures travaillées : ");
        double hoursWorked = scanner.nextDouble();
        if (hoursWorked <= 0) {
            System.out.println("Le nombre d'heures travaillées doit être supérieur à 0.");
            return;
        }
        labor.setHoursWorked(hoursWorked);

        // Productivity Factor
        System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
        double workerProductivity = scanner.nextDouble();
        if (workerProductivity <= 0) {
            System.out.println("Le facteur de productivité doit être supérieur à 0.");
            return;
        }
        labor.setWorkerProductivity(workerProductivity);

        labor.setProject(project);
        labor = DependencyInjector.createInstance(LaborService.class).addLabor(labor);
        if (labor != null) {
            System.out.println("Main-d'œuvre créée avec succès !");
        } else {
            System.out.println("Quelque chose s'est mal passé lors de la création de la main-d'œuvre.");
        }
    }

    private static void calculateProjectCost(){
        System.out.println("calcule codt");
        displayALLProjects();
        System.out.println("choisi un project");
        int choice =  scanner.nextInt();
        scanner.nextLine();
        calculateProjectCost(choice);
    }

    private static void displayExistingProjects() {
        System.out.println("--- Affichage des projets existants ---");
        System.out.println("Liste des projets : ");
        displayALLProjects();

        Project project;
        int choice;

        // Loop to select a valid project
        do {
            System.out.print("Sélectionnez un projet : ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer
            project = DependencyInjector.createInstance(ProjectService.class).getProject(choice);
        } while (project == null);
        System.out.println("\n\nroject tu a selection");
        project.showAsMenu();
        System.out.println("1 - Rédiger un devis");
        System.out.println("2 - Changer le statut du projet");
        System.out.print("entrer votre chois :");

        int choice1 = scanner.nextInt();
        scanner.nextLine();

        switch (choice1) {
            case 1:
                addDevi(project);
                break;
            case 2:
                changeStatusProject(project);
                break;
            default:
                System.out.println("Choix invalide. Veuillez réessayer.");
                break;
        }
    }

    private static void changeStatusProject(Project project) {
        ProjectService projectService = DependencyInjector.createInstance(ProjectService.class);

        // Display the status change menu
        System.out.println("--- Changer le statut du projet ---");
        System.out.println("1 - Changer le statut en : Complété");
        System.out.println("2 - Changer le statut en : Annulé");
        System.out.print("Veuillez entrer votre choix : ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        // Process the user's choice
        switch (choice) {
            case 1:
                project.setProjectStatus(Status.COMPLETED);
                projectService.updateProject(project);
                System.out.println("Succès : Le statut du projet a été changé en 'Complété'.");
                break;
            case 2:
                project.setProjectStatus(Status.CANCELLED);
                projectService.updateProject(project);
                System.out.println("Succès : Le statut du projet a été changé en 'Annulé'.");
                break;
            default:
                System.out.println("Erreur : Choix invalide. Veuillez réessayer.");
                break;
        }
    }

    private static void addDevi(Project project) {
        Estimate estimate = new Estimate();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        estimate.setProject(project);
        while (estimate.getIssueDate() == null) {
            System.out.print("Entrez la date d'émission du devis (format : jj/mm/aaaa) : ");
            String inputDate = scanner.nextLine();

            try {
                LocalDate issueDate = LocalDate.parse(inputDate, dateFormatter);
                estimate.setIssueDate(issueDate);
            } catch (DateTimeParseException e) {
                System.out.println("Date invalide. Veuillez entrer une date au format jj/mm/aaaa.");
            }
        }


        while (estimate.getValidityDate() == null) {
            System.out.print("Entrez la date de validité du devis (format : jj/mm/aaaa) : ");
            String inputDate = scanner.nextLine();

            try {
                LocalDate validityDate = LocalDate.parse(inputDate, dateFormatter);
                estimate.setValidityDate(validityDate);
            } catch (DateTimeParseException e) {
                System.out.println("Date invalide. Veuillez entrer une date au format jj/mm/aaaa.");
            }
        }


        ValidationUtils validationUtils = new ValidationUtils();
        if (validationUtils.isValidEstimate(estimate)) {
            if (DependencyInjector.createInstance(EstimateService.class).addEstimate(estimate) != null) {
                System.out.println("Devis ajouté avec succès.");
            } else {
                System.out.println("Something went wrong during estimation.");
            }
        } else {
            System.out.println("Le devis n'est pas valide. Veuillez vérifier les dates et le montant estimé.");
        }
    }

    private static void displayALLProjects() {
        List<Project> projects  = DependencyInjector.createInstance(ProjectService.class).getAllProject();
        for (Project project : projects) {
            project.showAsMenu();
        }
    }

    private static void calculateProjectCost(int projectId) {
        System.out.println("--- Calcul du coût d'un projet ---");

        ProjectService projectService = DependencyInjector.createInstance(ProjectService.class);
        Project project = projectService.getProject(projectId);
        if (project == null) {
            System.out.println("Le projet n'existe pas");
            return; // Exit instead of calling main(null)
        }

        System.out.print("Souhaitez-vous appliquer une TVA au projet ? (y/n) : ");
        String tvaResponse = scanner.next();
        double tva = (tvaResponse.equalsIgnoreCase("y")) ? getInputDouble("Entrez le taux de TVA : ") : 0;

        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        String mBResponse = scanner.next();
        double mB = 0;
        if (mBResponse.equalsIgnoreCase("y")) {
            mB = getInputDouble("Entrez le taux de marge bénéficiaire : ");
            if (mB < 0) {
                System.out.println("Le taux de marge bénéficiaire ne peut pas être négatif.");
                return;
            }
        }

        System.out.println("Marge bénéficiaire : " + mB);
        System.out.println("Calcul du coût en cours...");
        project = projectService.calculProjectCost(project, tva, mB);
        System.out.println("Coût total calculé : " + project.getTotalCost());
        displayProjectDetails(project, tva);
    }

    // Ensure this method correctly handles input and exceptions
    private static double getInputDouble(String message) {
        double value = -1;
        while (value < 0) { // Adjust condition based on your needs
            System.out.print(message);
            try {
                value = scanner.nextDouble();
                if (value < 0) {
                    System.out.println("Veuillez entrer une valeur positive.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Valeur invalide. Veuillez entrer un nombre.");
                scanner.next(); // Clear the invalid input
            }
        }
        scanner.nextLine(); // Consume the newline character
        return value;
    }


    private static void displayProjectDetails(Project project ,double tva) {
        // Display project basic information
        System.out.println("Nom du projet : " + project.getProjectName());
        System.out.println("Client : " + project.getClient().getName());
        System.out.println("Adresse du chantier : " + project.getClient().getAddress());

        System.out.println("--- Détail des Coûts ---");


        double vatRate = tva;
        double profitMargin = project.getProfitMargin();

        if (vatRate <= 0) {
            System.out.println("Erreur : le taux de TVA doit être supérieur à 0.");
            return;
        }
        if (profitMargin <= 0) {
            System.out.println("Erreur : la marge bénéficiaire doit être supérieure à 0.");
            return;
        }

        // Calculate material costs
        double totalMaterialsCost = 0;
        double totalMaterialsCostWithTva = 0;
        System.out.println("1. Matériaux :");
        for (Material material : project.getMaterials()) {
            double materialCost = material.calculateCost();
            totalMaterialsCost += materialCost;
            double materialCostWithTva = materialCost * (1 + vatRate / 100);
            totalMaterialsCostWithTva += materialCostWithTva;

            System.out.printf("- %s : %.2f € (quantité : %.2f %s, coût unitaire : %.2f €/m², qualité : %.2f, transport : %.2f €)%n",
                    material.getName(), materialCost, material.getQuantity(), material.getUnitCost(),
                    material.getUnitCost(), material.getQualityCoefficient(), material.getTransportCost());
        }
        System.out.printf("**Coût total des matériaux avant TVA : %.2f €**%n", totalMaterialsCost);
        System.out.printf("**Coût total des matériaux avec TVA (%.0f%%) : %.2f €**%n",
                vatRate, totalMaterialsCostWithTva);

        // Calculate labor costs
        double totalLaborCost = 0;
        System.out.println("2. Main-d'œuvre :");
        for (Labor labor : project.getLabors()) {
            double laborCost = labor.calculateCost();
            totalLaborCost += laborCost;

            System.out.printf("- %s : %.2f € (taux horaire : %.2f €/h, heures travaillées : %.2f h, productivité : %.2f)%n",
                    labor.getName(), laborCost, labor.getHourlyRate(), labor.getHoursWorked(),
                    labor.getWorkerProductivity());
        }
        double laborCostWithVAT = totalLaborCost * (1 + 0.2);
        System.out.printf("**Coût total de la main-d'œuvre avant TVA : %.2f €**%n", totalLaborCost);
        System.out.printf("**Coût total de la main-d'œuvre avec TVA (%.2f%%) : %.2f €**%n",vatRate, laborCostWithVAT);

        // Calculate total costs
        double totalCostBeforeMargin = totalMaterialsCost + totalLaborCost;
        double margin = totalCostBeforeMargin * profitMargin;
        double finalTotalCost = totalCostBeforeMargin + margin;

        System.out.printf("3. Coût total avant marge : %.2f €%n", totalCostBeforeMargin);
        System.out.printf("4. Marge bénéficiaire (%.0f%%) : %.2f €%n", profitMargin , margin);
        System.out.printf("**Coût total final du projet : %.2f €**%n", finalTotalCost);
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