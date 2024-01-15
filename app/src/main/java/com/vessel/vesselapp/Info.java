package com.example.besoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import vessel.example.vessel.DataBaseHelper;

public class Info extends AppCompatActivity implements RecyclerItemClickListener{

    String id ,Name,phone , salary , picture,document,advances,rest;
    CircleImageView pfp;
    ImageButton add_advances,refresh,see_docs,add_doc;
    Button add;
    TextView Advances_value,name,phonetv,salarytv,resttv;
    DataBaseHelper dataBaseHelper;
    Bundle extras ;
    ArrayList<Document> documents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         extras = getIntent().getExtras();
        setContentView(R.layout.infos_layout);
        Name = extras.getString("name");
        phone = extras.getString("phone");
        salary  =extras.getString("salary");
        picture= extras.getString("picture");
        document = extras.getString("document");
        advances = extras.getString("advances");
        rest = extras.getString("rest");
        id = extras.getString("id");
        documents = new ArrayList<>();
        see_docs = findViewById(R.id.Voir_doc);
        add_doc = findViewById(R.id.Add_doc);

        refresh = findViewById(R.id.refresh_button);

        dataBaseHelper = new DataBaseHelper(this);
        add_advances = findViewById(R.id.add_acompte);

        Advances_value = findViewById(R.id.acompte_value);
        if(advances == null) {
            Advances_value.setText("0.0");
        }else
        {Advances_value.setText(advances);
        }
        name = findViewById(R.id.name_TV);
        name.setText(Name);

        phonetv = findViewById(R.id.phone_TV);
        phonetv.setText(phone);

        salarytv = findViewById(R.id.SalaryTV);
        salarytv.setText(salary);

        resttv = findViewById(R.id.restTV);
        if (rest == null ) {
            resttv.setText(salary);
        } else {
            resttv.setText(rest);
        }

        pfp = findViewById(R.id.profile_pic);
        if(!picture.equals("")) {
            pfp.setImageURI(Uri.parse(picture));
        }
        Advances_value.setText(advances);

        StoreDocuments();

        add_advances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Opendialog();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenRefreshDialog();

            }
        });
        see_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDocs();
            }
        });
        add_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFiles();
            }
        });


    }

    private void showFiles() {

        Intent choose_file = new Intent(Intent.ACTION_GET_CONTENT);
        choose_file.setType("*/*");
        startActivityForResult(choose_file,1);

    }
    public void onActivityResult(int requestCode , int resultCode , Intent data ) {

        super.onActivityResult(requestCode, resultCode, data);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        if(requestCode == requestCode && resultCode == Activity.RESULT_OK){
            if(data==null){
            return;
            }
            Uri uri = data.getData();

            Context context = this;

            String doc_path = RealPathUtils.getRealPath(context,uri);

            dataBaseHelper.addDocument(doc_path,id);
            Toast.makeText(this,R.string.document_added,Toast.LENGTH_LONG);

        }
    }

    private void StoreDocuments() {
        Cursor cursor = dataBaseHelper.ShowDocuments(id);

        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                Document document = new Document(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2));
                try {
                    if(document.getPath().contains(".pdf") ||
                            document.getPath().contains(".word")||
                            document.getPath().contains(".docx")){
                        document.setType("document");
                    }else{
                        document.setType("image");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                documents.add(document);

            }

        }else{

            Toast.makeText(this,R.string.aucun_document,Toast.LENGTH_LONG);
        }

    }


    public void showDocs(){

        DocumentsAdapter documentsAdapter = new DocumentsAdapter(documents,this,this);
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_documents);

        RecyclerView recyclerView = dialog.findViewById(R.id.docs_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(documentsAdapter);

                dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bottom_sheet_shape);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Info.this,Travailleurs.class));
        this.finish();
    }
    public void OpenRefreshDialog(){
        Button confirm;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.confirm_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirm=dialog.findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean update = dataBaseHelper.UpdateWorkerData(Name,phone,salary,picture,document,"0.0",salary,"id",id);

                if (update) {
                    dialog.cancel();
                    Toast.makeText(Info.this,R.string.Travailleurs_Refreshé+id,Toast.LENGTH_SHORT).show();

                } else {
                    dialog.cancel();
                    Toast.makeText(Info.this,R.string.Travailleurs_Refreshé_negative+id,Toast.LENGTH_SHORT).show();

                }
                Advances_value.setText("0.0");
                resttv.setText(salary);
            }
        });

        dialog.show();

    }
    public void Opendialog(){


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.advances_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        add = dialog.findViewById(R.id.ajouter);
        EditText acomptes = dialog.findViewById(R.id.acomptes);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float added_value = Float.valueOf(acomptes.getText().toString());
                float salary_value = Float.valueOf(salary);
                float advances_result = Float.valueOf(Advances_value.getText().toString().trim()) + added_value;
                float rest_result = salary_value - advances_result;
                boolean update ;
                String rest = String.valueOf(rest_result);

                Advances_value.setText(String.valueOf(advances_result));

                resttv.setText(rest);
                update = dataBaseHelper.UpdateWorkerData(Name,phone,salary,picture,document,String.valueOf(advances_result),rest,"id",id);

                if (update) {
                    Toast.makeText(Info.this,R.string.Acompte_added+id,Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Info.this,R.string.Acompte_not_added+id,Toast.LENGTH_SHORT).show();

                }

                dialog.cancel();


            }
        });
        dialog.show();

    }

    @Override
    public boolean onItemClick(int position) {

        Document document = documents.get(position);
        Intent openwith = new Intent(Intent.ACTION_SEND);
        if (document != null) {
            if(document.getPath().contains(".pdf")){
                openwith.setType("application/pdf");
            }else{
                openwith.setType("image/jpeg");
            }
        }

        openwith.putExtra(Intent.EXTRA_STREAM,Uri.parse(document.getPath()));

        startActivity(openwith);

        return true;

    }

    @Override
    public void onItemLongClick(int position) {

    }
}