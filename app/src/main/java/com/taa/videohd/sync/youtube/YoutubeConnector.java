package com.taa.videohd.sync.youtube;

import android.content.Context;
import android.util.Log;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.taa.videohd.base.StringUtils;
import com.taa.videohd.sync.RequestDTO;
import com.taa.videohd.sync.youtube.request.YoutubeRequestDTO;
import com.taa.videohd.ui.model.*;
import com.taa.videohd.utils.Constant;
import com.trungpt.videodownloadmaster.R;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 12/11/2015.
 */
public class YoutubeConnector
{
    public static long MAX_RESULT = 5l;

    private YouTube youtube;
    private YouTube.Search.List search;
    private YouTube.Videos.List searchForVideoDetails;
    private YouTube.Channels.List searchForChannelDetails;
    private YouTube.Playlists.List searchForPlayListDetails;
    private YouTube.PlaylistItems.List searchVideoForPlayList;

    private YouTube.Videos.List mostPopularQuery;

    public YoutubeConnector(Context context)
    {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer()
        {
            @Override
            public void initialize(HttpRequest hr) throws IOException
            {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        try
        {
            mostPopularQuery = youtube.videos().list("id,snippet,contentDetails,statistics");
            mostPopularQuery.setKey(Constant.KEY_YOUTUBE);
            mostPopularQuery.setMaxResults(MAX_RESULT);

            search = youtube.search().list("id,snippet");
            search.setKey(Constant.KEY_YOUTUBE);
            search.setMaxResults(MAX_RESULT);

            searchForVideoDetails = youtube.videos().list("statistics,snippet,contentDetails");
            searchForVideoDetails.setKey(Constant.KEY_YOUTUBE);

            searchForPlayListDetails = youtube.playlists().list("snippet,contentDetails");
            searchForPlayListDetails.setKey(Constant.KEY_YOUTUBE);

            searchForChannelDetails = youtube.channels().list("statistics,snippet,contentDetails,brandingSettings");
            searchForChannelDetails.setKey(Constant.KEY_YOUTUBE);

            searchVideoForPlayList = youtube.playlistItems().list("snippet,contentDetails");
            searchVideoForPlayList.setKey(Constant.KEY_YOUTUBE);
        }
        catch (IOException e)
        {
            Log.d(this.getClass().getName(), "Could not initialize query most popular: " + e);
        }
    }


    public PageModel mostPopulars(RequestDTO requestDTO)
    {
        mostPopularQuery.setChart(Constant.MOST_POPULAR);
        mostPopularQuery.setRegionCode(((YoutubeRequestDTO) requestDTO).getRegionCode());
        mostPopularQuery.setPageToken(((YoutubeRequestDTO) requestDTO).getPageToken());
        try
        {
            VideoListResponse response = mostPopularQuery.execute();
            List<Video> items = response.getItems();
            List<Item> videos = new ArrayList<>();
            if (items != null && items.size() > 0)
            {
                for (Video videoYoutube : items)
                {
                    videos.add(convertItemToVideoModel(videoYoutube));
                }
            }
            PageModel videoPage = new PageModel();
            videoPage.setItems(videos);
            if (response.getPageInfo() != null)
            {
                videoPage.setNextPage(response.getNextPageToken());
            }
            return videoPage;
        }
        catch (IOException e)
        {
            Log.d("YC", "Could not search: " + e);
            return null;
        }
    }

    //    ===================ANOTHER METHOD=========================
    private VideoModel convertItemToVideoModel(Video videoYoutube)
    {
        VideoModel videoModel = new VideoModel();
        videoModel.setName(videoYoutube.getSnippet().getTitle());
        videoModel.setDescription(videoYoutube.getSnippet().getDescription());
        videoModel.setUrlThumbnail(videoYoutube.getSnippet().getThumbnails().getMedium().getUrl());
        videoModel.setUrl("https://www.youtube.com/watch?v=" + videoYoutube.getId());
        videoModel.setViews(videoYoutube.getStatistics().getViewCount());
        videoModel.setLikes(videoYoutube.getStatistics().getLikeCount());
        videoModel.setDisLikes(videoYoutube.getStatistics().getDislikeCount());
        String duration = videoYoutube.getContentDetails().getDuration();
        videoModel.setDuration(getTimeFromString(duration));
        videoModel.setId(videoYoutube.getId());
        DateTime publishedAt = videoYoutube.getSnippet().getPublishedAt();
        videoModel.setPublishedAt(publishedAt.getValue());
        UserModel userModel = new UserModel();
        userModel.setName(videoYoutube.getSnippet().getChannelTitle());
        userModel.setId(videoYoutube.getSnippet().getChannelId());
        videoModel.setUserModel(userModel);
        return videoModel;
    }

    private String getTimeFromString(String duration)
    {
        String time = "";
        boolean hourexists = false, minutesexists = false, secondsexists = false;
        if (duration.contains("H"))
        {
            hourexists = true;
        }
        if (duration.contains("M"))
        {
            minutesexists = true;
        }
        if (duration.contains("S"))
        {
            secondsexists = true;
        }
        if (hourexists)
        {
            String hour = "";
            hour = duration.substring(duration.indexOf("T") + 1,
                    duration.indexOf("H"));
            if (hour.length() == 1)
            {
                hour = "0" + hour;
            }
            time += hour + ":";
        }
        if (minutesexists)
        {
            String minutes = "";
            if (hourexists)
            {
                minutes = duration.substring(duration.indexOf("H") + 1,
                        duration.indexOf("M"));
            }
            else
            {
                minutes = duration.substring(duration.indexOf("T") + 1,
                        duration.indexOf("M"));
            }
            if (minutes.length() == 1)
            {
                minutes = "0" + minutes;
            }
            time += minutes + ":";
        }
        else
        {
            time += "00:";
        }
        if (secondsexists)
        {
            String seconds = "";
            if (hourexists)
            {
                if (minutesexists)
                {
                    seconds = duration.substring(duration.indexOf("M") + 1,
                            duration.indexOf("S"));
                }
                else
                {
                    seconds = duration.substring(duration.indexOf("H") + 1,
                            duration.indexOf("S"));
                }
            }
            else if (minutesexists)
            {
                seconds = duration.substring(duration.indexOf("M") + 1,
                        duration.indexOf("S"));
            }
            else
            {
                seconds = duration.substring(duration.indexOf("T") + 1,
                        duration.indexOf("S"));
            }
            if (seconds.length() == 1)
            {
                seconds = "0" + seconds;
            }
            time += seconds;
        }
        return time;
    }


    public PageModel search(RequestDTO requestDTO)
    {
        YoutubeRequestDTO youtubeRequestDTO = (YoutubeRequestDTO) requestDTO;
        try
        {
            if (StringUtils.isNotEmpty(youtubeRequestDTO.getKeyWord()))
            {
                search.setQ(youtubeRequestDTO.getKeyWord());
            }
            if (StringUtils.isNotEmpty(youtubeRequestDTO.getLocation()))
            {
                search.setLocation(youtubeRequestDTO.getLocation());
            }
            if (StringUtils.isNotEmpty(youtubeRequestDTO.getOrder()))
            {
                search.setOrder(youtubeRequestDTO.getOrder());
            }
            if (StringUtils.isNotEmpty(youtubeRequestDTO.getRegionCode()))
            {
                search.setRegionCode(youtubeRequestDTO.getRegionCode());
            }
            search.setPublishedBefore(new DateTime(youtubeRequestDTO.getPublishBefore()));
            search.setPublishedAfter(new DateTime(youtubeRequestDTO.getPublishAfter()));
            String videoDefinition = youtubeRequestDTO.getVideoDefinition();
            String videoDimension = youtubeRequestDTO.getVideoDimension();
            String videoDuration = youtubeRequestDTO.getVideoDuration();
            String videoType = youtubeRequestDTO.getVideoType();
            if (StringUtils.isNotEmpty(videoDefinition)
                    || StringUtils.isNotEmpty(videoDuration)
                    || StringUtils.isNotEmpty(videoDimension)
                    || StringUtils.isNotEmpty(videoType))
            {
                search.setType("video");
            }
            else
            {
                search.setType(youtubeRequestDTO.getType());
            }
            if (StringUtils.isNotEmpty(videoDefinition))
            {
                search.setVideoDefinition(videoDefinition);
            }
            if (StringUtils.isNotEmpty(videoDimension))
            {
                search.setVideoDimension(videoDimension);
            }
            if (StringUtils.isNotEmpty(videoDuration))
            {
                search.setVideoDuration(videoDuration);
            }
            if (StringUtils.isNotEmpty(videoType))
            {
                search.setVideoType(videoType);
            }
            search.setMaxResults(MAX_RESULT);
            search.setPageToken(youtubeRequestDTO.getPageToken());
            //Get normal data
            SearchListResponse response = search.execute();
            List<SearchResult> results = response.getItems();
            List<Item> items = new ArrayList<>();
//            ===================================
            String ids = "";
            if (youtubeRequestDTO.getType().equals(Constant.YOUTUBE_VIDEOS))
            {
                for (SearchResult result : results)
                {
                    ids = ids + result.getId().getVideoId() + ",";
                }
                searchForVideoDetails.setId(ids);
                VideoListResponse resultDetails = searchForVideoDetails.execute();
                for (Video videoYoutube : resultDetails.getItems())
                {
                    items.add(convertItemToVideoModel(videoYoutube));
                }
            }
            else if (youtubeRequestDTO.getType().equals(Constant.YOUTUBE_PLAYLIST))
            {
                for (SearchResult result : results)
                {
                    ids = ids + result.getId().getPlaylistId() + ",";
                }
                searchForPlayListDetails.setId(ids);
                PlaylistListResponse resultDetails = searchForPlayListDetails.execute();
                for (Playlist playlist : resultDetails.getItems())
                {
                    items.add(convertResponseToPlayListModel(playlist));
                }
            }

            else
            {
                for (SearchResult result : results)
                {
                    ids = ids + result.getId().getChannelId() + ",";
                }
                searchForChannelDetails.setId(ids);
                ChannelListResponse resultDetails = searchForChannelDetails.execute();
                for (Channel channel : resultDetails.getItems())
                {
                    items.add(convertResponseToUserModel(channel));
                }
            }
            PageModel pageModel = new PageModel();
            pageModel.setItems(items);
            if (response.getPageInfo() != null)
            {
                pageModel.setNextPage(response.getNextPageToken());
            }
            return pageModel;
        }
        catch (IOException e)
        {
            Log.d(YoutubeConnector.class.getName(), "Could not search: " + e);
            return null;
        }
    }

    private PlayListModel convertResponseToPlayListModel(Playlist playlist)
    {
        PlayListModel playListModel = new PlayListModel();
        playListModel.setName(playlist.getSnippet().getTitle());
        playListModel.setDescription(playlist.getSnippet().getDescription());
        playListModel.setUrl("https://www.youtube.com/watch?v=" + playlist.getId());
        playListModel.setId(playlist.getId());
        playListModel.setThumbnail(playlist.getSnippet().getThumbnails().getHigh().getUrl());
        playListModel.setVideoCount(BigInteger.valueOf(playlist.getContentDetails().getItemCount()));
        UserModel userModel = new UserModel();
        userModel.setName(playlist.getSnippet().getChannelTitle());
        userModel.setId(playlist.getSnippet().getChannelId());
        playListModel.setUserModel(userModel);
        return playListModel;
    }

    private UserModel convertResponseToUserModel(Channel channel)
    {
        UserModel userModel = new UserModel();
        userModel.setId(channel.getId());
        userModel.setName(channel.getSnippet().getTitle());
        userModel.setDescription(channel.getSnippet().getDescription());
        userModel.setVideoCounts(channel.getStatistics().getVideoCount());
        userModel.setSubscrible(channel.getStatistics().getSubscriberCount());
        userModel.setUrlAvatar(channel.getSnippet().getThumbnails().getHigh().getUrl());
        userModel.setUrlCover(channel.getBrandingSettings().getImage().getBannerMobileHdImageUrl());
        return userModel;
    }

    public UserModel getAuthorInfo(String authorId)
    {
        UserModel userModel = null;
        try
        {
            YouTube.Channels.List searchChannel = youtube.channels().list("statistics,snippet,contentDetails,brandingSettings");
            searchChannel.setKey(Constant.KEY_YOUTUBE);
            searchChannel.setId(authorId);
            ChannelListResponse channelListResponse = searchChannel.execute();
            if (channelListResponse.getItems() != null && channelListResponse.getItems().size() > 0)
            {
                Channel channel = channelListResponse.getItems().get(0);
                userModel = convertResponseToUserModel(channel);
            }
        }
        catch (IOException e)
        {
            Log.d(YoutubeConnector.class.getName(), "Could not search: " + e);
            return null;
        }
        return userModel;
    }

    public PageModel getRelatedVideo(RequestDTO requestDTO)
    {
        YoutubeRequestDTO youtubeRequestDTO = (YoutubeRequestDTO) requestDTO;
        search.setRelatedToVideoId(youtubeRequestDTO.getId());
        search.setType(Constant.YOUTUBE_VIDEOS);
        search.setPageToken(youtubeRequestDTO.getPageToken());
        //Get normal data
        SearchListResponse response = null;
        try
        {
            response = search.execute();
            List<SearchResult> results = response.getItems();
            List<Item> items = new ArrayList<>();
//            ===================================
            String ids = "";
            for (SearchResult result : results)
            {
                ids = ids + result.getId().getVideoId() + ",";
            }
            searchForVideoDetails.setId(ids);
            VideoListResponse resultDetails = searchForVideoDetails.execute();
            for (Video videoYoutube : resultDetails.getItems())
            {
                items.add(convertItemToVideoModel(videoYoutube));
            }
            PageModel pageModel = new PageModel();
            pageModel.setItems(items);
            if (response.getPageInfo() != null)
            {
                pageModel.setNextPage(response.getNextPageToken());
            }

            return pageModel;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public PageModel getVideosByUser(RequestDTO requestDTO)
    {
        YoutubeRequestDTO youtubeRequestDTO = (YoutubeRequestDTO) requestDTO;
        search.setChannelId(youtubeRequestDTO.getId());
        search.setType(Constant.YOUTUBE_VIDEOS);
        search.setPageToken(youtubeRequestDTO.getPageToken());
        SearchListResponse response = null;
        try
        {
            response = search.execute();
            List<SearchResult> results = response.getItems();
            List<Item> items = new ArrayList<>();
//            ===================================
            String ids = "";
            for (SearchResult result : results)
            {
                ids = ids + result.getId().getVideoId() + ",";
            }
            searchForVideoDetails.setId(ids);
            VideoListResponse resultDetails = searchForVideoDetails.execute();
            for (Video videoYoutube : resultDetails.getItems())
            {
                items.add(convertItemToVideoModel(videoYoutube));
            }
            PageModel pageModel = new PageModel();
            pageModel.setItems(items);
            if (response.getPageInfo() != null)
            {
                pageModel.setNextPage(response.getNextPageToken());
            }

            return pageModel;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public PageModel getVideoByPlaylistId(RequestDTO requestDTO)
    {
        YoutubeRequestDTO youtubeRequestDTO = (YoutubeRequestDTO) requestDTO;
        searchVideoForPlayList.setPlaylistId(youtubeRequestDTO.getId());
        searchVideoForPlayList.setPageToken(youtubeRequestDTO.getPageToken());
        try
        {
            PlaylistItemListResponse response = searchVideoForPlayList.execute();
            List<PlaylistItem> playlistItems = response.getItems();
            List<Item> items = new ArrayList<>();
            String ids = "";
            for (PlaylistItem item : playlistItems)
            {
                ids = ids + item.getSnippet().getResourceId().getVideoId() + ",";
            }
            searchForVideoDetails.setId(ids);
            VideoListResponse resultDetails = searchForVideoDetails.execute();
            for (Video videoYoutube : resultDetails.getItems())
            {
                items.add(convertItemToVideoModel(videoYoutube));
            }
            for (int i = 0; i < playlistItems.size() - 1; i++)
            {
                ((VideoModel) items.get(i)).setIdOfPlayList(playlistItems.get(i).getId());
            }
            PageModel pageModel = new PageModel();
            pageModel.setItems(items);
            if (response.getPageInfo() != null)
            {
                pageModel.setNextPage(response.getNextPageToken());
            }
            return pageModel;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }


//    private VideoModel convertPlayListItemToVideoModel(PlaylistItem playlistItem)
//    {
//        VideoModel videoModel = new VideoModel();
//        videoModel.setName(playlistItem.getSnippet().getTitle());
//        playlistItem.getContentDetails().d
//        videoModel.setDescription(playlistItem.getSnippet().getDescription());
//        videoModel.setUrlThumbnail(playlistItem.getSnippet().getThumbnails().getMedium().getUrl());
//        videoModel.setUrl("https://www.youtube.com/watch?v=" + playlistItem.getId());
//        videoModel.setViews(playlistItem.getStatistics().getViewCount());
//        videoModel.setLikes(playlistItem.getStatistics().getLikeCount());
//        videoModel.setDisLikes(playlistItem.getStatistics().getDislikeCount());
//        String duration = playlistItem.getContentDetails().getDuration();
//        videoModel.setDuration(getTimeFromString(duration));
//        videoModel.setId(playlistItem.getId());
//        DateTime publishedAt = playlistItem.getSnippet().getPublishedAt();
//        videoModel.setPublishedAt(publishedAt.getValue());
//        UserModel userModel = new UserModel();
//        userModel.setName(playlistItem.getSnippet().getChannelTitle());
//        userModel.setId(playlistItem.getSnippet().getChannelId());
//        videoModel.setUserModel(userModel);
//        return videoModel;
//    }
}
