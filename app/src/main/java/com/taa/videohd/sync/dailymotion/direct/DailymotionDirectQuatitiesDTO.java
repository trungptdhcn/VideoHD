package com.taa.videohd.sync.dailymotion.direct;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 12/2/2015.
 */
public class DailymotionDirectQuatitiesDTO
{
    @SerializedName("auto")
    List<DailymotionDirectQualityDTO> _auto = new ArrayList<>();
    @SerializedName("240")
    List<DailymotionDirectQualityDTO> _240 = new ArrayList<>();
    @SerializedName("380")
    List<DailymotionDirectQualityDTO> _380 = new ArrayList<>();
    @SerializedName("480")
    List<DailymotionDirectQualityDTO> _480 = new ArrayList<>();
    @SerializedName("720")
    List<DailymotionDirectQualityDTO> _720 = new ArrayList<>();

    public List<DailymotionDirectQualityDTO> get_auto()
    {
        return _auto;
    }

    public void set_auto(List<DailymotionDirectQualityDTO> _auto)
    {
        this._auto = _auto;
    }

    public List<DailymotionDirectQualityDTO> get_240()
    {
        return _240;
    }

    public void set_240(List<DailymotionDirectQualityDTO> _240)
    {
        this._240 = _240;
    }

    public List<DailymotionDirectQualityDTO> get_380()
    {
        return _380;
    }

    public void set_380(List<DailymotionDirectQualityDTO> _380)
    {
        this._380 = _380;
    }

    public List<DailymotionDirectQualityDTO> get_480()
    {
        return _480;
    }

    public void set_480(List<DailymotionDirectQualityDTO> _480)
    {
        this._480 = _480;
    }

    public List<DailymotionDirectQualityDTO> get_720()
    {
        return _720;
    }

    public void set_720(List<DailymotionDirectQualityDTO> _720)
    {
        this._720 = _720;
    }
}
