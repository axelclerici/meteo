package com.example.poissonblob.tp_villes.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Poisson Blob on 02/11/2017.
 */

public class SyncService extends Service
{
    private static SyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate()
    {
        sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
