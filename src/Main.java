import main.java.com.app.entities.*;

import main.java.com.app.service.ClientService;
import main.java.com.app.service.LaborService;

import main.java.com.app.service.MaterialService;
import main.java.com.app.service.ProjectService;
import main.myframework.injector.DependencyInjector;

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
                    //calculateProjectCost();
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
            System.out.print("Entrez le nom du matériau : ");
            material.setName(scanner.nextLine());
            System.out.print("Entrez la quantité de ce matériau (en m²) : ");
            material.setQuantity(scanner.nextDouble());
            System.out.print("Entrez le coût unitaire de ce matériau (€/m²) : ");
            material.setUnitCost(scanner.nextDouble());
            System.out.print("Entrez le coût de transport de ce matériau (€) : ");
            material.setTransportCost(scanner.nextDouble());
            scanner.nextLine();
            material.setProject(project);
            material = DependencyInjector.createInstance(MaterialService.class).addMaterial(material);
            if(material == null){
                System.out.println("material ajouter avec success");
            }
            else System.out.println("somthing is wrong try again");
    }
    private static void createNewLabor(Project project) {
        Labor labor = new Labor();
        System.out.print("--- Ajout de la main-d'œuvre ---\n");
        System.out.print("Entrez le type de main-d'œuvre (e.g., Ouvrier de base, Spécialiste) : ");
        labor.setName(scanner.nextLine());

        System.out.print("Entrez le taux horaire de cette main-d'œuvre (€/h) : ");
        labor.setHourlyRate(scanner.nextDouble());

        System.out.print("Entrez le nombre d'heures travaillées : ");
        labor.setHoursWorked(scanner.nextDouble());

        System.out.print("Entrez le facteur de productivité (1.0 = standard, > 1.0 = haute productivité) : ");
        labor.setWorkerProductivity(scanner.nextDouble());

        labor.setProject(project);
        labor = DependencyInjector.createInstance(LaborService.class).addLabor(labor);
        if( labor != null) {
            System.out.println("Main-d'œuvre créée avec succès !");
        }
        else System.out.println("something is wrong during labor creation");
    }

    private static void displayExistingProjects() {
        System.out.println("--- Affichage des projets existants ---");
        System.out.println("Liste des projets : ");
        displayALLProjects();
    }

    private static void displayALLProjects() {
        List<Project> projects  = DependencyInjector.createInstance(ProjectService.class).getAllProject();
        for (Project project : projects) {
            System.out.println(project);
        }
    }

    private static void calculateProjectCost(int projectId) {
        System.out.println("--- Calcul du coût d'un projet ---");

        ProjectService projectService = DependencyInjector.createInstance(ProjectService.class);
        Project project = projectService.getProject(projectId);


        System.out.print("Souhaitez-vous appliquer une TVA au projet ? (y/n) : ");
        String tvaResponse = scanner.next();
        double tva = (tvaResponse.equalsIgnoreCase("y")) ? getInputDouble("Entrez le taux de TVA : ") : 0;


        System.out.print("Souhaitez-vous appliquer une marge bénéficiaire au projet ? (y/n) : ");
        String mBResponse = scanner.next();
        double mB = (mBResponse.equalsIgnoreCase("y")) ? getInputDouble("Entrez le taux de marge bénéficiaire : ") : 0;

        System.out.println("Calcul du coût en cours...");
        project = projectService.calculProjectCost(project, tva, mB);
        System.out.println("Coût total calculé : " + project.getTotalCost());
        displayProjectDetails(project);
    }

    private static void displayProjectDetails(Project project) {
        System.out.println("Nom du projet : " + project.getProjectName());
        System.out.println("Client : " + project.getClient().getName());
        System.out.println("Adresse du chantier : " + project.getClient().getAddress());

        System.out.println("--- Détail des Coûts ---");

        double totalMaterialsCost = 0;
        double totalMaterialsCostWithTva = 0;
        System.out.println("1. Matériaux :");
        for (Material material : project.getMaterials()) {
            double materialCost = material.calculateCost();
            totalMaterialsCost += materialCost;
            double materialCostWithTva = materialCost * (1 + material.getVatRate());
            totalMaterialsCostWithTva += materialCostWithTva;

            System.out.printf("- %s : %.2f € (quantité : %.2f %s, coût unitaire : %.2f €/m², qualité : %.2f, transport : %.2f €)%n",
                    material.getName(), materialCost, material.getQuantity(), material.getUnitCost(),
                    material.getUnitCost(), material.getQualityCoefficient(), material.getTransportCost());
        }
        System.out.printf("**Coût total des matériaux avant TVA : %.2f €**%n", totalMaterialsCost);
        System.out.printf("**Coût total des matériaux avec TVA (%.0f%%) : %.2f €**%n",
                project.getMaterials().get(0).getVatRate() * 100, totalMaterialsCostWithTva);

        double totalLaborCost = 0;
        System.out.println("2. Main-d'œuvre :");
        for (Labor labor : project.getLabors()) {
            double laborCost = labor.calculateCost();
            totalLaborCost += laborCost;

            System.out.printf("- %s : %.2f € (taux horaire : %.2f €/h, heures travaillées : %.2f h, productivité : %.2f)%n",
                    labor.getName(), laborCost, labor.getHourlyRate(), labor.getHoursWorked(),
                    labor.getWorkerProductivity());
        }
        double laborCostWithVAT = totalLaborCost * 1.2; // 20% VAT
        System.out.printf("**Coût total de la main-d'œuvre avant TVA : %.2f €**%n", totalLaborCost);
        System.out.printf("**Coût total de la main-d'œuvre avec TVA (20%%) : %.2f €**%n", laborCostWithVAT);

        double totalCostBeforeMargin = totalMaterialsCost + totalLaborCost;
        double margin = totalCostBeforeMargin * project.getProfitMargin(); // Using dynamic margin
        double finalTotalCost = totalCostBeforeMargin + margin;

        System.out.printf("3. Coût total avant marge : %.2f €%n", totalCostBeforeMargin);
        System.out.printf("4. Marge bénéficiaire (%.0f%%) : %.2f €%n", project.getProfitMargin() * 100, margin);
        System.out.printf("**Coût total final du projet : %.2f €**%n", finalTotalCost);
    }



    private static double getInputDouble(String prompt) {
        System.out.print(prompt);
        while (scanner.hasNextDouble())
            return scanner.nextDouble();
        return 0;
    }


    public static double calculateCostPlusTva(Component component,double tva) {
        return component.calculateCost() + component.calculateCost() * tva;
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