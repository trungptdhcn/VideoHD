package com.taa.videohd.ui.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.OnItemClick;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.taa.videohd.asyntask.AsyncTaskSearchData;
import com.taa.videohd.asyntask.YoutubeGetDirectLink;
import com.taa.videohd.asyntask.listener.AsyncTaskListener;
import com.taa.videohd.asyntask.listener.YoutubeDirectListener;
import com.taa.videohd.base.BaseFragment;
import com.taa.videohd.base.StringUtils;
import com.taa.videohd.download.YouTubeParser;
import com.taa.videohd.download.YoutubeInfo;
import com.taa.videohd.event.DownloadEvent;
import com.taa.videohd.sync.youtube.YoutubeConnector;
import com.taa.videohd.sync.youtube.request.YoutubeRequestDTO;
import com.taa.videohd.ui.activity.UserDetailActivity;
import com.taa.videohd.ui.activity.YoutubeVideoDetailActivity;
import com.taa.videohd.ui.adapter.ChoseDownloadAdapter;
import com.taa.videohd.ui.adapter.CommonAdapter;
import com.taa.videohd.ui.listener.EndlessScrollListener;
import com.taa.videohd.ui.model.*;
import com.taa.videohd.utils.Constant;
import com.taa.videohd.utils.Utils;
import com.trungpt.videodownloadmaster.R;
import de.greenrobot.event.EventBus;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by trung on 12/14/2015.
 */
public class YoutubeRelatedFragment extends BaseFragment implements AsyncTaskListener
//        , YoutubeDirectListener
{
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.list_view_related)
    ListView listView;
    CommonAdapter adapter;
    String nextPage;
    YoutubeRequestDTO requestDTO;
    VideoModel videoModel;
    ProgressDialog progressDialog;

    @Override
    public int getLayout()
    {
        return R.layout.related_videos_fragment;
    }

    @Override
    public void setDataToView(Bundle savedInstanceState)
    {
        videoModel = getArguments().getParcelable("video");
        if (videoModel != null)
        {
            addHeader();
            requestDTO = new YoutubeRequestDTO
                    .YoutubeRequestBuilder("")
                    .id(videoModel.getId())
                    .build();
            AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(getActivity(), Constant.HOST_NAME.YOUTUBE_RELATED, requestDTO);
            asyncTask.setListener(YoutubeRelatedFragment.this);
            asyncTask.execute();
            EndlessScrollListener endlessScrollListener = new EndlessScrollListener()
            {
                @Override
                public boolean onLoadMore(int page, int totalItemsCount)
                {
                    if (StringUtils.isNotEmpty(nextPage))
                    {
                        requestDTO.setPageToken(nextPage);
                        AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(getActivity(), Constant.HOST_NAME.YOUTUBE_RELATED, requestDTO);
                        asyncTask.setListener(YoutubeRelatedFragment.this);
                        asyncTask.execute();
                        return true;
                    }
                    else
                    {
                        return false;
                    }

                }
            };
            listView.setOnScrollListener(endlessScrollListener);
        }


    }

    @Override
    public void prepare()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void complete(Object obj)
    {
        PageModel page = (PageModel) obj;
        if (page != null)
        {
            List<Item> videos = page.getItems();
            nextPage = page.getNextPage();
            if (adapter == null)
            {
                adapter = new CommonAdapter(videos, getActivity());
                listView.setAdapter(adapter);
            }
            else
            {
                adapter.getVideos().addAll(videos);
                adapter.notifyDataSetChanged();
            }

        }
        progressBar.setVisibility(View.GONE);
    }

    @OnItemClick(R.id.list_view_related)
    public void itemClick(int position)
    {
        Intent intent = new Intent(getActivity(), YoutubeVideoDetailActivity.class);
        intent.putExtra("video", (Parcelable) adapter.getItem(position - 1));
        startActivity(intent);
        getActivity().finish();
    }

    public void addHeader()
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup userInfo = (ViewGroup) inflater.inflate(R.layout.layout_video_detail_information, listView, false);
        TextView tvName = (TextView) userInfo.findViewById(R.id.tvName);
        CheckBox cbExpand = (CheckBox) userInfo.findViewById(R.id.cbExpand);
        TextView tvViews = (TextView) userInfo.findViewById(R.id.tvViews);
        final TextView tvDescription = (TextView) userInfo.findViewById(R.id.tvDescription);
        RelativeLayout rlUser = (RelativeLayout) userInfo.findViewById(R.id.rlUser);
        TextView tvLikes = (TextView) userInfo.findViewById(R.id.tvLikes);
        TextView tvDisLikes = (TextView) userInfo.findViewById(R.id.tvDisLikes);
        ImageView ivDownload = (ImageView) userInfo.findViewById(R.id.ivDownload);
//        SharedPreferences prefs = getActivity().getSharedPreferences(
//                "com.taa.videohd", Context.MODE_PRIVATE);
//        int isDownload = prefs.getInt("key_isDownloadYoutube", -1);
//        int config = prefs.getInt("key_isDisplay", -1);
//        if (isDownload == 1 && config == 1)
//        {
            ivDownload.setVisibility(View.GONE);
//        }
//        else
//        {
//            ivDownload.setVisibility(View.GONE);
//        }
        AdView adView = (AdView) userInfo.findViewById(R.id.adView);
        Utils.loadBannerAds(adView);
        final TextView tvUserName = (TextView) userInfo.findViewById(R.id.layout_user_information_tvUserName);
        final TextView tvUserSubscrible = (TextView) userInfo.findViewById(R.id.layout_user_information_tvSub);
        final ImageView ivUserAvatar = (ImageView) userInfo.findViewById(R.id.layout_user_information_ivAvatar);

        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        new AsyncTask<Void, Void, UserModel>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected UserModel doInBackground(Void... params)
            {
                YoutubeConnector youtubeConnector = new YoutubeConnector(YoutubeRelatedFragment.this.getActivity());
                return youtubeConnector.getAuthorInfo(videoModel.getUserModel().getId());
            }

            @Override
            protected void onPostExecute(UserModel userModel)
            {
                super.onPostExecute(userModel);
                tvUserSubscrible.setText(userModel.getSubscrible() != null ? (nf.format(userModel.getSubscrible()) + " subscribes") : "0 subscribes");
                Glide.with(YoutubeRelatedFragment.this)
                        .load(userModel.getUrlAvatar())
                        .placeholder(R.drawable.ic_default)
                        .centerCrop()
                        .crossFade()
                        .into(ivUserAvatar);
                videoModel.setUserModel(userModel);
                tvUserName.setText(userModel.getName());
                progressBar.setVisibility(View.GONE);
            }
        }.execute();

        if (cbExpand.isChecked())
        {
            tvDescription.setVisibility(View.VISIBLE);
        }
        else
        {
            tvDescription.setVisibility(View.GONE);
        }
        cbExpand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    tvDescription.setVisibility(View.VISIBLE);
                }
                else
                {
                    tvDescription.setVisibility(View.GONE);
                }
            }
        });
        rlUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra("video", videoModel.getUserModel());
                intent.putExtra("host_name", Constant.HOST_NAME.YOUTUBE);
                startActivity(intent);
                getActivity().finish();
            }
        });
        tvName.setText(videoModel.getName());
        tvViews.setText(videoModel.getViews() != null ? nf.format(videoModel.getViews()) : "0");
        tvLikes.setText(videoModel.getLikes() != null ? nf.format(videoModel.getLikes()) : "0");
        tvDisLikes.setText(videoModel.getDisLikes() != null ? nf.format(videoModel.getDisLikes()) : "0");
        tvDescription.setText(videoModel.getDescription());
        tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        listView.addHeaderView(userInfo, null, false);

//        ivDownload.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                String url = "https://www.youtube.com/watch?v=" + videoModel.getId();
//                YoutubeGetDirectLink youtubeGetDirectLink = new YoutubeGetDirectLink(url);
//                youtubeGetDirectLink.setListener(YoutubeRelatedFragment.this);
//                youtubeGetDirectLink.execute();
////                Toast.makeText(YoutubeRelatedFragment.this.getActivity(), "We disable action download video from youtube because due to YouTube Terms of Service", Toast.LENGTH_LONG).show();
//            }
//        });

    }

//    @Override
//    public void prepareDirect()
//    {
//        progressDialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_DARK);
//        progressDialog.setMessage("Get data...!");
//        progressDialog.show();
//    }
//
//    @Override
//    public void completeDirect(Object obj)
//    {
//        progressDialog.dismiss();
//        List<YouTubeParser.VideoDownload> videoDownloads = (List<YouTubeParser.VideoDownload>) obj;
//        List<DirectLink> directLinks = new ArrayList<>();
//        for (YouTubeParser.VideoDownload videoDownload : videoDownloads)
//        {
//            if (videoDownload.stream != null)
//            {
//                DirectLink directLink = new DirectLink();
//                directLink.setUri(videoDownload.url.toString());
//                directLink.setUrlThumb(videoModel.getUrlThumbnail());
//                directLink.setName(videoModel.getName());
//                directLink.setUserName(videoModel.getUserModel().getName());
//                if (videoDownload.stream.c == YoutubeInfo.Container.FLV)
//                {
//                    directLink.setType(".FLV");
//                    directLink.setQuality("FLV");
//                }
//                else if (videoDownload.stream.c == YoutubeInfo.Container.GP3)
//                {
//                    directLink.setType(".3GP");
//                    directLink.setQuality("3GP");
//                }
//                else if (videoDownload.stream.c == YoutubeInfo.Container.MP4)
//                {
//                    directLink.setType(".mp4");
//                    directLink.setQuality("MP4");
//                }
//                else if (videoDownload.stream.c == YoutubeInfo.Container.WEBM)
//                {
//                    directLink.setType(".WEBM");
//                    directLink.setQuality("WEBM");
//                }
//                directLinks.add(directLink);
//            }
//        }
//        if (directLinks != null)
//        {
//            final android.support.v7.app.AlertDialog.Builder countryDialog = new android.support.v7.app.AlertDialog.Builder(YoutubeRelatedFragment.this.getActivity());
//            final ChoseDownloadAdapter choseDownloadAdapter = new ChoseDownloadAdapter(getActivity(), android.R.layout.simple_list_item_1, directLinks);
//            countryDialog.setSingleChoiceItems(choseDownloadAdapter, 0, new DialogInterface.OnClickListener()
//            {
//                @Override
//                public void onClick(DialogInterface dialog, int n)
//                {
//                    DirectLink directLink = choseDownloadAdapter.getItem(n);
//                    DownloadEvent downloadEvent = new DownloadEvent(directLink);
//                    EventBus.getDefault().post(downloadEvent);
//                    Toast.makeText(getActivity(), "Video downloading..!", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                }
//
//            });
//            countryDialog.setNegativeButton("Cancel", null);
//            countryDialog.setTitle("Chose Quantity");
//            countryDialog.show();
//        }
//    }
}
