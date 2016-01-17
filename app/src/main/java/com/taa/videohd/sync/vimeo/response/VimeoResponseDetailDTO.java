package com.taa.videohd.sync.vimeo.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoResponseDetailDTO
{
    private String uri;
    private String name;
    private String description;
    private String link;
    private int duration;
    @SerializedName("embed")
    private VimeoResponseEmbedDTO embedDTO;
    private String created_time;
    private String modified_time;
    private String license;
    @SerializedName("privacy")
    private VimeoResponsePrivacyDTO vimeoPrivacyDTO;
    @SerializedName("pictures")
    private VimeoResponsePicturesDTO vimeoPicturesDTO;
    @SerializedName("stats")
    private VimeoResponseStatsDTO vimeoResponseStatsDTO;
    @SerializedName("metadata")
    private VimeoResponseMetaDataDTO vimeoResponseMetadataDTO;
    @SerializedName("user")
    private VimeoResponseUserDetailDTO vimeoResponseUserDetailDTO;


    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public int getDuration()
    {
        return duration;
    }

    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    public VimeoResponseEmbedDTO getEmbedDTO()
    {
        return embedDTO;
    }

    public void setEmbedDTO(VimeoResponseEmbedDTO embedDTO)
    {
        this.embedDTO = embedDTO;
    }

    public String getCreated_time()
    {
        return created_time;
    }

    public void setCreated_time(String created_time)
    {
        this.created_time = created_time;
    }

    public String getModified_time()
    {
        return modified_time;
    }

    public void setModified_time(String modified_time)
    {
        this.modified_time = modified_time;
    }

    public String getLicense()
    {
        return license;
    }

    public void setLicense(String license)
    {
        this.license = license;
    }

    public VimeoResponsePrivacyDTO getVimeoPrivacyDTO()
    {
        return vimeoPrivacyDTO;
    }

    public void setVimeoPrivacyDTO(VimeoResponsePrivacyDTO vimeoPrivacyDTO)
    {
        this.vimeoPrivacyDTO = vimeoPrivacyDTO;
    }

    public VimeoResponsePicturesDTO getVimeoPicturesDTO()
    {
        return vimeoPicturesDTO;
    }

    public void setVimeoPicturesDTO(VimeoResponsePicturesDTO vimeoPicturesDTO)
    {
        this.vimeoPicturesDTO = vimeoPicturesDTO;
    }

    public VimeoResponseStatsDTO getVimeoResponseStatsDTO()
    {
        return vimeoResponseStatsDTO;
    }

    public void setVimeoResponseStatsDTO(VimeoResponseStatsDTO vimeoResponseStatsDTO)
    {
        this.vimeoResponseStatsDTO = vimeoResponseStatsDTO;
    }

    public VimeoResponseMetaDataDTO getVimeoResponseMetadataDTO()
    {
        return vimeoResponseMetadataDTO;
    }

    public void setVimeoResponseMetadataDTO(VimeoResponseMetaDataDTO vimeoResponseMetadataDTO)
    {
        this.vimeoResponseMetadataDTO = vimeoResponseMetadataDTO;
    }

    public VimeoResponseUserDetailDTO getVimeoResponseUserDetailDTO()
    {
        return vimeoResponseUserDetailDTO;
    }

    public void setVimeoResponseUserDetailDTO(VimeoResponseUserDetailDTO vimeoResponseUserDetailDTO)
    {
        this.vimeoResponseUserDetailDTO = vimeoResponseUserDetailDTO;
    }
}
