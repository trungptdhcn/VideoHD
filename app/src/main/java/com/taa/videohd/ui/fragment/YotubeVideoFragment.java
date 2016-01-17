package com.taa.videohd.ui.fragment;

import android.os.Bundle;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.taa.videohd.ui.listener.YoutubeControlListener;
import com.taa.videohd.utils.Constant;

/**
 * Created by trung on 12/28/2015.
 */
public class YotubeVideoFragment extends YouTubePlayerFragment implements YouTubePlayer.OnInitializedListener
{
    private YouTubePlayer player;
    private String playlistId;
    private YoutubeControlListener youtubeControlListener;

    public static YotubeVideoFragment newInstance()
    {
        return new YotubeVideoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        playlistId = getArguments().getString("playlist_id");
        initialize(Constant.KEY_YOUTUBE, this);
    }

    @Override
    public void onDestroy()
    {
        if (player != null)
        {
            player.release();
        }
        super.onDestroy();
    }

    public void setVideoIndex(int position)
    {
        if (player != null)
        {
            player.cuePlaylist(playlistId, position, 0);
        }
    }

    public void pause()
    {
        if (player != null)
        {
            player.pause();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean restored)
    {
        this.player = player;
        if (!restored && playlistId != null)
        {
            player.cuePlaylist(playlistId);
        }
    }

    public void setYoutubeControlListener(YoutubeControlListener youtubeControlListener)
    {
        this.youtubeControlListener = youtubeControlListener;
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result)
    {
        this.player = null;
    }
}
