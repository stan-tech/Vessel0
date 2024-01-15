package com.example.besoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vessel.example.vessel.DataBaseHelper;
import vessel.example.vessel.Dialogs.Stock_Fill_Dialogue;
import vessel.example.vessel.openMode;

public class Stock extends AppCompatActivity implements RecyclerItemClickListener{

    ImageButton bill , add , validate;
    RecyclerItemClickListener recyclerItemClickListener;
    boolean item_added;
    public static RecyclerView products_RV;
    public static TextView purchase_sum;
    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Product> Cart ;
    StockAdapter stockAdapter;
    DataBaseHelper dataBaseHelper;
    String OpenMode;
    FrameLayout cart;
    String dateString;
    Product selectedProduct;
    public List<Integer> ChangedItemsPositions;
    Date date;
    public static TextView cartTotalSum;
    ArrayList<Product> prods_selected;
    public static Activity finish;
    Calendar calendar;
    public static TextView cart_content_size;
    public static float selectedPrice,selectedQuantity;
    public static  String QuantityLeft;
    public static int CartContent;
    public static float PurchaseSum,AddedPurchaseSum;
    public String Bill_Type;
    public static boolean validated;
    public ArrayList<Product> AddedCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        finish = this;
        recyclerItemClickListener =this;
        ChangedItemsPositions = new ArrayList<>();
        Cart = new ArrayList<>();
        cart = findViewById(R.id.cart);
        bill = findViewById(R.id.cartButton);
        cart_content_size = findViewById(R.id.purchase_num);
        add = findViewById(R.id.Add);
        validate = findViewById(R.id.Validate);
        validate.setVisibility(View.GONE);
        validate.setEnabled(false);

            AddedCart = (ArrayList<Product>) getIntent().getSerializableExtra("Cart");
                AddedPurchaseSum = getIntent().getExtras().getFloat("cartSum");
        boolean buying = getIntent().getExtras().get("openMode") == openMode.buying;
        if (!buying){

            Bill_Type = "out";
        }else{

            Bill_Type = "in";
        }
        calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateString = sdf.format(calendar.getTime());
        try {
            date = sdf.parse(dateString);

        } catch (ParseException e) {

            e.printStackTrace();
        }

        dataBaseHelper = new DataBaseHelper(this);
        ShowProductData();
        stockAdapter = new StockAdapter(this,products,this,Bill_Type);
        products_RV = findViewById(R.id.products_RV);
        products_RV.setLayoutManager(new GridLayoutManager(this,2));

        try {
            item_added = (boolean) getIntent().getExtras().get("itemAdded");

            if(item_added && buying ){
                Cart.addAll(AddedCart);
                if(Cart.size() == 1) {

                    dataBaseHelper.AddBill(dateString, String.valueOf(AddedCart.get(AddedCart.size() - 1).getQuantity()
                                    * AddedCart.get(AddedCart.size() - 1).getBuying_price()), Bill_Type,
                            "0", String.valueOf(AddedCart.get(AddedCart.size() - 1).getSupplier().getId()));
                }
                products_RV.smoothScrollToPosition(stockAdapter.getItemCount()- 1);
            }
        } catch (Exception e) {
           // e.printStackTrace();
        }
        purchase_sum= findViewById(R.id.purchase_sum);
        purchase_sum.setText(AddedPurchaseSum+" DZD");
        cart_content_size.setText(String.valueOf(Cart.size()));

        PurchaseSum = Float.parseFloat(purchase_sum.getText().toString().replace(" DZD","").trim());

        products_RV.setAdapter(stockAdapter);
        ConnectivityManager cm = (ConnectivityManager)Stock.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();



        try {
            switch((openMode)getIntent().getExtras().get("openMode")){

                case Consulting :
                    add.setVisibility(View.VISIBLE);
                    add.setEnabled(true);

                    purchase_sum.setVisibility(View.GONE);
                    cart.setVisibility(View.GONE);
                    bill.setEnabled(false);

                    OpenMode = "Consulting";
                    break;
                case buying:
                    add.setVisibility(View.VISIBLE);
                    purchase_sum.setVisibility(View.VISIBLE);

                    add.setEnabled(true);
                    cart.setVisibility(View.VISIBLE);
                    bill.setEnabled(true);

                    OpenMode = "buying";

                    break;

                case selling :

                    add.setVisibility(View.GONE);
                    add.setEnabled(false);
                    purchase_sum.setVisibility(View.VISIBLE);

                    cart.setVisibility(View.VISIBLE);
                    bill.setEnabled(true);
                    OpenMode = "selling";

                    break;

            }
        } catch (Exception e) {
           // e.printStackTrace();
        }
        bill.setOnTouchListener(new View.OnTouchListener() {
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
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartContentView();
            }
        });
        add.setOnTouchListener(new View.OnTouchListener() {
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
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add_product = new Intent(Stock.this, Stock_Fill_Dialogue.class);
                add_product.putExtra("openMode",OpenMode);
                add_product.putExtra("Cart",Cart);
                add_product.putExtra("cartSum",Float.valueOf(purchase_sum.getText().toString().replace(" DZD","")));
                startActivity(add_product);
                finish();
            }
        });
    }

    public void CartContentView() {


        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.bill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        cartTotalSum = dialog.findViewById(R.id.total);

        Button cancel = dialog.findViewById(R.id.empty_cart),
                print = dialog.findViewById(R.id.print);

        CartAdapter cartAdapter = new CartAdapter(Stock.this,Cart,cart_content_size
                ,null,dialog,Bill_Type);
        RecyclerView cartRV = dialog.findViewById(R.id.cartRV);


        cartRV.setLayoutManager(new LinearLayoutManager(Stock.this));
        cartRV.setAdapter(cartAdapter);
        cartTotalSum.setText(purchase_sum.getText());

        selectedPrice =Float.parseFloat(cartTotalSum.getText().toString().replace(" DZD",""));

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                float Qleft=0
                        ,BillSum = 0 ;

                Stock.validated = true;
                String BillID = LatestBill();

                    for (int i = 0;i<Cart.size();i++) {

                        Cursor cursor = dataBaseHelper.SelectProductInfoWithID("quantity",Cart.get(i).getId());
                        cursor.moveToLast();
                        Stock.selectedQuantity = Float.parseFloat(cursor.getString(0));

                        BillSum = Cart.get(i).getBuying_price() * Cart.get(i).getQuantity();

                        if (Bill_Type == "out") {

                            Qleft = Stock.selectedQuantity - Cart.get(i).getQuantity();

                        } else{

                            Qleft = Stock.selectedQuantity + Cart.get(i).getQuantity();

                        }
                        dataBaseHelper.AddBillLine(BillID, String.valueOf(Cart.get(i).getId()),String.valueOf(Cart.get(i).getQuantity())
                                ,String.valueOf(BillSum),Cart.get(i).getName());

                        dataBaseHelper.UpdateBill(BillID);

                        dataBaseHelper.UpdateSoldProductData(String.valueOf(Qleft)
                                ,String.valueOf(Cart.get(i).getId()));


                    }





                PurchaseSum+= BillSum;
                cartTotalSum.setText(" "+PurchaseSum+" DZD");

                products.clear();
                ShowProductData();

                for (int i = 0 ; i<ChangedItemsPositions.size();i++){

                    stockAdapter.notifyItemChanged(ChangedItemsPositions.get(i));

                }
                CartContent = 0;
                Cart.clear();
                PurchaseSum = 0;
                purchase_sum.setText("0.00 DZD ");
                cart_content_size.setText(String.valueOf(Cart.size()));


            dialog.hide();

            }
        });

       cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stock.validated = false;

                dialog.hide();

            }
        });

        dialog.show();

      //  return Stock.validated;

    }

    public void ShowProductData(){

        Cursor cursor = dataBaseHelper.ShowProductData();
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                try {
                    Product product = new Product(
                            cursor.getString(1),
                            cursor.getString(2),
                            Float.parseFloat(cursor.getString(3)),
                            Float.parseFloat(cursor.getString(4)),
                            Float.parseFloat( cursor.getString(5)),
                            cursor.getString(6),
                            new Supplier(cursor.getString(7)));
                    try {
                        product.setId(Integer.parseInt(cursor.getString(0)));
                        product.setUnit_index(Integer.parseInt(cursor.getString(10)));
                        product.setType_index(Integer.parseInt(cursor.getString(11)));
                    } catch (NumberFormatException e) {
                        //
                    }

                    products.add(product);

                } catch (Exception e) {
                    Toast.makeText(this,"An error occurred try again",Toast.LENGTH_LONG);

                }
            }

        }else{
            Toast.makeText(this,R.string.aucun_produit,Toast.LENGTH_LONG);

        }
    }

    @Override
    public boolean onItemClick(int position) {

        if(OpenMode == null){

            OpenMode = "Consulting";

        }
         prods_selected = (ArrayList<Product>) products.clone();
        Product product = prods_selected.get(position);
        ChangedItemsPositions.add(position);
         QuantityLeft = String.valueOf(product.getQuantity());

        switch (OpenMode) {

            case "Consulting":
            Intent product_info = new Intent(Stock.this, Product_Info.class);
            product_info.putExtra("name", product.getName());
            product_info.putExtra("b_price", String.valueOf(product.getBuying_price()));
            product_info.putExtra("s_price",  String.valueOf(product.getSelling_price()));
            product_info.putExtra("quantity",  String.valueOf(product.getQuantity()));
            product_info.putExtra("supplier", product.getSupplier().getName());
            product_info.putExtra("unit", product.getUnit());
            product_info.putExtra("image", product.getImage());
            product_info.putExtra("type", product.getType());
            product_info.putExtra("id",String.valueOf(product.getId()));
            product_info.putExtra("unit_index",String.valueOf(product.getUnit_index()));
            product_info.putExtra("type_index",String.valueOf(product.getType_index()));
            product_info.putExtra("openMode", OpenMode);

            startActivity(product_info);
            finish();
            break;

            case "selling":
                Bill_Type = "out";
                validated = OpenDialog(position,1);
                break;

            case "buying":

                Bill_Type = "in";
                validated = OpenDialog(position,2);
                break;

        }

        return validated;
    }
    public boolean OpenDialog(int position,int type){


        selectedProduct =  prods_selected.get(position);

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         validated = false;
        EditText quantity;
        EditText priceET;
        TextView ProdName,ProdPrice,Total;

       Stock.CartContent = Integer.parseInt(cart_content_size.getText().toString().trim());
        dialog.setCancelable(true);

        if (type == 1 ){

            dialog.setContentView(R.layout.selling_options);

        }else{

            dialog.setContentView(R.layout.buying_options);

        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        quantity = dialog.findViewById(R.id.Quantity);
        quantity.setText("1");
        ProdName = dialog.findViewById(R.id.Product_name);
        ProdName.setText(selectedProduct.getName());
        Cursor cursor =  dataBaseHelper.SelectProductInfoWithID("quantity",selectedProduct.getId());
        Total = dialog.findViewById(R.id.total_price);


            priceET = dialog.findViewById(R.id.product_price_et);



            ProdPrice = dialog.findViewById(R.id.product_price_tv);


        if(cursor!= null && cursor.getCount()!=0){
            if(cursor.moveToLast()){

                Stock.QuantityLeft = cursor.getString(0);

            }
        }

        if (Bill_Type == "out") {
            ProdPrice.setText(selectedProduct.getSelling_price() +" DZD");
        } else {

            priceET.setText(" "+String.valueOf(selectedProduct.getBuying_price()) +" ");
            priceET.setEnabled(false);

        }
        if (type == 2) {

            Button ChangePrice = dialog.findViewById(R.id.ChangeP);

            ChangePrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    priceET.setEnabled(true);
                    priceET.requestFocus();
                    priceET.setSelection(priceET.getText().length()-1);
                }
            });
            priceET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {


                    if(!hasFocus){

                        priceET.setEnabled(false);
                        if(type == 2){
                            selectedPrice = Float.parseFloat(priceET.getText().toString());
                        }
                    }else{
                       InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);                }

                }
            });
        }

        Button yes = dialog.findViewById(R.id.Ok);
        Button no = dialog.findViewById(R.id.cancel);

        ImageButton plus = dialog.findViewById(R.id.plus2);
        ImageButton minus=dialog.findViewById(R.id.minus2);

        if (Bill_Type == "out") {

            selectedPrice =Float.parseFloat(ProdPrice.getText().toString().replace(" DZD",""));

        }else{

            selectedPrice = Float.parseFloat(priceET.getText().toString());
        }
       selectedQuantity = Float.parseFloat(quantity.getText().toString());

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                float BillSum  = 0f;
                Product addedProduct ;
                if(type == 2) {
                    selectedPrice = Float.parseFloat(priceET.getText().toString().replace(" DZD", ""));
                }

                BillSum += selectedPrice * selectedQuantity;

                Stock.validated = true;
                addedProduct  = new Product(selectedProduct.getName(),
                        selectedProduct.getUnit(),selectedProduct.getBuying_price(),
                        selectedProduct.getSelling_price(),selectedProduct.getQuantity(),
                        selectedProduct.getType(),selectedProduct.getSupplier());
                addedProduct.setId(selectedProduct.getId());

                if (Cart.size() == 0) {

                     Stock.QuantityLeft = String.valueOf(selectedProduct.getQuantity());


                    Cart.add(addedProduct);
                    Cart.get(Stock.CartContent).setQuantity(Float.parseFloat(quantity.getText().toString()));

                    dataBaseHelper.AddBill(dateString, String.valueOf(BillSum),Bill_Type,
                            "0",String.valueOf(addedProduct.getSupplier().getId()));


                }else{

                    if(!AddtoCart(Cart,addedProduct,Float.parseFloat(quantity.getText().toString()))){

                        Cart.get(Stock.CartContent).setQuantity(Float.parseFloat(quantity.getText().toString()));
                        Cart.get(Stock.CartContent).setBuying_price(selectedPrice);
                    }



                }

                dataBaseHelper.UpdateProductSellingPrice(String.valueOf(addedProduct.getId()),selectedPrice);
                Stock.CartContent++;
                cart_content_size.setText(String.valueOf(Cart.size()));
                PurchaseSum+=BillSum;
                purchase_sum.setText(" "+PurchaseSum+" DZD ");


                dialog.hide();

            }
        });

        plus.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Stock.selectedQuantity = Float.parseFloat(quantity.getText().toString());

                Stock.selectedQuantity++;

                if (type == 1) {

                    if (Stock.selectedQuantity <= Float.parseFloat(QuantityLeft)) {

                        float prc = selectedPrice * Stock.selectedQuantity;

                            ProdPrice.setText(prc+" DZD");

                        quantity.setText(String.valueOf(Stock.selectedQuantity));


                    }else{



                        Toast toast =  Toast.makeText( Stock.this, QuantityLeft+" "+getResources().getString(R.string.Available), Toast.LENGTH_SHORT);

                        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);

                        toast.show();

                    }
                }else{

                    float prc = Float.parseFloat(priceET.getText().toString()) * Stock.selectedQuantity;

                        Total.setText(String.valueOf(prc));

                    quantity.setText(String.valueOf(Stock.selectedQuantity));

                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            float prc = 0
                    ,qtt =Stock.selectedQuantity;
            @Override
            public void onClick(View v) {

                if (Stock.selectedQuantity >0) {
                    Stock.selectedQuantity = Stock.selectedQuantity - 1;
                }

                if(type == 1){
                    prc = Stock.selectedPrice *Stock.selectedQuantity;
                    ProdPrice.setText(prc + " DZD");

                }else{

                    prc = Float.parseFloat(priceET.getText().toString()) *Stock.selectedQuantity;

                    Total.setText(String.valueOf(prc));
                }
                quantity.setText(String.valueOf(Stock.selectedQuantity));
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stock.validated = false;

                dialog.hide();

            }
        });

        dialog.show();

        return Stock.validated;
    }

    public boolean AddtoCart(ArrayList<Product> cart , Product item,float quantity){
        Product cart_item;
        boolean match = false;
        for(int i = 0 ; i<cart.size();i++){
            cart_item = cart.get(i);
            if(item.getId() == cart.get(i).getId()){

                cart_item.setQuantity(quantity+cart_item.getQuantity());
                match = true;
                break;

            }else{
                match = false;
                continue;
            }
        }
        if(!match){

            cart.add(item);
        }

        return  match;

    }
    private String LatestBill() {

        Cursor cursor = dataBaseHelper.Last_Added_Bill();
        String id = "";
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                try {

                    id = cursor.getString(0);

                } catch (Exception e) {

                    Toast.makeText(this,"An error occurred try again",Toast.LENGTH_LONG);

                }
            }

        }else{

            Toast.makeText(this,"An error occurred try again",Toast.LENGTH_LONG);

        }
        return id;

    }

    @Override
    public void onItemLongClick(int position) {

    }
}