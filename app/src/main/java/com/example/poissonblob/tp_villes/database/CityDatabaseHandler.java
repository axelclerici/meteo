package com.example.poissonblob.tp_villes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Poisson Blob on 23/10/2017.
 */

public class CityDatabaseHandler extends SQLiteOpenHelper
{
    // Toutes les informations utiles pour manipuler les tables
    // Ici, il n'y a que la table villes dans notre base de données
    protected static final String CITY_TABLE_NAME = "villes";
    public static final String CITY_KEY = "_id";
    public static final String CITY_NAME = "nom";
    public static final String CITY_COUNTRY = "pays";
    public static final String CITY_WINDSPEED = "vent";
    public static final String CITY_PRESSURE = "pression";
    public static final String CITY_TEMPERATURE = "temperature";
    public static final String CITY_LASTCHECKDATE = "date";
    protected static final String CITY_CONSTRAINT = "name_unique";

    protected static final String CITY_TABLE_CREATE =
            "CREATE TABLE " + CITY_TABLE_NAME + " (" +
                    CITY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CITY_NAME + " TEXT, " +
                    CITY_COUNTRY + " TEXT, " +
                    CITY_WINDSPEED + " INTEGER, " +
                    CITY_PRESSURE + " INTEGER, " +
                    CITY_TEMPERATURE + " INTEGER, " +
                    CITY_LASTCHECKDATE + " DATETIME, " +
                    "CONSTRAINT "+ CITY_CONSTRAINT +" UNIQUE (" + CITY_NAME + ", "+ CITY_COUNTRY +")" +
                    ");"
            ;

    protected static final String CITY_TABLE_DROP = "DROP TABLE IF EXISTS " + CITY_TABLE_NAME + ";";

    public CityDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CITY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(CITY_TABLE_DROP);
        onCreate(db);
    }

}
