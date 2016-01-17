package com.taa.videohd.sync.vimeo.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoResponseMetaDataDTO
{
    @SerializedName("connections")
    private VimeoResponseMetaDataConnectionsDTO vimeoResponseMetaDataConnectionsDTO;
    public VimeoResponseMetaDataConnectionsDTO getVimeoResponseMetaDataConnectionsDTO()
    {
        return vimeoResponseMetaDataConnectionsDTO;
    }

    public void setVimeoResponseMetaDataConnectionsDTO(VimeoResponseMetaDataConnectionsDTO vimeoResponseMetaDataConnectionsDTO)
    {
        this.vimeoResponseMetaDataConnectionsDTO = vimeoResponseMetaDataConnectionsDTO;
    }
}
