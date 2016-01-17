package com.taa.videohd.sync.vimeo.response;

import java.math.BigInteger;

/**
 * Created by Trung on 11/27/2015.
 */
public class VimeoResponseFollowingDTO
{
    private String url;
    private BigInteger total;

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public BigInteger getTotal()
    {
        return total;
    }

    public void setTotal(BigInteger total)
    {
        this.total = total;
    }
}
