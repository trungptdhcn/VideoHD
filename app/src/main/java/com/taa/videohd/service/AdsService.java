package com.taa.videohd.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.trungpt.videodownloadmaster.R;

/**
 * Created by trung on 12/29/2015.
 */
public class AdsService extends Service
{
    InterstitialAd mInterstitialAd;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mInterstitialAd= new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getString(R.string.download_plus_integrate));
        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdClosed()
            {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        mInterstitialAd.show();
        Toast.makeText(this, "Trung dai ca", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    private void requestNewInterstitial()
    {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("85875FC3563C88A48AD872E6EF2EB7")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }
}
