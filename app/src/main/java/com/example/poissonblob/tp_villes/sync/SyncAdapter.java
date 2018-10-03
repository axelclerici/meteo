package com.example.poissonblob.tp_villes.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.example.poissonblob.tp_villes.city.City;
import com.example.poissonblob.tp_villes.database.CityProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;

/**
 * Created by Poisson Blob on 02/11/2017.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter
{
    ContentResolver mContentResolver;
    JSONResponseHandler responseHandler;

    public SyncAdapter(Context context, boolean autoInitialize)
    {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        this.responseHandler = new JSONResponseHandler();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs)
    {
        super(context, autoInitialize, allowParallelSyncs);
        this.responseHandler = new JSONResponseHandler();
        mContentResolver = context.getContentResolver();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
    {
        Cursor cursor = getContext().getContentResolver().query(CityProvider.CONTENT_URI, null, null, null);
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                ContentValues content = null;
                String cityInfos[] = CityProvider.getNameandCountry(cursor);
                Uri uri = Uri.parse(CityProvider.CONTENT_URI + CityProvider.getQueryComplement(cursor));
                try {
                    content = getApiInfos(cityInfos[0], cityInfos[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
               getContext().getContentResolver().update(uri, content, null, null);
                cursor.moveToNext();
            }
        }
    }

    private ContentValues getApiInfos(String name, String country) throws IOException, ParseException
    {
        String test = name + ", " + country;
        String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", test);
        String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
        URL url = new URL(endpoint);
        URLConnection uc = url.openConnection();
        InputStream is = uc.getInputStream();
        City newCity = new City(name, country, responseHandler.handleResponse(is, " "));
        return CityProvider.toContent(newCity);
    }
}
