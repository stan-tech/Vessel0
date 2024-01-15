package com.vessel.vesselapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Trav_info extends AppCompatActivity {

    ImageButton add_advance, show_docs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infos_layout);
        add_advance = findViewById(R.id.add_acompte);

        show_docs = findViewById(R.id.Voir_doc);
        show_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Trav_info.this,Documents.class));
            }
        });

        add_advance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
            }
        });

    }

    public float OpenDialog() {



        return 0.0f;
    }
}