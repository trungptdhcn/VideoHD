package com.taa.videohd.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.taa.videohd.ui.model.DirectLink;
import com.trungpt.videodownloadmaster.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 12/11/2015.
 */
public class VideoPlayListAdapter extends BaseAdapter
{
    private List<DirectLink> videos = new ArrayList<>();
    private Activity activity;

    public VideoPlayListAdapter(List<DirectLink> videos, Activity activity)
    {
        this.videos = videos;
        this.activity = activity;
    }

    @Override
    public int getViewTypeCount()
    {
        return 3;
    }

    @Override
    public int getCount()
    {
        return videos.size();
    }

    @Override
    public Object getItem(int position)
    {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        DirectLink item = videos.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        ViewHolder holder;
        if (convertView != null)
        {
            holder = (ViewHolder) convertView.getTag();
        }
        else
        {

            convertView = inflater.inflate(R.layout.item_video_playlist, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.title.setText(item.getName());
        holder.tvUserName.setText(item.getUserName());
        holder.tvDuration.setText(item.getDuration());
        Glide.with(activity)
                .load(item.getUrlThumb())
                .centerCrop()
                .placeholder(R.drawable.ic_default)
                .crossFade()
                .into(holder.ivThumbnail);
        return convertView;
    }

    public List<DirectLink> getVideos()
    {
        return videos;
    }

    public void setVideos(List<DirectLink> videos)
    {
        this.videos = videos;
    }


    static class ViewHolder
    {
        @Bind(R.id.item_video_playlist_tvName)
        TextView title;
        @Bind(R.id.item_video_playlist_tvUserName)
        TextView tvUserName;
        @Bind(R.id.item_video_playlist_tvDuration)
        TextView tvDuration;
        @Bind(R.id.item_video_playlist_ivThumbnail)
        ImageView ivThumbnail;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
