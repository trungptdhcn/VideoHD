package com.taa.videohd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.taa.videohd.ui.fragment.YoutubeRelatedFragment;
import com.taa.videohd.ui.model.VideoModel;
import com.taa.videohd.utils.Constant;
import com.trungpt.videodownloadmaster.R;

public class YoutubeVideoDetailActivity extends YouTubeFailureRecoveryActivity
{
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.layout_video_related)
    FrameLayout layoutVideoRelated;
    VideoModel video;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video_detail);
        ButterKnife.bind(this);
        Intent i = getIntent();
        video = i.getParcelableExtra("video");
        if (video != null)
        {
            YouTubePlayerFragment youTubePlayerFragment =
                    (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
            youTubePlayerFragment.initialize(Constant.KEY_YOUTUBE, this);
            YoutubeRelatedFragment relatedFragment = new YoutubeRelatedFragment();
            Bundle args = new Bundle();
            args.putParcelable("video", video);
            relatedFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.layout_video_related, relatedFragment);
            transaction.commit();
        }
    }
    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider()
    {
        return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored)
    {
        if (!wasRestored && video != null)
        {
            youTubePlayer.cueVideo(video.getId());
        }
    }
}
