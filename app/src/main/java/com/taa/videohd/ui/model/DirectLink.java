package com.taa.videohd.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by trung on 12/17/2015.
 */
public class DirectLink implements Parcelable
{
    private String uri;
    private String userName;
    private String duration;
    private String name;
    private String urlThumb;
    private String quality;
    private String type;
    private int typePlay;

    public DirectLink()
    {
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getQuality()
    {
        return quality;
    }

    public void setQuality(String quality)
    {
        this.quality = quality;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getTypePlay()
    {
        return typePlay;
    }

    public void setTypePlay(int typePlay)
    {
        this.typePlay = typePlay;
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrlThumb()
    {
        return urlThumb;
    }

    public void setUrlThumb(String urlThumb)
    {
        this.urlThumb = urlThumb;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(uri);
        dest.writeString(urlThumb);
        dest.writeString(name);
        dest.writeString(userName);
        dest.writeString(duration);
        dest.writeString(quality);
        dest.writeString(type);
        dest.writeInt(typePlay);
    }

    public static final Creator<DirectLink> CREATOR = new Creator<DirectLink>()
    {
        public DirectLink createFromParcel(Parcel in)
        {
            return new DirectLink(in);
        }

        public DirectLink[] newArray(int size)
        {
            return new DirectLink[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private DirectLink(Parcel in)
    {
        uri = in.readString();
        urlThumb = in.readString();
        userName = in.readString();
        duration = in.readString();
        name = in.readString();
        quality = in.readString();
        type = in.readString();
        typePlay = in.readInt();
    }
}
