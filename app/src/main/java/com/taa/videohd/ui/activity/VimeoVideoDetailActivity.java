package com.taa.videohd.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.drm.UnsupportedDrmException;
import com.google.android.exoplayer.util.Util;
import com.taa.videohd.asyntask.AsyncDailymotionParser;
import com.taa.videohd.base.StringUtils;
import com.taa.videohd.sync.RestfulService;
import com.taa.videohd.sync.dailymotion.direct.DailymotionDirectMetaDataDTO;
import com.taa.videohd.sync.dailymotion.direct.DailymotionDirectQualityDTO;
import com.taa.videohd.sync.dailymotion.direct.DailymotionDirectQuatitiesDTO;
import com.taa.videohd.sync.vimeo.direct.VimeoDirectDTO;
import com.taa.videohd.sync.vimeo.direct.VimeoDirectProgressiveDTO;
import com.taa.videohd.sync.vimeo.direct.VimeoDirectStreamURLDTO;
import com.taa.videohd.ui.customview.player.*;
import com.taa.videohd.ui.fragment.VideoRelatedFragment;
import com.taa.videohd.ui.listener.FullScreenListener;
import com.taa.videohd.ui.model.DirectLink;
import com.taa.videohd.ui.model.VideoModel;
import com.taa.videohd.utils.Constant;
import com.trungpt.videodownloadmaster.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VimeoVideoDetailActivity extends FragmentActivity implements SurfaceHolder.Callback
        , FullScreenListener, DemoPlayer.Listener
{

    @Bind(R.id.progressPreparing)
    ProgressBar progressPreparing;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.layout_video_related)
    FrameLayout layoutVideoRelated;
    @Bind(R.id.controller)
    FrameLayout frController;
    @Bind(R.id.video_view)
    SurfaceView videoSurface;
    @Bind(R.id.videoSurfaceContainer)
    RelativeLayout root;

    VideoModel video;
    private DemoPlayer player;
    private boolean isFullScreen = false;
    private long playerPosition;
    private boolean playerNeedsPrepare;
    Uri urlVideo;
    int videoType;
    VideoControllerView controller;
    public static final int TIME_OUT_CONTROLLER = 3000;
    private boolean enableBackgroundAudio;
    private Constant.HOST_NAME host_name;
    Map<String, DailymotionDirectQualityDTO> hashMapUrl = new HashMap<>();
    ProgressDialog barProgressDialog;
    List<DirectLink> directLinksDownload = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vimeo_video_detail);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        Intent i = getIntent();
        video = i.getParcelableExtra("video");
        host_name = (Constant.HOST_NAME) i.getSerializableExtra("host_name");
        controller = new VideoControllerView(this);
        controller.setAnchorView(frController);
        root.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    toggleControlsVisibility();
                }
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    view.performClick();
                }
                return true;
            }
        });
        setFullScreen();
//        VideoRelatedFragment relatedFragment = new VideoRelatedFragment();
//        Bundle args = new Bundle();
//        args.putParcelable("video", video);
//        args.putSerializable("host_name", host_name);
//        relatedFragment.setArguments(args);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.layout_video_related, relatedFragment);
//        transaction.commit();

    }

    public void setFullScreen()
    {
        if (isFullScreen())
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoSurface.getLayoutParams();
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) frController.getLayoutParams();
            params.width = width;
            params.height = height;// -80 for android controls
            params.setMargins(0, 0, 0, 0);
            params2.width = width;
            params2.height = height;// -80 for android controls
            params2.setMargins(0, 0, 0, 0);
            controller.updateFullScreen();
            isFullScreen = false;
        }
        else
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoSurface.getLayoutParams();
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) frController.getLayoutParams();
            params.width = width;
            params.height = (int) getResources().getDimension(R.dimen.size_of_video);
            params.setMargins(0, 0, 0, 0);
            params2.width = width;
            params2.height = (int) getResources().getDimension(R.dimen.size_of_video);
            params2.setMargins(0, 0, 0, 0);
            controller.updateFullScreen();
            isFullScreen = true;
        }
    }

    private void toggleControlsVisibility()
    {
        if (controller != null)
        {
            if (controller.isShowing())
            {
                controller.hide();
            }
            else
            {
                controller.show(TIME_OUT_CONTROLLER);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (player != null)
        {
            player.setSurface(holder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (player != null)
        {
            player.blockingClearSurface();
        }
    }

    @Override
    public void toggleFullScreen()
    {
        setFullScreen();
    }

    @Override
    public boolean isFullScreen()
    {
        return isFullScreen;
    }

    @Override
    public void onError(Exception e)
    {
        if (e instanceof UnsupportedDrmException)
        {
            // Special case DRM failures.
            UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
            int stringId = Util.SDK_INT < 18 ? R.string.drm_error_not_supported
                    : unsupportedDrmException.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                    ? R.string.drm_error_unsupported_scheme : R.string.drm_error_unknown;
            Toast.makeText(getApplicationContext(), stringId, Toast.LENGTH_LONG).show();
        }
        playerNeedsPrepare = true;
        controller.show(TIME_OUT_CONTROLLER);
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio)
    {

    }

    @Override
    public void onNewIntent(Intent intent)
    {
        releasePlayer();
        playerPosition = 0;
        setIntent(intent);
    }

    private void releasePlayer()
    {
        if (player != null)
        {
            playerPosition = player.getCurrentPosition();
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Intent i = getIntent();
        video = i.getParcelableExtra("video");
        if (video != null)
        {
            switch (host_name)
            {
                case VIMEO:
                    getDirectLinkVimeo();
                    break;
                case DAILYMOTION:
                    getDirectLinkDailymotion();
                    break;
            }

        }
    }

    private void getDirectLinkDailymotion()
    {
        super.onResume();
        progressPreparing.getIndeterminateDrawable()
                .setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        final ProgressDialog progressDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_DARK);
        progressDialog.setMessage("Get data...!");
        Intent i = getIntent();
        AsyncDailymotionParser asyncDailymotionParser = new AsyncDailymotionParser(video.getUrl())
        {
            @Override
            public void prepare()
            {
                progressDialog.show();
            }

            @Override
            public void conplete(Object o)
            {
                DailymotionDirectMetaDataDTO dailymotionDirectMetaDataDTO = (DailymotionDirectMetaDataDTO) o;
                if (dailymotionDirectMetaDataDTO != null)
                {
                    DailymotionDirectQuatitiesDTO dailymotionDirectQuatitiesDTO = dailymotionDirectMetaDataDTO.getDailymotionDirectQuatities();
                    if (null != dailymotionDirectQuatitiesDTO)
                    {
                        List<DailymotionDirectQualityDTO> _auto = dailymotionDirectQuatitiesDTO.get_auto();
                        List<DailymotionDirectQualityDTO> _240 = dailymotionDirectQuatitiesDTO.get_240();
                        List<DailymotionDirectQualityDTO> _380 = dailymotionDirectQuatitiesDTO.get_380();
                        List<DailymotionDirectQualityDTO> _480 = dailymotionDirectQuatitiesDTO.get_480();
                        List<DailymotionDirectQualityDTO> _720 = dailymotionDirectQuatitiesDTO.get_720();
                        if (_auto != null && _auto.size() > 0 && StringUtils.isNotEmpty(_auto.get(0).getUrl()))
                        {
                            hashMapUrl.put("_auto", _auto.get(0));
                        }
                        if (_240 != null && _240.size() > 0 && StringUtils.isNotEmpty(_auto.get(0).getUrl()))
                        {
                            hashMapUrl.put("_240", _240.get(0));
                            DirectLink directLink = new DirectLink();
                            directLink.setName(video.getName());
                            directLink.setUri(_240.get(0).getUrl());
                            directLink.setType(".mp4");
                            directLink.setQuality("240p");
                            directLink.setTypePlay(4);
                            directLink.setUrlThumb(video.getUrlThumbnail());
                            directLinksDownload.add(directLink);
                        }
                        if (_380 != null && _380.size() > 0 && StringUtils.isNotEmpty(_auto.get(0).getUrl()))
                        {
                            hashMapUrl.put("_380", _380.get(0));
                            DirectLink directLink = new DirectLink();
                            directLink.setName(video.getName());
                            directLink.setUri(_380.get(0).getUrl());
                            directLink.setType(".mp4");
                            directLink.setQuality("380p");
                            directLink.setTypePlay(4);
                            directLink.setUrlThumb(video.getUrlThumbnail());
                            directLinksDownload.add(directLink);
                        }
                        if (_480 != null && _480.size() > 0 && StringUtils.isNotEmpty(_auto.get(0).getUrl()))
                        {
                            hashMapUrl.put("_480", _480.get(0));
                            DirectLink directLink = new DirectLink();
                            directLink.setName(video.getName());
                            directLink.setUri(_480.get(0).getUrl());
                            directLink.setType(".mp4");
                            directLink.setQuality("480p");
                            directLink.setTypePlay(4);
                            directLink.setUrlThumb(video.getUrlThumbnail());
                            directLinksDownload.add(directLink);
                        }
                        if (_720 != null && _720.size() > 0 && StringUtils.isNotEmpty(_auto.get(0).getUrl()))
                        {
                            hashMapUrl.put("_720", _720.get(0));
                            DirectLink directLink = new DirectLink();
                            directLink.setName(video.getName());
                            directLink.setUri(_720.get(0).getUrl());
                            directLink.setType(".mp4");
                            directLink.setQuality("720p");
                            directLink.setTypePlay(4);
                            directLink.setUrlThumb(video.getUrlThumbnail());
                            directLinksDownload.add(directLink);
                        }
                    }
                    if (hashMapUrl.get("_auto") != null)
                    {
                        videoSurface.getHolder().addCallback(VimeoVideoDetailActivity.this);
                        urlVideo = Uri.parse(hashMapUrl.get("_auto").getUrl());
                        videoType = 3;
                    }
                    else
                    {
                        videoType = 4;
                        if (hashMapUrl.get("_720") != null)
                        {
                            urlVideo = Uri.parse(hashMapUrl.get("_720").getUrl());
                            preparePlayer(true);
                            player.setBackgrounded(false);
                            return;
                        }
                        if (hashMapUrl.get("_480") != null)
                        {
                            urlVideo = Uri.parse(hashMapUrl.get("_480").getUrl());
                            preparePlayer(true);
                            player.setBackgrounded(false);
                            return;
                        }
                        if (hashMapUrl.get("_380") != null)
                        {
                            urlVideo = Uri.parse(hashMapUrl.get("_380").getUrl());
                            preparePlayer(true);
                            player.setBackgrounded(false);
                            return;
                        }
                        if (hashMapUrl.get("_240") != null)
                        {
                            urlVideo = Uri.parse(hashMapUrl.get("_240").getUrl());
                            preparePlayer(true);
                            player.setBackgrounded(false);
                            return;
                        }
                    }
                }
                video.setDirectLinks(directLinksDownload);
                VideoRelatedFragment relatedFragment = new VideoRelatedFragment();
                Bundle args = new Bundle();
                args.putParcelable("video", video);
                args.putSerializable("host_name", host_name);
                relatedFragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.layout_video_related, relatedFragment);
                transaction.commit();

                preparePlayer(true);
                player.setBackgrounded(false);
                progressDialog.dismiss();
            }
        };
        if (null == urlVideo)
        {
            asyncDailymotionParser.execute();
        }

    }


    private void getDirectLinkVimeo()
    {
        RestfulService.getInstance(Constant.HOST_NAME.VIMEO_PLAYER).getDirectLink(video.getId(), new Callback<VimeoDirectDTO>()
        {
            @Override
            public void success(VimeoDirectDTO vimeoDirectDTO, Response response)
            {
                List<VimeoDirectProgressiveDTO> vimeoDirectProgressiveDTOs = vimeoDirectDTO.getVimeoDirectRequestDTO()
                        .getVimeoDirectFileDTO().getVimeoDirectProgressiveDTOs();
                if (vimeoDirectProgressiveDTOs != null && vimeoDirectProgressiveDTOs.size() > 0)
                {
                    for (VimeoDirectProgressiveDTO vimeoDirectProgressiveDTO : vimeoDirectProgressiveDTOs)
                    {
                        DirectLink directLink = new DirectLink();
                        directLink.setQuality(vimeoDirectProgressiveDTO.getQuality());
                        directLink.setUri(vimeoDirectProgressiveDTO.getUrl());
                        directLink.setType(Constant.VIDEO_TYPE_MP4);
                        directLink.setTypePlay(vimeoDirectProgressiveDTO.getType());
                        directLink.setName(video.getName());
                        directLink.setUrlThumb(video.getUrlThumbnail());
                        directLinksDownload.add(directLink);
                    }
                }
                VimeoDirectStreamURLDTO vimeoDirectStreamURLDTO = vimeoDirectDTO.getVimeoDirectRequestDTO().getVimeoDirectFileDTO().getVimeoDirectStreamURLDTO();
                if (vimeoDirectStreamURLDTO != null && StringUtils.isNotEmpty(vimeoDirectStreamURLDTO.getUrl()))
                {
                    videoSurface.getHolder().addCallback(VimeoVideoDetailActivity.this);
                    String hls_url = vimeoDirectDTO.getVimeoDirectRequestDTO()
                            .getVimeoDirectFileDTO().getVimeoDirectStreamURLDTO().getUrl();
                    if (StringUtils.isNotEmpty(hls_url))
                    {
                        urlVideo = Uri.parse(hls_url);
                        videoType = vimeoDirectDTO.getVimeoDirectRequestDTO()
                                .getVimeoDirectFileDTO().getVimeoDirectStreamURLDTO().getType();
                    }

                }
                else
                {
                    if (directLinksDownload != null && directLinksDownload.size() > 0)
                    {
                        urlVideo = Uri.parse(directLinksDownload.get(0).getUri());
                        videoType = directLinksDownload.get(0).getTypePlay();
                    }
                }
                video.setDirectLinks(directLinksDownload);
                VideoRelatedFragment relatedFragment = new VideoRelatedFragment();
                Bundle args = new Bundle();
                args.putParcelable("video", video);
                args.putSerializable("host_name", host_name);
                relatedFragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.layout_video_related, relatedFragment);
                transaction.commit();

                if (player == null)
                {
                    preparePlayer(true);
                }
                else
                {
                    player.setBackgrounded(false);
                }
            }

            @Override
            public void failure(RetrofitError error)
            {

            }
        });
    }

    private void preparePlayer(boolean playWhenReady)
    {
        if (player == null)
        {
            player = new DemoPlayer(getRendererBuilder(urlVideo, videoType));
            player.addListener(this);
            player.seekTo(playerPosition);
            playerNeedsPrepare = true;
            CustomMediaControl playerControl = player.getPlayerControl();
            playerControl.setFullScreenListener(this);
            controller.setMediaPlayer(playerControl);
            controller.setEnabled(true);
        }
        if (playerNeedsPrepare)
        {
            player.prepare();
            playerNeedsPrepare = false;
        }
        controller.show();
        player.setSurface(videoSurface.getHolder().getSurface());
        player.setPlayWhenReady(playWhenReady);
    }

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState)
    {
        if (playbackState == ExoPlayer.STATE_ENDED)
        {
            controller.show(TIME_OUT_CONTROLLER);
        }
        String text = "playWhenReady=" + playWhenReady + ", playbackState=";
        switch (playbackState)
        {
            case ExoPlayer.STATE_BUFFERING:
                progressPreparing.setVisibility(View.VISIBLE);
                controller.show(5000);
                break;
            case ExoPlayer.STATE_ENDED:
                progressPreparing.setVisibility(View.GONE);
                controller.show(500000);
                break;
            case ExoPlayer.STATE_IDLE:
                progressPreparing.setVisibility(View.GONE);
                controller.show(500000);
                break;
            case ExoPlayer.STATE_PREPARING:
                progressPreparing.setVisibility(View.VISIBLE);
                controller.show(8000);
                break;
            case ExoPlayer.STATE_READY:
                progressPreparing.setVisibility(View.GONE);
                controller.show(3000);
                break;
            default:
                break;
        }
    }

    private DemoPlayer.RendererBuilder getRendererBuilder(Uri uri, int type)
    {
        String userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
        switch (type)
        {
            case 1:
                return new SmoothStreamingRendererBuilder(this, userAgent, uri.toString(),
                        new SmoothStreamingTestMediaDrmCallback());
//            case 2:
//                return new DashRendererBuilder(this, userAgent, uri.toString(),
//                        new WidevineTestMediaDrmCallback(contentId));
            case 3:
                return new HlsRendererBuilder(this, userAgent, uri.toString());
            case 4:
                return new ExtractorRendererBuilder(this, userAgent, uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (!enableBackgroundAudio)
        {
            releasePlayer();
        }
        else
        {
            player.setBackgrounded(true);
        }
    }

}
