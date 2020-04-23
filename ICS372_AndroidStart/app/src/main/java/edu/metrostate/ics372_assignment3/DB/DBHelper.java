package edu.metrostate.ics372_assignment3.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import edu.metrostate.ics372_assignment3.model.Shipment;
import edu.metrostate.ics372_assignment3.model.Warehouse;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CompanyDB.db";

    private static final String WAREHOUSE_TABLE_NAME = "warehouses";
    private static final String COMPANY_COLUMN_WAREHOUSE_ID = "id";
    private static final String COMPANY_COLUMN_WAREHOUSE_NAME = "name";
    private static final String COMPANY_COLUMN_FREIGHT_STATUS = "status";

    private static final String SHIPMENT_TABLE_NAME = "shipments";
    private static final String SHIPMENT_COLUMN_SHIPMENT_ID = "shipment_id";
    private static final String SHIPMENT_COLUMN_WAREHOUSE_ID = "warehouse_id";
    private static final String SHIPMENT_COLUMN_SHIPPING_METHOD = "method";
    private static final String SHIPMENT_COLUMN_WEIGHT = "weight";
    private static final String SHIPMENT_COLUMN_RECEIPT = "receipt";
    private static final String SHIPMENT_COLUMN_DEPARTURE = "departure";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table warehouses " + "(id text primary key, name text,status integer)");
        db.execSQL("create table shipments " + "(shipment_id text primary key, warehouse_id text,method text,weight real,receipt integer,departure integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS warehouses");
        db.execSQL("DROP TABLE IF EXISTS shipments");
        onCreate(db);
    }

    public boolean insertWarehouse (String id, String name, Boolean status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPANY_COLUMN_WAREHOUSE_ID, name);
        contentValues.put(COMPANY_COLUMN_WAREHOUSE_NAME, name);
        contentValues.put(COMPANY_COLUMN_FREIGHT_STATUS, status);
        db.insert(WAREHOUSE_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertShipment (String shipment_id, String warehouse_id, String method, double weight, long receipt, long departure) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHIPMENT_COLUMN_SHIPMENT_ID, shipment_id);
        contentValues.put(SHIPMENT_COLUMN_WAREHOUSE_ID, warehouse_id);
        contentValues.put(SHIPMENT_COLUMN_SHIPPING_METHOD, method);
        contentValues.put(SHIPMENT_COLUMN_WEIGHT, weight);
        contentValues.put(SHIPMENT_COLUMN_RECEIPT, receipt);
        contentValues.put(SHIPMENT_COLUMN_DEPARTURE, departure);
        db.insert(SHIPMENT_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery( "select * from contacts where id="+id+"", null );
    }

    public int numberOfWarehouseRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, WAREHOUSE_TABLE_NAME);
    }

    public int numberOfShipmentRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, SHIPMENT_TABLE_NAME);
    }

    public boolean updateShipment (String shipment_id, String warehouse_id, String method, double weight, long receipt, long departure) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHIPMENT_COLUMN_WAREHOUSE_ID, warehouse_id);
        contentValues.put(SHIPMENT_COLUMN_SHIPPING_METHOD, method);
        contentValues.put(SHIPMENT_COLUMN_WEIGHT, weight);
        contentValues.put(SHIPMENT_COLUMN_RECEIPT, receipt);
        contentValues.put(SHIPMENT_COLUMN_DEPARTURE, departure);
        db.update(SHIPMENT_TABLE_NAME, contentValues, "id = ? ", new String[] { shipment_id } );
        return true;
    }

    public boolean updateWarehouse (String id, String name, Boolean status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPANY_COLUMN_WAREHOUSE_NAME, name);
        contentValues.put(COMPANY_COLUMN_FREIGHT_STATUS, status);
        db.update(WAREHOUSE_TABLE_NAME, contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    public Integer deleteWarehouse (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WAREHOUSE_TABLE_NAME, "id = ? ", new String[] { id });
    }

    public Integer deleteShipment (String shipment_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SHIPMENT_TABLE_NAME, "id = ? ", new String[] { shipment_id });
    }

    public ArrayList<Warehouse> getAllWarehouses() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from warehouses", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(WAREHOUSE_TABLE_NAME)));
            res.moveToNext();
        }
        return null;
    }

    public ArrayList<Shipment> getWarehouseContents(String warehouse_id){
        ArrayList<Shipment> shipments = new ArrayList<Shipment>();


        return shipments;
    }
/*

    public ArrayList<String> getAllShipments() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from shipments", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(WAREHOUSE_TABLE_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
*/
}
