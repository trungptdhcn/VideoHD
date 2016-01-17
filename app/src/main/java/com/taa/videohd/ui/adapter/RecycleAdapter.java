package com.taa.videohd.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.taa.videohd.ui.activity.VimeoVideoDetailActivity;
import com.taa.videohd.ui.activity.YoutubeVideoDetailActivity;
import com.taa.videohd.ui.model.Item;
import com.taa.videohd.ui.model.VideoModel;
import com.taa.videohd.utils.Constant;
import com.trungpt.videodownloadmaster.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by trung on 12/7/2015.
 */
public class RecycleAdapter extends ParallaxRecyclerAdapter<Item>
{
    private Context context;
    private List<Item> data;
    private Constant.HOST_NAME host_name;

    public RecycleAdapter(List<Item> data, Context context, Constant.HOST_NAME host_name)
    {
        super(data);
        this.context = context;
        this.data = data;
        this.host_name = host_name;
    }

    @Override
    public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder
            , ParallaxRecyclerAdapter<Item> itemParallaxRecyclerAdapter, int position)
    {
        final VideoModel video = (VideoModel) data.get(position);
        DecimalFormat df = new DecimalFormat("#,###");
        ((ViewHolder) viewHolder).title.setText(video.getName());
        ((ViewHolder) viewHolder).tvLike.setText(video.getLikes() != null ? df.format(video.getLikes()) : "0");
        ((ViewHolder) viewHolder).tvViews.setText(video.getViews() != null ? df.format(video.getViews()) : "0");
        ((ViewHolder) viewHolder).tvDisLike.setText(video.getDisLikes() != null ? df.format(video.getDisLikes()) : "0");
        ((ViewHolder) viewHolder).tvUserName.setText(String.valueOf(video.getUserModel().getName()));
        ((ViewHolder) viewHolder).tvDuration.setText(video.getDuration());
        Glide.with(context)
                .load(video.getUrlThumbnail())
                .centerCrop()
                .placeholder(R.drawable.ic_default)
                .crossFade()
                .into(((ViewHolder) viewHolder).ivThumbnail);
        ((ViewHolder) viewHolder).rlContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = null;
                switch (host_name)
                {
                    case YOUTUBE:
                        intent = new Intent(context,YoutubeVideoDetailActivity.class);
                        break;
                    case VIMEO:
                    case DAILYMOTION:
                        intent = new Intent(context,VimeoVideoDetailActivity.class);
                        break;

                }
                intent.putExtra("video", (Parcelable) video);
                intent.putExtra("host_name", host_name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup
            , ParallaxRecyclerAdapter<Item> itemParallaxRecyclerAdapter, final int i)
    {
        View v = ((Activity) context).getLayoutInflater().inflate(R.layout.item_video, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCountImpl(ParallaxRecyclerAdapter<Item> itemParallaxRecyclerAdapter)
    {
        return data.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder
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
        @Bind(R.id.item_video_rlContainer)
        RelativeLayout rlContainer;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
