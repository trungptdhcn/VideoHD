package com.taa.videohd.ui.activity;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import com.google.android.gms.ads.AdView;
import com.taa.videohd.asyntask.AsyncTaskSearchData;
import com.taa.videohd.asyntask.listener.AsyncTaskListener;
import com.taa.videohd.base.StringUtils;
import com.taa.videohd.sync.RequestDTO;
import com.taa.videohd.sync.dailymotion.DailymotionRequestDTO;
import com.taa.videohd.sync.vimeo.request.VimeoRequestDTO;
import com.taa.videohd.sync.youtube.request.YoutubeRequestDTO;
import com.taa.videohd.ui.adapter.CommonAdapter;
import com.taa.videohd.ui.adapter.SpinnerCategoryAdapter;
import com.taa.videohd.ui.adapter.SpinnerCommonAdapter;
import com.taa.videohd.ui.listener.EndlessScrollListener;
import com.taa.videohd.ui.model.*;
import com.taa.videohd.utils.Constant;
import com.taa.videohd.utils.ResourceUtils;
import com.taa.videohd.utils.Utils;
import com.trungpt.videodownloadmaster.R;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class YoutubeSearchActivity extends AppCompatActivity implements AsyncTaskListener
{
    String[] uploads = new String[]{Constant.YOUTUBE_UPLOAD_ALL
            , Constant.YOUTUBE_UPLOAD_TODAY, Constant.YOUTUBE_UPLOAD_THISWEEK
            , Constant.YOUTUBE_UPLOAD_THISMONTH, Constant.YOUTUBE_UPLOAD_THISYEAR};

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.fab_up)
    com.melnykov.fab.FloatingActionButton fabUp;
    private MenuItem mSearchAction;
    Dialog dialog;

    RequestDTO requestDTO;
    CommonAdapter adapter;
    String nextPage;
    String keyword = "";
    private Constant.HOST_NAME host_name;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_search_activity);
        ButterKnife.bind(this);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.ads_layout, listView, false);
        AdView adView = (AdView) header.findViewById(R.id.adView);
        Utils.loadBannerAds(adView);
        listView.addHeaderView(header, null, false);
        host_name = (Constant.HOST_NAME) getIntent().getSerializableExtra("host_name");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener()
        {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount)
            {
                if (StringUtils.isNotEmpty(nextPage))
                {
                    switch (host_name)
                    {
                        case YOUTUBE:
                            ((YoutubeRequestDTO) requestDTO).setPageToken(nextPage);

                            break;
                        case VIMEO:
                            ((VimeoRequestDTO) requestDTO).setPageToken(page + "");
                            break;
                        case DAILYMOTION:
                            ((DailymotionRequestDTO) requestDTO).setPage(page);
                            break;
                    }
                    AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(YoutubeSearchActivity.this
                            , host_name, requestDTO);
                    asyncTask.setListener(YoutubeSearchActivity.this);
                    asyncTask.execute();
                    return true;
                }
                else
                {
                    return false;
                }
            }
        };
        fabUp.attachToListView(listView, null, endlessScrollListener);
        fabUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_youtube_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                adapter = null;
                keyword = query;
                switch (host_name)
                {
                    case YOUTUBE:
                        requestDTO = new YoutubeRequestDTO
                                .YoutubeRequestBuilder(keyword)
                                .type("video")
                                .build();
                        break;
                    case VIMEO:
                        requestDTO = new VimeoRequestDTO
                                .VimeoRequestBuilder(keyword)
                                .type(Constant.VIMEO_VIDEOS)
                                .build();
                        break;
                    case DAILYMOTION:
                        requestDTO = new DailymotionRequestDTO
                                .DailymotionRequestBuilder(keyword)
                                .type(Constant.DAILYMOTION_VIDEOS)
                                .fields(Constant.DAILYMOTION_VIDEO_FIELDS)
                                .flags(Constant.DAILYMOTION_VIDEO_FLAG)
                                .page(1)
                                .limit(10)
                                .build();
                        break;
                }
                search();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {
                keyword = query;
                return true;
            }

        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_search:
                return true;
            case R.id.advanced_filter:
                if (host_name.equals(Constant.HOST_NAME.YOUTUBE))
                {
                    handleFilterYoutube();
                }
                else if (host_name.equals(Constant.HOST_NAME.VIMEO))
                {
                    handleFilterVimeo();
                }
                else
                {
                    handleFilterDailymotion();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void handleFilterDailymotion()
    {
        SpinnerCommonAdapter choseTimePublish;
        SpinnerCommonAdapter choseDurationAdapter;
        SpinnerCommonAdapter choseTypeAdapter;

        final SpinnerCategoryAdapter choseSortAdapter;
        SpinnerCategoryAdapter choseCountryAdapter;

        dialog = new Dialog(this);
        dialog.setTitle("Advanced Filter");
        dialog.setContentView(R.layout.dialog_dailymotion_filter);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        final Spinner spType = (Spinner) dialog.findViewById(R.id.spinner_type);
        final Spinner spSort = (Spinner) dialog.findViewById(R.id.spinner_sort);
        final Spinner spPublish = (Spinner) dialog.findViewById(R.id.spinner_publish);
        final Spinner spDuration = (Spinner) dialog.findViewById(R.id.spinner_duration);
        final Spinner spCountry = (Spinner) dialog.findViewById(R.id.spinner_country);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
        Button btOk = (Button) dialog.findViewById(R.id.btOk);
        try
        {
            String[] type = new String[]{Constant.DAILYMOTION_VIDEOS, Constant.DAILYMOTION_USERS, Constant.DAILYMOTION_PLAYLIST};
            choseTypeAdapter = new SpinnerCommonAdapter(this,
                    R.layout.spinner_selector_item, type);
            choseTypeAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spType.setAdapter(choseTypeAdapter);

            List<KeyValue> sort = ResourceUtils.getStringResource(this, R.xml.dailymotion_sort);
            choseSortAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, sort);
            choseSortAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spSort.setAdapter(choseSortAdapter);

            String[] duration = new String[]{"Any", "Short", "Med", "Long"};
            choseDurationAdapter = new SpinnerCommonAdapter(this,
                    R.layout.spinner_selector_item, duration);
            choseDurationAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spDuration.setAdapter(choseDurationAdapter);

            List<KeyValue> countries = ResourceUtils.getCountries();
            choseCountryAdapter = new SpinnerCategoryAdapter(this, R.layout.spinner_selector_item, countries);
            choseCountryAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spCountry.setAdapter(choseCountryAdapter);


            String[] publishAt = new String[]{"Any", "Today", "This week", "This month", "This year"};
            choseTimePublish = new SpinnerCommonAdapter(this,
                    R.layout.spinner_selector_item, publishAt);
            choseTimePublish.setDropDownViewResource(R.layout.spinner_selector_item);
            spPublish.setAdapter(choseTimePublish);

        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        btOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adapter = null;
                String type = (String) spType.getSelectedItem();
                String sort = ((KeyValue) spSort.getSelectedItem()).getKey();
                String country = ((KeyValue) spCountry.getSelectedItem()).getKey();
                if (StringUtils.isEmpty(country))
                {
                    country = null;
                }
                Long timeBefore = null;
                Long timeAfter = null;
                Integer longThan = null;
                Integer shortThan = null;

                switch (spPublish.getSelectedItemPosition())
                {
                    case 0:

                        break;
                    case 1:
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DATE);
                        calendar.set(year, month, day, 0, 0, 0);
                        timeAfter = calendar.getTimeInMillis();
                        break;
                    case 2:
                        Calendar dayAgo = Calendar.getInstance();
                        Calendar weekAgo = Calendar.getInstance();
                        dayAgo.add(Calendar.DAY_OF_WEEK, -1);
                        weekAgo.add(Calendar.DAY_OF_WEEK, -7);
                        timeBefore = dayAgo.getTimeInMillis();
                        timeAfter = weekAgo.getTimeInMillis();
                        break;
                    case 3:
                        Calendar monthAgo = Calendar.getInstance();
                        Calendar oneWeekAgo = Calendar.getInstance();
                        monthAgo.add(Calendar.MONTH, -1);
                        oneWeekAgo.add(Calendar.WEEK_OF_MONTH, -1);
                        timeBefore = oneWeekAgo.getTimeInMillis();
                        timeAfter = monthAgo.getTimeInMillis();
                        break;
                    case 4:
                        Calendar sixMonthsAgo = Calendar.getInstance();
                        Calendar aYearAgo = Calendar.getInstance();
                        sixMonthsAgo.add(Calendar.MONTH, -6);
                        aYearAgo.add(Calendar.MONTH, -12);
                        timeBefore = sixMonthsAgo.getTimeInMillis();
                        timeAfter = aYearAgo.getTimeInMillis();
                        break;
                }
                switch (spDuration.getSelectedItemPosition())
                {
                    case 0:
                        break;
                    case 1:
                        shortThan = 4;
                        break;
                    case 2:
                        longThan = 4;
                        shortThan = 20;
                        break;
                    case 3:
                        longThan = 20;
                        break;

                }
                if (timeAfter != null)
                {
                    timeAfter = timeAfter / 1000;
                }
                if (timeBefore != null)
                {
                    timeBefore = timeBefore/1000;
                }
                requestDTO = new DailymotionRequestDTO
                        .DailymotionRequestBuilder(keyword)
                        .fields(Constant.DAILYMOTION_VIDEO_FIELDS)
                        .flags(Constant.DAILYMOTION_VIDEO_FLAG)
                        .createdAfter(timeAfter)
                        .createdBefore(timeBefore)
                        .longerThan(longThan)
                        .shorterThan(shortThan)
                        .type(type)
                        .country(country)
                        .sort(sort)
                        .page(1)
                        .limit(10)
                        .build();
                search();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void handleFilterYoutube()
    {
        SpinnerCategoryAdapter contentTypeAdapter;
        SpinnerCategoryAdapter durationAdapter;
        SpinnerCategoryAdapter countryAdapter;
        SpinnerCategoryAdapter sortAdapter;
        SpinnerCategoryAdapter videoTypeAdapter;
        SpinnerCategoryAdapter videoDefinitionAdapter;
        SpinnerCategoryAdapter videoDimensionAdapter;
        SpinnerCommonAdapter uploadDateAdapter;

        dialog = new Dialog(this);
        dialog.setTitle("Advanced Filter");
        dialog.setContentView(R.layout.dialog_youtube_filter);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Spinner spContentType = (Spinner) dialog.findViewById(R.id.spinner_type);
        final Spinner spUploadTime = (Spinner) dialog.findViewById(R.id.spinner_update_time);
        final Spinner spDuration = (Spinner) dialog.findViewById(R.id.spinner_duration);
        final Spinner spCountry = (Spinner) dialog.findViewById(R.id.spinner_country);
        final Spinner spSort = (Spinner) dialog.findViewById(R.id.spinner_sort);
        final Spinner spDefinition = (Spinner) dialog.findViewById(R.id.spinner_definition);
        final Spinner spDimension = (Spinner) dialog.findViewById(R.id.spinner_dimension);
        final Spinner spVideoType = (Spinner) dialog.findViewById(R.id.spinner_videoType);
        Button btCancel = (Button) dialog.findViewById(R.id.dialog_youtube_advanced_filter_btCancel);
        Button btOk = (Button) dialog.findViewById(R.id.dialog_youtube_advanced_filter_btOk);

        uploadDateAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, uploads);
        uploadDateAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
        spUploadTime.setAdapter(uploadDateAdapter);

        try
        {
            List<KeyValue> contentTypes = ResourceUtils.getStringResource(this, R.xml.youtube_type);
            contentTypeAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, contentTypes);
            contentTypeAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spContentType.setAdapter(contentTypeAdapter);

            List<KeyValue> countries = ResourceUtils.getCountries();
            countryAdapter = new SpinnerCategoryAdapter(this, R.layout.spinner_selector_item, countries);
            countryAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spCountry.setAdapter(countryAdapter);

            List<KeyValue> durations = ResourceUtils.getStringResource(this, R.xml.youtube_duration);
            durationAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, durations);
            durationAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spDuration.setAdapter(durationAdapter);

            List<KeyValue> sorts = ResourceUtils.getStringResource(this, R.xml.youtube_order);
            sortAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, sorts);
            sortAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spSort.setAdapter(sortAdapter);

            List<KeyValue> videoTypes = ResourceUtils.getStringResource(this, R.xml.youtube_video_type);
            videoTypeAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, videoTypes);
            videoTypeAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spVideoType.setAdapter(videoTypeAdapter);

            List<KeyValue> dimension = ResourceUtils.getStringResource(this, R.xml.youtube_dimension);
            videoDimensionAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, dimension);
            videoDimensionAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spDimension.setAdapter(videoDimensionAdapter);

            List<KeyValue> definition = ResourceUtils.getStringResource(this, R.xml.youtube_definition);
            videoDefinitionAdapter = new SpinnerCategoryAdapter(this,
                    R.layout.spinner_selector_item, definition);
            videoDefinitionAdapter.setDropDownViewResource(R.layout.spinner_selector_item);
            spDefinition.setAdapter(videoDefinitionAdapter);

        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        btCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        btOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adapter = null;
                String contentType = ((KeyValue) spContentType.getSelectedItem()).getKey();
                String duration = ((KeyValue) spDuration.getSelectedItem()).getKey();
                String region = ((KeyValue) spCountry.getSelectedItem()).getKey();
                String sort = ((KeyValue) spSort.getSelectedItem()).getKey();
                String definition = ((KeyValue) spDefinition.getSelectedItem()).getKey();
                String demension = ((KeyValue) spDimension.getSelectedItem()).getKey();
                String videoType = ((KeyValue) spVideoType.getSelectedItem()).getKey();
                long timeBefore = 0;
                long timeAfter = 0;
                if (spUploadTime.getSelectedItemPosition() == 0)
                {
                    timeBefore = -1;
                    timeAfter = -1;
                }
                else if (spUploadTime.getSelectedItemPosition() == 1)
                {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DATE);
                    calendar.set(year, month, day, 0, 0, 0);
                    timeAfter = calendar.getTimeInMillis();
                }
                else if (spUploadTime.getSelectedItemPosition() == 2)
                {
                    Calendar dayAgo = Calendar.getInstance();
                    Calendar weekAgo = Calendar.getInstance();
                    dayAgo.add(Calendar.DAY_OF_WEEK, -1);
                    weekAgo.add(Calendar.DAY_OF_WEEK, -7);
                    timeBefore = dayAgo.getTimeInMillis();
                    timeAfter = weekAgo.getTimeInMillis();
                }
                else if (spUploadTime.getSelectedItemPosition() == 3)
                {
                    Calendar monthAgo = Calendar.getInstance();
                    Calendar weekAgo = Calendar.getInstance();
                    monthAgo.add(Calendar.MONTH, -1);
                    weekAgo.add(Calendar.WEEK_OF_MONTH, -1);
                    timeBefore = weekAgo.getTimeInMillis();
                    timeAfter = monthAgo.getTimeInMillis();
                }
                else
                {
                    Calendar sixMonthsAgo = Calendar.getInstance();
                    Calendar aYearAgo = Calendar.getInstance();
                    sixMonthsAgo.add(Calendar.MONTH, -6);
                    aYearAgo.add(Calendar.MONTH, -12);
                    timeBefore = sixMonthsAgo.getTimeInMillis();
                    timeAfter = aYearAgo.getTimeInMillis();
                }
                requestDTO = new YoutubeRequestDTO
                        .YoutubeRequestBuilder(keyword)
                        .type(contentType)
                        .regionCode(region)
                        .publishBefore(timeBefore)
                        .publishAfter(timeAfter)
                        .videoDuration(duration)
                        .order(sort)
                        .videoDefinition(definition)
                        .videoDimension(demension)
                        .videoType(videoType)
                        .build();
                search();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void handleFilterVimeo()
    {
        SpinnerCommonAdapter choseTypeAdapter;
        SpinnerCommonAdapter choseSortAdapter;
        SpinnerCommonAdapter choseDirectionAdapter;
        SpinnerCommonAdapter choseFillterAdapter;

        String[] type = new String[]{Constant.VIMEO_VIDEOS, Constant.VIMEO_USERS, Constant.VIMEO_CHANNELS};
        String[] sort = new String[]{"Relevant", "Date", "Alphabetical", "Plays", "Likes", "Comments", "Duration"};
        String[] direction = new String[]{"ASC", "DESC"};
        String[] filter = new String[]{"CC", "CC-BY", "CC-BY-SA", "CC-BY-ND", "CC-BY-NC", "CC-BY-NC-SA", "CC-BY-NC-ND", "in-progress"};

        dialog = new Dialog(this);
        dialog.setTitle("Advanced Filter");
        dialog.setContentView(R.layout.dialog_vimeo_filter);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        choseTypeAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, type);
        choseTypeAdapter.setDropDownViewResource(R.layout.spinner_selector_item);

        choseSortAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, sort);
        choseSortAdapter.setDropDownViewResource(R.layout.spinner_selector_item);

        choseDirectionAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, direction);
        choseDirectionAdapter.setDropDownViewResource(R.layout.spinner_selector_item);

        choseFillterAdapter = new SpinnerCommonAdapter(this,
                R.layout.spinner_selector_item, filter);
        choseFillterAdapter.setDropDownViewResource(R.layout.spinner_selector_item);

        final Spinner spinnerType = (Spinner) dialog.findViewById(R.id.spinner_type);
        final Spinner spinnerShort = (Spinner) dialog.findViewById(R.id.spinner_sort);
        final Spinner spinnerDirection = (Spinner) dialog.findViewById(R.id.spinner_direction);
        final Spinner spinnerfillter = (Spinner) dialog.findViewById(R.id.spinner_filter);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
        Button btOk = (Button) dialog.findViewById(R.id.btOk);

        spinnerType.setAdapter(choseTypeAdapter);
        spinnerShort.setAdapter(choseSortAdapter);
        spinnerDirection.setAdapter(choseDirectionAdapter);
        spinnerfillter.setAdapter(choseFillterAdapter);
        btCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        btOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                adapter = null;
                String sort = (String) spinnerShort.getSelectedItem();
                String filter = (String) spinnerfillter.getSelectedItem();
                String direction = (String) spinnerDirection.getSelectedItem();
                requestDTO = new VimeoRequestDTO
                        .VimeoRequestBuilder(keyword)
                        .sort(sort.toLowerCase())
                        .filter(filter)
                        .direction(direction.toLowerCase())
                        .type((String) spinnerType.getSelectedItem())
                        .build();
                search();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void search()
    {
        AsyncTaskSearchData asyncTask = new AsyncTaskSearchData(this, host_name, requestDTO);
        asyncTask.setListener(this);
        asyncTask.execute();
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
                adapter = new CommonAdapter(videos, this);
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
        Item item = (Item) adapter.getItem(position);
        Intent intent = null;
        switch (host_name)
        {
            case YOUTUBE:
                if (item instanceof VideoModel)
                {
                    intent = new Intent(this, YoutubeVideoDetailActivity.class);
                    intent.putExtra("video", (Parcelable) adapter.getItem(position));
                }
                else if (item instanceof UserModel)
                {
                    intent = new Intent(this, UserDetailActivity.class);
                    intent.putExtra("video", (UserModel) adapter.getItem(position));
                }
                else
                {
                    intent = new Intent(this, PlayListYoutubeDetailActivity.class);
                    intent.putExtra("video", (Parcelable) adapter.getItem(position));
                }
                break;
            case VIMEO:
                if (item instanceof VideoModel)
                {
                    intent = new Intent(this, VimeoVideoDetailActivity.class);
                    intent.putExtra("video", (Parcelable) adapter.getItem(position));
                }
                else if (item instanceof UserModel)
                {
                    intent = new Intent(this, UserDetailActivity.class);
                    intent.putExtra("video", (UserModel) adapter.getItem(position));
                }
                else
                {
                    intent = new Intent(this, ListVideoDetailActivity.class);
                    intent.putExtra("video", (Parcelable) adapter.getItem(position));
                }
                break;
            case DAILYMOTION:
                if (item instanceof VideoModel)
                {
                    intent = new Intent(this, VimeoVideoDetailActivity.class);
                    intent.putExtra("video", (Parcelable) adapter.getItem(position));
                }
                else if (item instanceof UserModel)
                {
                    intent = new Intent(this, UserDetailActivity.class);
                    intent.putExtra("video", (UserModel) adapter.getItem(position));
                }
                else
                {
                    intent = new Intent(this, ListVideoDetailActivity.class);
                    intent.putExtra("video", (Parcelable) adapter.getItem(position));
                }
                break;
        }
        intent.putExtra("host_name", host_name);
        startActivity(intent);
    }
}
