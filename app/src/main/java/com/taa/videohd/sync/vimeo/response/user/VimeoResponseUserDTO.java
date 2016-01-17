package com.taa.videohd.sync.vimeo.response.user;

import com.google.gson.annotations.SerializedName;
import com.taa.videohd.sync.vimeo.response.VimeoPagingResponseDTO;
import com.taa.videohd.sync.vimeo.response.VimeoResponseUserDetailDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 12/17/2015.
 */
public class VimeoResponseUserDTO
{
    private VimeoPagingResponseDTO paging;
    @SerializedName("data")
    private List<VimeoResponseUserDetailDTO> vimeoResponseUserDetailDTOs = new ArrayList<>();

    public VimeoPagingResponseDTO getPaging()
    {
        return paging;
    }

    public void setPaging(VimeoPagingResponseDTO paging)
    {
        this.paging = paging;
    }

    public List<VimeoResponseUserDetailDTO> getVimeoResponseUserDetailDTOs()
    {
        return vimeoResponseUserDetailDTOs;
    }

    public void setVimeoResponseUserDetailDTOs(List<VimeoResponseUserDetailDTO> vimeoResponseUserDetailDTOs)
    {
        this.vimeoResponseUserDetailDTOs = vimeoResponseUserDetailDTOs;
    }
}
