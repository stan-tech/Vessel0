package com.vessel.vesselapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.besoin.Adapters.SupplierAdapter;
import com.example.besoin.RecyclerItemClickListener;
import com.example.besoin.state;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import vessel.example.vessel.Dialogs.SupplierFillDialog;

public class Fournisseurs extends AppCompatActivity implements RecyclerItemClickListener, SupplierFillDialog.SupplierFillDialogListener {

    ArrayList<Supplier> suppliers;
    RecyclerView recyclerView;
    ImageButton add;
    SupplierFillDialog fillDialog;
    SupplierAdapter supplierAdapter;
    String address = "";
    String permission;
    DataBaseHelper dataBaseHelper;
    int position;
    View constraintLayout;
    LocationManager locationManager;
    LocationListener locationListener;
    public static Activity finish;
    int LOCATION_REFRESH_TIME = 1000;
    int LOCATION_REFRESH_DISTANCE = 100;
    private ViewGroup.LayoutParams layoutParams;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fournisseurs);
        finish = this;
        suppliers = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(this);
        recyclerView = findViewById(R.id.fournisseurs_rev);
        add = findViewById(R.id.add_button);
        ShowSupplierData();
        supplierAdapter = new SupplierAdapter(suppliers,this,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(supplierAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=
                        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                do{
                    try {
                        Location _location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(_location.getLatitude(),_location.getLongitude(),1);
                        address = addresses.get(0).getAddressLine(0);

                    } catch (IOException e) {

                        address = "no address";
                        continue;
                    }
                } while (address == "no address");


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
                Address  address_ = new Geocoder(Fournisseurs.this).getFromLocationName(address,1).get(0);

                latitude = address_.getLatitude()+0.018f;
                longitude = address_.getLongitude()+0.018f;


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+address_.getLatitude()+","+address_.getLongitude()+"&destination="+latitude+","+longitude+"&waypoints="+(latitude+0.028f)+
                    ","+(longitude+0.028f)+"|"+(latitude+0.039f)+","+(longitude+0.039f)+"|"+(latitude+0.0127f)+","+(longitude+0.0127f)+"|"+(latitude+0.0197f)+","+(longitude+0.0197f)+"&travelmode=driving"));
            intent.setPackage("com.google.android.apps.maps");


            if(direction == ItemTouchHelper.LEFT){
                if(intent.resolveActivity(getPackageManager()) != null) {


                    int position = viewHolder.getAbsoluteAdapterPosition();
                    Supplier supplier = suppliers.get(position);

                    startActivity(intent);

                    recreate();
                }
            }else{
                return;
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(Fournisseurs.this, R.color.app_color))
                    .addActionIcon(R.drawable.forward)
                    .create()
                    .decorate();
        }
    };

    private void OpenDialog() {

        fillDialog = new SupplierFillDialog(address,permission);
        fillDialog.show(getSupportFragmentManager(),"Fill");
    }
    public void revealExtra(ViewGroup.LayoutParams params, View layout){

        Animation expand = AnimationUtils.loadAnimation(this, R.anim.expand);


        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = layout.getWidth();

        layout.setAnimation(expand);

        layout.setLayoutParams(params);
        layout.setVisibility(View.VISIBLE);

    }

    public void ShowSupplierData(){


        Cursor cursor = dataBaseHelper.ShowSupplierData();
        if(cursor!=null){

            while(cursor.moveToNext()){

                Supplier supplier = new Supplier(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(5),
                        cursor.getString(6));
                supplier.setId(cursor.getInt(0));
                suppliers.add(supplier);


            }
        }



    }

    @Override
    public boolean onItemClick(int position) {

        Supplier supplier = suppliers.get(position);
        Open_popUp(supplier.getId(),supplier.getName(),supplier.getTelephone(),
               supplier.getCompanyName(), SelectSupplierProductName(supplier.getId()));
        return true;

    }

    private ArrayList<Product> SelectSupplierProductName(int id) {

        Cursor cursor = dataBaseHelper.SelectSupplierProductWithID(id);
        ArrayList<Product> products = new ArrayList<>();
        Product product ;

        if(cursor !=null){

            while(cursor.moveToNext()){

                product = new Product(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getFloat(3),
                        cursor.getFloat(4),
                        cursor.getFloat(5),
                        cursor.getString(6),null);

                product.setId(cursor.getInt(0));

                products.add(product);


            }

        }

        return products;
    }

    public void Open_popUp(int id , String name,String phone,String CompanyName,ArrayList<Product> products){

        TextView name_tv,phone_tv,CompanyNameTv,productsTv,AddressTv;
        StringBuilder stringBuilder = new StringBuilder();

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.supplier_info_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        products = SelectSupplierProductName(id);

        for(Product p : products){

            stringBuilder.append(p.getName());
            stringBuilder.append("\n,");
        }
        stringBuilder.toString().replace(",","").trim();
        name_tv = dialog.findViewById(R.id.nameTV);
        phone_tv=dialog.findViewById(R.id.phone_number);
        CompanyNameTv = dialog.findViewById(R.id.Company_name);

        name_tv.setText(name);
        phone_tv.setText(phone);
        if (CompanyName.length() > 1) {
            CompanyNameTv.setText(CompanyName);
        }else {

            CompanyNameTv.setText(R.string.not_available);

        }
        dialog.show();


    }


    @Override
    public void onItemLongClick(int position) {

        this.position = position;
        Supplier supp = suppliers.get(position);

        View layout = supp.getLayout();
        constraintLayout = layout;

        ViewGroup.LayoutParams params = layout.getLayoutParams();
        layoutParams = params;

        supp.setState(state.opened);
        revealExtra(params,layout);


    }


    @Override
    public void add_supplier(String name, String phone, String company_name, String image, String address) {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            dataBaseHelper.addSupplier(name,company_name,phone,image,address);
            supplierAdapter.InsertData(new ArrayList<>(Arrays.asList(new Supplier(name,company_name,phone,image,address))));

    }

    @Override
    public void update(String name, String phone, String address, String table_name, String column) {

    }
}