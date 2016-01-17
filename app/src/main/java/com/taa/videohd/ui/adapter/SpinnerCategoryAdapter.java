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
 * Created by Trung on 12/4/2015.
 */
public class SpinnerCategoryAdapter extends ArrayAdapter<KeyValue>
{
    private List<KeyValue> items = new ArrayList<>();
    private Context context;
    private int layoutResourceId;

    public SpinnerCategoryAdapter(Context context, int layoutResourceId, List<KeyValue> items)
    {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public KeyValue getItem(int position)
    {
        return items.get(position);
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
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View v = inflater.inflate(R.layout.spinner_selector_item, parent, false);
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTextViewItem);
        tvTitle.setText(items.get(position).getValue());
        return v;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View v = inflater.inflate(layoutResourceId, parent, false);
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTextViewItem);
        tvTitle.setText(items.get(position).getValue());
        return v;
    }
}
