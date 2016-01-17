package com.taa.videohd.event;


import com.taa.videohd.ui.model.DirectLink;

/**
 * Created by trung on 12/19/2015.
 */
public class DownloadEvent
{
    DirectLink directLink;

    public DownloadEvent(DirectLink directLink)
    {
        this.directLink = directLink;
    }

    public DirectLink getDirectLink()
    {
        return directLink;
    }

    public void setDirectLink(DirectLink directLink)
    {
        this.directLink = directLink;
    }
}
