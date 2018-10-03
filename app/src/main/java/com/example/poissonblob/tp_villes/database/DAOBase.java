package com.example.poissonblob.tp_villes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Poisson Blob on 23/10/2017.
 */

// Classe qui permet de gérer les ouvertures et fermetures de la base.
public abstract class DAOBase
{
    protected final static int DB_VERSION = 1;
    protected final static String DB_NAME = "database.db";

    protected SQLiteDatabase mDb = null;
    protected CityDatabaseHandler mHandler = null;
    protected Context context;

    public DAOBase(Context context)
    {
        this.mHandler = new CityDatabaseHandler(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public SQLiteDatabase open()
    {
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close()
    {
        mDb.close();
    }

    public SQLiteDatabase getDb()
    {
        return mDb;
    }
}