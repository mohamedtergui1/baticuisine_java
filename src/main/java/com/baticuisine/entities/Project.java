package main.java.com.baticuisine.entities;

import main.java.com.baticuisine.enums.Status;
import main.java.com.baticuisine.interfaces.GetId;

public class Project  implements GetId {

    private int id;
    private String projectName;
    private double profitMargin;
    private double totalCost;
    private Status projectStatus;
    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and Setters
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public double getProfitMargin() { return profitMargin; }
    public void setProfitMargin(double profitMargin) { this.profitMargin = profitMargin; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public Status getProjectStatus() { return projectStatus; }
    public void setProjectStatus(Status projectStatus) { this.projectStatus = projectStatus; }
}
