package main.java.com.app.entities;

import main.myframework.annotation.CompositionType;
import main.myframework.enums.CascadeType;
import main.myframework.interfaces.GetId;

import java.time.LocalDate;

public class Estimate implements GetId {
    private int id;
    private double estimatedAmount;
    private LocalDate issueDate;
    private LocalDate validityDate;
    private boolean isAccepted;
    @CompositionType(cascade = CascadeType.CASCADE)
    private Project project;

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

    public double getEstimatedAmount() { return estimatedAmount; }
    public void setEstimatedAmount(double estimatedAmount) { this.estimatedAmount = estimatedAmount; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getValidityDate() { return validityDate; }
    public void setValidityDate(LocalDate validityDate) { this.validityDate = validityDate; }

    public boolean isAccepted() { return isAccepted; }
    public void setAccepted(boolean isAccepted) { this.isAccepted = isAccepted; }
}
