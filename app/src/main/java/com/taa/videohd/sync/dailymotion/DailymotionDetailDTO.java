package com.taa.videohd.sync.dailymotion;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

/**
 * Created by Trung on 11/19/2015.
 */
public class DailymotionDetailDTO
{
    private String channel;
    private String country;
    private String description;
    private String id;
    private String thumbnail_720_url;
    private String url;
    private String title;
    private Long created_time;
    private BigInteger views_total;
    @SerializedName("owner.videos_total")
    private BigInteger userVideoCount;
    private int duration;
    @SerializedName("owner.screenname")
    private String author;
    @SerializedName("owner.fans_total")
    private BigInteger subscribe;
    @SerializedName("owner.avatar_240_url")
    private String authorAvatar;
    @SerializedName("owner.description")
    private String userDescription;
    @SerializedName("owner.cover_250_url")
    private String userCoverUrl;
    @SerializedName("owner.id")
    private String userId;

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getThumbnail_720_url()
    {
        return thumbnail_720_url;
    }

    public void setThumbnail_720_url(String thumbnail_720_url)
    {
        this.thumbnail_720_url = thumbnail_720_url;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public BigInteger getViews_total()
    {
        return views_total;
    }

    public void setViews_total(BigInteger views_total)
    {
        this.views_total = views_total;
    }

    public int getDuration()
    {
        return duration;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public BigInteger getSubscribe()
    {
        return subscribe;
    }

    public void setSubscribe(BigInteger subscribe)
    {
        this.subscribe = subscribe;
    }

    public String getAuthorAvatar()
    {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar)
    {
        this.authorAvatar = authorAvatar;
    }

    public Long getCreated_time()
    {
        return created_time;
    }

    public void setCreated_time(Long created_time)
    {
        this.created_time = created_time;
    }

    public BigInteger getUserVideoCount()
    {
        return userVideoCount;
    }

    public void setUserVideoCount(BigInteger userVideoCount)
    {
        this.userVideoCount = userVideoCount;
    }

    public String getUserDescription()
    {
        return userDescription;
    }

    public void setUserDescription(String userDescription)
    {
        this.userDescription = userDescription;
    }

    public String getUserCoverUrl()
    {
        return userCoverUrl;
    }

    public void setUserCoverUrl(String userCoverUrl)
    {
        this.userCoverUrl = userCoverUrl;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
}
