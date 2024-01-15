package com.example.besoin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import vessel.example.vessel.DataBaseHelper;
import vessel.example.vessel.openMode;

public class Product_Info extends AppCompatActivity  {

    String name,supplier,unit,image,type,selection;
    String b_price,s_price,quantity;
    EditText name_et,b_price_et,s_price_et,quantity_et,supplier_et;
    AutoCompleteTextView unitSpinner,typeSpinner;
    CircleImageView Image;
    ImageButton delete , edit, validate , add_pic;
    String id,unit_index,type_index,selected_type,selected_unit;
    String dateString,BillID;
    Dialog dialog;
    Product product;
    Date date;
    float BillSum;
    Calendar calendar;
    openMode mode;
    DataBaseHelper dbHelper;
    boolean deleted = false;
    public static Activity finish ;
    ActivityResultLauncher<Intent> CameraActivityResultLauncher,
            FileChooserActivityLauncher;
    private Uri capturedImageURI;
    List<String> units = Arrays.asList("Unit","Kilogram", "Liter" , "Meter");
    ArrayList<String> types = new ArrayList<>();
    public String BillType;

    public String getImage_path() {
        return this.image;
    }

    public void setImage_path(String image) {
        this.image= image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        dbHelper = new DataBaseHelper(this);
        ShowTypesData();

        name_et = findViewById(R.id.Name_ET);
        supplier_et = findViewById(R.id.Supplier);
        quantity_et = findViewById(R.id.Quantity);
        s_price_et = findViewById(R.id.S_Price_ET);
        b_price_et = findViewById(R.id.B_Price_ET);
        unitSpinner = findViewById(R.id.units_filter);
        typeSpinner = findViewById(R.id.typesFilter);
        Image = findViewById(R.id.prod_pic);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);
        finish = this;
        ArrayAdapter unit_adapter = new ArrayAdapter<>(this,R.layout.spinner_item
                , units);
        ArrayAdapter types_adapter = new ArrayAdapter<>(this,R.layout.spinner_item
                , types);

        calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
         dateString = sdf.format(calendar.getTime());
        try {
            date = sdf.parse(dateString);

        } catch (ParseException e) {

            e.printStackTrace();
        }


        unitSpinner.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_storage));
        unitSpinner.setAdapter(unit_adapter);
        typeSpinner.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_storage));
        typeSpinner.setAdapter(types_adapter);

        unitSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_unit = units.get(position);
                unit_index = String.valueOf(position);
            }
        });

        typeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_type = types.get(position);
                type_index = String.valueOf(position);

            }
        });

        name = getIntent().getExtras().getString("name");
       b_price= getIntent().getExtras().getString("b_price");
        s_price =  getIntent().getExtras().getString("s_price");
        quantity =  getIntent().getExtras().getString("quantity");
        supplier = getIntent().getExtras().getString("supplier");
        unit = getIntent().getExtras().getString("unit");
        image = getIntent().getExtras().getString("image");
        type = getIntent().getExtras().getString("type");
        id = getIntent().getExtras().getString("id");
        unit_index = getIntent().getExtras().getString("unit_index");
        type_index = getIntent().getExtras().getString("type_index");
        String Omode =  getIntent().getExtras().getString("openMode");

        switch(Omode){
            case "Consulting":
                mode = openMode.Consulting;
                break;
            case"buying":
                mode = openMode.buying;

                break;
            case "selling":
                mode = openMode.selling;
                break;
        }
        add_pic = findViewById(R.id.add_pic);
        validate = findViewById(R.id.Validate);

        name_et.setText(name);
        b_price_et.setText(b_price);
        s_price_et.setText(s_price);
        quantity_et.setText(quantity);
        supplier_et.setText(supplier);
        selected_type = types.get(Integer.parseInt(type_index));
        selected_unit = units.get(Integer.parseInt(unit_index));


        unitSpinner.setText(units.get(Integer.parseInt(unit_index)));
        unit_adapter.getFilter().filter(null);

        typeSpinner.setText(types.get(Integer.parseInt(type_index)));
        types_adapter.getFilter().filter(null);

        findViewById(R.id.Unit).setEnabled(false);
        findViewById(R.id.spinner).setEnabled(false);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDeletedialog();
            }
        });



         validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Float.valueOf(String.valueOf(quantity_et.getText())).equals(
                        Float.valueOf(quantity))){
                    OpenDialog();
                }else{
                        Update();
                }

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_et.setEnabled(true);
                b_price_et.setEnabled(true);
                s_price_et.setEnabled(true);
                quantity_et.setEnabled(true);
                supplier_et.setEnabled(true);
                findViewById(R.id.Unit).setEnabled(true);
                findViewById(R.id.spinner).setEnabled(true);
                name_et.findFocus();
            }
        });

        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        CameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        String[] projection = { MediaStore.Images.Media.DATA};
                        Cursor cursor = managedQuery(capturedImageURI, projection, null, null, null);
                        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        capturedImageURI = Uri.parse(cursor.getString(column_index_data));
                        image = RealPathUtils.getRealPath(Product_Info.this , capturedImageURI);
                        add_pic.setImageResource(R.drawable.rechoose);
                    }
                });
        FileChooserActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){

                            if(result.getData() == null){
                                return;
                            }

                            Uri uri = result.getData().getData();
                            Context context = Product_Info.this;

                            image = RealPathUtils.getRealPath(context,uri);


                        }
                    }
                });



    }

    public void OpenDialog(){


        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_bill_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button yes = dialog.findViewById(R.id.Yesbtt);
        Button no = dialog.findViewById(R.id.Nobtt);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Float.parseFloat(String.valueOf(quantity_et.getText())) >
                        Float.parseFloat(quantity)){
                    BillType= "out";
                }else{

                    BillType = "in";
                }
                float new_quantity = Math.abs(Float.parseFloat(String.valueOf(quantity_et.getText())) -
                        Float.parseFloat(quantity));

                Supplier supp = new Supplier(supplier_et.getText().toString());
                Client client = new Client("Uknown");

                product = new Product(name ,  unit,
                        Float.parseFloat(b_price_et.getText().toString())
                                ,Float.parseFloat(s_price_et.getText().toString()),
                        Float.parseFloat(quantity), type, supp);
                product.setId(Integer.parseInt(id));
                BillSum = new_quantity * Float.parseFloat(b_price_et.getText().toString());

                Bill_line bill_line = new Bill_line(product.getName(),product,new_quantity,BillSum);

                 Bill bill = new Bill(date,BillSum,
                         supp, client,BillType);


                Update();
                dbHelper.AddBill(dateString, String.valueOf(BillSum),BillType,
                        String.valueOf(supp.getId()),String.valueOf(client.getId()));
                BillID = LatestBill();
                dbHelper.AddBillLine(BillID,String.valueOf(product.getId()),quantity_et.getText().toString(),String.valueOf(BillSum),product.getName());
                dialog.hide();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Update();
                dialog.hide();

            }
        });

        dialog.show();

    }

    private String LatestBill() {


        return "";
    }

    public void Update(){

        if (dbHelper.UpdateProductData(name_et.getText().toString(),selected_type
                ,quantity_et.getText().toString(),selected_unit,image,b_price_et.getText().toString(),s_price_et.getText().toString()                       ,supplier_et.getText().toString(),unit_index,type_index,id)){
            Toast.makeText(Product_Info.this, R.string.product_updated, Toast.LENGTH_SHORT).show();
        }else{

            Toast.makeText(Product_Info.this, R.string.product_not_updated, Toast.LENGTH_SHORT).show();

        }
    }
    public void showDialog() {
        ImageButton choose,take_pic;

         dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        take_pic = dialog.findViewById(R.id.picture);
        choose = dialog.findViewById(R.id.folders);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selection = "picture";
                showFiles("image/*");

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
                capturedImageURI = Product_Info.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageURI);
                startActivityForResult(takePicture, 0);


            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (deleted) {

                    onBackPressed();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bottom_sheet_shape);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public void OpenDeletedialog(){


        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.delete_prompt);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button yes = dialog.findViewById(R.id.Yesbtt);
        Button no = dialog.findViewById(R.id.Nobtt);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor = dbHelper.ShowWorkerData();

                if (dbHelper.DeleteRow("product",
                        "id",
                        id)) {
                    deleted = true;
                    Toast.makeText(Product_Info.this,R.string.product_deleted,Toast.LENGTH_LONG).show();

                }else{
                    deleted = false;

                    Toast.makeText(Product_Info.this,R.string.product_not_deleted,Toast.LENGTH_LONG).show();

                }
                dialog.hide();

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleted = false;
                dialog.hide();
            }
        });

        dialog.show();


    }


    public void showFiles(String type){

        int requestCode = 0;
        Intent choose_file = new Intent(Intent.ACTION_GET_CONTENT);
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageURI);
        choose_file.setType(type);

        if(selection != "picture"){

            String filename = "temp.jpg";

            ContentValues cv= new ContentValues();
            cv.put(MediaStore.Images.Media.TITLE,filename);


            cv.put(MediaStore.Images.Media.TITLE,filename);
            capturedImageURI = Product_Info.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);

            CameraActivityResultLauncher.launch(camera);

        }else{
            FileChooserActivityLauncher.launch(choose_file);
        }

    }

    public void ShowTypesData() {

        Cursor cursor = dbHelper.ShowTypesData();
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                Type type = null;
                try {
                    type = new Type(
                            cursor.getString(1),
                            cursor.getString(2));
                } catch (Exception e) {

                }
                types.add(type.getCategory());

            }

        }
        if( types.isEmpty()){
            types.add(new Type("null","No type added yet").getCategory());
        }

    }

    @Override
    public void onBackPressed(){

        Intent stock = new Intent(Product_Info.this,Stock.class);
        stock.putExtra("openMode",mode);
        startActivity(stock);
       finish();


    }

}