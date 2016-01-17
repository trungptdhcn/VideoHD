package com.taa.videohd.asyntask;

import android.os.AsyncTask;
import com.taa.videohd.asyntask.listener.YoutubeDirectListener;
import com.taa.videohd.download.YouTubeParser;
import com.taa.videohd.download.YoutubeInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by trung on 12/30/2015.
 */
public class YoutubeGetDirectLink extends AsyncTask<Void, Void, Object>
{
    YoutubeDirectListener listener;
    String url;

    public YoutubeGetDirectLink(String url)
    {
        this.url = url;
    }

    @Override
    protected Object doInBackground(Void... params)
    {
        List<YouTubeParser.VideoDownload> list = null;
        try
        {
            YoutubeInfo info = new YoutubeInfo(new URL(url));
            YouTubeParser youTubeParser = new YouTubeParser();
            list = youTubeParser.extractLinks(info);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    public void setListener(YoutubeDirectListener listener)
    {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(Object o)
    {
        super.onPostExecute(o);
        listener.completeDirect(o);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        listener.prepareDirect();
    }
}
