package com.robsterthelobster.ucibustracker.data.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.robsterthelobster.ucibustracker.data.models.Route;

public class BusContentProvider extends ContentProvider {

    private BusDbHelper mDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int ROUTES = 100;
    private static final int STOPS = 200;
    private static final int ARRIVALS = 300;
    private static final int ARRIVALS_ROUTE_STOP = 301;
    private static final int VEHICLES = 400;
    private static final int VEHICLES_ROUTE = 401;

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BusContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BusContract.PATH_ROUTES, ROUTES);
        matcher.addURI(authority, BusContract.PATH_STOPS, STOPS);
        matcher.addURI(authority, BusContract.PATH_ARRIVALS, ARRIVALS);
        matcher.addURI(authority, BusContract.PATH_VEHICLES, VEHICLES);

        matcher.addURI(authority, BusContract.PATH_ARRIVALS + "/*/*", ARRIVALS_ROUTE_STOP);
        matcher.addURI(authority, BusContract.PATH_VEHICLES + "/*", VEHICLES_ROUTE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new BusDbHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";

        switch (match){
            case ROUTES:
                rowsDeleted =
                        db.delete(BusContract.RouteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STOPS:
                rowsDeleted =
                        db.delete(BusContract.StopEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ARRIVALS:
                rowsDeleted =
                        db.delete(BusContract.ArrivalEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case VEHICLES:
                rowsDeleted =
                        db.delete(BusContract.VehicleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match){
            case ROUTES:
                return BusContract.RouteEntry.CONTENT_TYPE;
            case STOPS:
                return BusContract.StopEntry.CONTENT_TYPE;
            case ARRIVALS:
                return BusContract.ArrivalEntry.CONTENT_TYPE;
            case ARRIVALS_ROUTE_STOP:
                return BusContract.ArrivalEntry.CONTENT_TYPE;
            case VEHICLES:
                return BusContract.VehicleEntry.CONTENT_TYPE;
            case VEHICLES_ROUTE:
                return BusContract.VehicleEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        long _id;

        switch(match){
            case ROUTES:
                _id = db.replace(BusContract.RouteEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = BusContract.RouteEntry.buildRouteUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case STOPS:
                _id = db.insert(BusContract.StopEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = BusContract.StopEntry.buildStopUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case ARRIVALS:
                _id = db.insert(BusContract.ArrivalEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = BusContract.ArrivalEntry.buildArrivalUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case VEHICLES:
                _id = db.insert(BusContract.VehicleEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = BusContract.VehicleEntry.buildVehicleUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        int match = sUriMatcher.match(uri);
        Cursor retCurosr;

        switch(match) {
            case ROUTES:
                retCurosr = mDbHelper.getReadableDatabase().query(
                        BusContract.RouteEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case STOPS:
                retCurosr = mDbHelper.getReadableDatabase().query(
                        BusContract.StopEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case ARRIVALS:
                retCurosr = mDbHelper.getReadableDatabase().query(
                        BusContract.ArrivalEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case ARRIVALS_ROUTE_STOP:
                //TODO
                retCurosr = mDbHelper.getReadableDatabase().query(
                        BusContract.ArrivalEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case VEHICLES:
                retCurosr = mDbHelper.getReadableDatabase().query(
                        BusContract.VehicleEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case VEHICLES_ROUTE:
                //TODO
                retCurosr = mDbHelper.getReadableDatabase().query(
                        BusContract.VehicleEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCurosr.setNotificationUri(getContext().getContentResolver(), uri);
        return retCurosr;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match){
            case ROUTES:
                rowsUpdated = db.update(BusContract.RouteEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case STOPS:
                rowsUpdated = db.update(BusContract.StopEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case ARRIVALS:
                rowsUpdated = db.update(BusContract.ArrivalEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case VEHICLES:
                rowsUpdated = db.update(BusContract.VehicleEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final int match = sUriMatcher.match(uri);
        int returnCount;

        switch (match) {
            case ROUTES:
                returnCount = bulkInsert(BusContract.RouteEntry.TABLE_NAME, values);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case STOPS:
                returnCount = bulkInsert(BusContract.StopEntry.TABLE_NAME, values);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case ARRIVALS:
                returnCount = bulkInsert(BusContract.ArrivalEntry.TABLE_NAME, values);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case VEHICLES:
                returnCount = bulkInsert(BusContract.VehicleEntry.TABLE_NAME, values);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                return super.bulkInsert(uri, values);
        }
        return returnCount;
    }

    private int bulkInsert(String tableName, ContentValues[] values){
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int returnCount = 0;
        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                long _id = db.replace(tableName, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return returnCount;
    }
}
