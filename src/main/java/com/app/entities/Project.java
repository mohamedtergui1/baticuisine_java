package main.java.com.app.entities;

import main.java.com.app.enums.Status;
import main.myframework.annotation.CompositionType;
import main.myframework.annotation.DefaultValueString;
import main.myframework.annotation.Nullable;
import main.myframework.enums.CascadeType;
import main.myframework.interfaces.GetId;

import java.util.List;

public class Project  implements GetId {

    private int id;
    private String projectName;
    private double profitMargin;
    @Nullable()
    private double totalCost;
    @DefaultValueString(value = "IN_PROGRESS")
    private Status projectStatus;
    private double surfaceArea;
    @CompositionType(cascade = CascadeType.CASCADE)
    private Client client;
    private List<Material> materials;
    private List<Labor> labors;

    public List<Labor> getLabors() {
        return labors;
    }

    public void setLabors(List<Labor> labors) {
        this.labors = labors;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public void setSurfaceArea(double surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public double getSurfaceArea() {
        return surfaceArea;
    }

    public List<Material> getMaterials() {
        return materials;
    }

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

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public double getProfitMargin() { return profitMargin; }
    public void setProfitMargin(double profitMargin) { this.profitMargin = profitMargin; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public Status getProjectStatus() { return projectStatus; }
    public void setProjectStatus(Status projectStatus) { this.projectStatus = projectStatus; }

    public String toString() {

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
    public void showAsMenu() {

        System.out.println("-------------");
        System.out.printf("ID: %d%n", id);
        System.out.printf("Project Name: %s%n", projectName != null ? projectName : "Not specified");
        System.out.printf("Profit Margin: %.2f%%%n", profitMargin);
        System.out.printf("Total Cost: %.2f%n", totalCost >= 0 ? totalCost : 0);
        System.out.printf("Project Status: %s%n", projectStatus != null ? projectStatus.name() : "Not specified");
        System.out.printf("Surface Area: %.2f%n", surfaceArea);

        if (client != null) {
            System.out.print("Client Information:");
            System.out.println(client.toString());
        } else {
            System.out.println("Client: Not assigned");
        }

        System.out.println("-------------");
    }

}
