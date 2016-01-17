package com.taa.videohd.sync.vimeo.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoResponsePicturesDTO
{
    private String uri;
    private Boolean active;
    private String type;
    @SerializedName("sizes")
    private List<VimeoResponsePictureSizeDTO> vimeoPicturesSizeDTO;

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public List<VimeoResponsePictureSizeDTO> getVimeoPicturesSizeDTO()
    {
        return vimeoPicturesSizeDTO;
    }

    public void setVimeoPicturesSizeDTO(List<VimeoResponsePictureSizeDTO> vimeoPicturesSizeDTO)
    {
        this.vimeoPicturesSizeDTO = vimeoPicturesSizeDTO;
    }
}
