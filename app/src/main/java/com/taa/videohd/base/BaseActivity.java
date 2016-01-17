package com.taa.videohd.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import butterknife.ButterKnife;
import com.taa.videohd.download.DownloadService;
import com.trungpt.videodownloadmaster.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Trung on 10/13/2015.
 */
public abstract class BaseActivity extends AppCompatActivity
{
    private Toolbar toolbar;
//    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setDataToView(savedInstanceState);
//        MyApplication application = (MyApplication) getApplication();
//        mTracker = application.getDefaultTracker();
    }

    public abstract void setDataToView(Bundle savedInstanceState);

    public abstract int getLayout();

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        getActionBarToolbar();
    }

    protected Toolbar getActionBarToolbar()
    {
        if (toolbar == null)
        {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null)
            {
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
        return toolbar;
    }

    public boolean isVisibleNav()
    {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        invokeFragmentManagerNoteStateNotSaved();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void invokeFragmentManagerNoteStateNotSaved()
    {
        /**
         * For post-Honeycomb devices
         */
        if (Build.VERSION.SDK_INT < 11)
        {
            return;
        }
        try
        {
            Class cls = getClass();
            do
            {
                cls = cls.getSuperclass();
            } while (!"Activity".equals(cls.getSimpleName()));
            Field fragmentMgrField = cls.getDeclaredField("mFragments");
            fragmentMgrField.setAccessible(true);

            Object fragmentMgr = fragmentMgrField.get(this);
            cls = fragmentMgr.getClass();

            Method noteStateNotSavedMethod = cls.getDeclaredMethod("noteStateNotSaved", new Class[]{});
            noteStateNotSavedMethod.invoke(fragmentMgr, new Object[]{});
            Log.d("DLOutState", "Successful call for noteStateNotSaved!!!");
        }
        catch (Exception ex)
        {
            Log.e("DLOutState", "Exception on worka FM.noteStateNotSaved", ex);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
    }
}

