package com.taa.videohd.sync.vimeo;


import com.taa.videohd.base.StringUtils;
import com.taa.videohd.sync.RequestDTO;
import com.taa.videohd.sync.RestfulService;
import com.taa.videohd.sync.vimeo.direct.VimeoDirectDTO;
import com.taa.videohd.sync.vimeo.direct.VimeoDirectProgressiveDTO;
import com.taa.videohd.sync.vimeo.direct.VimeoDirectStreamURLDTO;
import com.taa.videohd.sync.vimeo.request.VimeoRequestDTO;
import com.taa.videohd.sync.vimeo.response.*;
import com.taa.videohd.sync.vimeo.response.channel.ChannelVimeoResponseDTO;
import com.taa.videohd.sync.vimeo.response.channel.ChannelVimeoResponseDetailDTO;
import com.taa.videohd.sync.vimeo.response.user.VimeoResponseUserDTO;
import com.taa.videohd.ui.model.*;
import com.taa.videohd.utils.Constant;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Trung on 11/25/2015.
 */
public class VimeoConnector
{
    public PageModel getVideoWithCategory(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        VimeoResponseDTO vimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .getVideoWithCategory(vimeoRequestDTO.getCategory()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        return convertListVideoToPageModel(vimeoResponseDTO);
    }

    public PageModel convertListVideoToPageModel(VimeoResponseDTO vimeoResponseDTO)
    {
        List<Item> videos = new ArrayList<>();
        if (vimeoResponseDTO != null)
        {
            List<VimeoResponseDetailDTO> vimeoResponseDetailDTOs = vimeoResponseDTO.getVimeoResponseDetailDTOs();
            for (VimeoResponseDetailDTO vimeoResponseDetailDTO : vimeoResponseDetailDTOs)
            {
                VideoModel videoModel = new VideoModel();
                String uri = vimeoResponseDetailDTO.getUri();
                String regex = "[0-9].+$";
                String vimeo_id = "";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(uri);
                while (matcher.find())
                {
                    vimeo_id = matcher.group(0);
                }
                videoModel.setUrl("https://vimeo.com/" + vimeo_id);
                if (vimeoResponseDetailDTO.getVimeoPicturesDTO() != null)
                {
                    VimeoResponsePicturesDTO vimeoResponsePicturesDTO = vimeoResponseDetailDTO.getVimeoPicturesDTO();
                    List<VimeoResponsePictureSizeDTO> vimeoResponsePicturesDTOs
                            = vimeoResponsePicturesDTO.getVimeoPicturesSizeDTO();
                    videoModel.setUrlThumbnail(vimeoResponseDetailDTO.getVimeoPicturesDTO().getVimeoPicturesSizeDTO().get(vimeoResponsePicturesDTOs.size() - 1).getLink());
                }
                videoModel.setName(vimeoResponseDetailDTO.getName());
                videoModel.setDescription(vimeoResponseDetailDTO.getDescription());
                videoModel.setLikes(vimeoResponseDetailDTO.getVimeoResponseMetadataDTO()
                        .getVimeoResponseMetaDataConnectionsDTO().getVimeoResponseLikesDTO().getTotal());
                videoModel.setViews(vimeoResponseDetailDTO.getVimeoResponseStatsDTO().getPlays());
                videoModel.setDisLikes(new BigInteger("0"));
                videoModel.setId(vimeo_id);
                int duration = vimeoResponseDetailDTO.getDuration();
                int minutes = duration / (60);
                int seconds = duration % 60;
                String str = String.format("%d:%02d", minutes, seconds);
                videoModel.setDuration(str);
                SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_RFC_3339_FOMATED_VIMEO);
                try
                {
                    Long dateCreated = sdf.parse(vimeoResponseDetailDTO.getCreated_time()).getTime();
                    videoModel.setPublishedAt(dateCreated);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                if (vimeoResponseDetailDTO.getVimeoResponseUserDetailDTO() != null)
                {
                    UserModel userModel = new UserModel();
                    VimeoResponseUserDetailDTO responseUserDTO = vimeoResponseDetailDTO.getVimeoResponseUserDetailDTO();
                    userModel.setName(responseUserDTO.getName());
                    String uriUser = responseUserDTO.getUri();
                    String userId = "";
                    Matcher matcherUser = pattern.matcher(uriUser);
                    while (matcherUser.find())
                    {
                        userId = matcherUser.group(0);
                    }
                    userModel.setId(userId);
                    userModel.setSubscrible
                            (responseUserDTO.getVimeoResponseMetaDataDTO()
                                    .getVimeoResponseMetaDataConnectionsDTO()
                                    .getVimeoResponseFollowingDTO().getTotal());
                    if (responseUserDTO.getVimeoResponsePicturesDTO() != null)
                    {
                        List<VimeoResponsePictureSizeDTO> vimeoResponsePictureSizeDTOs = responseUserDTO
                                .getVimeoResponsePicturesDTO().getVimeoPicturesSizeDTO();
                        if (vimeoResponsePictureSizeDTOs != null && vimeoResponsePictureSizeDTOs.size() >= 1)
                        {
                            userModel.setUrlAvatar(vimeoResponsePictureSizeDTOs.get(1).getLink());
                        }
                    }
                    userModel.setVideoCounts(responseUserDTO.getVimeoResponseMetaDataDTO()
                            .getVimeoResponseMetaDataConnectionsDTO().getVimeoResponseVideoDTO().getTotal());
                    videoModel.setUserModel(userModel);
                }

                videos.add(videoModel);
            }
        }
        PageModel videoPage = new PageModel();
        videoPage.setItems(videos);
        videoPage.setNextPage(vimeoResponseDTO.getPaging().getNextPage());
        return videoPage;
    }

    public PageModel getVideoRelated(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        VimeoResponseDTO vimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .getVideoRelated(vimeoRequestDTO.getId()
                        , 5
                        , vimeoRequestDTO.getPageToken(), "related");
        return convertListVideoToPageModel(vimeoResponseDTO);
    }

    public PageModel getVideoByUserId(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        VimeoResponseDTO vimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .getVideoByUserId(vimeoRequestDTO.getId()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        return convertListVideoToPageModel(vimeoResponseDTO);
    }

    public PageModel getVideoByChannelId(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        VimeoResponseDTO vimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .getVideoByChannelId(vimeoRequestDTO.getId()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        return convertListVideoToPageModel(vimeoResponseDTO);
    }

    public PageModel getVideoOfCategory(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        VimeoResponseDTO vimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .getVideoOfCategory(vimeoRequestDTO.getCategory()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        return convertListVideoToPageModel(vimeoResponseDTO);
    }

    public List<DirectLink> getDirectLink(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        VimeoResponseDTO vimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .getVideoByChannelId(vimeoRequestDTO.getId()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        PageModel pageModel = convertListVideoToPageModel(vimeoResponseDTO);
        List<Item> items = pageModel.getItems();
        List<DirectLink> directLinks = new ArrayList<>();
        if (items != null && items.size() > 0)
        {
            for (Item item : items)
            {
                VideoModel videoModel = (VideoModel) item;
                VimeoDirectDTO vimeoDirectDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO_PLAYER).getDirectLink(videoModel.getId());
                VimeoDirectStreamURLDTO vimeoDirectStreamURLDTO = vimeoDirectDTO.getVimeoDirectRequestDTO().getVimeoDirectFileDTO().getVimeoDirectStreamURLDTO();
                if (vimeoDirectStreamURLDTO != null && StringUtils.isNotEmpty(vimeoDirectStreamURLDTO.getUrl()))
                {
                    String hls_url = vimeoDirectDTO.getVimeoDirectRequestDTO()
                            .getVimeoDirectFileDTO().getVimeoDirectStreamURLDTO().getUrl();
                    if (StringUtils.isNotEmpty(hls_url))
                    {
//                        Uri urlVideo = Uri.parse(hls_url);
                        int videoType = vimeoDirectDTO.getVimeoDirectRequestDTO()
                                .getVimeoDirectFileDTO().getVimeoDirectStreamURLDTO().getType();
                        DirectLink directLink = new DirectLink();
                        directLink.setUri(hls_url);
                        directLink.setName(videoModel.getName());
                        directLink.setUrlThumb(videoModel.getUrlThumbnail());
                        directLink.setUserName(videoModel.getUserModel().getName());
                        directLink.setDuration(videoModel.getDuration());
                        directLink.setTypePlay(videoType);
                        directLink.setType(Constant.VIDEO_TYPE_HLS);
                        directLinks.add(directLink);
                    }
                }
                else
                {
                    List<VimeoDirectProgressiveDTO> vimeoDirectProgressiveDTOs = vimeoDirectDTO.getVimeoDirectRequestDTO()
                            .getVimeoDirectFileDTO().getVimeoDirectProgressiveDTOs();
                    if (vimeoDirectProgressiveDTOs != null && vimeoDirectProgressiveDTOs.size() > 0)
                    {
//                        Uri urlVideo = Uri.parse(vimeoDirectProgressiveDTOs.get(0).getUrl());
                        int videoType = vimeoDirectProgressiveDTOs.get(0).getType();
                        DirectLink directLink = new DirectLink();
                        directLink.setName(videoModel.getName());
                        directLink.setUrlThumb(videoModel.getUrlThumbnail());
                        directLink.setUserName(videoModel.getUserModel().getName());
                        directLink.setDuration(videoModel.getDuration());
                        directLink.setUri(vimeoDirectProgressiveDTOs.get(0).getUrl());
                        directLink.setTypePlay(videoType);
                        directLink.setType(Constant.VIDEO_TYPE_MP4);
                        directLinks.add(directLink);
                    }
                }
            }
        }
        return directLinks;
    }

    //===================================SEARCH==============================
    public PageModel search(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        if (StringUtils.isNotEmpty(vimeoRequestDTO.getKeyWord()))
        {
            if (vimeoRequestDTO.getType().equals(Constant.VIMEO_VIDEOS))
            {
                PageModel videoPage = searchVideo(vimeoRequestDTO);
                return videoPage;
            }
            else if (vimeoRequestDTO.getType().equals(Constant.VIMEO_USERS))
            {
                return searchUser(requestDTO);
            }
            else
            {
                return searchChannel(requestDTO);
            }
        }
        return null;
    }

    private PageModel searchVideo(VimeoRequestDTO vimeoRequestDTO)
    {
        VimeoResponseDTO vimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .search("videos"
                        , vimeoRequestDTO.getKeyWord()
                        , vimeoRequestDTO.getSort()
                        , vimeoRequestDTO.getDirection()
                        , vimeoRequestDTO.getFilter()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        return convertListVideoToPageModel(vimeoResponseDTO);
    }

    public PageModel searchUser(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        VimeoResponseUserDTO vimeoResponseUserDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .searchUser(vimeoRequestDTO.getKeyWord()
                        , vimeoRequestDTO.getSort()
                        , vimeoRequestDTO.getDirection()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        List<VimeoResponseUserDetailDTO> vimeoResponseUserDetailDTOs = vimeoResponseUserDTO.getVimeoResponseUserDetailDTOs();
        List<Item> items = new ArrayList<>();
        if (vimeoResponseUserDetailDTOs != null && vimeoResponseUserDetailDTOs.size() > 0)
        {
            for (VimeoResponseUserDetailDTO vimeoResponseUserDetailDTO : vimeoResponseUserDetailDTOs)
            {
                items.add(convertUserToUserModel(vimeoResponseUserDetailDTO));
            }
        }
        PageModel videoPage = new PageModel();
        videoPage.setItems(items);
        videoPage.setNextPage(vimeoResponseUserDTO.getPaging().getNextPage());
        return videoPage;
    }

    public UserModel convertUserToUserModel(VimeoResponseUserDetailDTO vimeoResponseUserDetailDTO)
    {
        String regex = "[0-9].+$";
        Pattern pattern = Pattern.compile(regex);
        String uriUser = vimeoResponseUserDetailDTO.getUri();
        String userId = "";
        Matcher matcherUser = pattern.matcher(uriUser);
        while (matcherUser.find())
        {
            userId = matcherUser.group(0);
        }

        UserModel userModel = new UserModel();
        userModel.setId(userId);
        userModel.setName(vimeoResponseUserDetailDTO.getName());
        userModel.setDescription(vimeoResponseUserDetailDTO.getBio());
        userModel.setSubscrible
                (vimeoResponseUserDetailDTO.getVimeoResponseMetaDataDTO()
                        .getVimeoResponseMetaDataConnectionsDTO()
                        .getVimeoResponseFollowingDTO().getTotal());
        if (vimeoResponseUserDetailDTO.getVimeoResponsePicturesDTO() != null)
        {
            List<VimeoResponsePictureSizeDTO> vimeoResponsePictureSizeDTOs = vimeoResponseUserDetailDTO
                    .getVimeoResponsePicturesDTO().getVimeoPicturesSizeDTO();
            if (vimeoResponsePictureSizeDTOs != null && vimeoResponsePictureSizeDTOs.size() >= 1)
            {
                userModel.setUrlAvatar(vimeoResponsePictureSizeDTOs.get(1).getLink());
            }
        }
        userModel.setVideoCounts(vimeoResponseUserDetailDTO.getVimeoResponseMetaDataDTO()
                .getVimeoResponseMetaDataConnectionsDTO().getVimeoResponseVideoDTO().getTotal());
        return userModel;
    }

    public PageModel searchChannel(RequestDTO requestDTO)
    {
        VimeoRequestDTO vimeoRequestDTO = (VimeoRequestDTO) requestDTO;
        ChannelVimeoResponseDTO channelVimeoResponseDTO = RestfulService.getInstance(Constant.HOST_NAME.VIMEO)
                .searchChannel(vimeoRequestDTO.getKeyWord()
                        , vimeoRequestDTO.getSort()
                        , vimeoRequestDTO.getDirection()
                        , 5
                        , vimeoRequestDTO.getPageToken());
        List<ChannelVimeoResponseDetailDTO> channelVimeoResponseDetailDTOs = channelVimeoResponseDTO.getChannelVimeoResponseDetailDTOs();
        List<Item> items = new ArrayList<>();
        if (channelVimeoResponseDetailDTOs != null && channelVimeoResponseDetailDTOs.size() > 0)
        {
            for (ChannelVimeoResponseDetailDTO channelVimeoResponseDetailDTO : channelVimeoResponseDetailDTOs)
            {
                items.add(convertChannelToPlayList(channelVimeoResponseDetailDTO));
            }
        }
        PageModel videoPage = new PageModel();
        videoPage.setItems(items);
        videoPage.setNextPage(channelVimeoResponseDTO.getPaging().getNextPage());
        return videoPage;
    }

    private PlayListModel convertChannelToPlayList(ChannelVimeoResponseDetailDTO channelVimeoResponseDetailDTO)
    {
        PlayListModel playListModel = new PlayListModel();
        String uri = channelVimeoResponseDetailDTO.getUri();
        String regex = "[0-9].+$";
        String channel_id = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(uri);
        while (matcher.find())
        {
            channel_id = matcher.group(0);
        }
        playListModel.setId(channel_id);
        if (channelVimeoResponseDetailDTO.getVimeoPicturesDTO() != null)
        {
            VimeoResponsePicturesDTO vimeoResponsePicturesDTO = channelVimeoResponseDetailDTO.getVimeoPicturesDTO();
            List<VimeoResponsePictureSizeDTO> vimeoResponsePicturesDTOs
                    = vimeoResponsePicturesDTO.getVimeoPicturesSizeDTO();
            playListModel.setThumbnail(channelVimeoResponseDetailDTO.getVimeoPicturesDTO().getVimeoPicturesSizeDTO().get(vimeoResponsePicturesDTOs.size() - 1).getLink());
        }
        playListModel.setName(channelVimeoResponseDetailDTO.getName());
        playListModel.setDescription(channelVimeoResponseDetailDTO.getDescription());
//        playListModel.setSubscrible(channelVimeoResponseDetailDTO.getVimeoResponseMetadataDTO()
//                .getVimeoResponseMetaDataConnectionsDTO().getUserOfChannel().getTotal());
        playListModel.setVideoCount(channelVimeoResponseDetailDTO.getVimeoResponseMetadataDTO()
                .getVimeoResponseMetaDataConnectionsDTO().getVimeoResponseVideoDTO().getTotal());
//        if (channelVimeoResponseDetailDTO.getImageCover() != null)
//        {
//            ChannelVimeoHeaderResponseDTO channelVimeoHeaderResponseDTO
//                    = channelVimeoResponseDetailDTO.getImageCover();
//            if (channelVimeoHeaderResponseDTO.getSizes() != null
//                    && channelVimeoHeaderResponseDTO.getSizes().size() > 0)
//            {
//                playListModel.setUrlCover(channelVimeoHeaderResponseDTO.getSizes().get(
//                        channelVimeoHeaderResponseDTO.getSizes().size() - 1).getLink());
//            }
//        }
        UserModel userModel = new UserModel();
        if (channelVimeoResponseDetailDTO.getVimeoResponseUserDetailDTO() != null)
        {

            VimeoResponseUserDetailDTO vimeoResponseUserDetailDTO
                    = channelVimeoResponseDetailDTO.getVimeoResponseUserDetailDTO();
            userModel.setName(vimeoResponseUserDetailDTO.getName());
            if (vimeoResponseUserDetailDTO.getVimeoResponsePicturesDTO() != null &&
                    vimeoResponseUserDetailDTO.getVimeoResponsePicturesDTO().getVimeoPicturesSizeDTO() != null
                    && vimeoResponseUserDetailDTO.getVimeoResponsePicturesDTO().getVimeoPicturesSizeDTO().size() > 0)
            {
                List<VimeoResponsePictureSizeDTO> avatarOfUserOfChannel = vimeoResponseUserDetailDTO.getVimeoResponsePicturesDTO()
                        .getVimeoPicturesSizeDTO();
                userModel.setUrlAvatar(avatarOfUserOfChannel.get(avatarOfUserOfChannel.size() - 1).getLink());
            }
            Matcher userIdMatcher = pattern.matcher(vimeoResponseUserDetailDTO.getUri());
            while (userIdMatcher.find())
            {
                String user_id = userIdMatcher.group(0);
                userModel.setId(user_id);
            }
        }
        playListModel.setUserModel(userModel);
        return playListModel;
    }
}
