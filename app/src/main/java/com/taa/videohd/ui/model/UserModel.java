package com.taa.videohd.ui.model;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Trung on 12/4/2015.
 */
public class UserModel extends Item implements Serializable
{
    private String id;
    private String name;
    private String description;
    private String urlAvatar;
    private String urlCover;
    private BigInteger subscrible;
    private BigInteger videoCounts;

    public UserModel()
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

    public String getUrlAvatar()
    {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar)
    {
        this.urlAvatar = urlAvatar;
    }

    public String getUrlCover()
    {
        return urlCover;
    }

    public void setUrlCover(String urlCover)
    {
        this.urlCover = urlCover;
    }

    public BigInteger getSubscrible()
    {
        return subscrible;
    }

    public void setSubscrible(BigInteger subscrible)
    {
        this.subscrible = subscrible;
    }

    public BigInteger getVideoCounts()
    {
        return videoCounts;
    }

    public void setVideoCounts(BigInteger videoCounts)
    {
        this.videoCounts = videoCounts;
    }

    //    @Override
//    public int describeContents()
//    {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags)
//    {
//        dest.writeString(id);
//        dest.writeString(name);
//        dest.writeString(description);
//        dest.writeString(urlAvatar);
//        dest.writeString(urlCover);
//        if (subscrible!=null)
//        {
//            dest.writeInt(subscrible.intValue());
//        }
//        if (videoCounts != null)
//        {
//            dest.writeInt(videoCounts.intValue());
//        }
//    }

//    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>()
//    {
//        public UserModel createFromParcel(Parcel in)
//        {
//            return new UserModel(in);
//        }
//
//        public UserModel[] newArray(int size)
//        {
//            return new UserModel[size];
//        }
//    };
//
//    private UserModel(Parcel in)
//    {
//        id = in.readString();
//        name = in.readString();
//        description = in.readString();
//        urlAvatar = in.readString();
//        urlCover = in.readString();
//        subscrible = BigInteger.valueOf(in.readInt());
//        videoCounts = BigInteger.valueOf(in.readInt());
//    }
}
