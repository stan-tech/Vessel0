package com.vessel.vesselapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vessel.vesselapp.Adapters.BillLinesAdapter;
import com.example.besoin.Adapters.BillsAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FactureInfo extends AppCompatActivity {

    TextView date,sum;
    Button delete,print;
    Dialog dialog;
boolean deleted;
    AutoCompleteTextView sort_by;
    EditText search;
    RecyclerView bill_lines_rv;
    BillLinesAdapter billLinesAdapter;
    int id;
    float sumF;
    static String sortBySelection;
    String billType;
    Date dateD;
    DataBaseHelper dataBaseHelper;
    List<String> sortByElements;
    ArrayList<Bill_line> billLines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facture_info);

        date = findViewById(R.id.date);
        sum= findViewById(R.id.BillSum);
        delete = findViewById(R.id.delete_bill);
        print = findViewById(R.id.print_bill);
        sort_by = findViewById(R.id.sort_by_filter);
        search= findViewById(R.id.search);
        bill_lines_rv = findViewById(R.id.bill_line_rv);
        id = getIntent().getExtras().getInt("id");
        dateD = (Date) getIntent().getExtras().get("date");
        dialog = new Dialog(this);

        sumF = getIntent().getExtras().getFloat("sum");
        sortByElements = Arrays.asList("  "+getResources().getString(R.string.Name)
                ,"  "+getResources().getString(R.string.Quantity),
                "  "+getResources().getString(R.string.Price));
        ArrayAdapter<String> sortByadapter = new ArrayAdapter<>(this,
                R.layout.small_spinner_item,sortByElements);
        sort_by.setAdapter(sortByadapter);

        date.setText(new SimpleDateFormat("dd-MM-yyyy").format(dateD).replace("-","/"));
        sum.setText(sumF+" DZD");

        billLines = BillsAdapter.ShowBillLines(id);
        billType = getIntent().getExtras().getString("billType");
        billLinesAdapter  = new BillLinesAdapter(this,billLines,null);
        bill_lines_rv.setLayoutManager(new LinearLayoutManager(this));
        bill_lines_rv.setAdapter(billLinesAdapter);

        Comparator<Bill_line> Quantitycomparator = new Comparator<Bill_line>() {
            @Override
            public int compare(Bill_line num1, Bill_line num2) {

                return Float.compare(num2.getQuantity(),num1.getQuantity());
            }
        };
        Comparator<Bill_line> Pricecomparator = new Comparator<Bill_line>() {
            @Override
            public int compare(Bill_line num1, Bill_line num2) {


                    return Float.compare(num2.getSum()/num2.getQuantity(),num1.getSum()/num1.getQuantity());

            }
        };
        Comparator<Bill_line> StringComparator = new Comparator<Bill_line>() {
            @Override
            public int compare(Bill_line o1, Bill_line o2) {

                return o1.getName().compareToIgnoreCase(o2.getName());

            }
            };

        sort_by.setDropDownBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_search));
        sort_by.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sortBySelection = sortByElements.get(position);

                switch(sortBySelection){

                    case "  Name":
                        Collections.sort(billLines,StringComparator);
                        break;
                    case "  Quantity":
                        Collections.sort(billLines,Quantitycomparator);
                    break;
                    case "  Price":
                        Collections.sort(billLines,Pricecomparator);
                        break;


                }

            billLinesAdapter.notifyDataSetChanged();

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

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    Filter(s.toString());
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDeletedialog();
            }
        });

    }
    public void OpenDeletedialog(){


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.delete_prompt);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button yes = dialog.findViewById(R.id.Yesbtt);
        Button no = dialog.findViewById(R.id.Nobtt);
        dataBaseHelper = new DataBaseHelper(this);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor = dataBaseHelper.ShowWorkerData();

                if (dataBaseHelper.DeleteRow("Bill",
                        "Bill_id",
                        String.valueOf(id))) {
                    deleted = true;

                    Toast.makeText(FactureInfo.this,R.string.bill_deleted,Toast.LENGTH_LONG).show();

                }else{
                    deleted = false;

                    Toast.makeText(FactureInfo.this,R.string.bill_not_deleted,Toast.LENGTH_LONG).show();

                }
                dialog.dismiss();

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleted = false;

                dialog.hide();
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


    }

    public void Filter(String s){

        ArrayList<Bill_line> billLines = new ArrayList<>();

        if (!s.isEmpty()) {
            for(Bill_line billLine : this.billLines){
                if(billLine.getName().toLowerCase().contains(s)){

                    billLines.add(billLine);
                }
            }
            billLinesAdapter.filterList(billLines);
        }else{
            billLinesAdapter.filterList(this.billLines);
            return;
        }

    }
    @Override
    public void onBackPressed(){

        Intent stock = new Intent(FactureInfo.this, Factures.class);
        startActivity(stock);
        finish();


    }
}