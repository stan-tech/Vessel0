package com.vessel.vesselapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.besoin.RecyclerItemClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class Factures extends AppCompatActivity implements RecyclerItemClickListener {

    BottomNavigationView bottomNavigationView;
    ArrayList<Bill> bills;
    DataBaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factures);

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new Ventes()).commit();

        bottomNavigationView =  findViewById(R.id.nav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected  = null;
                switch(item.getItemId()){

                    case R.id.Ventes:

                        selected = new Ventes();
                        break;
                    case R.id.Achats:
                        selected = new Achats();
                        break;


                }


                getSupportFragmentManager().beginTransaction().setCustomAnimations(
                        R.anim.frag_slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.frag_slide_out  // popExit
                ).replace(R.id.frag_container,selected).addToBackStack(null).commit();


                return true;
            }
        });


    }

    @Override
    public boolean onItemClick(int position) {

   return false;
    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Factures.this,MainActivity.class);
        startActivity(intent);
        finish();

    }
}