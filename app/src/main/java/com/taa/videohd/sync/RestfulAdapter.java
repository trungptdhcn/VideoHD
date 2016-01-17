package com.taa.videohd.sync;

import com.squareup.okhttp.OkHttpClient;
import com.taa.videohd.utils.Constant;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by Trung on 7/22/2015.
 */
public class RestfulAdapter
{
    private static RestAdapter restAdapter;

    public static RestAdapter getRestAdapter(Constant.HOST_NAME host_name)
    {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(6000, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(6000, TimeUnit.SECONDS);
        switch (host_name)
        {
            case VIMEO:
                restAdapter = new RestAdapter.Builder()
                        .setEndpoint(Constant.VIMEO_BASE_URL)
                        .setClient(new OkClient(okHttpClient))
                        .build();
                break;
            case VIMEO_PLAYER:
                restAdapter = new RestAdapter.Builder()
                        .setEndpoint(Constant.VIMEO_PLAYER_BASE_URL)
                        .setClient(new OkClient(okHttpClient))
                        .build();
                break;
            case DAILYMOTION:
                restAdapter = new RestAdapter.Builder()
                        .setEndpoint(Constant.DAILYMOTION_BASE_URL)
                        .setClient(new OkClient(okHttpClient))
                        .build();
                break;
            case FACEBOOK:
                break;
            default:
        }
        return restAdapter;
    }
}
