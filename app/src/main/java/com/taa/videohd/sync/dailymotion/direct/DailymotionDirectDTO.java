package com.taa.videohd.sync.dailymotion.direct;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trung on 12/2/2015.
 */
public class DailymotionDirectDTO
{
    @SerializedName("metadata")
    private DailymotionDirectMetaDataDTO dailymotionDirectMetaData;

    public DailymotionDirectMetaDataDTO getDailymotionDirectMetaData()
    {
        return dailymotionDirectMetaData;
    }

    public void setDailymotionDirectMetaData(DailymotionDirectMetaDataDTO dailymotionDirectMetaData)
    {
        this.dailymotionDirectMetaData = dailymotionDirectMetaData;
    }
}
