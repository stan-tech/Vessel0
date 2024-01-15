package com.example.besoin.Models;

import android.view.View;

public class Travailleur {
    public String id ;
    public String phone ;
    public String name;
    public String salary;
    public String doc;
    public String Image;
    public View layout;
    public com.example.besoin.state state;
    public String advances;
    public String rest;

    public Travailleur(String ID) {
        id = ID;
    }


    public String getAdvances() {
        return advances;
    }

    public void setAdvances(String advances) {
        this.advances = advances;
    }

    public String getRest() {
        return rest;
    }

    public void setRest(String rest) {
        this.rest = rest;
    }

    public String getImage() {
        return this.Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }



    public com.example.besoin.state getState() {
        return state;
    }

    public void setState(com.example.besoin.state state) {
        this.state = state;
    }

    public View getLayout() {
        return layout;
    }

    public void setLayout(View layout) {
        this.layout = layout;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public Travailleur(String id,String name, String phone, String salary, String image, String doc,String advances) {
        this.phone = phone;
        this.name = name;
        this.salary = salary;
        this.Image = image;
        this.doc = doc;
        this.id = id;
        this.advances = advances;

    }
    public Travailleur(String name, String phone, String salary, String image, String doc) {
        this.phone = phone;
        this.name = name;
        this.salary = salary;
        this.Image = image;
        this.doc = doc;
        this.id = id;

    }
    public Travailleur(String id ,String name, String phone, String salary) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.salary = salary;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
