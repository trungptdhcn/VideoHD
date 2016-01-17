package com.taa.videohd.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.taa.videohd.utils.Constant;
import com.trungpt.videodownloadmaster.R;

/**
 * Created by Trung on 11/25/2015.
 */
public abstract class YouTubeFailureRecoveryActivity extends FragmentActivity implements
        YouTubePlayer.OnInitializedListener
{

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason)
    {
        if (errorReason.isUserRecoverableError())
        {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        }
        else
        {
            String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RECOVERY_DIALOG_REQUEST)
        {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Constant.KEY_YOUTUBE, this);
        }
    }

    protected abstract YouTubePlayer.Provider getYouTubePlayerProvider();

}
