package main.java.com.app.entities;

import main.myframework.annotation.CompositionType;
import main.myframework.enums.CascadeType;
import main.myframework.interfaces.GetId;

public abstract class Component implements GetId {
    protected int id;
    protected String name;
    protected double vatRate;
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

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }


    public double getVatRate() { return vatRate; }

    public void setVatRate(double vatRate) { this.vatRate = vatRate; }

    public abstract double calculateCost();
}

