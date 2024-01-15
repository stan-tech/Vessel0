package com.vessel.vesselapp.Models;

import java.time.LocalDateTime;

public class Bill_line {

    public int ID;
    public LocalDateTime Date;
    public Product product;
    public String Name;
    public float quantity;
    public float sum;

    public Bill_line(String ProductName, Product product, float quantity, float sum) {
        this.product = product;
        this.quantity = quantity;
        this.sum = sum;
        this.Name = ProductName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDate() {
        return Date;
    }

    public void setDate(LocalDateTime date) {
        Date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }
}
