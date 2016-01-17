package com.taa.videohd.sync.vimeo.direct;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trung on 11/27/2015.
 */
public class VimeoDirectRequestDTO
{
    @SerializedName("files")
    private VimeoDirectFileDTO vimeoDirectFileDTO;

    public VimeoDirectFileDTO getVimeoDirectFileDTO()
    {
        return vimeoDirectFileDTO;
    }

    public void setVimeoDirectFileDTO(VimeoDirectFileDTO vimeoDirectFileDTO)
    {
        this.vimeoDirectFileDTO = vimeoDirectFileDTO;
    }
}
