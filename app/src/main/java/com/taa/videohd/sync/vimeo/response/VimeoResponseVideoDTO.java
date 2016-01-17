package com.taa.videohd.sync.vimeo.response;

import java.math.BigInteger;

/**
 * Created by Trung on 12/4/2015.
 */
public class VimeoResponseVideoDTO
{
    private String uri;
    private BigInteger total;

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
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
