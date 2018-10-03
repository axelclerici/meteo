package com.example.poissonblob.tp_villes.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.poissonblob.tp_villes.R;
import com.example.poissonblob.tp_villes.city.City;
import com.example.poissonblob.tp_villes.database.CityProvider;
import com.example.poissonblob.tp_villes.database.CityDatabaseHandler;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int URL_LOADER = 0;
    public static final String ACCOUNT_TYPE = "com.example";
    public static final String ACCOUNT = "dummyaccount";

    private Toolbar myToolbar;
    private ListView listView;
    private SimpleCursorAdapter cursorAdapter;
    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        listView = (ListView)findViewById(R.id.liste);

        int[] to = {android.R.id.text1, android.R.id.text2};
        String[] from = {CityDatabaseHandler.CITY_NAME, CityDatabaseHandler.CITY_COUNTRY};
        cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2 ,null,
                from, to , 0 );
        getLoaderManager().initLoader(URL_LOADER, null, this);

        listView.setAdapter(cursorAdapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView a, View view, int position, long id)
            {
                try {
                    startDetailedActivity(position);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        mAccount = createSyncAccount();
        ContentResolver.addPeriodicSync(mAccount, CityProvider.AUTHORITY, Bundle.EMPTY, 3600);
    }

    private void startDetailedActivity(int position) throws ParseException
    {
        Cursor cursor = cursorAdapter.getCursor();
        cursor.moveToPosition(position);
        City city = CityProvider.cursorToCity(cursor);
        Bundle bundle = new Bundle();
        bundle.putSerializable("city", city);
        Intent i = new Intent(getApplicationContext(), DetailedCityActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }

    private void startAddCityActivity()
    {
        Intent i = new Intent(getApplicationContext(), AddCityActivity.class);
        startActivityForResult(i, 1);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        deleteCity(position);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    private void deleteCity(int position)
    {
        Cursor cursor = cursorAdapter.getCursor();
        cursor.moveToPosition(position);
        String complement = CityProvider.getQueryComplement(cursor);
        Uri uri = Uri.parse(CityProvider.CONTENT_URI + complement);
        getContentResolver().delete(uri, null, null);
        cursorAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Suppression Réussie" ,Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.add)
            startAddCityActivity();
        else if (id == R.id.refresh)
        {
            ContentResolver.requestSync(mAccount, CityProvider.AUTHORITY, Bundle.EMPTY);
            cursorAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Mise à Jour terminée" ,Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                City newCity = (City)data.getSerializableExtra("newCity");
                getContentResolver().insert(CityProvider.CONTENT_URI, CityProvider.toContent(newCity));
                cursorAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri CONTACT_URI = CityProvider.CONTENT_URI;
        CursorLoader cursorLoader = new CursorLoader(this, CONTACT_URI, null,
                null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    public Account createSyncAccount() {
        AccountManager am = AccountManager.get(this);
        Account[] accounts;

        try {
            accounts = am.getAccountsByType(ACCOUNT_TYPE);
        } catch (SecurityException e) { // This never should happen
            accounts = new Account[]{};
        }
        if (accounts.length > 0) { // already have an account defined
            return accounts[0];
        }

        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        if (am.addAccountExplicitly(newAccount, "pass1", null)) {
            ContentResolver.setIsSyncable(newAccount, CityProvider.AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(newAccount, CityProvider.AUTHORITY, true);
        } else {// else The account exists or some other error occurred.
            newAccount = null;
        }
        return newAccount;
    }
}
