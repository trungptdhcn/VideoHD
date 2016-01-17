package com.taa.videohd.sync.vimeo.response;

import com.google.gson.annotations.SerializedName;
import com.taa.videohd.sync.ResponseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoResponseDTO extends ResponseDTO
{
    private VimeoPagingResponseDTO paging;
    @SerializedName("data")
    private List<VimeoResponseDetailDTO> vimeoResponseDetailDTOs = new ArrayList<>();

    public VimeoPagingResponseDTO getPaging()
    {
        return paging;
    }

    public void setPaging(VimeoPagingResponseDTO paging)
    {
        this.paging = paging;
    }

    public List<VimeoResponseDetailDTO> getVimeoResponseDetailDTOs()
    {
        return vimeoResponseDetailDTOs;
    }

    public void setVimeoResponseDetailDTOs(List<VimeoResponseDetailDTO> vimeoResponseDetailDTOs)
    {
        this.vimeoResponseDetailDTOs = vimeoResponseDetailDTOs;
    }
}
