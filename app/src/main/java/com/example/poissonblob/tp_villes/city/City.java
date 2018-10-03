package com.example.poissonblob.tp_villes.city;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Poisson Blob on 08/10/2017.
 */

public class City implements Serializable
{
    public String name;
    public String country;
    public int windSpeed;
    public int pressure;
    public int temperature;
    public Date lastCheck;
    public static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public City(String name, String country)
    {
        this.name = name;
        this.country = country;
        this.windSpeed = 0;
        this.pressure = 0;
        this.temperature = 0;
        this.lastCheck = Calendar.getInstance().getTime();
    }

    public City(String name, String country, int windSpeed, int pressure, int temperature, Date lastCheck)
    {
        this.name = name;
        this.country = country;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.temperature = temperature;
        this.lastCheck = lastCheck;
    }

    // Permet de créer une ville à partir d'un nom, d'un pays et d'informations tirées
    // de l'API Yahoo Weather (stockées dans une List<String>)
    public City(String name, String country, List<String> list) throws ParseException
    {
        DateFormat old = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm aaa");
        this.name = name;
        this.country = country;
        this.windSpeed = Integer.parseInt(list.get(0).split(" ")[0]);
        this.temperature = Integer.parseInt(list.get(1));
        this.pressure = Integer.parseInt(list.get(2).split("\\.")[0]);
        this.lastCheck = old.parse(list.get(3));
    }

    // Renvoie une liste contenant les informations et les labels correspondant
    // Destiné à être exploité par CityAdapter.
    public ArrayList<String> convertToString()
    {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        ArrayList<String> list = new ArrayList<String>(12);
        list.add(name);
        list.add(country);
        list.add(String.valueOf(windSpeed) +" km/h");
        list.add(String.valueOf(pressure) +" kPa");
        list.add(String.valueOf(temperature) +" °C");
        list.add(df.format(lastCheck));
        list.add("Ville:");
        list.add("Pays:");
        list.add("Vitesse Vent:");
        list.add("Préssion:");
        list.add("Température:");
        list.add("Dernier Relevé:");
        return list;
    }
}
