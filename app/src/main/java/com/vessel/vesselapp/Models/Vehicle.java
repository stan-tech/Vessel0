package com.example.besoin.Models;

import android.location.Location;

import java.util.ArrayList;

public class Vehicle {
    public String ID ;
    public Travailleur driver;
    public Location location;
    public ArrayList<Product> stock;
    public ArrayList<Bill> transactions;

    public Vehicle() {

    }

    public Vehicle(String ID, Travailleur driver, Location location) {
        this.ID = ID;
        this.driver = driver;
        this.location = location;
    }

    public Vehicle(String ID, Travailleur driver, Location location,
                   ArrayList<Product> stock, ArrayList<Bill> transactions) {
        this.ID = ID;
        this.driver = driver;
        this.location = location;
        this.stock = stock;
        this.transactions = transactions;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Travailleur getDriver() {
        return driver;
    }

    public void setDriver(Travailleur driver) {
        this.driver = driver;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<Product> getStock() {
        return stock;
    }

    public void setStock(ArrayList<Product> stock) {
        this.stock = stock;
    }

    public ArrayList<Bill> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Bill> transactions) {
        this.transactions = transactions;
    }
}
