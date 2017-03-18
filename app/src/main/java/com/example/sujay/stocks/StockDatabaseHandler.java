package com.example.sujay.stocks;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Sujay on 3/11/2017.
 */

public class StockDatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "StockDatabaseHandler";
    private static final int DATABASE_VERSION = 7;

    // DB Name
    private static final String DATABASE_NAME = "StockAppDB";
    // DB Table Name
    private static final String TABLE_NAME = "StockTable";
    ///DB Columns
    private static final String STOCK_NAME = "STOCK_NAME";
    private static final String STOCK_SYMBOL = "STOCK_SYMBOL";
    private static final String STOCK_PERCENTAGE = "STOCK_PERCENTAGE";
    private static final String STOCK_CHANGE = "STOCK_CHANGE";
    private static final String STOCK_VALUE = "STOCK_VALUE";

    // DB Table Create Code
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    STOCK_NAME + " TEXT not null unique," +
                    STOCK_SYMBOL + " TEXT not null, " +
                    STOCK_PERCENTAGE + " REAL not null, " +
                    STOCK_CHANGE + " REAL not null, " +
                    STOCK_VALUE + " REAL not null)";

    private SQLiteDatabase database;

    private static final String DATABASE_ALTER_TABLE_FOR_V4 = "DROP TABLE  "
            + TABLE_NAME;


    public StockDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
        Log.d(TAG, "DatabaseHandler: C'tor DONE");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate is only called is the DB does not exist
        Log.d(TAG, "onCreate: Mking New DB");
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion == 7) {
            db.execSQL(SQL_CREATE_TABLE);
        }
    }

    public ArrayList<Stock> loadCountries() {

        //Log.d(TAG, "loadCountries: LOADING COUNTRY DATA FROM DB");
        ArrayList<Stock> stocks = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_NAME,
                new String[]{STOCK_NAME, STOCK_SYMBOL, STOCK_PERCENTAGE, STOCK_CHANGE, STOCK_VALUE}, // The columns to return
                null,
                null,
                null,
                null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String stock_name = cursor.getString(0);
                String stock_symbol = cursor.getString(1);
                double stock_percentage = cursor.getDouble(2);
                double stock_change = cursor.getDouble(3);
                double stock_value = cursor.getDouble(4);
                stocks.add(new Stock(stock_name, stock_symbol, stock_value, stock_percentage,stock_change));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return stocks;
    }

    public void addStock(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(STOCK_NAME, stock.getstock_name());
        values.put(STOCK_SYMBOL, stock.getstock_symbol());
        values.put(STOCK_PERCENTAGE, stock.getstock_percentage());
        values.put(STOCK_CHANGE, stock.getstock_change());
        values.put(STOCK_VALUE, stock.getstock_value());

        deleteStock(stock.getstock_name());
        long key = database.insert(TABLE_NAME, null, values);
        //Log.d(TAG, "addStock: " + key);
    }

    public void updateStock(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(STOCK_NAME, stock.getstock_name());
        values.put(STOCK_SYMBOL, stock.getstock_symbol());
        values.put(STOCK_PERCENTAGE, stock.getstock_percentage());
        values.put(STOCK_CHANGE, stock.getstock_change());
        values.put(STOCK_VALUE, stock.getstock_value());

        long key = database.update(
                TABLE_NAME, values, STOCK_NAME + " = ?", new String[]{stock.getstock_name()});

        //Log.d(TAG, "updateStock: " + key);
    }

    public void deleteStock(String name) {
        //Log.d(TAG, "deleteStock: " + name);
        int cnt = database.delete(TABLE_NAME, STOCK_NAME + " = ?", new String[]{name});
        //Log.d(TAG, "deleteStock: " + cnt);
    }

    public void dumpLog() {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null) {
            cursor.moveToFirst();

            //Log.d(TAG, "dumpLog: ##############################");
            for (int i = 0; i < cursor.getCount(); i++) {
                String stock_name = cursor.getString(0);
                String stock_symbol = cursor.getString(1);
                String stock_percentage = cursor.getString(2);
                String stock_change = cursor.getString(3);
                String stock_vlaue = cursor.getString(4);
               /* Log.d(TAG, "dumpLog: " +
                        String.format("%s %-18s", STOCK_NAME + ":", stock_name) +
                        String.format("%s %-18s", STOCK_SYMBOL + ":", stock_symbol) +
                        String.format("%s %-18s", STOCK_PERCENTAGE + ":", stock_percentage) +
                        String.format("%s %-18s", STOCK_CHANGE + ":", stock_change) +
                        String.format("%s %-18s", STOCK_VALUE + ":", stock_vlaue)); */
                cursor.moveToNext();
            }
            cursor.close();
        }
        //Log.d(TAG, "dumpLog: #####################################");
    }
}
