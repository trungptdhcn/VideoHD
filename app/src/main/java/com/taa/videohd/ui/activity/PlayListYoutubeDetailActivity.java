package com.taa.videohd.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.taa.videohd.asyntask.AsyncTaskSearchData;
import com.taa.videohd.asyntask.listener.AsyncTaskListener;
import com.taa.videohd.base.StringUtils;
import com.taa.videohd.sync.youtube.request.YoutubeRequestDTO;
import com.taa.videohd.ui.adapter.YoutubePlayListAdapter;
import com.taa.videohd.ui.fragment.YotubeVideoFragment;
import com.taa.videohd.ui.listener.EndlessScrollListener;
import com.taa.videohd.ui.model.Item;
import com.taa.videohd.ui.model.PageModel;
import com.taa.videohd.ui.model.PlayListModel;
import com.taa.videohd.utils.Constant;
import com.trungpt.videodownloadmaster.R;

import java.util.List;

public class PlayListYoutubeDetailActivity extends FragmentActivity implements AsyncTaskListener
{
    PlayListModel playList;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.activity_play_list_detail_rlContainer)
    RelativeLayout rlContainer;
    @Bind(R.id.activity_play_list_detail_tvTitleOfPlayList)
    TextView tvTitleOfPlayList;
    @Bind(R.id.activity_play_list_detail_tvVideoCountOfPlayList)
    TextView tvVideoCountOfPlayList;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.activity_play_list_detail_cbExpand)
    CheckBox cbExpand;
    @Bind(R.id.activity_play_list_detail_youtube_fragment)
    FrameLayout layoutVideo;

    YoutubePlayListAdapter adapter;
    String nextPage;
    YoutubeRequestDTO requestDTO;
    YotubeVideoFragment youTubePlayerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_playlist_detail);
        ButterKnife.bind(this);
        Intent i = getIntent();
        playList = i.getParcelableExtra("video");
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener()
        {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    requestDTO.setPageToken(nextPage);
                    AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(PlayListYoutubeDetailActivity.this, Constant.HOST_NAME.YOUTUBE_VIDEO_BY_PLAYLIST_ID, requestDTO);
                    asyncTask.setListener(PlayListYoutubeDetailActivity.this);
                    asyncTask.execute();
                    return true;
                }
                else
                {
                    return false;
                }

            }
        };
        listView.setOnScrollListener(endlessScrollListener);
        if (playList != null)
        {
            requestDTO = new YoutubeRequestDTO
                    .YoutubeRequestBuilder("")
                    .id(playList.getId())
                    .build();
            AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(this, Constant.HOST_NAME.YOUTUBE_VIDEO_BY_PLAYLIST_ID, requestDTO);
            asyncTask.setListener(this);
            asyncTask.execute();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            youTubePlayerFragment = YotubeVideoFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("playlist_id", playList.getId());
            youTubePlayerFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.activity_play_list_detail_youtube_fragment, youTubePlayerFragment);
            fragmentTransaction.commit();
        }
        if (cbExpand.isChecked())
        {
            listView.setVisibility(View.VISIBLE);
        }
        else
        {
            listView.setVisibility(View.GONE);
        }
        cbExpand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    listView.setVisibility(View.VISIBLE);
                }
                else
                {
                    listView.setVisibility(View.GONE);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                if (youTubePlayerFragment != null)
                {
                    youTubePlayerFragment.setVideoIndex(position);
                }
            }
        });
        tvTitleOfPlayList.setText(playList.getName());
        tvVideoCountOfPlayList.setText(playList.getUserModel().getName() + " - " + playList.getVideoCount());
    }

    @Override
    public void prepare()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void complete(Object obj)
    {
        PageModel page = (PageModel) obj;
        if (page != null)
        {
            List<Item> videos = page.getItems();
            nextPage = page.getNextPage();
            if (adapter == null)
            {
                adapter = new YoutubePlayListAdapter(videos, this);
                listView.setAdapter(adapter);
            }
            else
            {
                adapter.getVideos().addAll(videos);
                adapter.notifyDataSetChanged();
            }

        }
        progressBar.setVisibility(View.GONE);
    }

}
