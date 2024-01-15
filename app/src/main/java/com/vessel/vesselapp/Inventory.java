package com.vessel.vesselapp;

public class Inventory {

    public Product type;
    public float Somme;
    public float Profit;

    public Inventory(Product type, float somme, float profit) {
        this.type = type;
        Somme = somme;
        Profit = profit;
    }

    public Product getType() {
        return type;
    }

    public void setType(Product type) {
        this.type = type;
    }

    public float getSomme() {
        return Somme;
    }

    public void setSomme(float somme) {
        Somme = somme;
    }

    public float getProfit() {
        return Profit;
    }

    public void setProfit(float profit) {
        Profit = profit;
    }
}
