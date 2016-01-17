package com.taa.videohd.sync.vimeo.response;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoResponsePrivacyDTO
{
    private String view;
    private String embed;
    private String download;
    private String add;
    private String comments;

    public String getView()
    {
        return view;
    }

    public void setView(String view)
    {
        this.view = view;
    }

    public String getEmbed()
    {
        return embed;
    }

    public void setEmbed(String embed)
    {
        this.embed = embed;
    }

    public String getDownload()
    {
        return download;
    }

    public void setDownload(String download)
    {
        this.download = download;
    }

    public String getAdd()
    {
        return add;
    }

    public void setAdd(String add)
    {
        this.add = add;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments(String comments)
    {
        this.comments = comments;
    }
}
