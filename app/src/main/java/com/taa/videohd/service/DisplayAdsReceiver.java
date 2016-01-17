package com.taa.videohd.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by trung on 12/29/2015.
 */
public class DisplayAdsReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent service = new Intent(context, AdsService.class);
        context.startService(service);
    }
}
