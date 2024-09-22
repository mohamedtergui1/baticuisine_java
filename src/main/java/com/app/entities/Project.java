package main.java.com.app.entities;

import main.java.com.app.enums.Status;
import main.myframework.annotation.CompositionType;
import main.myframework.annotation.DefaultValueString;
import main.myframework.enums.CascadeType;
import main.myframework.interfaces.GetId;

public class Project  implements GetId {

    private int id;
    private String projectName;
    private double profitMargin;
    private double totalCost;
    @DefaultValueString(value = "IN_PROGRESS")
    private Status projectStatus;
    @CompositionType(cascade = CascadeType.CASCADE)
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

    public String toString() {
        // Convert client object to JSON string if it's not null
        String clientJson = (client != null) ? client.toString() : "null";

        return String.format(
                "{\"id\": %d, \"projectName\": \"%s\", \"profitMargin\": %.2f, \"totalCost\": %.2f, \"projectStatus\": \"%s\", \"client\": %s}",
                id,
                projectName != null ? projectName : "",
                profitMargin,
                totalCost,
                projectStatus != null ? projectStatus.name() : "",
                clientJson
        );
    }
}
