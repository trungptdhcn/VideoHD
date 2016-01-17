package com.taa.videohd.sync.vimeo.direct;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trung on 11/27/2015.
 */
public class VimeoDirectDTO
{
    @SerializedName("request")
    private VimeoDirectRequestDTO vimeoDirectRequestDTO;

    public VimeoDirectRequestDTO getVimeoDirectRequestDTO()
    {
        return vimeoDirectRequestDTO;
    }

    public void setVimeoDirectRequestDTO(VimeoDirectRequestDTO vimeoDirectRequestDTO)
    {
        this.vimeoDirectRequestDTO = vimeoDirectRequestDTO;
    }
}
