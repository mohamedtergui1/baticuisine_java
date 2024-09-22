package main.java.com.app.entities;

import main.myframework.annotation.CompositionType;
import main.myframework.enums.CascadeType;
import main.myframework.interfaces.GetId;

public abstract class Component implements GetId {
    protected int id;
    protected String name;
    protected double unitCost;
    protected double quantity;
    protected String componentType;
    protected double vatRate;
    protected double transportCost;
    protected double qualityCoefficient;
    @CompositionType(cascade = CascadeType.CASCADE)
    protected Project project;

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getUnitCost() { return unitCost; }
    public void setUnitCost(double unitCost) { this.unitCost = unitCost; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getComponentType() { return componentType; }
    public void setComponentType(String componentType) { this.componentType = componentType; }

    public double getVatRate() { return vatRate; }
    public void setVatRate(double vatRate) { this.vatRate = vatRate; }

    public double getTransportCost() { return transportCost; }
    public void setTransportCost(double transportCost) { this.transportCost = transportCost; }

    public double getQualityCoefficient() { return qualityCoefficient; }
    public void setQualityCoefficient(double qualityCoefficient) { this.qualityCoefficient = qualityCoefficient; }


    public abstract double calculateCost();
}

