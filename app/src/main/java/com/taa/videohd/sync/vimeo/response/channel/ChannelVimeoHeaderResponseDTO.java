package com.taa.videohd.sync.vimeo.response.channel;

import com.taa.videohd.sync.vimeo.response.VimeoResponsePictureSizeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 12/8/2015.
 */
public class ChannelVimeoHeaderResponseDTO
{
    private String uri;
    private String type;
    private List<VimeoResponsePictureSizeDTO> sizes = new ArrayList<>();

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public List<VimeoResponsePictureSizeDTO> getSizes()
    {
        return sizes;
    }

    public void setSizes(List<VimeoResponsePictureSizeDTO> sizes)
    {
        this.sizes = sizes;
    }
}
