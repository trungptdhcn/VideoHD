package com.taa.videohd.sync.vimeo.direct;

import java.math.BigInteger;

/**
 * Created by Trung on 11/27/2015.
 */
public class VimeoDirectProgressiveDTO
{
    private BigInteger profile;
    private BigInteger width;
    private String mime;
    private BigInteger fps;
    private String url;
    private String cdn;
    private String quality;
    private String id;
    private String origin;
    private BigInteger height;
    private int type = 4;

    public BigInteger getProfile()
    {
        return profile;
    }

    public void setProfile(BigInteger profile)
    {
        this.profile = profile;
    }

    public BigInteger getWidth()
    {
        return width;
    }

    public void setWidth(BigInteger width)
    {
        this.width = width;
    }

    public String getMime()
    {
        return mime;
    }

    public void setMime(String mime)
    {
        this.mime = mime;
    }

    public BigInteger getFps()
    {
        return fps;
    }

    public void setFps(BigInteger fps)
    {
        this.fps = fps;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getCdn()
    {
        return cdn;
    }

    public void setCdn(String cdn)
    {
        this.cdn = cdn;
    }

    public String getQuality()
    {
        return quality;
    }

    public void setQuality(String quality)
    {
        this.quality = quality;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    public BigInteger getHeight()
    {
        return height;
    }

    public void setHeight(BigInteger height)
    {
        this.height = height;
    }

    public int getType()
    {
        return type;
    }

}
