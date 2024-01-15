package com.example.besoin.Models;

import android.view.View;

import java.io.Serializable;

public class Supplier  implements Serializable {
    public String Name;
    public String CompanyName;
    public String Telephone;
    public String image;
    public int id;
    public View layout;
    public String  address;
    public com.example.besoin.state state;


    public Supplier(String name,String CompanyName ,String telephone) {
        Name = name;
        Telephone = telephone;
        this.CompanyName = CompanyName;
    }

    public Supplier(String name, String companyName,
                    String telephone, String image, String address) {
        Name = name;
        CompanyName = companyName;
        Telephone = telephone;
        this.image = image;
        this.id = id;
        this.layout = layout;
        this.address = address;
    }
    public Supplier(int id){
        this.id = id;
    }

    public Supplier(String name){
        this.Name = name;
    }

    public Supplier(int id , String name){
        this.id = id;
        this.Name = name;

    }
    public com.example.besoin.state getState() {
        return state;
    }

    public void setState(com.example.besoin.state state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public View getLayout() {
        return layout;
    }

    public void setLayout(View layout) {
        this.layout = layout;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

}
