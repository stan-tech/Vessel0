package com.vessel.vesselapp.Models;

import android.view.View;

import java.util.ArrayList;
import java.util.Date;

public class Bill {
    public Date date;
    public float sum;
    public com.example.besoin.Models.Travailleur worker;
    public com.example.besoin.Models.Supplier supplier;
    public com.example.besoin.Models.Client client;
    public String type;
    public ArrayList<Bill_line> billLines;
    public int ID;
    public View layout;



    public Travailleur getWorker() {
        return worker;
    }

    public void setWorker(Travailleur worker) {
        this.worker = worker;
    }

    public ArrayList<Bill_line> getBillLines() {
        return billLines;
    }

    public void setBillLines(ArrayList<Bill_line> billLines) {
        this.billLines = billLines;
    }

    public View getLayout() {
        return layout;
    }

    public void setLayout(View layout) {
        this.layout = layout;
    }

    public Bill(int id , Travailleur worker, Date date, float sum,
                Supplier supplier, Client client, String type) {
        this.date = date;
        this.sum = sum;
        this.supplier = supplier;
        this.client = client;
        this.type = type;
        ID = id;
        this.worker = worker;
    }

    public Bill(Date date, float billSum, Supplier supp, Client client, String billType) {

        this.date = date;
        this.sum = sum;
        this.supplier = supplier;
        this.client = client;
        this.type = type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public void setID(int id){

        ID=id;
    }
    public int getId() {

        return ID;
    }
}
