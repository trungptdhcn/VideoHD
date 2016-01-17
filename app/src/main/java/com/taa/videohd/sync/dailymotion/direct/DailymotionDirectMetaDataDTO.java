package com.taa.videohd.sync.dailymotion.direct;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trung on 12/2/2015.
 */
public class DailymotionDirectMetaDataDTO
{
    private Long duration;
    private String id;
    private String title;
    private String poster_url;
    @SerializedName("qualities")
    private DailymotionDirectQuatitiesDTO dailymotionDirectQuatities;

    public Long getDuration()
    {
        return duration;
    }

    public void setDuration(Long duration)
    {
        this.duration = duration;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getPoster_url()
    {
        return poster_url;
    }

    public void setPoster_url(String poster_url)
    {
        this.poster_url = poster_url;
    }

    public DailymotionDirectQuatitiesDTO getDailymotionDirectQuatities()
    {
        return dailymotionDirectQuatities;
    }

    public void setDailymotionDirectQuatities(DailymotionDirectQuatitiesDTO dailymotionDirectQuatities)
    {
        this.dailymotionDirectQuatities = dailymotionDirectQuatities;
    }
}
