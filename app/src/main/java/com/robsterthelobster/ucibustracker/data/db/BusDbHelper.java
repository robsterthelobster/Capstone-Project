package com.robsterthelobster.ucibustracker.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by robin on 6/22/2016.
 */
public class BusDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UCIBustracker.db";

    private static final String SQL_DELETE = "DROP TABLE IF EXISTS ";

    final String SQL_CREATE_ROUTE_TABLE =
            "CREATE TABLE " + BusContract.RouteEntry.TABLE_NAME + " (" +
                    BusContract.RouteEntry.ROUTE_ID + " INTEGER UNIQUE NOT NULL, " +
                    BusContract.RouteEntry.ROUTE_NAME + " TEXT NOT NULL, " +
                    BusContract.RouteEntry.COLOR + " TEXT NOT NULL " +
                    " );";

    final String SQL_CREATE_STOP_TABLE =
            "CREATE TABLE " + BusContract.StopEntry.TABLE_NAME + " (" +
                    BusContract.StopEntry.STOP_ID + " INTEGER UNIQUE NOT NULL, " +
                    BusContract.StopEntry.STOP_NAME + " TEXT NOT NULL, " +
                    BusContract.StopEntry.LATITUDE + " REAL NOT NULL, " +
                    BusContract.StopEntry.LONGITUDE + " REAL NOT NULL " +
                    " );";

    final String SQL_CREATE_ARRIVAL_TABLE =
            "CREATE TABLE " + BusContract.ArrivalEntry.TABLE_NAME + " (" +
                    BusContract.ArrivalEntry.ROUTE_ID + " INTEGER NOT NULL, " +
                    BusContract.ArrivalEntry.ROUTE_NAME + " TEXT NOT NULL, " +
                    BusContract.ArrivalEntry.STOP_ID + " INTEGER NOT NULL, " +
                    BusContract.ArrivalEntry.PREDICTION_TIME + " TEXT NOT NULL, " +
                    BusContract.ArrivalEntry.MINUTES + " INTEGER NOT NULL, " +
                    BusContract.ArrivalEntry.SECONDS_TO_ARRIVAL + " REAL NOT NULL, " +
                    BusContract.ArrivalEntry.IS_CURRENT + " INTEGER NOT NULL" +
                    " );";

    final String SQL_CREATE_VEHICLE_TABLE =
            "CREATE TABLE " + BusContract.VehicleEntry.TABLE_NAME + " (" +
                    BusContract.VehicleEntry.ROUTE_ID + " INTEGER NOT NULL, " +
                    BusContract.VehicleEntry.BUS_NAME + " TEXT NOT NULL, " +
                    BusContract.VehicleEntry.LATITUDE + " REAL NOT NULL, " +
                    BusContract.VehicleEntry.LONGITUDE + " REAL NOT NULL, " +
                    BusContract.VehicleEntry.PERCENTAGE + " INTEGER NOT NULL " +
                    " );";

    public BusDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ROUTE_TABLE);
        db.execSQL(SQL_CREATE_STOP_TABLE);
        db.execSQL(SQL_CREATE_ARRIVAL_TABLE);
        db.execSQL(SQL_CREATE_VEHICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE + BusContract.VehicleEntry.TABLE_NAME);
        db.execSQL(SQL_DELETE + BusContract.ArrivalEntry.TABLE_NAME);
        db.execSQL(SQL_DELETE + BusContract.StopEntry.TABLE_NAME);
        db.execSQL(SQL_DELETE + BusContract.RouteEntry.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
