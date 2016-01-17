package com.taa.videohd.download;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import com.taa.videohd.event.DownloadEvent;
import com.taa.videohd.ui.model.DirectLink;
import com.taa.videohd.utils.Utils;
import de.greenrobot.event.EventBus;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by trung on 12/22/2015.
 */
public class DownloadService extends Service
{
    private static final String TAG = DownloadService.class.getSimpleName();
    public static final String PENDING_RESULT_EXTRA = "direct_link";
    private NotificationManager notify;
    public static boolean serviceState = false;
    public DirectLink directLink;

    @Override
    public void onCreate()
    {
        serviceState = true;
        notify = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        EventBus.getDefault().register(this);
        Utils.loadFullScreenAds(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {

        Log.d("SERVICE-DESTROY", "DESTORY");
        serviceState = false;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void onEventBackgroundThread(DownloadEvent event)
    {
        DirectLink directLink = event.getDirectLink();
        if (directLink != null)
        {
            try
            {
                DownloadManager.getInstance().createDownload(new URL(directLink.getUri()),
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/", directLink.getName(), directLink.getType(), directLink);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        }
    }
}
