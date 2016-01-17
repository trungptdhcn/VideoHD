package com.taa.videohd.sync.dailymotion;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 11/19/2015.
 */
public class DailymotionDTO
{
    private String page;
    private String limit;
    private boolean hasmore = true;
    @SerializedName("list")
    List<DailymotionDetailDTO> dailymotionDetailDTOs  = new ArrayList<>();

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

    public List<DailymotionDetailDTO> getDailymotionDetailDTOs()
    {
        return dailymotionDetailDTOs;
    }

    public void setDailymotionDetailDTOs(List<DailymotionDetailDTO> dailymotionDetailDTOs)
    {
        this.dailymotionDetailDTOs = dailymotionDetailDTOs;
    }
}
