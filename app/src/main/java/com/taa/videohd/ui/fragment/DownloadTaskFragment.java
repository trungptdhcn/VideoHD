package com.taa.videohd.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import com.google.android.gms.ads.AdView;
import com.taa.videohd.base.BaseFragment;
import com.taa.videohd.ui.adapter.DashboardDownloadAdapter;
import com.taa.videohd.utils.Utils;
import com.trungpt.videodownloadmaster.R;

/**
 * Created by trung on 12/18/2015.
 */
public class DownloadTaskFragment extends BaseFragment
{
    @Bind(R.id.list_dashboard)
    ListView listDashboard;

    @Override
    public int getLayout()
    {
        return R.layout.download_fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setDataToView(Bundle savedInstanceState)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.layout_most_popular, listDashboard, false);
        AdView adView = (AdView) header.findViewById(R.id.adView);
        TextView tvTitle = (TextView) header.findViewById(R.id.tvTitle);
        tvTitle.setText("Download Task");
        Utils.loadBannerAds(adView);
        listDashboard.addHeaderView(header, null, false);
        DashboardDownloadAdapter adapter = new DashboardDownloadAdapter(getActivity());
        listDashboard.setAdapter(adapter);
    }
}
