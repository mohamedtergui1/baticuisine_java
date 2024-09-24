package main.java.com.app.entities;

import main.myframework.annotation.DefaultValueBoolean;
import main.myframework.annotation.Nullable;
import main.myframework.annotation.StringMinLength;
import main.myframework.interfaces.GetId;
import main.myframework.annotation.StringMaxLength;

import java.util.List;

public class Client implements GetId {

    private int id;
    @StringMaxLength(maxLength = 30)
    @Nullable(false)
    @StringMinLength(6)
    private String name;
    @StringMaxLength(maxLength = 255)
    @Nullable(false)
    @StringMinLength(6)
    private String address;
    @Nullable(false)
    @StringMaxLength(maxLength = 13)
    @StringMinLength(9)
    private String phoneNumber;
    @DefaultValueBoolean(false)
    private boolean isProfessional;
    private List<Project> projects;

    public List<Project> getProjects() {
         projects.stream().forEach((e) -> {
                e.setClient(this);
         });
         return projects;
    }

    public void setProjects(List<Project> projects) {

        this.projects = projects;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public boolean isProfessional() { return isProfessional; }
    public void setProfessional(boolean isProfessional) { this.isProfessional = isProfessional; }
    @Override
    public String toString() {
        return String.format(
                "{\"id\": %d, \"name\": \"%s\", \"address\": \"%s\", \"phoneNumber\": \"%s\", \"isProfessional\": %b}",
                id,
                name != null ? name : "",
                address != null ? address : "",
                phoneNumber != null ? phoneNumber : "",
                isProfessional
        );
    }
    public void showAsMenu() {

        System.out.println("------------");
        System.out.printf("ID: %d%n", id);
        System.out.printf("Name: %s%n", name != null ? name : "Not specified");
        System.out.printf("Address: %s%n", address != null ? address : "Not specified");
        System.out.printf("Phone Number: %s%n", phoneNumber != null ? phoneNumber : "Not specified");
        System.out.printf("Professional: %s%n", isProfessional ? "Yes" : "No");
        System.out.println("------------");
    }


}

