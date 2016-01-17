package com.taa.videohd.utils;

import android.content.Context;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.trungpt.videodownloadmaster.R;

/**
 * Created by trung on 12/11/2015.
 */
public class Utils
{
    private static AdRequest adRequest = new AdRequest.Builder()
//            .addTestDevice("85875FC3563C88A48AD872E6EF2EB7")
            .build();
    public static String calculateTime(long before, long after)
    {
        long time = after - before;
        long hours = time / (1000 * 60 * 60);
        long day = hours / 24;
        long week = day / 7;
        long month = day / 30;
        long year = day / 365;
        if (hours < 24)
        {
            if (hours == 0)
            {
                return "one hours ago";
            }
            else
            {
                return hours + " hours ago";
            }
        }
        else if (day > 0 && week < 1)
        {
            return day + " days ago";
        }
        if (week >= 1 && month < 1)
        {
            return week + " weeks ago";
        }
//        else if (day > 7 && day < 30)
//        {
//            return week + " weeks ago";
//        }
//        else if (day==30)
//        {
//            return "a months ago";
//        }
        else if (month >= 1 && year < 1)
        {
            return month + " months ago";
        }
        else
        {
            return year + " years ago";
        }
    }

    public static void loadBannerAds(AdView adView)
    {
        adView.loadAd(adRequest);
    }

    public static void loadFullScreenAds(Context context)
    {
        final InterstitialAd mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getString(R.string.download_plus_integrate));
        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdClosed()
            {
                mInterstitialAd.loadAd(adRequest);
            }
        });
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.show();
    }
}
