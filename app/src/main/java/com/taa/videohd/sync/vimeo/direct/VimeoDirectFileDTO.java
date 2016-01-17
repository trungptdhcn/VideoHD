package com.taa.videohd.sync.vimeo.direct;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 11/27/2015.
 */
public class VimeoDirectFileDTO
{
    @SerializedName("hls")
    private VimeoDirectStreamURLDTO vimeoDirectStreamURLDTO;
    @SerializedName("progressive")
    private List<VimeoDirectProgressiveDTO> vimeoDirectProgressiveDTOs
            = new ArrayList<>();

    public VimeoDirectStreamURLDTO getVimeoDirectStreamURLDTO()
    {
        return vimeoDirectStreamURLDTO;
    }

    public void setVimeoDirectStreamURLDTO(VimeoDirectStreamURLDTO vimeoDirectStreamURLDTO)
    {
        this.vimeoDirectStreamURLDTO = vimeoDirectStreamURLDTO;
    }

    public List<VimeoDirectProgressiveDTO> getVimeoDirectProgressiveDTOs()
    {
        return vimeoDirectProgressiveDTOs;
    }

    public void setVimeoDirectProgressiveDTOs(List<VimeoDirectProgressiveDTO> vimeoDirectProgressiveDTOs)
    {
        this.vimeoDirectProgressiveDTOs = vimeoDirectProgressiveDTOs;
    }
}
