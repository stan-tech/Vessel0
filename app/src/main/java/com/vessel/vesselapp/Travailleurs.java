package com.example.besoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

import vessel.example.vessel.DataBaseHelper;
import vessel.example.vessel.Dialogs.FillDialog;

public class Travailleurs extends AppCompatActivity implements RecyclerItemClickListener,
        FillDialog.FillDialogListener{

    ImageButton addButton;

    RecyclerView recyclerView;
    ArrayList<Travailleur> travailleurs;
    Trav_Adapter trav_adapter;
    FillDialog fillDialog;
    DataBaseHelper dataBaseHelper;
    Cursor cursor;
    int position=0;
    ViewGroup.LayoutParams layoutParams;
    View constraintLayout;
    public static Activity finish ;

    public Trav_Adapter getAdapter(){
        return this.trav_adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travailleurs);
        finish = this;
        fillDialog = new FillDialog();
        addButton = findViewById(R.id.add_button);
        dataBaseHelper = new DataBaseHelper(this);
        travailleurs = new ArrayList<Travailleur>();
        recyclerView = findViewById(R.id.trav_rev);
        StoreWorkerData();
        trav_adapter = new Trav_Adapter(this,travailleurs,this);
        recyclerView.setAdapter(trav_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageButton view = (ImageButton) v;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                         view = (ImageButton) v;
                        view.getBackground().setColorFilter(Color.parseColor("#99BAF1"), PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        v.performClick();
                        break;
                    }

                }

                return true;

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();

            }
        });


    }

    @Override
    public boolean onItemClick(int position) {

        Travailleur trav = travailleurs.get(position);

        if(trav.getState() == state.opened){

            close(layoutParams,constraintLayout);
            trav.setState(state.closed);

            if(trav_adapter.isItem_deleted())
                this.recreate();

        }else{
         Intent intent= new Intent(Travailleurs.this,Info.class);
            intent.putExtra("name",trav.getName());
            intent.putExtra("phone",trav.getPhone());
            intent.putExtra("salary",trav.getSalary());
            intent.putExtra("picture",trav.getImage());
            intent.putExtra("document",trav.getDoc());
            intent.putExtra("advances",trav.getAdvances());
            intent.putExtra("rest",trav.getRest());
            intent.putExtra("id",trav.getId());
            startActivity(intent);
            this.finish();
        }
        return true;

    }



    @Override
    public void onItemLongClick(int position) {


        this.position = position;
       Travailleur trav = travailleurs.get(position);

        View layout = trav.getLayout();
        constraintLayout = layout;

        ViewGroup.LayoutParams params = layout.getLayoutParams();
        layoutParams = params;

        trav.setState(state.opened);
        revealExtra(params,layout);

        if(trav_adapter.isItem_deleted())
            this.recreate();

    }
    public void revealExtra(ViewGroup.LayoutParams params, View layout){

        Animation expand = AnimationUtils.loadAnimation(this,R.anim.expand);


        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = layout.getWidth();

        layout.setAnimation(expand);

        layout.setLayoutParams(params);
        layout.setVisibility(View.VISIBLE);

    }
    public void close(ViewGroup.LayoutParams params, View layout){
       Animation shrink = AnimationUtils.loadAnimation(this,R.anim.shrink);
        params.width = layout.getWidth();
        params.height = 0;

        layout.setAnimation(shrink);

        layout.setLayoutParams(params);
        layout.setVisibility(View.INVISIBLE);

    }

    private void OpenDialog() {
        fillDialog = new FillDialog();
        fillDialog.show(getSupportFragmentManager(),"Fill");
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT){


        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


    @Override
    public void add_text(String name, String phone, String salary,String image,String advances,String rest) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(Travailleurs.this);
        cursor = dataBaseHelper.SelectLastWorkerID();


        if(image == null){
            image = "";
        }
        dataBaseHelper.addWorker(name.toString().trim(),
                    phone.toString().trim(),
                    salary.toString().trim(),
                    image.toString().trim(),
                   "0.0",
                    salary.trim());

            trav_adapter.InsertData(new ArrayList<Travailleur>(Arrays.asList(new Travailleur(name.toString().trim(),
                    phone.toString().trim(),
                    salary.toString().trim(),
                    image.toString().trim(),
                    ""))));


    }


    @Override
    public void update(String name, String phone, String salary, String image, String document, String table_name, String column) {

    }

    @Override
    public void add_document(String path, String Worker_id) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(Travailleurs.this);
        Cursor cursor  =dataBaseHelper.Last_Added_id();
        String id = "";
        if(cursor.getCount() >= 1) {
            while (cursor.moveToNext()) {
                id = cursor.getString(0);
            }
        }
        dataBaseHelper.addDocument(path,id);
    }

    @Override
    public void onAttach(Context context) {

    }


    public void StoreWorkerData(){

        Cursor cursor = dataBaseHelper.ShowWorkerData();

        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
        Travailleur travailleur = new Travailleur(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
                travailleur.setRest(cursor.getString(7));
                travailleurs.add(travailleur);

            }

        }else{

            Toast.makeText(this,R.string.aucun_employe,Toast.LENGTH_LONG);
        }

    }

}