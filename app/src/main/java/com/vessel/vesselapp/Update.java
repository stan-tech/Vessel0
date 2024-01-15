package com.example.besoin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import vessel.example.vessel.DataBaseHelper;

public class Update extends AppCompatActivity{

    ImageButton upload_doc,apply;
    CircleImageView upload_pic;
    TextView Name,phone,salary;
    String picture;
    String selection;
    String id;
    Uri capturedImageURI;
    String document_path;
    String advances;
    Bundle extras ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);
        Name = findViewById(R.id.Name_ET);
        phone = findViewById(R.id.Phone_ET);
        salary = findViewById(R.id.Salary);
        upload_pic = findViewById(R.id.Profile_pic);
        apply = findViewById(R.id.apply);
        upload_doc = findViewById(R.id.add_doc);

        upload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        upload_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFiles("*/*");
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DataBaseHelper dataBaseHelper = new DataBaseHelper(Update.this);


                if (dataBaseHelper.UpdateWorkerData( Name.getText().toString(),  phone.getText().toString(),
                        salary.getText().toString(),  picture,  document_path,extras.getString("advances"),extras.getString("rest"),  "id", id)) {

                    Toast.makeText(Update.this,R.string.mise_a_jour,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Update.this,R.string.non_mise_à_jour,Toast.LENGTH_LONG).show();
                }

                startActivity(new Intent(Update.this,Travailleurs.class));
                    finish();

            }
        });


        if (savedInstanceState == null){
             extras = getIntent().getExtras();
            if(extras!=null){
                Name.setText(extras.getString("name"));
                phone.setText(extras.getString("phone"));
                salary.setText(extras.getString("salary"));
                picture= extras.getString("picture");
                id = extras.getString("id");

                if(!picture.equals("")){
                upload_pic.setImageURI(Uri.parse(picture));
                }

                document_path = extras.getString("document");


            }else{
                Toast.makeText(this,"no information",Toast.LENGTH_SHORT).show();
            }

        }


    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(Update.this,Travailleurs.class));
        finish();
    }
    public void showDialog() {
        ImageButton choose,take_pic;

        final Dialog dialog = new Dialog(Update.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        take_pic = dialog.findViewById(R.id.picture);
        choose = dialog.findViewById(R.id.folders);

        choose.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                selection = "picture";
                showFiles("image/jpeg");

            }
        });
        take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selection = "capture";
                String filename = "temp.jpg";

                ContentValues cv= new ContentValues();
                cv.put(MediaStore.Images.Media.TITLE,filename);


                cv.put(MediaStore.Images.Media.TITLE,filename);
                capturedImageURI = Update.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageURI);
                startActivityForResult(takePicture, 0);


            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bottom_sheet_shape);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    public void showFiles(String type){

        int requestCode = 0;

        if(selection != "picture"){

            requestCode = 1;
        }

        Intent choose_file = new Intent(Intent.ACTION_GET_CONTENT);
        choose_file.setType(type);
        startActivityForResult(choose_file,requestCode);



    }
    public void onActivityResult(int requestCode , int resultCode , Intent data ){

        super.onActivityResult(requestCode,resultCode,data);

        if (selection == "capture") {

            String[] projection = { MediaStore.Images.Media.DATA};
            Cursor cursor = this.managedQuery(capturedImageURI, projection, null, null, null);
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            upload_pic.setImageURI(Uri.parse(cursor.getString(column_index_data)));

            return;
        }


        if(requestCode == requestCode && resultCode == Activity.RESULT_OK){

            if(data == null){
                return;
            }

            Uri uri = data.getData();
            Context context = Update.this;

            String picturePath = RealPathUtils.getRealPath(context,uri);


            if( selection == "picture"){

                picture = picturePath;
                upload_pic.setImageURI(Uri.parse(picturePath));

            }else{
                document_path = picturePath;
            }


        }

    }








}