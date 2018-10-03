package com.example.poissonblob.tp_villes.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.poissonblob.tp_villes.R;
import com.example.poissonblob.tp_villes.city.City;
import com.example.poissonblob.tp_villes.city.CityAdapter;

import java.util.ArrayList;

/**
 * Created by Poisson Blob on 08/10/2017.
 */

public class DetailedCityActivity extends Activity
{
    private City city;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        this.city = ((City)bundle.getSerializable("city"));
        listView = this.findViewById(R.id.grid);
        listView.setAdapter(new CityAdapter(this, city));
    }
}
