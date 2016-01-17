package com.taa.videohd.sync.dailymotion;

import java.math.BigInteger;

/**
 * Created by trung on 12/18/2015.
 */
public class DailymotionUserDetailDTO
{
    private String id;
    private String screenname;
    private String avatar_720_url;
    private String cover_250_url;
    private BigInteger fans_total;
    private BigInteger videos_total;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getScreenname()
    {
        return screenname;
    }

    public void setScreenname(String screenname)
    {
        this.screenname = screenname;
    }

    public String getAvatar_720_url()
    {
        return avatar_720_url;
    }

    public void setAvatar_720_url(String avatar_720_url)
    {
        this.avatar_720_url = avatar_720_url;
    }

    public String getCover_250_url()
    {
        return cover_250_url;
    }

    public void setCover_250_url(String cover_250_url)
    {
        this.cover_250_url = cover_250_url;
    }

    public BigInteger getFans_total()
    {
        return fans_total;
    }

    public void setFans_total(BigInteger fans_total)
    {
        this.fans_total = fans_total;
    }

    public BigInteger getVideos_total()
    {
        return videos_total;
    }

    public void setVideos_total(BigInteger videos_total)
    {
        this.videos_total = videos_total;
    }
}
