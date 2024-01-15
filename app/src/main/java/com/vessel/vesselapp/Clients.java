package com.example.besoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import vessel.example.vessel.DataBaseHelper;
import vessel.example.vessel.Dialogs.Client_Fill_Dialog;

public class Clients extends AppCompatActivity implements  Client_Fill_Dialog.ClientFillDialogListener,RecyclerItemClickListener
{
    Client_Fill_Dialog fillDialog;
    String address = "";
    String permission;
    ImageButton add_button;
    ClientAdapter clientAdapter;
    ArrayList<Client> clients;
    RecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    int position;
    View constraintLayout;
    LocationManager locationManager;
    LocationListener locationListener;

    int LOCATION_REFRESH_TIME = 1000;
    int LOCATION_REFRESH_DISTANCE = 100;
    private ViewGroup.LayoutParams layoutParams;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        add_button = findViewById(R.id.add_button);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
        }

       // locationManager1.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        //old onLocationChanged
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                try {
                    Location _location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Geocoder geocoder = new Geocoder(Clients.this, Locale.getDefault());

                    List<Address> addresses = geocoder.getFromLocation(_location.getLatitude(),_location.getLongitude(),1);
                    address = addresses.get(0).getAddressLine(0);

                } catch (IOException e) {
                    address = "no address";
                }

            }


        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                        LOCATION_REFRESH_DISTANCE, locationListener);
            } catch (Exception e) {
                Toast.makeText(this, "Could not get your location , please turn on location access", Toast.LENGTH_LONG).show();
            }
        }

        recyclerView = findViewById(R.id.clients_rev);
        dataBaseHelper = new DataBaseHelper(this);
        clients = new ArrayList<>();

        ShowClientData();

        clientAdapter = new ClientAdapter(clients,this,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(clientAdapter);

        add_button.setOnTouchListener(new View.OnTouchListener() {
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
    add_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OpenDialog();

        }
    });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            double latitude = 0f ,longitude=0f;
            try {
                Address address_ = new Geocoder(Clients.this).getFromLocationName(address,1).get(0);
                 latitude = address_.getLatitude()+0.018f;
                 longitude = address_.getLongitude()+0.018f;

            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+String.valueOf(latitude)+","+String.valueOf(longitude)+"&mode=d"));
                intent.setPackage("com.google.android.apps.maps");

            if(direction == ItemTouchHelper.LEFT){
                if(intent.resolveActivity(getPackageManager()) != null) {


                    int position = viewHolder.getAbsoluteAdapterPosition();
                    Client client = clients.get(position);

                    startActivity(intent);

                    recreate();
                }
            }else{
                return;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(Clients.this, R.color.app_color))
                    .addActionIcon(R.drawable.forward)
                    .create()
                    .decorate();
        }
    };


    private void OpenDialog() {

        fillDialog = new Client_Fill_Dialog(address,permission);
        fillDialog.show(getSupportFragmentManager(),"Fill");
    }
    public void revealExtra(ViewGroup.LayoutParams params, View layout){

        Animation expand = AnimationUtils.loadAnimation(this,R.anim.expand);


        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = layout.getWidth();

        layout.setAnimation(expand);

        layout.setLayoutParams(params);
        layout.setVisibility(View.VISIBLE);

    }

    @Override
    public void add_text(String name, String phone,String image, String address) {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);

            dataBaseHelper.addClient(name,phone,image,address);
            clientAdapter.InsertData(new ArrayList<>(Arrays.asList(new Client(name,phone,image,address))));
    }



    @Override
    public void update(String name, String phone, String address, String table_name, String column) {

    }

    @Override
    public boolean onItemClick(int position) {

        return true;
    }

    @Override
    public void onItemLongClick(int position) {


        this.position = position;
        Client client = clients.get(position);

        View layout = client.getLayout();
        constraintLayout = layout;

        ViewGroup.LayoutParams params = layout.getLayoutParams();
        layoutParams = params;

        client.setState(state.opened);
        revealExtra(params,layout);

        if(clientAdapter.isItem_deleted())
            this.recreate();

    }
    public void ShowClientData(){

        Cursor cursor = dataBaseHelper.ShowClientData();
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                Client client = null;
                try {
                    client = new Client(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4));
                } catch (Exception e) {

                }
                clients.add(client);

            }

        }
    }

}