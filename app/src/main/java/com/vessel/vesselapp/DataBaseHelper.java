package com.vessel.vesselapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.besoin.Product_Info;
import com.example.besoin.Stock;

public class DataBaseHelper extends SQLiteOpenHelper {

    //region Fields
    private Context context;
    public static final String DATABASE_NAME = "Vessel.db";
    public static final int DATABASE_VERSION = 1;



    public static final String MANAGER = "manager";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SHOP_NAME = "shop_name";
    public static final String COLUMN_SHOP_TYPE = "shop_type";

    public static final String WORKER = "worker";
    public static final String WORKER_ID = "id";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String SALARY = "salary";
    public static final String IMAGE = "image";
    public static final String DOC = "document";
    public static final String ADVANCES = "advances";
    public static final String REST = "rest";

    public static final String CLIENT = "client";
    public static final String CLIENT_ID = "id";
    public static final String CLIENT_NAME = "name";
    public static final String CLIENT_IMAGE = "image";
    public static final String CLIENT_PHONE = "phone";
    public static final String ADDRESS = "address";

    public static final String INCREMENT = "UPDATE sequence_helper SET "+CLIENT_ID+" = seq+1 WHERE name = 'client_id';" +
            " END;";

    public static final String DOCUMENT="Document";
    public static final String DOCUMENT_ID="doc_id";
    public static final String PATH = "path";
    public static final String WORKER_ID_FK = "worker_id";

    public static final String BILL="Bill";
    public static final String Bill_ID = "Bill_id";
    public static final String SUPPLIER = "Supplier";
    public static final String CLIENTS="clients";
    public static final String VEHICLES = "vehicles";
    public static final String BILL_TYPE = "BillType";

    public static final String Bill_line="Bill_line";
    public static final String Bill_line_id="Bill_line_id";
    public static final String PRODUCT="product";
    public static final String SUM="sum";


    public static final String VEHICLE_ID =  "Vehicle_id";
    public static final String Driver_id = "driver";
    public static final String Longitude = "Longitude";
    public static final String Latitude = "Latitude";

    public static final String TYPE="Type";
    public static final String TYPE_NAME = "name";
    public static final String CATEGORY = "category";

    public static final String SUPPLIER_NAME="name";
    public static final String SUPP_CMP_NAME="company_name";
    public static final String SUPPLIER_ID ="id";


    public boolean successful_product_update;

    //endregion



    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String manager_query = "create table "+ MANAGER +"("+COLUMN_ID+
                " integer primary key autoincrement ," +COLUMN_NAME+
                " varchar(50), "+COLUMN_SHOP_NAME+" varchar(50),"+COLUMN_SHOP_TYPE+" varchar(50)); ";

        String client_query = "create table "+ CLIENT +"("+CLIENT_ID+
                " integer primary key autoincrement ," +CLIENT_NAME+
                " varchar(50), "+CLIENT_PHONE+" varchar(50) ,"+CLIENT_IMAGE+" varchar(50),"+ADDRESS+" varchar(50)); ";

        String worker_query = "create table "+ WORKER +"("+ WORKER_ID +
                " integer primary key autoincrement ," +NAME+
                " varchar(50), "+PHONE+" interger,"+SALARY+" money , "+IMAGE+" text , "+DOC+" integer, "+ADVANCES+" float , "+REST+" float,foreign key("+DOC+") references "+DOCUMENT+"(DOCUMENT_ID) ); ";

        String bill_query="create table "+BILL+"( "+Bill_ID+
                " integer primary key autoincrement, wroker_id integer, date DATETIME , sum decimal,"+BILL_TYPE+
                " varchar(10) ,  supplier_id integer , client_id integer, "
                +"foreign key(supplier_id) references "+SUPPLIER+"(id), " +
                "foreign key (client_id) references "+CLIENT+" ("+CLIENT_ID+")," +
                " foreign key (wroker_id) references "+WORKER+" ("+WORKER_ID+"));";


    /*    String sequenceHelper =
                " CREATE TABLE sequence_helper (" +
                "     name TEXT PRIMARY KEY COLLATE NOCASE default 'worker_id'," +
                "     seq INTEGER DEFAULT 1" +
                " );";*/

/*
        String trigger = " CREATE TRIGGER autoincr_bill_worker_id AFTER INSERT ON "+BILL+" FOR EACH ROW BEGIN" +
                "     UPDATE "+BILL+" SET " +
                "         "+WORKER_ID+" = (SELECT seq FROM sequence_helper WHERE name = 'worker_id')" +
                "where "+WORKER_ID+" = max("+WORKER_ID+");END;";
*/

        String bill_line_query = "create table "+Bill_line+"(id integer primary key autoincrement ," +
                "date datetime, id_product integer , quantity decimal , sum  decimal ,bill_id integer" +
                ",product_name varchar(100) ,foreign key (id_product) references product(id), foreign key (bill_id) references "+BILL+"(id));";


        String Document_query = "create table "+DOCUMENT+"("+DOCUMENT_ID+" integer primary key autoincrement ," +
                PATH+" text , "+ WORKER_ID_FK +" integer , foreign key ("+ WORKER_ID_FK +") references "+WORKER+"("+ WORKER_ID +") );";

        String Clients_query = "create table "+CLIENTS+"(id integer primary key autoincrement ," +
                              "date datetime, id_product integer , quantity integer , sum  decimal , foreign key (id_product) references product(id));";

        String Vehicles_query="create table "+VEHICLES+"(id integer primary key autoincrement ," +
                Driver_id+" integer, id_product integer , quantity integer , sum  decimal , foreign key (id_product) references product(id));";

        String Type_query = "create table "+TYPE+"(id integer primary key autoincrement,"
                +TYPE_NAME+" varchar(40) , "+CATEGORY+" varchar(40));";

        String Products_table = "create table product (id integer primary key autoincrement  ," +
                "name varchar(50) , unit varchar(20) , buying_p decimal , selling_p decimal, quantity decimal ," +
                "type integer , Supplier  varchar(100) , image varchar(40),supplier_id integer, unit_index integer,type_index integer," +
                " foreign key(type) references "+TYPE+"(id) , " +
                "foreign key(supplier_id) references "+SUPPLIER+"(id))";

        String Supplier_table = "create table "+SUPPLIER+" ("+SUPPLIER_ID+" integer primary key autoincrement," +
                " "+SUPPLIER_NAME+" varchar(50) , "+SUPP_CMP_NAME+" varchar(50), " +
                " "+PHONE+" varchar(60)  ,product_id integer, image varchar(100), address varchar(100) , foreign key ("+"" +
                "product_id) references product (id))";

        db.execSQL(Type_query);
        db.execSQL(manager_query);
        db.execSQL(worker_query);
        db.execSQL(bill_query);
        db.execSQL(bill_line_query);
        db.execSQL(Clients_query);
        db.execSQL(Vehicles_query);
        db.execSQL(Document_query);
        db.execSQL(client_query);
        db.execSQL(Products_table);
        db.execSQL(Supplier_table);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+ MANAGER +";");
        db.execSQL("drop table if exists "+ WORKER +";");
        db.execSQL("drop table if exists "+BILL+";");
        db.execSQL("drop table if exists "+ CLIENTS +";");
        db.execSQL("drop table if exists "+ Bill_line +";");
        db.execSQL("drop table if exists "+ VEHICLES +";");
        db.execSQL("drop table if exists "+ DOCUMENT +";");
        db.execSQL("drop table if exists "+ CLIENT +";");
        db.execSQL("drop table if exists  product ;");
        db.execSQL("drop table if exists "+TYPE+";");
        db.execSQL("drop table if exists  "+SUPPLIER+" ;");


        onCreate(db);
    }

    public void addWorker(String name , String phone, String salary, String image,String advances,String rest){

        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv  = new ContentValues();

        cv.put(NAME,name);
        cv.put(PHONE,phone);
        cv.put(SALARY,salary);
        cv.put(IMAGE,image);
        cv.put(ADVANCES,advances);
        cv.put(REST,rest);

        long result = db.insert(WORKER,null,cv);
        if(result == -1){
            Toast.makeText(this.context, R.string.ajouter_negative,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this.context,R.string.ajouter_positive,Toast.LENGTH_LONG).show();
        }


    }

    public void addClient(String name , String phone, String image,String address){

        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv  = new ContentValues();

        cv.put(CLIENT_NAME,name);
        cv.put(CLIENT_PHONE,phone);
        cv.put(CLIENT_IMAGE,image);
        cv.put(ADDRESS,address);

        long result = db.insert(CLIENT,null,cv);
        if(result == -1){
            Toast.makeText(this.context,R.string.client_ajouter_negative,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this.context,R.string.client_ajouter_positive,Toast.LENGTH_LONG).show();
        }


    }
    public boolean addProduct(String name , String type, String quantity
            ,String unit,String image,String b_price
            ,String s_price,String supplier,String unit_idx,String type_idx,int supplier_id){

        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv  = new ContentValues();

        cv.put("name",name);
        cv.put("type",type);
        cv.put("quantity",quantity);
        cv.put("unit",unit);
        cv.put("image",image);
        cv.put("buying_p",b_price);
        cv.put("selling_p",s_price);
        cv.put("supplier_id",supplier_id);
        cv.put(SUPPLIER,supplier);
        cv.put("unit_index",unit_idx);
        cv.put("type_index",type_idx);


        long result = db.insert("product",null,cv);
        if(result == -1){
            Toast.makeText(context,R.string.product_not_added,Toast.LENGTH_LONG).show();
            return false;
        }else{
            Toast.makeText(context,R.string.product_added,Toast.LENGTH_LONG).show();
            return true;
        }


    }
    public Cursor selectSupplierNames(){

        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select name , id from supplier";

        cursor = db.rawQuery(query,null);

        return cursor;

    }
    public boolean UpdateSupplierData(String name , String CompanyName, String phone
            ,String image,String Address,String value){

        ContentValues cv  = new ContentValues();


        SQLiteDatabase db = this.getWritableDatabase();

        cv.put("name",name);
        cv.put("Company_name",CompanyName);
        cv.put("phone",phone);
        cv.put("image",image);
        cv.put("address",Address);



        return db.update("supplier",cv,"id = "+value,null) > 0;
    }
    public void addSupplier(String name , String CompanyName, String phone
            ,String image,String Address){

        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv  = new ContentValues();

        cv.put(SUPPLIER_NAME,name);
        cv.put(PHONE,phone);
        cv.put(SUPP_CMP_NAME,CompanyName);
        cv.put("image",image);
        cv.put("address",Address);

        long result = db.insert(SUPPLIER,null,cv);

        if(result == -1){
            Toast.makeText(this.context,R.string.fourniss_ajouter_negative,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this.context,R.string.fourniss_ajouter_positive,Toast.LENGTH_LONG).show();
        }


    }

    public boolean AddBillLine(String BillID, String product_id,String quantity,String BillSum,String ProductName){

        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv  = new ContentValues();

        cv.put("bill_id",BillID);
        cv.put("id_product",product_id);
        cv.put("sum",BillSum);
        cv.put("quantity",quantity);
        cv.put("product_name",ProductName);
        long result = db.insert(Bill_line,null,cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }


    }
    public void AddBill(String date_time , String sum, String type,String client_id,String supplier_id){

        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv  = new ContentValues();

        cv.put("date",date_time);
        cv.put("sum",sum);
        cv.put(BILL_TYPE,type);
        cv.put("client_id",client_id);
        cv.put("supplier_id",supplier_id);

        long result = db.insert(BILL,null,cv);
        if(result == -1){
            Toast.makeText(this.context,R.string.BillNotAdded,Toast.LENGTH_LONG).show();
        }else{
        }


    }

    public  boolean UpdateClientData(String name , String type, String quantity
            ,String unit,String image,String b_price
            ,String s_price,String supplier,String value ){

        ContentValues cv  = new ContentValues();


        SQLiteDatabase db = this.getWritableDatabase();

        cv.put("name",name);
        cv.put("type",type);
        cv.put("quantity",quantity);
        cv.put("unit",unit);
        cv.put("image",image);
        cv.put("buying_p",b_price);
        cv.put("selling_p",s_price);
        cv.put(SUPPLIER,supplier);


        return db.update("porduct",cv,"id = "+value,null) > 0;
    }
    public Cursor SelectLastWorkerID(){
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select max(id) from worker";

        cursor = db.rawQuery(query,null);

        return cursor;
    }
    public int SelectLastSupplierID(){
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select max(id) from supplier";
            int id = 0;
        cursor = db.rawQuery(query,null);

        if(cursor != null){
            if (cursor.moveToNext()){

                id = cursor.getInt(0);
            }
        }
        return id;
    }

    public void addDocument(String path , String Worker_id){

        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv  = new ContentValues();

        cv.put(PATH,path);
        cv.put(WORKER_ID_FK,Worker_id);

        db.insert(DOCUMENT,null,cv);



    }
    public boolean DeleteRow(String table_name,String column,String row_value){

        SQLiteDatabase db = this.getWritableDatabase();
        boolean deleted = db.delete(table_name,column+" = "+row_value,null) > 0;

        if(table_name == "product" ){
            Intent back = new Intent(context, Stock.class);
            back.putExtra("openMode", openMode.Consulting);

            ActivityCompat.startActivity(context,back,null);
            Product_Info.finish.finish();
        }
         return deleted;

    }

    public  boolean UpdateWorkerData(String name , String phone, String salary
            , String image, String document,String Advances,String rest, String column, String value){

        ContentValues cv  = new ContentValues();


        SQLiteDatabase db = this.getWritableDatabase();

        cv.put(NAME,name);
        cv.put(PHONE,phone);
        cv.put(SALARY,salary);
        cv.put(IMAGE,image);
        cv.put(DOC,document);
        cv.put(ADVANCES,Advances);
        cv.put(REST,rest);

        return db.update(WORKER,cv,column+" = "+value,null) > 0;
    }
    public  boolean UpdateProductData(String name , String type, String quantity
            ,String unit,String image,String b_price
            ,String s_price,String supplier, String unit_idx,String type_idx,String value){

        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv  = new ContentValues();

        cv.put("name",name);
        cv.put("type",type);
        cv.put("quantity",quantity);
        cv.put("unit",unit);
        cv.put("image",image);
        cv.put("buying_p",b_price);
        cv.put("selling_p",s_price);
        cv.put(SUPPLIER,supplier);
        cv.put("unit_index",unit_idx);
        cv.put("type_index",type_idx);


        successful_product_update = db.update("product",cv,"id = "+value,null) > 0;

        Intent back = new Intent(context,Stock.class);
        back.putExtra("openMode", openMode.Consulting);

        ActivityCompat.startActivity(context,back,null);
        Product_Info.finish.finish();

        return successful_product_update;
    }


    public boolean UpdateSoldProductData(String quantity,String value){

        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues cv  = new ContentValues();
        cv.put("quantity",quantity);


        synchronized( Stock.products_RV){

           // Stock.finish.recreate();
            Stock.products_RV.notifyAll();
        }

       // Stock.products_RV.notify();

      return db.update("product",cv,"id = "+value,null) > 0;


    }


    public Cursor Last_Added_id(){
        String query = "select max(id) from "+WORKER+";";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;

        if(database!=null){
            cursor = database.rawQuery(query,null);

        }
            return cursor;
    }
    public Cursor Last_Added_Bill(){
        String query = "select max(Bill_id) from "+BILL+";";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;

        if(database!=null){
            cursor = database.rawQuery(query,null);

        }
        return cursor;
    }

    public Cursor ShowDocuments(String id){

        String query = "select "+DOCUMENT+".doc_id,path,worker_id from "+DOCUMENT+" inner join "+WORKER+"" +
                " on "+WORKER+"."+WORKER_ID+" = "+DOCUMENT+".worker_id where "+WORKER_ID_FK+" = "+id+"; ";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = null;
        if(database!=null){
            cursor = database.rawQuery(query,null);
        }
        return cursor;
    }
    public Cursor ShowWorkerData() {

        String query = "select id, name , phone , salary, image ,"+DOC+", advances, rest from "+WORKER+" ;";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;

        if(db!=null){

             cursor = db.rawQuery(query,null);

        }

        return cursor;
    }
    public Cursor ShowClientData() {

        String query = "select * from "+CLIENT+" ;";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;

        if(db!=null){

            cursor = db.rawQuery(query,null);

        }

        return cursor;
    }
    public Cursor ShowProductData() {

        String query = "select * from product;";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;

        if(db!=null){

            cursor = db.rawQuery(query,null);

        }

        return cursor;
    }
    public Cursor ShowTypesData() {

        String query = "select * from "+TYPE+";";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;

        if(db!=null){

            cursor = db.rawQuery(query,null);

        }

        return cursor;
    }

    public Cursor ShowSupplierData() {

        String query = "select * from "+SUPPLIER+" ;";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;

        if(db!=null){

            cursor = db.rawQuery(query,null);

        }

        return cursor;
    }
    public Cursor ShowOutBillsData() {

        String query = "select * from "+BILL+" where "+BILL_TYPE+" = 'out' ;";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;

        if(db!=null){

            cursor = db.rawQuery(query,null);

        }

        return cursor;
    }
    public Cursor ShowInBillsData() {

        String query = "select * from "+BILL+" where "+BILL_TYPE+" = 'in' ;";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;

        if(db!=null){

            cursor = db.rawQuery(query,null);

        }

        return cursor;
    }


    public boolean UpdateBill(String billID) {


        ContentValues cv  = new ContentValues();
        String Sum = "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select sum(sum) from "+Bill_line+" where "+Bill_ID+" = "
                +billID,null);

        while (cursor.moveToNext()){
            Sum =cursor.getString(0);
        }


        cv.put("sum",Sum);
        cv.put(Bill_ID,billID);



        return db.update(BILL,cv,Bill_ID+" = "+billID,null) > 0;
    }

    public Cursor SelectProductInfoWithID(String column ,int id) {

        String query = "select "+column+" from product where id = "+id+";";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;

        if(db!=null){

            cursor = db.rawQuery(query,null);

        }

        return cursor;
    }

    public Cursor SelectSupplierProductWithID(int id) {

        String query = "select * from product where supplier = "+id+";";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;

        if(db!=null){

            cursor = db.rawQuery(query,null);

        }

        return cursor;
    }

    public void UpdateProductSellingPrice(String id, float selectedPrice) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("id",id);
        cv.put("buying_p",selectedPrice);

        db.update("product",cv," id = "+id,null);

    }

    public Cursor ShowBillLines(int id) {
        String query = "select * from "+Bill_line+" where "+Bill_ID+" = "+id+";";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;

        if(db!=null){

            cursor = db.rawQuery(query,null);

        }

        return cursor;
    }

    public Cursor ShowBillItemCount(int id) {

        String query = "select count(bill_id) from "+Bill_line+" where bill_id = "+id+";";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;

        if(db!=null){

            cursor = db.rawQuery(query,null);

        }

        return cursor;
    }

    public Cursor SelectLastProductID() {

        String query = "select max(id) from "+PRODUCT;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;

        if(db!=null){

            cursor = db.rawQuery(query,null);

        }

        return cursor;
    }
}
