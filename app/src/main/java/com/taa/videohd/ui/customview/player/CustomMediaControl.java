package com.taa.videohd.ui.customview.player;

import android.widget.MediaController;
import com.google.android.exoplayer.ExoPlayer;
import com.taa.videohd.ui.listener.FullScreenListener;

/**
 * Created by Trung on 12/1/2015.
 */
public class CustomMediaControl implements MediaController.MediaPlayerControl
{
    private FullScreenListener fullScreenListener;
    private final ExoPlayer exoPlayer;

    public CustomMediaControl(ExoPlayer exoPlayer)
    {
        this.exoPlayer = exoPlayer;
    }

    @Override
    public boolean canPause()
    {
        return true;
    }

    @Override
    public boolean canSeekBackward()
    {
        return true;
    }

    @Override
    public boolean canSeekForward()
    {
        return true;
    }

    /**
     * This is an unsupported operation.
     * <p/>
     * Application of audio effects is dependent on the audio renderer used. When using
     * {@link com.google.android.exoplayer.MediaCodecAudioTrackRenderer}, the recommended approach is
     * to extend the class and override
     * {@link com.google.android.exoplayer.MediaCodecAudioTrackRenderer#onAudioSessionId}.
     *
     * @throws UnsupportedOperationException Always thrown.
     */
    @Override
    public int getAudioSessionId()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBufferPercentage()
    {
        return exoPlayer.getBufferedPercentage();
    }

    @Override
    public int getCurrentPosition()
    {
        return exoPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
                : (int) exoPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration()
    {
        return exoPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
                : (int) exoPlayer.getDuration();
    }

    @Override
    public boolean isPlaying()
    {
        return exoPlayer.getPlayWhenReady();
    }

    @Override
    public void start()
    {
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void pause()
    {
        exoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void seekTo(int timeMillis)
    {
        long seekPosition = exoPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
                : Math.min(Math.max(0, timeMillis), getDuration());
        exoPlayer.seekTo(seekPosition);
    }

    public void toggleFullScreen()
    {
        fullScreenListener.toggleFullScreen();
    }

    public boolean isFullScreen()
    {
        return fullScreenListener.isFullScreen();
    }

    public void setFullScreenListener(FullScreenListener fullScreenListener)
    {
        this.fullScreenListener = fullScreenListener;
    }
}
