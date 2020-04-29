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

/**
 * The DB helper class manages all operations related to the database.
 * It automatically manages the creation and update of the database.
 */
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

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 8);
    }

    /**
     * Creates a database of warehouse and shipment data
     *
     * @param db the database that holds the information about the warehouses
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table warehouses " + "(id text primary key, name text,status integer)");

        db.execSQL("create table shipments " + "(shipment_id text, warehouse_id text" +
                ",method text,weight real,receipt integer,departure integer" +
                ", PRIMARY KEY (shipment_id,warehouse_id, receipt))");
    }

    /**
     * Used to update a database. Drops the old shipment and warehouse tables. Then takes a database
     * and calls the onCreate method to change the information
     *
     * @param db         database that needs to be updated
     * @param oldVersion the old database version
     * @param newVersion the new database version
     **/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS warehouses");
        db.execSQL("DROP TABLE IF EXISTS shipments");
        onCreate(db);
    }

    /**
     * Inserts a new warehouse by opening the database for reading and writing
     * and then inserting the contents into the database
     *
     * @param id the id of the new warehouse
     * @return returns true when the warehouse has sucessfully been added
     */
    public boolean insertWarehouse(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPANY_COLUMN_WAREHOUSE_ID, id);
        contentValues.put(COMPANY_COLUMN_FREIGHT_STATUS, true);
        try {
            db.insert(WAREHOUSE_TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Inserts a new shipment by opening the database for reading and writing
     * and then inserting the contents into the database
     *
     * @param shipment_id
     * @param warehouse_id
     * @param method
     * @param weight
     * @param receipt
     * @param departure
     * @return returns true when the shipment has sucessfully been added
     */
    public boolean insertShipment(String shipment_id, String warehouse_id, String method, double weight, long receipt, long departure) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHIPMENT_COLUMN_SHIPMENT_ID, shipment_id);
        contentValues.put(SHIPMENT_COLUMN_WAREHOUSE_ID, warehouse_id);
        contentValues.put(SHIPMENT_COLUMN_SHIPPING_METHOD, method);
        contentValues.put(SHIPMENT_COLUMN_WEIGHT, weight);
        contentValues.put(SHIPMENT_COLUMN_RECEIPT, receipt);
        contentValues.put(SHIPMENT_COLUMN_DEPARTURE, departure);
        try {
            db.insert(SHIPMENT_TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Opens the database for reading and queries the table for the number of rows in the warehouse
     * table
     *
     * @return returns the number of rows
     */
    public int numberOfWarehouseRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, WAREHOUSE_TABLE_NAME);
    }

    /**
     * Opens the database for reading and queries the table fof the number of rows in the shipment
     * table
     *
     * @return returns the number of rows
     */
    public int numberOfShipmentRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, SHIPMENT_TABLE_NAME);
    }

    /**
     * Updates a shipment by opening the database for writing and then inserting the contents into the database
     *
     * @param shipment_id
     * @param warehouse_id
     * @param method
     * @param weight
     * @param receipt
     * @param departure
     * @return returns true when the shipment is sucessfully updated
     */
    public boolean updateShipment(String shipment_id, String warehouse_id, String method, double weight, long receipt, long departure) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SHIPMENT_COLUMN_SHIPPING_METHOD, method);
        contentValues.put(SHIPMENT_COLUMN_WEIGHT, weight);
        contentValues.put(SHIPMENT_COLUMN_RECEIPT, receipt);
        contentValues.put(SHIPMENT_COLUMN_DEPARTURE, departure);
        try {
            db.update(SHIPMENT_TABLE_NAME, contentValues, "shipment_id = ? AND warehouse_id = ?", new String[]{shipment_id, warehouse_id});
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Updates a warehouse by opening the database for writing and then inserting the contents into the database
     *
     * @param id
     * @param name
     * @param status
     * @return returns true when the warehouse is sucessfully updated
     */
    public boolean updateWarehouse(String id, String name, Boolean status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COMPANY_COLUMN_WAREHOUSE_NAME, name);
        contentValues.put(COMPANY_COLUMN_FREIGHT_STATUS, status);
        try {
            db.update(WAREHOUSE_TABLE_NAME, contentValues, "id = ? ", new String[]{id});
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Deletes a warehouse by opening the database for writing, accessing the warehouse table,
     * and deleting the row the warehouse is in
     *
     * @param id
     * @return deletes the row the warehouse occurs in
     */
    public Integer deleteWarehouse(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WAREHOUSE_TABLE_NAME, "id = ? ", new String[]{id});
    }

    /**
     * Deletes a shipment by opening the database for writing, accessing the shipment table,
     * and deleting the row the shipment is in
     *
     * @param shipment_id
     * @param warehouse_id
     * @return deletes the row the shipment occurs in
     */
    public Integer deleteShipment(String shipment_id, String warehouse_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SHIPMENT_TABLE_NAME, "shipment_id = ? AND warehouse_id = ?", new String[]{shipment_id, warehouse_id});
    }

    /**
     * Creates a HashMap by opening the database and reading each row of warehouses and putting them
     * into the company_contents
     *
     * @return returns company_contents
     */
    public HashMap<Integer, Warehouse> getAllWarehouses() {
        //ArrayList<String> array_list = new ArrayList<String>();
        HashMap<Integer, Warehouse> company_contents = new HashMap<Integer, Warehouse>();
        String warehouse_id;
        String warehouse_name;
        boolean receiving_freight;

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from warehouses", null);
        while (res.moveToNext()) {
            warehouse_id = res.getString(res.getColumnIndexOrThrow(COMPANY_COLUMN_WAREHOUSE_ID));
            warehouse_name = res.getString(res.getColumnIndexOrThrow(COMPANY_COLUMN_WAREHOUSE_NAME));
            receiving_freight =
                    (1 == res.getInt(res.getColumnIndexOrThrow(COMPANY_COLUMN_FREIGHT_STATUS))) ? true : false;

            Warehouse current_warehouse = new Warehouse(warehouse_id);
            current_warehouse.setWarehouse_name(warehouse_name);
            current_warehouse.setReceiving_freight(receiving_freight);

            ArrayList<Shipment> all_shipments = getWarehouseContents(warehouse_id);
            ArrayList<Shipment> active = new ArrayList<>();
            ArrayList<Shipment> inactive = new ArrayList<>();
            for (Shipment s : all_shipments) {
                if (s.getDeparture_date() == null || s.getDeparture_date() == 0) {
                    active.add(s);
                } else {
                    inactive.add(s);
                }
            }
            current_warehouse.setWarehouse_contents((ArrayList<Shipment>) active.clone());
            current_warehouse.setWarehouse_contents_inactive((ArrayList<Shipment>) inactive.clone());

            company_contents.put(current_warehouse.hashCode(), current_warehouse);
        }
        res.close();
        return company_contents;
    }

    /**
     * Creates an ArrayList of shipments by opening the database and reading each row of shipments
     * and putting them into the warehouse_contents
     *
     * @param warehouse_id id of warehouse ArrayList of shipments should be made from
     * @return warehouse_contents
     */
    public ArrayList<Shipment> getWarehouseContents(String warehouse_id) {
        ArrayList<Shipment> warehouse_contents = new ArrayList<Shipment>();

        String shipment_id;
        Shipment.ShippingMethod shipment_method;
        double weight;
        Long receipt_date, departure_date;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from shipments where warehouse_id = ?", new String[]{warehouse_id});

        while (res.moveToNext()) {
            shipment_id = res.getString(res.getColumnIndexOrThrow(SHIPMENT_COLUMN_SHIPMENT_ID));
            shipment_method = Shipment.ShippingMethod.valueOf(res.getString(res.getColumnIndexOrThrow(SHIPMENT_COLUMN_SHIPPING_METHOD)));
            weight = res.getDouble(res.getColumnIndexOrThrow(SHIPMENT_COLUMN_WEIGHT));
            receipt_date = res.getLong(res.getColumnIndexOrThrow(SHIPMENT_COLUMN_RECEIPT));
            departure_date = res.getLong(res.getColumnIndexOrThrow(SHIPMENT_COLUMN_DEPARTURE));

            Shipment current_shipment = new Shipment(warehouse_id, shipment_method, shipment_id, weight, receipt_date);
            current_shipment.setDeparture_date(departure_date);

            warehouse_contents.add(current_shipment);
        }
        res.close();
        return warehouse_contents;
    }
}
