package com.example.poissonblob.tp_villes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.text.ParseException;

/**
 * Created by Poisson Blob on 23/10/2017.
 */

// Fait interface entre les informations de la base de données et la classe City
// Contient les méthodes CRUD qui seront utilisées par le ContentProvider
public class CityDAO extends DAOBase
{
    public CityDAO(Context context)
    {
        super(context);
    }

    public long insert(ContentValues values)
    {
        open();
        long id = mDb.insert(mHandler.CITY_TABLE_NAME, null, values);
        return id;
    }

    public int delete(String selection, String[] selectionArgs)
    {
        open();
        int rowsDeleted = 0;
        rowsDeleted = mDb.delete(mHandler.CITY_TABLE_NAME, selection, selectionArgs);
        return rowsDeleted;
    }

    public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        open();
        Cursor cursor = mDb.query(mHandler.CITY_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    public int update(ContentValues values, String selection, String[] selectionArgs)
    {
        open();
        int rowsupdated  = 0;
        rowsupdated = mDb.update(mHandler.CITY_TABLE_NAME, values, selection, selectionArgs);
        return rowsupdated;
    }

    public Cursor selectCursor(String cityName, String cityCountry) throws ParseException
    {
        open();
        String query = "select * from " + mHandler.CITY_TABLE_NAME + selectWhere(cityName, cityCountry);
        Cursor cursor = mDb.rawQuery(query, null);
        return cursor;
    }

    public String selectWhere(String cityName, String cityCountry)
    {
        return (" where " + mHandler.CITY_NAME + " = '" + cityName+"'" + " AND " + mHandler.CITY_COUNTRY + " = '" + cityCountry + "'");
    }
}
