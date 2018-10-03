package com.example.poissonblob.tp_villes.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;


import com.example.poissonblob.tp_villes.city.City;

import java.text.ParseException;

/**
 * Created by Poisson Blob on 29/10/2017.
 */

public class CityProvider extends ContentProvider
{
    private static String TABLE_NAME = CityDatabaseHandler.CITY_TABLE_NAME;

    public static final String AUTHORITY = "com.example.poissonblob.tp_villes.database.CityProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    // Les types MIME
    private static final String TYPE_DIR = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE_NAME;
    private static final String TYPE_ITEM = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE_NAME;

    private static final int DIR = 0;
    private static final int ITEM = 1;

    // En charge de reconnaître si une Uri est de type DIR ou de type ITEM
    private static final UriMatcher uriMatcher = getUriMatcher();
    private static UriMatcher getUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, DIR);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/*/*", ITEM);
        return uriMatcher;
    }

    private CityDAO cityDAO;

    @Override
    public String getType(Uri uri)
    {
        if (uriMatcher.match(uri) == 0)
            return(TYPE_DIR);
        return TYPE_ITEM;
    }

    @Override
    public boolean onCreate()
    {
        cityDAO = new CityDAO(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort)
    {
        int uriType = uriMatcher.match(uri);
        Cursor cursor = null;
        switch (uriType)
        {
            case ITEM:
                try {
                    String[] segments = uri.getPath().split("/");
                    cursor = cityDAO.selectCursor(segments[2], segments[3]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case DIR:
                    cursor = cityDAO.query(projection, selection, selectionArgs, sort);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        int uriType = uriMatcher.match(uri);
        long id = 0;
        switch (uriType)
        {
            case DIR:
                id = cityDAO.insert(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(TABLE_NAME + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int uriType = uriMatcher.match(uri);
        int rowsDeleted = 0;

        switch (uriType) {
            case DIR:
                rowsDeleted = cityDAO.delete(selection, selectionArgs);
                break;

            case ITEM:
                rowsDeleted = cityDAO.delete(CityDatabaseHandler.CITY_NAME + " = ? AND " + CityDatabaseHandler.CITY_COUNTRY + " =?",
                        cityArgs(uri));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        int uriType = uriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (uriType) {
            case DIR:
                rowsUpdated = cityDAO.update(values, selection, selectionArgs);
                break;
            case ITEM:
                rowsUpdated = cityDAO.update(values, CityDatabaseHandler.CITY_NAME + " = ? AND " + CityDatabaseHandler.CITY_COUNTRY + " =?",
                        cityArgs(uri));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private String[] cityArgs(Uri uri)
    {
        String[] segments = uri.getPath().split("/");
        String[] args = {segments[2], segments[3]};
        return args;
    }

    public static ContentValues toContent(City c)
    {
        ContentValues value = new ContentValues();
        value.put(CityDatabaseHandler.CITY_NAME, c.name);
        value.put(CityDatabaseHandler.CITY_COUNTRY, c.country);
        value.put(CityDatabaseHandler.CITY_WINDSPEED, c.windSpeed);
        value.put(CityDatabaseHandler.CITY_PRESSURE, c.pressure);
        value.put(CityDatabaseHandler.CITY_TEMPERATURE, c.temperature);
        value.put(CityDatabaseHandler.CITY_LASTCHECKDATE, c.df.format(c.lastCheck));
        return value;
    }

    public static City cursorToCity(Cursor cursor) throws ParseException
    {
        return new City(
                cursor.getString(cursor.getColumnIndex(CityDatabaseHandler.CITY_NAME)),
                cursor.getString(cursor.getColumnIndex(CityDatabaseHandler.CITY_COUNTRY)),
                cursor.getInt(cursor.getColumnIndex(CityDatabaseHandler.CITY_WINDSPEED)),
                cursor.getInt(cursor.getColumnIndex(CityDatabaseHandler.CITY_PRESSURE)),
                cursor.getInt(cursor.getColumnIndex(CityDatabaseHandler.CITY_TEMPERATURE)),
                City.df.parse(cursor.getString(cursor.getColumnIndex(CityDatabaseHandler.CITY_LASTCHECKDATE)))
        );
    }

    public static String getQueryComplement(Cursor cursor)
    {
        String name = cursor.getString(cursor.getColumnIndex(CityDatabaseHandler.CITY_NAME));
        String country = cursor.getString(cursor.getColumnIndex(CityDatabaseHandler.CITY_COUNTRY));
        return "/" + name + "/" + country;
    }

    public static String[] getNameandCountry(Cursor cursor)
    {
        String[] str = {
                cursor.getString(cursor.getColumnIndex(CityDatabaseHandler.CITY_NAME)),
                cursor.getString(cursor.getColumnIndex(CityDatabaseHandler.CITY_COUNTRY))} ;
        return str;
    }
}