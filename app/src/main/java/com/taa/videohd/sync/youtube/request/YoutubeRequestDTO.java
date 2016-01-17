package com.taa.videohd.sync.youtube.request;


import com.taa.videohd.sync.RequestDTO;

/**
 * Created by Trung on 11/25/2015.
 */
public class YoutubeRequestDTO extends RequestDTO
{
    private final String keyWord;
    private final String location;
    private final String order;
    private final String topic;
    private final String type;
    private final String videoDefinition;
    private final String videoDimension;
    private final String videoDuration;
    private final String videoLicense;
    private final String videoType;
    private final long publishAfter;
    private final long publishBefore;
    private final String id;
    private String pageToken;
    private String regionCode;

    public YoutubeRequestDTO(YoutubeRequestBuilder builder)
    {
        this.keyWord = builder.keyWord;
        this.location = builder.location;
        this.order = builder.order;
        this.regionCode = builder.regionCode;
        this.topic = builder.topic;
        this.type = builder.type;
        this.videoDefinition = builder.videoDefinition;
        this.videoDimension = builder.videoDimension;
        this.videoDuration = builder.videoDuration;
        this.videoLicense = builder.videoLicense;
        this.videoType = builder.videoType;
        this.publishAfter = builder.publishAfter;
        this.publishBefore = builder.publishBefore;
        this.pageToken = builder.pageToken;
        this.id = builder.id;
    }

    public String getKeyWord()
    {
        return keyWord;
    }

    public String getLocation()
    {
        return location;
    }

    public String getOrder()
    {
        return order;
    }

    public String getRegionCode()
    {
        return regionCode;
    }

    public String getTopic()
    {
        return topic;
    }

    public String getType()
    {
        return type;
    }

    public String getVideoDefinition()
    {
        return videoDefinition;
    }

    public String getVideoDimension()
    {
        return videoDimension;
    }

    public String getVideoDuration()
    {
        return videoDuration;
    }

    public String getVideoLicense()
    {
        return videoLicense;
    }

    public String getVideoType()
    {
        return videoType;
    }

    public String getPageToken()
    {
        return pageToken;
    }

    public String getId()
    {
        return id;
    }

    public void setPageToken(String pageToken)
    {
        this.pageToken = pageToken;
    }

    public void setRegionCode(String regionCode)
    {
        this.regionCode = regionCode;
    }

    public long getPublishAfter()
    {
        return publishAfter;
    }

    public long getPublishBefore()
    {
        return publishBefore;
    }

    public static class YoutubeRequestBuilder
    {
        private final String keyWord;
        private String location;
        private String order;
        private String regionCode;
        private String topic;
        private String type;
        private String videoDefinition;
        private String videoDimension;
        private String videoDuration;
        private String videoLicense;
        private String videoType;
        private long publishAfter;
        private long publishBefore;
        private String pageToken;
        private String id;

        public YoutubeRequestBuilder(String keyWord)
        {
            this.keyWord = keyWord;
        }

        public YoutubeRequestBuilder location(String location)
        {
            this.location = location;
            return this;
        }

        public YoutubeRequestBuilder order(String order)
        {
            this.order = order;
            return this;
        }

        public YoutubeRequestBuilder regionCode(String regionCode)
        {
            this.regionCode = regionCode;
            return this;
        }

        public YoutubeRequestBuilder topic(String topic)
        {
            this.topic = topic;
            return this;
        }

        public YoutubeRequestBuilder type(String type)
        {
            this.type = type;
            return this;
        }

        public YoutubeRequestBuilder pageToken(String pageToken)
        {
            this.pageToken = pageToken;
            return this;
        }

        public YoutubeRequestBuilder videoDimension(String videoDimension)
        {
            this.videoDimension = videoDimension;
            return this;
        }

        public YoutubeRequestBuilder videoDuration(String videoDuration)
        {
            this.videoDuration = videoDuration;
            return this;
        }

        public YoutubeRequestBuilder videoLicense(String videoLicense)
        {
            this.videoLicense = videoLicense;
            return this;
        }

        public YoutubeRequestBuilder videoType(String videoType)
        {
            this.videoType = videoType;
            return this;
        }

        public YoutubeRequestBuilder id(String id)
        {
            this.id = id;
            return this;
        }

        public YoutubeRequestBuilder videoDefinition(String videoDefinition)
        {
            this.videoDefinition = videoDefinition;
            return this;
        }

        public YoutubeRequestBuilder publishAfter(long publishAfter)
        {
            this.publishAfter = publishAfter;
            return this;
        }
        public YoutubeRequestBuilder publishBefore(long publishBefore)
        {
            this.publishBefore = publishBefore;
            return this;
        }

        public YoutubeRequestDTO build()
        {
            return new YoutubeRequestDTO(this);
        }
    }
}
