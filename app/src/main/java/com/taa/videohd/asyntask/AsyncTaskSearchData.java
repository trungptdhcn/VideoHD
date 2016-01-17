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
 * Created by Trung on 11/25/2015.
 */
public class AsyncTaskSearchData extends AsyncTask<Void, Void, Object>
{
    AsyncTaskListener listener;
    private Constant.HOST_NAME host_name;
    YoutubeConnector ytbConnect;
    VimeoConnector vimeoConnect;
    DailymotionConnector dailymotionConnector;
    private RequestDTO requestDTO;

    public AsyncTaskSearchData(Context context, Constant.HOST_NAME host_name, RequestDTO requestDTO)
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
                o = ytbConnect.search(requestDTO);
                break;
            case YOUTUBE_RELATED:
                o = ytbConnect.getRelatedVideo(requestDTO);
                break;
            case YOUTUBE_VIDEO_BY_CHANNEL_ID:
                o = ytbConnect.getVideosByUser(requestDTO);
                break;
            case YOUTUBE_VIDEO_BY_PLAYLIST_ID:
                o = ytbConnect.getVideoByPlaylistId(requestDTO);
                break;
            case VIMEO:
                o = vimeoConnect.search(requestDTO);
                break;
            case DAILYMOTION:
                o = dailymotionConnector.search(requestDTO);
        }
        return o;
    }

    @Override
    protected void onPostExecute(Object o)
    {
        super.onPostExecute(o);
        listener.complete(o);
    }


    public void setListener(AsyncTaskListener listener)
    {
        this.listener = listener;
    }

}
