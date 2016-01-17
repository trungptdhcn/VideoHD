package com.taa.videohd.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;
import com.taa.videohd.asyntask.AsyncTaskMostPopular;
import com.taa.videohd.asyntask.AsyncTaskSearchData;
import com.taa.videohd.asyntask.listener.AsyncTaskListener;
import com.taa.videohd.base.StringUtils;
import com.taa.videohd.sync.RequestDTO;
import com.taa.videohd.sync.dailymotion.DailymotionRequestDTO;
import com.taa.videohd.sync.vimeo.request.VimeoRequestDTO;
import com.taa.videohd.sync.youtube.request.YoutubeRequestDTO;
import com.taa.videohd.ui.adapter.RecycleAdapter;
import com.taa.videohd.ui.customview.SimpleDividerItemDecoration;
import com.taa.videohd.ui.listener.EndlessRecyclerOnScrollListener;
import com.taa.videohd.ui.model.Item;
import com.taa.videohd.ui.model.PageModel;
import com.taa.videohd.ui.model.UserModel;
import com.taa.videohd.utils.Constant;
import com.trungpt.videodownloadmaster.R;

import java.text.DecimalFormat;
import java.util.List;

public class UserDetailActivity extends AppCompatActivity implements AsyncTaskListener, ParallaxRecyclerAdapter.OnParallaxScroll
{
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.activity_user_detail_toolbar)
    Toolbar toolbar;

    String nextPage;
    RecycleAdapter adapter;
    RequestDTO requestDTO;
    UserModel userModel;
    EndlessRecyclerOnScrollListener scrollListener;
    View header;
    private Constant.HOST_NAME host_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        Intent i = getIntent();
        userModel = (UserModel) i.getSerializableExtra("video");
        host_name = (Constant.HOST_NAME) i.getSerializableExtra("host_name");
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(userModel.getName());
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
        tvLikeOfUser.setText(userModel.getSubscrible() != null ? (df.format(userModel.getSubscrible()) + " likes") : "0 likes");
        tvFollowsOfUser.setText(userModel.getSubscrible() != null ? (df.format(userModel.getSubscrible()) + " subscribes") : "0 subscribes");
        Glide.with(this)
                .load(userModel.getUrlAvatar())
                .centerCrop()
                .placeholder(R.drawable.ic_default)
                .crossFade()
                .into(ivUserAvatar);
        Glide.with(this)
                .load(userModel.getUrlCover())
                .centerCrop()
                .placeholder(R.drawable.ic_default)
                .crossFade()
                .into(ivCover);
        if (host_name.equals(Constant.HOST_NAME.YOUTUBE))
        {
            requestDTO = new YoutubeRequestDTO
                    .YoutubeRequestBuilder("")
                    .id(userModel.getId())
                    .build();
            AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(this, Constant.HOST_NAME.YOUTUBE_VIDEO_BY_CHANNEL_ID, requestDTO);
            asyncTask.setListener(this);
            asyncTask.execute();
        }
        else if (host_name.equals(Constant.HOST_NAME.VIMEO))
        {
            requestDTO = new VimeoRequestDTO
                    .VimeoRequestBuilder("")
                    .id(userModel.getId())
                    .build();
            AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(UserDetailActivity.this
                    , Constant.HOST_NAME.VIMEO_VIDEO_BY_USER_ID, requestDTO);
            asyncTask.setListener(UserDetailActivity.this);
            asyncTask.execute();
        }
        else
        {
            requestDTO = new DailymotionRequestDTO
                    .DailymotionRequestBuilder("")
                    .id(userModel.getId())
                    .fields(Constant.DAILYMOTION_VIDEO_FIELDS)
                    .page(1)
                    .limit(10)
                    .build();
            AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(UserDetailActivity.this
                    , Constant.HOST_NAME.DAILYMOTION_VIDEO_BY_USER_ID, requestDTO);
            asyncTask.setListener(UserDetailActivity.this);
            asyncTask.execute();
        }
        tvUserName.setText(userModel.getName());

        scrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(final int current_page)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    if (host_name.equals(Constant.HOST_NAME.YOUTUBE))
                    {
                        ((YoutubeRequestDTO) requestDTO).setPageToken(nextPage);
                        AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(UserDetailActivity.this
                                , Constant.HOST_NAME.YOUTUBE_VIDEO_BY_CHANNEL_ID, requestDTO);
                        asyncTask.setListener(UserDetailActivity.this);
                        asyncTask.execute();
                    }
                    else if (host_name.equals(Constant.HOST_NAME.VIMEO))
                    {
                        ((VimeoRequestDTO) requestDTO).setPageToken(current_page + "");
                        AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(UserDetailActivity.this
                                , Constant.HOST_NAME.VIMEO_VIDEO_BY_USER_ID, requestDTO);
                        asyncTask.setListener(UserDetailActivity.this);
                        asyncTask.execute();
                    }
                    else
                    {
                        ((DailymotionRequestDTO)requestDTO).setPage(current_page);
                        AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(UserDetailActivity.this
                                , Constant.HOST_NAME.DAILYMOTION_VIDEO_BY_USER_ID, requestDTO);
                        asyncTask.setListener(UserDetailActivity.this);
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_user_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onParallaxScroll(float percentage, float offset, View parallax)
    {
        Drawable c = toolbar.getBackground();
        c.setAlpha(Math.round(percentage * 255));
        toolbar.setBackgroundDrawable(c);
    }
}
