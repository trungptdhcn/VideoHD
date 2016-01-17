package com.taa.videohd;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import butterknife.Bind;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.taa.videohd.base.BaseActivity;
import com.taa.videohd.ui.fragment.DailymotionFragment;
import com.taa.videohd.ui.fragment.DownloadTaskFragment;
import com.taa.videohd.ui.fragment.VimeoFragment;
import com.taa.videohd.ui.fragment.YoutubeFragment;
import com.trungpt.videodownloadmaster.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends BaseActivity
{
    @Bind(R.id.activity_main_viewpager)
    ViewPager viewPager;
    @Bind(R.id.activity_main_tab)
    ViewGroup flTabContainer;
    //    @Bind(R.id.ivLogo)
//    ImageView ivLogo;
//    @Bind(R.id.toolbar)
//    Toolbar toolbar;
    FragmentPagerItems pages;
    FragmentPagerItemAdapter adapter;
    private Tracker mTracker;

    @Override
    public void setDataToView(Bundle savedInstanceState)
    {

        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.taa.videohd",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Your Tag", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {

        }
        catch (NoSuchAlgorithmException e)
        {

        }

        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();
        flTabContainer.addView(LayoutInflater.from(this).inflate(R.layout.tab_main, flTabContainer, false));
        final SmartTabLayout tabLayout = (SmartTabLayout) findViewById(R.id.tab_indicator_menu_viewpagertab);
        viewPager.setOffscreenPageLimit(5);
        pages = new FragmentPagerItems(this);
        pages.add(FragmentPagerItem.of("Youtube", YoutubeFragment.class));
        pages.add(FragmentPagerItem.of("Vimeo Video", VimeoFragment.class));
        pages.add(FragmentPagerItem.of("Dailymotion Video", DailymotionFragment.class));
        SharedPreferences prefs = this.getSharedPreferences(
                "com.taa.videohd", Context.MODE_PRIVATE);
        int config = prefs.getInt("key_isDisplay", -1);
        if (config == 1)
        {
            pages.add(FragmentPagerItem.of("Download Task", DownloadTaskFragment.class));
        }
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
    }

    @Override
    public int getLayout()
    {
        return R.layout.activity_main;
    }

    public void otherFragment(int position)
    {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }
}
