package com.taa.videohd.sync.vimeo.response;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoResponsePictureSizeDTO
{
    private Integer width;
    private Integer height;
    private String link;

    public Integer getWidth()
    {
        return width;
    }

    public void setWidth(Integer width)
    {
        this.width = width;
    }

    public Integer getHeight()
    {
        return height;
    }

    public void setHeight(Integer height)
    {
        this.height = height;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }
}
