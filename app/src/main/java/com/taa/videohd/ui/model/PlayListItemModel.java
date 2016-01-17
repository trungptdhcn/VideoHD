package com.taa.videohd.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by trung on 12/15/2015.
 */
public class PlayListItemModel extends Item implements Parcelable
{
    private String id;
    private String name;
    private String description;
    private String userName;
    private String urlThumbnail;
    private String duration;

    public PlayListItemModel()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUrlThumbnail()
    {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail)
    {
        this.urlThumbnail = urlThumbnail;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(urlThumbnail);
        dest.writeString(duration);
        dest.writeString(userName);
    }

    public static final Creator<PlayListItemModel> CREATOR = new Creator<PlayListItemModel>()
    {
        public PlayListItemModel createFromParcel(Parcel in)
        {
            return new PlayListItemModel(in);
        }

        public PlayListItemModel[] newArray(int size)
        {
            return new PlayListItemModel[size];
        }
    };

    private PlayListItemModel(Parcel in)
    {
        id = in.readString();
        name = in.readString();
        urlThumbnail = in.readString();
        duration = in.readString();
        userName = in.readString();
    }
}
