package com.taa.videohd.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.taa.videohd.asyntask.AsyncTaskMostPopular;
import com.taa.videohd.asyntask.listener.AsyncTaskListener;
import com.taa.videohd.base.StringUtils;
import com.taa.videohd.sync.RequestDTO;
import com.taa.videohd.sync.dailymotion.DailymotionRequestDTO;
import com.taa.videohd.sync.vimeo.request.VimeoRequestDTO;
import com.taa.videohd.ui.adapter.RecycleAdapter;
import com.taa.videohd.ui.customview.SimpleDividerItemDecoration;
import com.taa.videohd.ui.listener.EndlessRecyclerOnScrollListener;
import com.taa.videohd.ui.model.Item;
import com.taa.videohd.ui.model.PageModel;
import com.taa.videohd.ui.model.PlayListModel;
import com.taa.videohd.utils.Constant;
import com.trungpt.videodownloadmaster.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by trung on 12/28/2015.
 */
public class ListVideoDetailActivity extends AppCompatActivity implements AsyncTaskListener, ParallaxRecyclerAdapter.OnParallaxScroll
{
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.list_video_detail_toolbar)
    Toolbar toolbar;

    String nextPage;
    RecycleAdapter adapter;
    RequestDTO requestDTO;
    PlayListModel playListModel;
    EndlessRecyclerOnScrollListener scrollListener;
    View header;
    private Constant.HOST_NAME host_name;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_video_detail);
        ButterKnife.bind(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        Intent i = getIntent();
        playListModel = i.getParcelableExtra("video");
        host_name = (Constant.HOST_NAME) i.getSerializableExtra("host_name");
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(playListModel.getName());
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onBackPressed();
                }
            });
        }
        header = getLayoutInflater().inflate(R.layout.cover_layout, recyclerView, false);
        ImageView ivCover = (ImageView) header.findViewById(R.id.cover_layout_ivCover);
        ImageView ivUserAvatar = (ImageView) header.findViewById(R.id.cover_layout_ivUserAvatar);
        TextView tvUserName = (TextView) header.findViewById(R.id.cover_layout_tvUserName);
        TextView tvLikeOfUser = (TextView) header.findViewById(R.id.cover_layout_tvLikes);
        TextView tvFollowsOfUser = (TextView) header.findViewById(R.id.cover_layout_tvFollows);
        DecimalFormat df = new DecimalFormat("#,###");
        tvLikeOfUser.setText(playListModel.getUserModel().getSubscrible() != null ? (df.format(playListModel.getUserModel().getSubscrible()) + " likes") : "0 likes");
        tvFollowsOfUser.setText(playListModel.getUserModel().getSubscrible() != null ? (df.format(playListModel.getUserModel().getSubscrible()) + " subscribes") : "0 subscribes");
        Glide.with(this)
                .load(playListModel.getUserModel().getUrlAvatar())
                .centerCrop()
                .placeholder(R.drawable.ic_default)
                .crossFade()
                .into(ivUserAvatar);
        Glide.with(this)
                .load(playListModel.getUserModel().getUrlCover())
                .centerCrop()
                .placeholder(R.drawable.ic_default)
                .crossFade()
                .into(ivCover);
        if (host_name.equals(Constant.HOST_NAME.VIMEO))
        {
            requestDTO = new VimeoRequestDTO
                    .VimeoRequestBuilder("")
                    .id(playListModel.getUserModel().getId())
                    .build();
            AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(ListVideoDetailActivity.this
                    , Constant.HOST_NAME.VIMEO_VIDEO_WITH_CHANNEL_ID, requestDTO);
            asyncTask.setListener(ListVideoDetailActivity.this);
            asyncTask.execute();
        }
        else
        {
            requestDTO = new DailymotionRequestDTO
                    .DailymotionRequestBuilder("")
                    .id(playListModel.getId())
                    .fields(Constant.DAILYMOTION_VIDEO_FIELDS)
                    .page(1)
                    .limit(10)
                    .build();
            AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(ListVideoDetailActivity.this
                    , Constant.HOST_NAME.DAILYMOTION_VIDEO_BY_PLAYLIST_ID, requestDTO);
            asyncTask.setListener(ListVideoDetailActivity.this);
            asyncTask.execute();
        }
        tvUserName.setText(playListModel.getName());

        scrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(final int current_page)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                   if (host_name.equals(Constant.HOST_NAME.VIMEO))
                    {
                        ((VimeoRequestDTO) requestDTO).setPageToken(current_page + "");
                        AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(ListVideoDetailActivity.this
                                , Constant.HOST_NAME.VIMEO_VIDEO_WITH_CHANNEL_ID, requestDTO);
                        asyncTask.setListener(ListVideoDetailActivity.this);
                        asyncTask.execute();
                    }
                    else
                    {
                        ((DailymotionRequestDTO)requestDTO).setPage(current_page);
                        AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(ListVideoDetailActivity.this
                                , Constant.HOST_NAME.DAILYMOTION_VIDEO_BY_PLAYLIST_ID, requestDTO);
                        asyncTask.setListener(ListVideoDetailActivity.this);
                        asyncTask.execute();
                    }
                }

            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void prepare()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void complete(Object obj)
    {
        PageModel pageModel = (PageModel) obj;
        if (pageModel != null)
        {
            List<Item> items = pageModel.getItems();
            nextPage = pageModel.getNextPage();
            if (adapter == null)
            {
                adapter = new RecycleAdapter(items, this, host_name);
                adapter.setParallaxHeader(header, recyclerView);
                adapter.setOnParallaxScroll(this);
                recyclerView.setAdapter(adapter);
            }
            else
            {
                adapter.getData().addAll(items);
                adapter.notifyItemInserted(adapter.getItemCount());
            }
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onParallaxScroll(float percentage, float offset, View parallax)
    {
        Drawable c = toolbar.getBackground();
        c.setAlpha(Math.round(percentage * 255));
        toolbar.setBackgroundDrawable(c);
    }
}
