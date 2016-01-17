package com.taa.videohd.sync.vimeo.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoResponseUserDetailDTO
{
    private String uri;
    private String name;
    private String bio;
    private String location;
    @SerializedName("metadata")
    private VimeoResponseMetaDataDTO vimeoResponseMetaDataDTO;
    @SerializedName("pictures")
    private VimeoResponsePicturesDTO vimeoResponsePicturesDTO;
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public VimeoResponseMetaDataDTO getVimeoResponseMetaDataDTO()
    {
        return vimeoResponseMetaDataDTO;
    }

    public void setVimeoResponseMetaDataDTO(VimeoResponseMetaDataDTO vimeoResponseMetaDataDTO)
    {
        this.vimeoResponseMetaDataDTO = vimeoResponseMetaDataDTO;
    }

    public VimeoResponsePicturesDTO getVimeoResponsePicturesDTO()
    {
        return vimeoResponsePicturesDTO;
    }

    public void setVimeoResponsePicturesDTO(VimeoResponsePicturesDTO vimeoResponsePicturesDTO)
    {
        this.vimeoResponsePicturesDTO = vimeoResponsePicturesDTO;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getBio()
    {
        return bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }
}
