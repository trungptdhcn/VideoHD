package com.taa.videohd.asyntask;

import android.content.Context;
import android.os.AsyncTask;
import com.taa.videohd.asyntask.listener.AsyncTaskListener;
import com.taa.videohd.sync.RequestDTO;
import com.taa.videohd.sync.dailymotion.DailymotionConnector;
import com.taa.videohd.sync.vimeo.VimeoConnector;
import com.taa.videohd.sync.youtube.YoutubeConnector;
import com.taa.videohd.utils.Constant;

/**
 * Created by Trung on 12/3/2015.
 */
public class AsyncTaskMostPopular extends AsyncTask<Void, Void, Object>
{
    AsyncTaskListener listener;
    private Constant.HOST_NAME host_name;
    YoutubeConnector ytbConnect;
    VimeoConnector vimeoConnect;
    DailymotionConnector dailymotionConnector;
    private RequestDTO requestDTO;

    public AsyncTaskMostPopular(Context context, Constant.HOST_NAME host_name, RequestDTO requestDTO)
    {
        this.host_name = host_name;
        this.requestDTO = requestDTO;
        ytbConnect = new YoutubeConnector(context);
        vimeoConnect = new VimeoConnector();
        dailymotionConnector = new DailymotionConnector();
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        listener.prepare();
    }

    @Override
    protected Object doInBackground(Void... params)
    {
        Object o = null;
        switch (host_name)
        {
            case YOUTUBE:
                o = ytbConnect.mostPopulars(requestDTO);
                break;
            case VIMEO:
                o = vimeoConnect.getVideoWithCategory(requestDTO);
                break;
            case VIMEO_RELATED:
                o = vimeoConnect.getVideoRelated(requestDTO);
                break;
            case VIMEO_VIDEO_BY_USER_ID:
                o = vimeoConnect.getVideoByUserId(requestDTO);
                break;
            case VIMEO_VIDEO_WITH_CHANNEL_ID:
                o = vimeoConnect.getVideoByChannelId(requestDTO);
                break;
            case VIMEO_DIRECT_LINK:
                o = vimeoConnect.getDirectLink(requestDTO);
                break;
            case VIMEO_VIDEO_WITH_CATEGORY:
                o = vimeoConnect.getVideoOfCategory(requestDTO);
                break;
            case DAILYMOTION:
                o = dailymotionConnector.mostPopular(requestDTO);
                break;
            case DAILYMOTION_RELATED:
                o = dailymotionConnector.getVideoRelated(requestDTO);
                break;
            case DAILYMOTION_VIDEO_BY_USER_ID:
                o = dailymotionConnector.getVideosByUser(requestDTO);
                break;
            case DAILYMOTION_VIDEO_BY_PLAYLIST_ID:
                o = dailymotionConnector.getVideosByPlaylist(requestDTO);
                break;
        }
        return o;
    }

    @Override
    protected void onPostExecute(Object o)
    {
        super.onPostExecute(o);
        listener.complete(o);
    }

    public AsyncTaskListener getListener()
    {
        return listener;

    }

    public void setListener(AsyncTaskListener listener)
    {
        this.listener = listener;
    }
}
