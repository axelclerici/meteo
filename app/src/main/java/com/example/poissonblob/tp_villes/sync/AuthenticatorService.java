package com.example.poissonblob.tp_villes.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Poisson Blob on 03/11/2017.
 */

public class AuthenticatorService extends Service
{

    private Authenticator mAuthenticator;

    @Override
    public void onCreate()
    {
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mAuthenticator.getIBinder();
    }
}
