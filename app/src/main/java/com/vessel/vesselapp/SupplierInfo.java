package com.example.besoin;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import vessel.example.vessel.DataBaseHelper;
import vessel.example.vessel.Fournisseurs;
import vessel.example.vessel.LocationActivity;

public class SupplierInfo extends AppCompatActivity implements LocationListener {

    String name, phone, picture, CompanyName, address;
    int id;
    LocationManager locationManager;
    TextView nameET, phoneET, CompanyNameET, AddressTv;
    CircleImageView pictureCIV;
    ImageButton changeLocation, ShowProducts, apply;
    LocationListener locationListener;
    int LOCATION_REFRESH_TIME = 1000;
    int LOCATION_REFRESH_DISTANCE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_info);
        name = getIntent().getExtras().getString("name");
        phone = getIntent().getExtras().getString("phone");
        id = getIntent().getExtras().getInt("id");
        CompanyName = getIntent().getExtras().getString("CompanyName");
        picture = getIntent().getExtras().getString("picture");
        address = getIntent().getExtras().getString("address");

        nameET = findViewById(R.id.Name_ET);
        phoneET = findViewById(R.id.Phone_ET);
        CompanyNameET = findViewById(R.id.CompanyET);
        AddressTv = findViewById(R.id.addressTv);
        changeLocation = findViewById(R.id.change_location);
        apply = findViewById(R.id.apply);
        ShowProducts = findViewById(R.id.products);
        pictureCIV = findViewById(R.id.Profile_pic);
        nameET.setText(name);
        phoneET.setText(phone);
        CompanyNameET.setText(CompanyName);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                try {
                    if (ActivityCompat.checkSelfPermission(SupplierInfo.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(SupplierInfo.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(SupplierInfo.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);

                        try {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                                    LOCATION_REFRESH_DISTANCE, locationListener);
                        } catch (Exception e) {
                            Toast.makeText(SupplierInfo.this, "Could not get your location , please turn on location access", Toast.LENGTH_LONG).show();
                        }

                        Location _location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        Geocoder geocoder = new Geocoder(SupplierInfo.this, Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(_location.getLatitude(),_location.getLongitude(),1);
                        address = addresses.get(0).getAddressLine(0);
                    }


                } catch (IOException e) {
                    address = "no address";
                }

            }


        };
        AddressTv.setText(address);

     /*   if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {



        }*/
        changeLocation.setOnTouchListener(new View.OnTouchListener() {
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
        ShowProducts.setOnTouchListener(new View.OnTouchListener() {
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
        apply.setOnTouchListener(new View.OnTouchListener() {
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
        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLocationWindow();

            }
        });

        ShowProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseHelper dataBaseHelper = new DataBaseHelper(SupplierInfo.this);


                if (dataBaseHelper.UpdateSupplierData(nameET.getText().toString(),
                        CompanyNameET.getText().toString(),phoneET.getText().toString(), picture, address, String.valueOf(id))) {

                    Toast.makeText(SupplierInfo.this, R.string.mise_a_jour_supplier, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SupplierInfo.this, R.string.non_mise_a_jour_supplier, Toast.LENGTH_LONG).show();
                }

                startActivity(new Intent(SupplierInfo.this, Fournisseurs.class));
                finish();


            }
        });

        try {
            // pictureCIV.setImageURI(Uri.parse(picture));

        } catch (Exception e) {
            // e.printStackTrace();
        }


    }

    public void showLocationWindow() {

        ImageButton from_map, current;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choose_location_selection_way);
        dialog.show();
        from_map = dialog.findViewById(R.id.from_map_button);
        current = dialog.findViewById(R.id.current_loc);

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
        from_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SupplierInfo.this, LocationActivity.class));
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bottom_sheet_shape);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(SupplierInfo.this, Fournisseurs.class));
        finish();
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            AddressTv.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }
}