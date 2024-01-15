package com.example.besoin.Models;

import android.view.View;

public class Client {
    public String name ;
    public String phone ;
    public String address;
    public String image;
    public View layout;
    public com.example.besoin.state state;
    public int id;


    public Client(String name) {
        this.name = name;
    }

    public Client(int id) {

        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Client(int id , String name, String phone, String image, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.image= image;
        this.id= id;
    }
    public Client(String name, String phone, String image, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.image= image;
        this.id= id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
