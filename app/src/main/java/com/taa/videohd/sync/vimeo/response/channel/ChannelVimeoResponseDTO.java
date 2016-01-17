package com.taa.videohd.sync.vimeo.response.channel;

import com.google.gson.annotations.SerializedName;
import com.taa.videohd.sync.vimeo.response.VimeoPagingResponseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 12/4/2015.
 */
public class ChannelVimeoResponseDTO
{
    private VimeoPagingResponseDTO paging;
    @SerializedName("data")
    private List<ChannelVimeoResponseDetailDTO> channelVimeoResponseDetailDTOs = new ArrayList<>();

    public VimeoPagingResponseDTO getPaging()
    {
        return paging;
    }

    public void setPaging(VimeoPagingResponseDTO paging)
    {
        this.paging = paging;
    }

    public List<ChannelVimeoResponseDetailDTO> getChannelVimeoResponseDetailDTOs()
    {
        return channelVimeoResponseDetailDTOs;
    }

    public void setChannelVimeoResponseDetailDTOs(List<ChannelVimeoResponseDetailDTO> channelVimeoResponseDetailDTOs)
    {
        this.channelVimeoResponseDetailDTOs = channelVimeoResponseDetailDTOs;
    }
}
