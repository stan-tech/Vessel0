package com.vessel.vesselapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vessel.vesselapp.Models;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.besoin.Adapters.BillsAdapter;
import com.vessel.vesselapp.FactureInfo;
import com.example.besoin.RecyclerItemClickListener;
import com.vessel.vesselapp.Models.Bill;
import com.vessel.vesselapp.Models.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Achats extends Fragment implements RecyclerItemClickListener {

    RecyclerView Brv;
    ArrayList<Bill> bills;
    DataBaseHelper db;

    public Achats(){


}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflator = TransitionInflater.from(requireContext());
        setExitTransition(inflator.inflateTransition(R.transition.fade));
        setEnterTransition(inflator.inflateTransition(R.transition.slide_right));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_achats, container, false);


        Brv = view.findViewById(R.id.achats_recv);
        bills = new ArrayList<>();

        db= new DataBaseHelper(this.getContext());
        ShowOutBills();
        BillsAdapter billsAdapter = new BillsAdapter(this.getContext(),bills,this);
        Brv.setAdapter(billsAdapter);
        Brv.setLayoutManager(new LinearLayoutManager(this.getContext()));


        return  view;
    }

    private void ShowOutBills()  {
        Cursor cursor = db.ShowInBillsData();
        Product product;
        Supplier supplier;
        Travailleur worker;
        Client client;
        java.util.Date date = null;
        Bill bill;
        if(cursor!=null){

            while(cursor.moveToNext()){
                supplier = new Supplier(cursor.getInt(5));
                client = new Client(cursor.getInt(6));
                worker = new Travailleur(cursor.getString(1));
                try {
                    date = new SimpleDateFormat("dd-MM-yy").
                            parse(cursor.getString(2).substring(0,10));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                bill = new Bill(cursor.getInt(0),
                        worker,date,cursor.getFloat(3),supplier,
                        client,cursor.getString(4)
                );

                bills.add(bill);

            }
        }
    }

    @Override
    public boolean onItemClick(int position) {
        Bill bill = bills.get(position);

        Intent info = new Intent(getContext(), FactureInfo.class);
        info.putExtra("id",bill.getId());
        info.putExtra("date",bill.getDate());
        info.putExtra("sum",bill.getSum());
        info.putExtra("bill_lines",bill.getBillLines());
        info.putExtra("billType",bill.getType());

        startActivity(info);
        return true;
    }

    @Override
    public void onItemLongClick(int position) {

    }
}