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
import com.taa.videohd.ui.model.Item;
import com.taa.videohd.ui.model.PlayListModel;
import com.taa.videohd.ui.model.UserModel;
import com.taa.videohd.ui.model.VideoModel;
import com.taa.videohd.utils.Utils;
import com.trungpt.videodownloadmaster.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 12/11/2015.
 */
public class CommonAdapter extends BaseAdapter
{
    private List<Item> videos = new ArrayList<>();
    private Activity activity;

    public CommonAdapter(List<Item> videos, Activity activity)
    {
        this.videos = videos;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (videos.get(position) instanceof VideoModel)
        {
            return 0;
        }
        else if (videos.get(position) instanceof PlayListModel)
        {
            return 1;
        }
        else
        {
            return 2;
        }
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
        Item item = videos.get(position);
        LayoutInflater inflater = activity.getLayoutInflater();
        if (getItemViewType(position) == 0)
        {
            ViewHolder holder;
            VideoModel video = (VideoModel) item;
            if (convertView != null)
            {
                holder = (ViewHolder) convertView.getTag();
            }
            else
            {

                convertView = inflater.inflate(R.layout.item_video, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            DecimalFormat df = new DecimalFormat("#,###");
            holder.title.setText(video.getName());
            holder.tvLike.setText(video.getLikes() != null ? df.format(video.getLikes()) : "0");
            holder.tvViews.setText(video.getViews() != null ? df.format(video.getViews()) : "0");
            holder.tvDisLike.setText(video.getDisLikes() != null ? df.format(video.getDisLikes()) : "0");
            holder.tvUserName.setText(String.valueOf(video.getUserModel().getName()));
            holder.tvDuration.setText(video.getDuration());
            Long currentTime = System.currentTimeMillis();
            if (video.getPublishedAt() != null)
            {
                String publishAtStr = Utils.calculateTime(video.getPublishedAt(), currentTime);
                holder.tvPublishedAt.setText(publishAtStr);
            }
            Glide.with(activity)
                    .load(video.getUrlThumbnail())
                    .centerCrop()
                    .placeholder(R.drawable.ic_default)
                    .crossFade()
                    .into(holder.ivThumbnail);
        }
        else if (getItemViewType(position) == 1)
        {
            ViewHolderPlayList holder;
            PlayListModel playListModel = (PlayListModel) item;
            if (convertView != null)
            {
                holder = (ViewHolderPlayList) convertView.getTag();
            }
            else
            {

                convertView = inflater.inflate(R.layout.item_playlist, parent, false);
                holder = new ViewHolderPlayList(convertView);
                convertView.setTag(holder);
            }
            DecimalFormat df = new DecimalFormat("#,###");
            holder.tvName.setText(playListModel.getName());
//            holder.tvUserName.setText(playListModel.getUserModel().getSubscrible() != null
//                    ? (df.format(playListModel.getUserModel().getSubscrible()) + " subscribes") : "0 subscribes");
            holder.tvVideoCount.setText(playListModel.getVideoCount() != null ? (df.format(playListModel.getVideoCount())) : "0");
            holder.tvVideoCount2.setText(playListModel.getVideoCount() != null ? (df.format(playListModel.getVideoCount()) + " videos") : "0 video");
            holder.tvUserName.setText(playListModel.getUserModel().getName());
            Glide.with(activity)
                    .load(playListModel.getThumbnail())
                    .centerCrop()
                    .placeholder(R.drawable.ic_default)
                    .crossFade()
                    .into(holder.ivThumbnail);
        }
        else
        {
            ViewHolderUser holder;
            UserModel userModel = (UserModel) item;
            if (convertView != null)
            {
                holder = (ViewHolderUser) convertView.getTag();
            }
            else
            {

                convertView = inflater.inflate(R.layout.item_user, parent, false);
                holder = new ViewHolderUser(convertView);
                convertView.setTag(holder);
            }
            DecimalFormat df = new DecimalFormat("#,###");
            holder.tvUserName.setText(userModel.getName());
//            holder.tvUserName.setText(playListModel.getUserModel().getSubscrible() != null
//                    ? (df.format(playListModel.getUserModel().getSubscrible()) + " subscribes") : "0 subscribes");
            holder.tvVideoCount.setText(userModel.getVideoCounts() != null ? (df.format(userModel.getVideoCounts())+ " videos") : "0 video");
            holder.tvFollows.setText(userModel.getSubscrible() != null ? (df.format(userModel.getSubscrible()) + " subscrible") : "0 subscrible");
            holder.tvDescription.setText(userModel.getDescription());
            Glide.with(activity)
                    .load(userModel.getUrlAvatar())
                    .centerCrop()
                    .placeholder(R.drawable.ic_default)
                    .crossFade()
                    .into(holder.ivAvatar);
        }
        return convertView;
    }

    public List<Item> getVideos()
    {
        return videos;
    }

    public void setVideos(List<Item> videos)
    {
        this.videos = videos;
    }


    static class ViewHolder
    {
        @Bind(R.id.item_video_tvName)
        TextView title;
        @Bind(R.id.item_video_tvLike)
        TextView tvLike;
        @Bind(R.id.item_video_tvDisLike)
        TextView tvDisLike;
        @Bind(R.id.item_video_tvUserName)
        TextView tvUserName;
        @Bind(R.id.item_video_tvViews)
        TextView tvViews;
        @Bind(R.id.item_video_ivThumbnail)
        ImageView ivThumbnail;
        @Bind(R.id.item_video_tvDuration)
        TextView tvDuration;
        @Bind(R.id.item_video_tvPublishedAt)
        TextView tvPublishedAt;

        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderPlayList
    {
        @Bind(R.id.item_playlist_tvName)
        TextView tvName;
        @Bind(R.id.item_playlist_tvUserName)
        TextView tvUserName;
        @Bind(R.id.item_playlist_tvVideoCount2)
        TextView tvVideoCount2;
        @Bind(R.id.item_playlist_tvVideoCount)
        TextView tvVideoCount;
        @Bind(R.id.item_playlist_ivThumbnail)
        ImageView ivThumbnail;

        public ViewHolderPlayList(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderUser
    {
        @Bind(R.id.item_user_tvName)
        TextView tvUserName;
        @Bind(R.id.item_user_tvVideos)
        TextView tvVideoCount;
        @Bind(R.id.item_user_tvFollows)
        TextView tvFollows;
        @Bind(R.id.item_user_tvDescription)
        TextView tvDescription;
        @Bind(R.id.item_user_ivAvatar)
        ImageView ivAvatar;

        public ViewHolderUser(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
