package com.taa.videohd.sync.vimeo.direct;

/**
 * Created by Trung on 11/27/2015.
 */
public class VimeoDirectStreamURLDTO
{
    private String url;
    private String origin;
    private String cdn;
    private int type = 3;

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    public String getCdn()
    {
        return cdn;
    }

    public void setCdn(String cdn)
    {
        this.cdn = cdn;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
