package com.taa.videohd.sync.dailymotion;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

/**
 * Created by trung on 12/9/2015.
 */
public class DailymotionPlayistDetailDTO
{
    private String id;
    private String description;
    private String name;
    @SerializedName("owner.avatar_720_url")
    private String avatarUrl;
    @SerializedName("owner.fans_total")
    private BigInteger follows;
    @SerializedName("thumbnail_720_url")
    private String coverUrl;
    @SerializedName("videos_total")
    private BigInteger videos;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAvatarUrl()
    {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl)
    {
        this.avatarUrl = avatarUrl;
    }

    public BigInteger getFollows()
    {
        return follows;
    }

    public void setFollows(BigInteger follows)
    {
        this.follows = follows;
    }

    public String getCoverUrl()
    {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl)
    {
        this.coverUrl = coverUrl;
    }

    public BigInteger getVideos()
    {
        return videos;
    }

    public void setVideos(BigInteger videos)
    {
        this.videos = videos;
    }
}
