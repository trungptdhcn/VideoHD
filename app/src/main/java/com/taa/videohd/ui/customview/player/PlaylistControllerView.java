package com.taa.videohd.ui.customview.player;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.trungpt.videodownloadmaster.R;

import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by trung on 12/17/2015.
 */
public class PlaylistControllerView extends FrameLayout
{
    private static final String TAG = "VideoControllerView";

    private CustomPlayListControl mPlayer;
    private Context mContext;
    private ViewGroup mAnchor;
    private View mRoot;
    private SeekBar mProgress;
    private TextView mEndTime, mCurrentTime;
    private boolean mShowing;
    private boolean mDragging;
    private static final int sDefaultTimeout = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private ImageButton mPauseButton;
    private ImageButton mFullscreenButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private Handler mHandler = new MessageHandler(this);

    public PlaylistControllerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mRoot = null;
        mContext = context;
        Log.i(TAG, TAG);
    }

    public PlaylistControllerView(Context context)
    {
        super(context);
        mContext = context;

        Log.i(TAG, TAG);
    }

    @Override
    public void onFinishInflate()
    {
        if (mRoot != null)
        {
            initControllerView(mRoot);
        }
    }

    public void setMediaPlayer(CustomPlayListControl player)
    {
        mPlayer = player;
        updatePausePlay();
        updateFullScreen();
        updateNext();
        updatePrev();
    }

    /**
     * Set the view that acts as the anchor for the control view.
     * This can for example be a VideoView, or your Activity's main view.
     *
     * @param view The view to which to anchor the controller when it is visible.
     */
    public void setAnchorView(ViewGroup view)
    {
        mAnchor = view;

        LayoutParams frameParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    /**
     * Create the view that holds the widgets that control playback.
     * Derived classes can override this to create their own.
     *
     * @return The controller view.
     * @hide This doesn't work as advertised
     */
    protected View makeControllerView()
    {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.playlist_controller, null);

        initControllerView(mRoot);

        return mRoot;
    }

    private void initControllerView(View v)
    {
        mPauseButton = (ImageButton) v.findViewById(R.id.pause);
        if (mPauseButton != null)
        {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }

        mNextButton = (ImageButton) v.findViewById(R.id.next);
        if (mNextButton != null)
        {
            mNextButton.requestFocus();
            mNextButton.setOnClickListener(mNext);
        }

        mPrevButton = (ImageButton) v.findViewById(R.id.previous);
        if (mPrevButton != null)
        {
            mPrevButton.requestFocus();
            mPrevButton.setOnClickListener(mPrev);
        }

        mFullscreenButton = (ImageButton) v.findViewById(R.id.fullscreen);
        if (mFullscreenButton != null)
        {
            mFullscreenButton.requestFocus();
            mFullscreenButton.setOnClickListener(mFullscreenListener);
        }


        // By default these are hidden. They will be enabled when setPrevNextListeners() is called

        mProgress = (SeekBar) v.findViewById(R.id.mediacontroller_progress);
        if (mProgress != null)
        {
            if (mProgress instanceof SeekBar)
            {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
        }

        mEndTime = (TextView) v.findViewById(R.id.time);
        mCurrentTime = (TextView) v.findViewById(R.id.time_current);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 3 seconds of inactivity.
     */
    public void show()
    {
        show(sDefaultTimeout);
    }

    /**
     * Disable pause or seek buttons if the stream cannot be paused or seeked.
     * This requires the control interface to be a MediaPlayerControlExt
     */
    private void disableUnsupportedButtons()
    {
        if (mPlayer == null)
        {
            return;
        }

        try
        {
            if (mPauseButton != null && !mPlayer.canPause())
            {
                mPauseButton.setEnabled(false);
            }
        }
        catch (IncompatibleClassChangeError ex)
        {
            // We were given an old version of the interface, that doesn't have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't disable
            // the buttons.
        }
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 'timeout' milliseconds of inactivity.
     *
     * @param timeout The timeout in milliseconds. Use 0 to show
     *                the controller until hide() is called.
     */
    public void show(int timeout)
    {
        if (!mShowing && mAnchor != null)
        {
            setProgress();
            if (mPauseButton != null)
            {
                mPauseButton.requestFocus();
            }
            disableUnsupportedButtons();

            LayoutParams tlp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM
            );

            mAnchor.addView(this, tlp);
            mShowing = true;
        }
        updatePausePlay();
        updateFullScreen();
        updateNext();
        updatePrev();

        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0)
        {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    public boolean isShowing()
    {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide()
    {
        if (mAnchor == null)
        {
            return;
        }

        try
        {
            mAnchor.removeView(this);
            mHandler.removeMessages(SHOW_PROGRESS);
        }
        catch (IllegalArgumentException ex)
        {
            Log.w("MediaController", "already removed");
        }
        mShowing = false;
    }

    private String stringForTime(int timeMs)
    {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0)
        {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        }
        else
        {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress()
    {
        if (mPlayer == null || mDragging)
        {
            return 0;
        }

        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null)
        {
            if (duration > 0)
            {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (mEndTime != null)
        {
            mEndTime.setText(stringForTime(duration));
        }
        if (mCurrentTime != null)
        {
            mCurrentTime.setText(stringForTime(position));
        }

        return position;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        show(sDefaultTimeout);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev)
    {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (mPlayer == null)
        {
            return true;
        }

        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE)
        {
            if (uniqueDown)
            {
                doPauseResume();
                show(sDefaultTimeout);
                if (mPauseButton != null)
                {
                    mPauseButton.requestFocus();
                }
            }
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY)
        {
            if (uniqueDown && !mPlayer.isPlaying())
            {
                mPlayer.start();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE)
        {
            if (uniqueDown && mPlayer.isPlaying())
            {
                mPlayer.pause();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE)
        {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU)
        {
            if (uniqueDown)
            {
                hide();
            }
            return true;
        }

        show(sDefaultTimeout);
        return super.dispatchKeyEvent(event);
    }

    private OnClickListener mPauseListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            doPauseResume();
            show(sDefaultTimeout);
        }
    };

    private OnClickListener mFullscreenListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            doToggleFullscreen();
            show(sDefaultTimeout);
        }
    };

    private OnClickListener mNext = new OnClickListener()
    {
        public void onClick(View v)
        {
            doNext();
            show(sDefaultTimeout);
        }
    };

    private void doNext()
    {
        if (mPlayer == null)
        {
            return;
        }

        mPlayer.next();
    }

    private OnClickListener mPrev = new OnClickListener()
    {
        public void onClick(View v)
        {
            doPrev();
            show(sDefaultTimeout);
        }
    };

    private void doPrev()
    {
        if (mPlayer == null)
        {
            return;
        }

        mPlayer.prev();
    }

    public void updatePausePlay()
    {
        if (mRoot == null || mPauseButton == null || mPlayer == null)
        {
            return;
        }

        if (mPlayer.isPlaying())
        {
            mPauseButton.setImageResource(R.drawable.ic_pause);
        }
        else
        {
            mPauseButton.setImageResource(R.drawable.ic_play);
        }
    }

    public void updateNext()
    {
        if (mRoot == null || mNextButton == null || mPlayer == null)
        {
            return;
        }

        if (mPlayer.isNext())
        {
            mNextButton.setImageResource(R.drawable.ic_vidcontrol_next);
        }
        else
        {
            mNextButton.setImageResource(R.drawable.ic_vidcontrol_next_disabled);
        }
    }

    public void updatePrev()
    {
        if (mRoot == null || mPrevButton == null || mPlayer == null)
        {
            return;
        }

        if (mPlayer.isPrev())
        {
            mPrevButton.setImageResource(R.drawable.ic_vidcontrol_prev);
        }
        else
        {
            mPrevButton.setImageResource(R.drawable.ic_vidcontrol_prev_disabled);
        }
    }

    public void updateFullScreen()
    {
        if (mRoot == null || mFullscreenButton == null || mPlayer == null)
        {
            return;
        }

        if (mPlayer.isFullScreen())
        {
            mFullscreenButton.setImageResource(R.drawable.ic_fullscreen);
        }
        else
        {
            mFullscreenButton.setImageResource(R.drawable.ic_fullscreen_exit);
        }
    }

    private void doPauseResume()
    {
        if (mPlayer == null)
        {
            return;
        }

        if (mPlayer.isPlaying())
        {
            mPlayer.pause();
        }
        else
        {
            mPlayer.start();
        }
        updatePausePlay();
    }

    private void doToggleFullscreen()
    {
        if (mPlayer == null)
        {
            return;
        }

        mPlayer.toggleFullScreen();
    }

    // There are two scenarios that can trigger the seekbar listener to trigger:
    //
    // The first is the user using the touchpad to adjust the posititon of the
    // seekbar's thumb. In this case onStartTrackingTouch is called followed by
    // a number of onProgressChanged notifications, concluded by onStopTrackingTouch.
    // We're setting the field "mDragging" to true for the duration of the dragging
    // session to avoid jumps in the position in case of ongoing playback.
    //
    // The second scenario involves the user operating the scroll ball, in this
    // case there WON'T BE onStartTrackingTouch/onStopTrackingTouch notifications,
    // we will simply apply the updated position without suspending regular updates.
    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener()
    {
        public void onStartTrackingTouch(SeekBar bar)
        {
            show(3600000);

            mDragging = true;

            // By removing these pending progress messages we make sure
            // that a) we won't update the progress while the user adjusts
            // the seekbar and b) once the user is done dragging the thumb
            // we will post one of these messages to the queue again and
            // this ensures that there will be exactly one message queued up.
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser)
        {
            if (mPlayer == null)
            {
                return;
            }

            if (!fromuser)
            {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
            if (mCurrentTime != null)
            {
                mCurrentTime.setText(stringForTime((int) newposition));
            }
        }

        public void onStopTrackingTouch(SeekBar bar)
        {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    @Override
    public void setEnabled(boolean enabled)
    {
        if (mPauseButton != null)
        {
            mPauseButton.setEnabled(enabled);
        }
        if (mProgress != null)
        {
            mProgress.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    private static class MessageHandler extends Handler
    {
        private final WeakReference<PlaylistControllerView> mView;

        MessageHandler(PlaylistControllerView view)
        {
            mView = new WeakReference<PlaylistControllerView>(view);
        }

        @Override
        public void handleMessage(Message msg)
        {
            PlaylistControllerView view = mView.get();
            if (view == null || view.mPlayer == null)
            {
                return;
            }

            int pos;
            switch (msg.what)
            {
                case FADE_OUT:
                    view.hide();
                    break;
                case SHOW_PROGRESS:
                    pos = view.setProgress();
                    if (!view.mDragging && view.mShowing && view.mPlayer.isPlaying())
                    {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    }
}
