package com.taa.videohd.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.trungpt.videodownloadmaster.R;

/**
 * Created by Trung on 11/13/2015.
 */
public class SpinnerCommonAdapter extends ArrayAdapter<String>
{
    private String[] items;
    private Context context;
    private int layoutResourceId;

    public SpinnerCommonAdapter(Context context, int layoutResourceId, String[] items)
    {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount()
    {
        return items.length;
    }

    @Override
    public String getItem(int position)
    {
        return items[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
//        return getCustomView(position, convertView, parent);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View v = inflater.inflate(R.layout.spinner_selector_item, parent, false);
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTextViewItem);
        tvTitle.setText(items[position]);
        return v;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent)
    {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View v = inflater.inflate(layoutResourceId, parent, false);
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTextViewItem);
        tvTitle.setText(items[position]);
        return v;
    }

    public void setItems(String[] items)
    {
        this.items = items;
    }
}
