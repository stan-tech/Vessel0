package com.vessel.vesselapp.Models;

import com.example.besoin.Models.Supplier;

import java.io.Serializable;

public class Product implements Serializable{

    public int id;
    public String Type;
    public float Quantity;
    public float Buying_price;
    public float Selling_price;
    public Supplier supplier;
    public String unit;
    public String Name;
    public int unit_index,type_index;
    public String image;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Product(){

    }


    public Product(String type, float quantity,
                   float buying_price, float selling_price,
                   Supplier supplier,
                   String unit, String name, String image) {
        Type = type;
        Quantity = quantity;
        Buying_price = buying_price;
        Selling_price = selling_price;
        this.supplier = supplier;
        this.unit = unit;
        Name = name;
        this.image = image;
    }

    public int getType_index() {
        return type_index;
    }

    public void setType_index(int type_index) {
        this.type_index = type_index;
    }

    public int getUnit_index() {
        return unit_index;
    }

    public void setUnit_index(int unit_index) {
        this.unit_index = unit_index;
    }

    public Product(String Name , String unit,
                   float buying_price, float selling_price,
                   float quantity
                , String type, Supplier supplier) {
        Type = type;
        Quantity = quantity;
        Buying_price = buying_price;
        Selling_price = selling_price;
        this.supplier = supplier;
        this.unit = unit;
        this.Name = Name;
    }

    public Product(String Name, float b_price, float s_price){

        Buying_price = b_price;
        Selling_price = s_price;
        this.Name = Name;


    }
    public Product(int id){

       this.id = id;


    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public float getQuantity() {
        return Quantity;
    }

    public void setQuantity(float quantity) {
        Quantity = quantity;
    }

    public float getBuying_price() {
        return Buying_price;
    }

    public void setBuying_price(float buying_price) {
        Buying_price = buying_price;
    }

    public float getSelling_price() {
        return Selling_price;
    }

    public void setSelling_price(float selling_price) {
        Selling_price = selling_price;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }


}
