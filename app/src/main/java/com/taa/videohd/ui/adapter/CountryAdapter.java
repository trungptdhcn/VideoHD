package com.taa.videohd.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.taa.videohd.ui.model.KeyValue;
import com.trungpt.videodownloadmaster.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 12/11/2015.
 */
public class CountryAdapter extends ArrayAdapter<KeyValue>
{
    List<KeyValue> countries = new ArrayList<>();

    public CountryAdapter(Context context, int resource, List<KeyValue> objects)
    {
        super(context, resource, objects);
        this.countries = objects;
    }

    @Override
    public int getCount()
    {
        return countries.size();
    }

    @Override
    public KeyValue getItem(int position)
    {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView tvCountry = (TextView) v.findViewById(android.R.id.text1);
        tvCountry.setTextColor(getContext().getResources().getColor(R.color.gray_21));
        tvCountry.setText(countries.get(position).getValue());
        return v;
    }
}
