package com.example.besoin;

public class Type {
    public String type_name;
    public String Category;

    public Type(String type_name, String category) {
        this.type_name = type_name;
        Category = category;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
