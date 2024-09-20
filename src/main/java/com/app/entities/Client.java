package main.java.com.app.entities;

import main.java.com.app.interfaces.GetId;
import main.myframework.annotation.StringValidation;

public class Client implements GetId {

    private int id;
    @StringValidation(minLength = 4,maxLength = 30)
    private String name;
    @StringValidation(minLength = 10,maxLength = 255)
    private String address;
    @StringValidation(minLength = 10,maxLength = 13)
    private String phoneNumber;
    private boolean isProfessional;

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

}

