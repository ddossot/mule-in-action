package com.muleinaction.common;

import java.io.Serializable;

public class Fare implements Serializable {

    private String airline;
    private String type;
    private Double price;

    public Fare() {
    }

    public Fare(String airline, String type, Double price) {
        this.airline = airline;
        this.type = type;
        this.price = price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getAirline() {
        return airline;
    }

    public String toString() {
        return "AIRLINE: " + airline + "\nTYPE: " + type + "\nPRICE: " + price + "\n\n";
    }
}


