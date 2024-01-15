package com.example.besoin.Models;

public class Manager {

    public String Firstname;

    public Manager(String firstname, String lastname, String telephone, String shop_name) {
        Firstname = firstname;
        Lastname = lastname;
        Telephone = telephone;
        Shop_name = shop_name;
    }

    public String Lastname;
    public String Telephone;
    public String Shop_name;

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getShop_name() {
        return Shop_name;
    }

    public void setShop_name(String shop_name) {
        Shop_name = shop_name;
    }
}
