package com.taa.videohd.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import butterknife.Bind;
import butterknife.OnItemClick;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.taa.videohd.asyntask.AsyncTaskMostPopular;
import com.taa.videohd.asyntask.listener.AsyncTaskListener;
import com.taa.videohd.base.BaseFragment;
import com.taa.videohd.base.StringUtils;
import com.taa.videohd.sync.vimeo.request.VimeoRequestDTO;
import com.taa.videohd.ui.activity.VimeoVideoDetailActivity;
import com.taa.videohd.ui.activity.YoutubeSearchActivity;
import com.taa.videohd.ui.adapter.CommonAdapter;
import com.taa.videohd.ui.adapter.CountryAdapter;
import com.taa.videohd.ui.listener.EndlessScrollListener;
import com.taa.videohd.ui.model.Item;
import com.taa.videohd.ui.model.KeyValue;
import com.taa.videohd.ui.model.PageModel;
import com.taa.videohd.utils.Constant;
import com.taa.videohd.utils.ResourceUtils;
import com.trungpt.videodownloadmaster.R;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by trung on 12/11/2015.
 */
public class VimeoFragment extends BaseFragment implements AsyncTaskListener, View.OnClickListener
{
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.menuOption)
    FloatingActionMenu menuOption;
    @Bind(R.id.fab_up)
    com.melnykov.fab.FloatingActionButton fabUp;
    @Bind(R.id.fab_search)
    com.github.clans.fab.FloatingActionButton fabSearch;
    @Bind(R.id.fab_filter)
    com.github.clans.fab.FloatingActionButton fabFilter;
    CountryAdapter categoryAdapter;
    CommonAdapter adapter;
    String nextPage;
    VimeoRequestDTO requestDTO;
    private int mPreviousVisibleItem;
    InterstitialAd mInterstitialAd;
    @Override
    public int getLayout()
    {
        return R.layout.vimeo_fragment;
    }

    @Override
    public void setDataToView(Bundle savedInstanceState)
    {
        mInterstitialAd= new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.download_plus_integrate));
        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdClosed()
            {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.layout_most_popular2, listView, false);
        AdView adView = (AdView) header.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
            .addTestDevice("85875FC3563C88A48AD872E6EF2EB7")
                .build();
        adView.loadAd(adRequest);
        listView.addHeaderView(header, null, false);
        List<KeyValue> category = null;
        try
        {
            category = ResourceUtils.getStringResource(VimeoFragment.this.getActivity(), R.xml.vimeo_category);
            categoryAdapter = new CountryAdapter(VimeoFragment.this.getActivity(),
                    R.layout.spinner_selector_item, category);
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        requestDTO = new VimeoRequestDTO
                .VimeoRequestBuilder("")
                .category("music")
                .build();
        AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(getActivity(), Constant.HOST_NAME.VIMEO, requestDTO);
        asyncTask.setListener(this);
        asyncTask.execute();
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener()
        {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    requestDTO.setPageToken(page + "");
                    AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(getActivity(), Constant.HOST_NAME.VIMEO, requestDTO);
                    asyncTask.setListener(VimeoFragment.this);
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
        menuOption.setOnMenuButtonClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (menuOption.isOpened())
                {
                }
                menuOption.toggle(true);
            }
        });
        menuOption.setClosedOnTouchOutside(true);
        fabFilter.setOnClickListener(this);
        fabSearch.setOnClickListener(this);
    }

    private void requestNewInterstitial()
    {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
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

    @OnItemClick(R.id.list_view)
    public void itemClick(int position)
    {
        Intent intent = new Intent(getActivity(), VimeoVideoDetailActivity.class);
        intent.putExtra("video", (Parcelable) adapter.getItem(position));
        intent.putExtra("host_name", Constant.HOST_NAME.VIMEO);
        startActivity(intent);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fab_filter:
                final AlertDialog.Builder countryDialog = new AlertDialog.Builder(VimeoFragment.this.getActivity());
                countryDialog.setSingleChoiceItems(categoryAdapter, 0, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int n)
                    {
                        adapter = null;
                        requestDTO = new VimeoRequestDTO
                                .VimeoRequestBuilder("")
                                .category(categoryAdapter.getItem(n).getKey())
                                .build();
                        AsyncTaskMostPopular asyncTask = new AsyncTaskMostPopular(getActivity(), Constant.HOST_NAME.VIMEO, requestDTO);
                        asyncTask.setListener(VimeoFragment.this);
                        asyncTask.execute();
                        dialog.dismiss();
                    }

                });
                countryDialog.setNegativeButton("Cancel", null);
                countryDialog.setTitle("Chose countries");
                countryDialog.show();
                mInterstitialAd.show();
                break;
//            case R.id.fab_up:
//                break;
            case R.id.fab_search:
                Intent intent = new Intent(getActivity(), YoutubeSearchActivity.class);
                intent.putExtra("host_name", Constant.HOST_NAME.VIMEO);
                startActivity(intent);
                break;
        }
    }
}
