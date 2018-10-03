package com.example.poissonblob.tp_villes.city;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.poissonblob.tp_villes.R;
import com.example.poissonblob.tp_villes.city.City;

import java.util.ArrayList;

public class CityAdapter extends BaseAdapter
{
    private City city;
    private ArrayList<String> cityString;
    private Context context;
    public CityAdapter(Context context, City city)
    {
        this.context = context;
        this.city = city;
        this.cityString = city.convertToString();
    }

    @Override
    public int getCount()
    {
        return cityString.size()/2;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public Object getItem(int position)
    {
        return cityString.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View list;
        if(convertView==null)
        {
            list=new View(context);
            LayoutInflater mLayoutinflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            list=mLayoutinflater.inflate(R.layout.detailled_city, parent, false);
        }
        else
            list=(View)convertView;

        TextView t1=(TextView)list.findViewById(R.id.text1);
        t1.setText(getLabel(position));

        TextView t2=(TextView)list.findViewById(R.id.text2);
            t2.setText(getValue(position));

        return list;
    }

    private String getLabel(int position)
    {
        return cityString.get(position + getCount());
    }

    private String getValue(int position)
    {
        return cityString.get(position);
    }
}
