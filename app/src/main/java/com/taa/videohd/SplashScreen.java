package com.taa.videohd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.trungpt.videodownloadmaster.R;

/**
 * Created by trung on 01/06/2016.
 */
public class SplashScreen extends Activity
{
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        progress = new ProgressDialog(this);
        progress.setTitle("Load data app.. !");
        progress.setCancelable(false);
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                progress.show();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Config");
                try
                {
                    ParseObject parseObject = query.get("Gqq7q7W520");
                    int isDisplay = (Integer) parseObject.get("isDisplay");
                    int isDownloadYoutube = (Integer) parseObject.get("isDownloadYoutube");
                    SharedPreferences prefs = SplashScreen.this.getSharedPreferences(
                            "com.taa.videohd", Context.MODE_PRIVATE);
                    prefs.edit().putInt("key_isDisplay", isDisplay).apply();
                    prefs.edit().putInt("key_isDownloadYoutube", isDownloadYoutube).apply();
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                progress.dismiss();
            }
        }.execute();

//        query.getInBackground("Gqq7q7W520", new GetCallback<ParseObject>()
//        {
//            public void done(ParseObject object, ParseException e)
//            {
//                if (object != null)
//                {
//                    Integer isDisplay = (Integer) object.get("isDisplay");
//                    SharedPreferences prefs = SplashScreen.this.getSharedPreferences(
//                            "com.taa.videohd", Context.MODE_PRIVATE);
//                    prefs.edit().putInt("key_config", isDisplay).apply();
//                }
//            }
//        });
//        Thread timerThread = new Thread()
//        {
//            public void run()
//            {
//                try
//                {
//                    sleep(3000);
//                }
//                catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
//                finally
//                {
//                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//                    startActivity(intent);
//                }
//            }
//        };
//        timerThread.start();
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
