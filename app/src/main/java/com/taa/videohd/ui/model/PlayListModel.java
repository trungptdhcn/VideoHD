package com.taa.videohd.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;

/**
 * Created by trung on 12/11/2015.
 */
public class PlayListModel extends Item implements Parcelable
{
    private String id;
    private String name;
    private String description;
    private String url;
    private BigInteger videoCount;
    private Long dateCreated;
    private String thumbnail;

    private UserModel userModel;
    private static final int USER_MODEL_FLAG = 4;

    public PlayListModel()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public BigInteger getVideoCount()
    {
        return videoCount;
    }

    public void setVideoCount(BigInteger videoCount)
    {
        this.videoCount = videoCount;
    }

    public Long getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public UserModel getUserModel()
    {
        return userModel;
    }

    public void setUserModel(UserModel userModel)
    {
        this.userModel = userModel;
    }

    public String getThumbnail()
    {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
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
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(thumbnail);
        dest.writeLong(dateCreated != null ? dateCreated : 0);
        dest.writeInt(videoCount != null ? videoCount.intValue() : 0);
        dest.writeSerializable(userModel);
    }

    public static final Creator<PlayListModel> CREATOR = new Creator<PlayListModel>()
    {
        public PlayListModel createFromParcel(Parcel in)
        {
            return new PlayListModel(in);
        }

        public PlayListModel[] newArray(int size)
        {
            return new PlayListModel[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private PlayListModel(Parcel in)
    {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        url = in.readString();
        thumbnail = in.readString();
        dateCreated = in.readLong();
        videoCount = BigInteger.valueOf(in.readInt());
        userModel = (UserModel) in.readSerializable();
    }
}
