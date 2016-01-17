package com.taa.videohd.asyntask;

import android.os.AsyncTask;
import com.taa.videohd.sync.dailymotion.direct.DailymotionParser;

import java.io.IOException;

/**
 * Created by Trung on 12/2/2015.
 */
public abstract class AsyncDailymotionParser extends AsyncTask<Void, Void, Object>
{
    protected AsyncDailymotionParser(String url)
    {
        this.url = url;
    }

    String url;

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        prepare();
    }

    @Override
    protected void onPostExecute(Object o)
    {
        super.onPostExecute(o);
        conplete(o);
    }

    @Override
    protected Object doInBackground(Void... params)
    {
        try
        {
            return DailymotionParser.extractHostDirect(url);
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public abstract void prepare();

    public abstract void conplete(Object o);

}
