package com.taa.videohd.sync.vimeo.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoPagingResponseDTO
{
    @SerializedName("total")
    private int total;
    @SerializedName("next")
    private String nextPage;
    @SerializedName("previous")
    private String previousPage;
    @SerializedName("first")
    private String firstPage;
    @SerializedName("last")
    private String lastPage;

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public String getNextPage()
    {
        return nextPage;
    }

    public void setNextPage(String nextPage)
    {
        this.nextPage = nextPage;
    }

    public String getPreviousPage()
    {
        return previousPage;
    }

    public void setPreviousPage(String previousPage)
    {
        this.previousPage = previousPage;
    }

    public String getFirstPage()
    {
        return firstPage;
    }

    public void setFirstPage(String firstPage)
    {
        this.firstPage = firstPage;
    }

    public String getLastPage()
    {
        return lastPage;
    }

    public void setLastPage(String lastPage)
    {
        this.lastPage = lastPage;
    }
}
