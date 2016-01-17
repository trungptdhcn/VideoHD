package com.taa.videohd.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.taa.videohd.ui.model.DirectLink;
import com.trungpt.videodownloadmaster.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 12/11/2015.
 */
public class ChoseDownloadAdapter extends ArrayAdapter<DirectLink>
{
    List<DirectLink> directLinks = new ArrayList<>();

    public ChoseDownloadAdapter(Context context, int resource, List<DirectLink> objects)
    {
        super(context, resource, objects);
        this.directLinks = objects;
    }

    @Override
    public int getCount()
    {
        return directLinks.size();
    }

    @Override
    public DirectLink getItem(int position)
    {
        return directLinks.get(position);
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
        tvCountry.setText(directLinks.get(position).getQuality());
        return v;
    }
}
