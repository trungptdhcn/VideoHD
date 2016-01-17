package com.taa.videohd.sync;

import com.taa.videohd.sync.dailymotion.DailymotionDTO;
import com.taa.videohd.sync.dailymotion.DailymotionPlayListDTO;
import com.taa.videohd.sync.dailymotion.DailymotionUserDTO;
import com.taa.videohd.sync.vimeo.direct.VimeoDirectDTO;
import com.taa.videohd.sync.vimeo.response.VimeoResponseDTO;
import com.taa.videohd.sync.vimeo.response.channel.ChannelVimeoResponseDTO;
import com.taa.videohd.sync.vimeo.response.user.VimeoResponseUserDTO;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Trung on 5/30/2015.
 */
public interface RestfulServiceIn
{
//    @GET("/latest")
//    public void getListFeedsWithParams(@Query("page") Integer page, @Query("limit") Integer limit,
//                                       @Query("gid") String gid, @Query("f") Boolean f, @Query("type") String type, Callback<ListFeedDTO> callback);
//
//    @GET("/latest")
//    public  void getListFeeds(Callback<ListFeedDTO> callback);
//
//    @GET("/post")
//    public FeedDetailDTO getFeedDetail(@Query("slug") String slug);
//    @GET("/post")
//    public void getFeedDetail(@Query("slug") String slug, Callback<FeedDetailDTO> callback);

//    @Headers({
//            "Accept: application/vnd.vimeo.*+json; version=3.2",
//            "Authorization: bearer 32b89491aaa8dd01957ead8d2181848a"
//    })
//    @GET("/categories")
//    public VimeoCategoriesDTO getCategories();

    @Headers({
            "Accept: application/vnd.vimeo.*+json; version=3.2",
            "Authorization: bearer 32b89491aaa8dd01957ead8d2181848a"
    })
    @GET("/categories/{category}/videos")
    public VimeoResponseDTO getVideoOfCategory(@Path("category") String category,
                                               @Query("per_page") int perpage, @Query("page") String page);

    @Headers({
            "Accept: application/vnd.vimeo.*+json; version=3.2",
            "Authorization: bearer 32b89491aaa8dd01957ead8d2181848a"
    })
    @GET("/{videos}")
    public VimeoResponseDTO search(@Path("videos") String slug
            , @Query("query") String query
            , @Query("sort") String sort
            , @Query("direction") String direction
            , @Query("filter") String filter
            , @Query("per_page") int perpage
            , @Query("page") String page);

    @Headers({
            "Accept: application/vnd.vimeo.*+json; version=3.2",
            "Authorization: bearer 32b89491aaa8dd01957ead8d2181848a"
    })
    @GET("/categories/{category}/videos")
    public VimeoResponseDTO getVideoWithCategory(@Path("category") String category, @Query("per_page") int perpage, @Query("page") String page);

    @Headers({
            "Accept: application/vnd.vimeo.*+json; version=3.2",
            "Authorization: bearer 32b89491aaa8dd01957ead8d2181848a"
    })
    @GET("/videos/{id}/videos")
    public VimeoResponseDTO getVideoRelated(@Path("id") String video_id
            , @Query("per_page") int perpage
            , @Query("page") String page
            , @Query("filter") String filter);

    @Headers({
            "Accept: application/vnd.vimeo.*+json; version=3.2",
            "Authorization: bearer 32b89491aaa8dd01957ead8d2181848a"
    })
    @GET("/users/{user_id}/videos")
    public VimeoResponseDTO getVideoByUserId(@Path("user_id") String user_id
            , @Query("per_page") int perpage
            , @Query("page") String page);

    @Headers({
            "Accept: application/vnd.vimeo.*+json; version=3.2",
            "Authorization: bearer 32b89491aaa8dd01957ead8d2181848a"
    })
    @GET("/users/{channel_id}/videos")
    public VimeoResponseDTO getVideoByChannelId(@Path("channel_id") String channel_id
            , @Query("per_page") int perpage
            , @Query("page") String page);


    @Headers({
            "Accept: application/vnd.vimeo.*+json; version=3.2",
            "Authorization: bearer 32b89491aaa8dd01957ead8d2181848a"
    })
    @GET("/users")
    public VimeoResponseUserDTO searchUser(
            @Query("query") String query
            , @Query("sort") String sort
            , @Query("direction") String direction
            , @Query("per_page") int perpage
            , @Query("page") String page);


    @Headers({
            "Accept: application/vnd.vimeo.*+json; version=3.2",
            "Authorization: bearer 32b89491aaa8dd01957ead8d2181848a"
    })
    @GET("/channels")
    public ChannelVimeoResponseDTO searchChannel(
            @Query("query") String query
            , @Query("sort") String sort
            , @Query("direction") String direction
            , @Query("per_page") int perpage
            , @Query("page") String page);


    @GET("/video/{id}/config")
    public void getDirectLink(@Path("id") String id, Callback<VimeoDirectDTO> callback);

    @GET("/video/{id}/config")
    public VimeoDirectDTO getDirectLink(@Path("id") String id);
//
//
//    @Headers({
//            "Accept: application/vnd.vimeo.*+json; version=3.2",
//            "Authorization: bearer 32b89491aaa8dd01957ead8d2181848a"
//    })
//    @GET("/channels/{channel_id}/videos")
//    public VimeoResponseDTO getVideosByUser(@Path("channel_id") String category, @Query("per_page") int perpage, @Query("page") String page);


//    @Headers({
//            "Accept: application/vnd.vimeo.*+json; version=3.2",
//            "Authorization: bearer 32b89491aaa8dd01957ead8d2181848a"
//    })
//    @GET("/videos/{video_id}")
//    public VimeoDTO getVideoById(@Path("video_id") String video_id);

    //    @GET("/videos")
//    public DailymotionDTO getMostPopularVideoDailymotion(@Query("fields") String fields
//            , @Query("flags") String flags, @Query("sort") String sort
//            , @Query("page") Long page, @Query("limit") Long limit);
//    @GET("/channel/{id}/videos")
//    public DailymotionDTO getVideosOfChannel(@Path("id") String id
//            , @Query("fields") String fields
//            , @Query("sort") String sort
//            , @Query("page") Long page
//            , @Query("limit") Long limit);
//
//    ============================Dailymotion========================
    @GET("/videos")
    public DailymotionDTO search(
            @Query("search") String keywords
            , @Query("fields") String fields
            , @Query("flags") String flags
            , @Query("created_after") Long createdAfter
            , @Query("created_before") Long createdBefore
            , @Query("sort") String sort
            , @Query("country") String country
            , @Query("longer_than") Integer longerThan
            , @Query("shorter_than") Integer shorterThan
            , @Query("page") int page
            , @Query("limit") int limit
    );

    @GET("/users")
    public DailymotionUserDTO searchUser(
            @Query("search") String keywords
            , @Query("fields") String fields
            , @Query("sort") String sort
            , @Query("page") int page
            , @Query("limit") int limit
    );


    @GET("/playlists")
    public DailymotionPlayListDTO searchPlaylist(
            @Query("search") String keywords
            , @Query("fields") String fields
            , @Query("sort") String sort
            , @Query("page") int page
            , @Query("limit") int limit
    );
//
    @GET("/playlist/{playlist_id}/videos")
    public DailymotionDTO getVideoByPlaylistId(
            @Path("playlist_id") String playListId
            , @Query("fields") String fields
            , @Query("page") int page
            , @Query("limit") int limit
    );

    @GET("/videos")
    public DailymotionDTO mostPopular(
            @Query("fields") String fields
            , @Query("country") String country
            , @Query("flags") String flags
            , @Query("sort") String sort
            , @Query("page") int page
            , @Query("limit") int limit);

    @GET("/video/{id}/related")
    public DailymotionDTO getVideoRelated(
            @Path("id") String id
            , @Query("fields") String fields
            , @Query("page") int page
            , @Query("limit") int limit);

  @GET("/user/{id}/videos")
    public DailymotionDTO getVideoByUserId(
          @Path("id") String id
          , @Query("fields") String fields
          , @Query("page") int page
          , @Query("limit") int limit);
}
