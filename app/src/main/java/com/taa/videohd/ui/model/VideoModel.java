package com.taa.videohd.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trung on 11/25/2015.
 */
public class VideoModel extends Item implements Parcelable
{
    private String id;
    private String idOfPlayList;
    private String url;
    private String name;
    private String description;
    private BigInteger views;
    private BigInteger likes;
    private BigInteger disLikes;
    private String urlThumbnail;
    private String duration;
    private UserModel userModel;
    private Long publishedAt;
//    private String urlVideoDirect;
//    private String videoType;
    private List<DirectLink> directLinks = new ArrayList<>();

    public VideoModel()
    {
        directLinks = new ArrayList<>();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
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

    public BigInteger getViews()
    {
        return views;
    }

    public void setViews(BigInteger views)
    {
        this.views = views;
    }

    public BigInteger getLikes()
    {
        return likes;
    }

    public void setLikes(BigInteger likes)
    {
        this.likes = likes;
    }

    public BigInteger getDisLikes()
    {
        return disLikes;
    }

    public void setDisLikes(BigInteger disLikes)
    {
        this.disLikes = disLikes;
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

    public UserModel getUserModel()
    {
        return userModel;
    }

    public void setUserModel(UserModel userModel)
    {
        this.userModel = userModel;
    }

    public Long getPublishedAt()
    {
        return publishedAt;
    }

    public void setPublishedAt(Long publishedAt)
    {
        this.publishedAt = publishedAt;
    }

//    public String getUrlVideoDirect()
//    {
//        return urlVideoDirect;
//    }
//
//    public void setUrlVideoDirect(String urlVideoDirect)
//    {
//        this.urlVideoDirect = urlVideoDirect;
//    }
//
//    public String getVideoType()
//    {
//        return videoType;
//    }

//    public void setVideoType(String videoType)
//    {
//        this.videoType = videoType;
//    }


    public List<DirectLink> getDirectLinks()
    {
        return directLinks;
    }

    public void setDirectLinks(List<DirectLink> directLinks)
    {
        this.directLinks = directLinks;
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
        if (idOfPlayList != null)
        {
            dest.writeString(idOfPlayList);
        }
        else
        {
            dest.writeString("");
        }
        dest.writeString(url);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(views != null ? views.intValue() : 0);
        dest.writeInt(likes != null ? likes.intValue() : 0);
        dest.writeInt(disLikes != null ? disLikes.intValue() : 0);
        dest.writeString(urlThumbnail);
        dest.writeString(duration);
        dest.writeLong(publishedAt != null ? publishedAt : 0);
        dest.writeSerializable(userModel);
        dest.writeTypedList(directLinks);
    }

    public static final Creator<VideoModel> CREATOR = new Creator<VideoModel>()
    {
        public VideoModel createFromParcel(Parcel in)
        {
            return new VideoModel(in);
        }

        public VideoModel[] newArray(int size)
        {
            return new VideoModel[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private VideoModel(Parcel in)
    {
        id = in.readString();
        idOfPlayList = in.readString();
        url = in.readString();
        name = in.readString();
        description = in.readString();
        views = BigInteger.valueOf(in.readInt());
        likes = BigInteger.valueOf(in.readInt());
        disLikes = BigInteger.valueOf(in.readInt());
        urlThumbnail = in.readString();
        duration = in.readString();
        publishedAt = in.readLong();
        userModel = (UserModel) in.readSerializable();
        directLinks = new ArrayList<>();
        in.readTypedList(directLinks, DirectLink.CREATOR);
    }

    public String getIdOfPlayList()
    {
        return idOfPlayList;
    }

    public void setIdOfPlayList(String idOfPlayList)
    {
        this.idOfPlayList = idOfPlayList;
    }
}
