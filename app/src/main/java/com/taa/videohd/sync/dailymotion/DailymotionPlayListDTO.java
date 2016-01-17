package com.taa.videohd.sync.dailymotion;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 12/9/2015.
 */
public class DailymotionPlayListDTO
{
    private String page;
    private String limit;
    private boolean hasmore = true;
    @SerializedName("list")
    List<DailymotionPlayistDetailDTO> dailymotionPlaylistDetailDTO  = new ArrayList<>();

    public String getPage()
    {
        return page;
    }

    public void setPage(String page)
    {
        this.page = page;
    }

    public String getLimit()
    {
        return limit;
    }

    public void setLimit(String limit)
    {
        this.limit = limit;
    }

    public boolean isHasmore()
    {
        return hasmore;
    }

    public void setHasmore(boolean hasmore)
    {
        this.hasmore = hasmore;
    }

    public List<DailymotionPlayistDetailDTO> getDailymotionPlaylistDetailDTO()
    {
        return dailymotionPlaylistDetailDTO;
    }

    public void setDailymotionPlaylistDetailDTO(List<DailymotionPlayistDetailDTO> dailymotionPlaylistDetailDTO)
    {
        this.dailymotionPlaylistDetailDTO = dailymotionPlaylistDetailDTO;
    }
}
