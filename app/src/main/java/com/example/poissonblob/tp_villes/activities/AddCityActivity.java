package com.example.poissonblob.tp_villes.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.poissonblob.tp_villes.R;
import com.example.poissonblob.tp_villes.city.City;

import java.util.ArrayList;

/**
 * Created by Poisson Blob on 12/10/2017.
 */
public class AddCityActivity extends Activity
{
    private EditText cityName;
    private EditText countryName;
    private Button validateButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcity_activity);
        cityName = (EditText)findViewById(R.id.editName);
        countryName = (EditText)findViewById(R.id.editCountry);
        validateButton = (Button) findViewById(R.id.validate);
        validateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = cityName.getText().toString();
                String country = countryName.getText().toString();
                if(!(name.isEmpty()) && !(country.isEmpty()))
                {
                    City newCity = new City(name, country);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("newCity", newCity);
                    intent.putExtra("newCity", newCity);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                    Toast.makeText(getBaseContext(), "Pas valide" ,Toast.LENGTH_SHORT).show();

            }
        });
    }
}
