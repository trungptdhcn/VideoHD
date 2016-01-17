package com.taa.videohd.ui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 12/11/2015.
 */
public class PageModel
{
    private List<Item> items = new ArrayList<>();
    private String nextPage;

    public List<Item> getItems()
    {
        return items;
    }

    public void setItems(List<Item> items)
    {
        this.items = items;
    }

    public String getNextPage()
    {
        return nextPage;
    }

    public void setNextPage(String nextPage)
    {
        this.nextPage = nextPage;
    }
}
