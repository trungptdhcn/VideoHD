package com.taa.videohd.utils;

/**
 * Created by Trung on 11/25/2015.
 */
public class Constant
{
    public static final String KEY_YOUTUBE
            = "AIzaSyAR9tjSAh68M7Bo800ssK_hymUuDAzvVeQ";

    public enum HOST_NAME
    {
        YOUTUBE, YOUTUBE_RELATED, YOUTUBE_VIDEO_BY_CHANNEL_ID, YOUTUBE_VIDEO_BY_PLAYLIST_ID, VIMEO, VIMEO_PLAYER,
        VIMEO_RELATED, VIMEO_VIDEO_BY_USER_ID, VIMEO_VIDEO_WITH_CATEGORY, VIMEO_VIDEO_WITH_CHANNEL_ID,
        VIMEO_DIRECT_LINK,
        DAILYMOTION, DAILYMOTION_RELATED, DAILYMOTION_VIDEO_BY_USER_ID, DAILYMOTION_VIDEO_BY_PLAYLIST_ID,
        FACEBOOK,
    }

    public enum TYPE_VIDEO
    {
        TYPE_SS(1), TYPE_DASH(2), TYPE_HLS(3), TYPE_OTHER(4);
        private int value;

        TYPE_VIDEO(int value)
        {
            this.value = value;
        }

    }


    public static String VIMEO_BASE_URL = "https://api.vimeo.com";
    public static String VIMEO_PLAYER_BASE_URL = "https://player.vimeo.com";
    public static String DAILYMOTION_BASE_URL = "https://api.dailymotion.com";

    //    ===============================VIDEO================================
    public final static String VIDEO_TYPE_MP4 = ".mp4";
    public final static String VIDEO_TYPE_HLS = ".m3u8";
    //    ================================VIMEO=======================================
    public final static String VIMEO_VIDEOS = "Videos";
    public final static String VIMEO_USERS = "Users";
    public final static String VIMEO_CHANNELS = "Channels";
//    ====================================Dailymotion====================================

    public final static String DAILYMOTION_VIDEO_FIELDS = "title,channel,country,description,duration,id,poster,thumbnail_720_url,url,owner.screenname,views_total,owner.fans_total,owner.avatar_240_url,created_time,owner.videos_total,owner.description,owner.cover_250_url,owner.id";
    public final static String DAILYMOTION_PLAYLIST_FIELDS = "description,id,name,owner.avatar_720_url,owner.fans_total,owner.screenname,owner.website_url,thumbnail_720_url,videos_total";
    public final static String DAILYMOTION_USER_FIELDS = "id,screenname,description,avatar_720_url,cover_250_url,fans_total,videos_total";
    public final static String DAILYMOTION_VIDEO_FLAG = "no_live,no_premium";

    public final static String DAILYMOTION_VIDEOS = "Videos";
    public final static String DAILYMOTION_USERS = "Users";
    public final static String DAILYMOTION_CHANNELS = "Channels";
    public final static String DAILYMOTION_PLAYLIST = "Playlists";

    //    =======================================Youtube=========================================
    public final static String YOUTUBE_VIDEOS = "video";
    public final static String YOUTUBE_ALL = "aLL";
    public final static String YOUTUBE_PLAYLIST = "playlist";
    public final static String YOUTUBE_CHANNELS = "channel";

    public final static String YOUTUBE_UPLOAD_ALL = "All time";
    public final static String YOUTUBE_UPLOAD_TODAY = "Today";
    public final static String YOUTUBE_UPLOAD_THISWEEK = "This week";
    public final static String YOUTUBE_UPLOAD_THISMONTH = "This month";
    public final static String YOUTUBE_UPLOAD_THISYEAR = "This year";

    public final static String YOUTUBE_DURATION_ALL = "Any";
    public final static String YOUTUBE_DURATION_SHORT = "Short";
    public final static String YOUTUBE_DURATION_MEDIUM = "Medium";
    public final static String YOUTUBE_DURATION_LONG = "Long";

    public final static String YOUTUBE_SORT_RELEVANCE = "Relevance";
    public final static String YOUTUBE_SORT_DATE = "Date Created";
    public final static String YOUTUBE_SORT_RATING = "Rating";
    public final static String YOUTUBE_SORT_ALPHABETA = "Alphabeta";
    public final static String YOUTUBE_SORT_VIDEOCOUNT = "Video count";
    public final static String YOUTUBE_SORT_VIEWCOUNT = "View count";
    public static String MOST_POPULAR = "mostPopular";
    public final static String DATE_RFC_3339_FOMATED = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public final static String DATE_RFC_3339_FOMATED_VIMEO = "yyyy-MM-dd'T'HH:mm:ssZZ";
}
