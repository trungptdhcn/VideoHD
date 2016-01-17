package com.taa.videohd.sync.dailymotion.direct;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Trung on 12/2/2015.
 */
public class DailymotionParser
{
    public static Object extractHostDirect(String url) throws IOException
    {
        StringBuilder builder = new StringBuilder();
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Cookie","family_filter=off; ff=off");
        try
        {
            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null)
            {
                builder.append(s);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String webPage = builder.toString();
        String regex = "buildPlayer\\(\\{.+?\\}\\);";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(webPage);
        while (matcher.find())
        {
            String json = matcher.group();
            String buildPlayer = json.substring(12, json.length() - 2);
            DailymotionDirectDTO directMetaData = new Gson().fromJson(buildPlayer, DailymotionDirectDTO.class);
            if (directMetaData != null)
            {
                return directMetaData.getDailymotionDirectMetaData();
            }
        }

        return null;
    }
}
