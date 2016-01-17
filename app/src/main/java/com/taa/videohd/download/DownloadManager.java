package com.taa.videohd.download;


import com.taa.videohd.ui.model.DirectLink;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 12/21/2015.
 */
public class DownloadManager
{
    private static DownloadManager instance = null;
    private static final int DEFAULT_NUM_CONN_PER_DOWNLOAD = 8;
    public static final String DEFAULT_OUTPUT_FOLDER = "";
    private int numConnPerDownload;
    private List<Downloader> downloadList;

    protected DownloadManager()
    {
        numConnPerDownload = DEFAULT_NUM_CONN_PER_DOWNLOAD;
        downloadList = new ArrayList<>();
    }

    public int getNumConnPerDownload()
    {
        return numConnPerDownload;
    }

    public void setNumConnPerDownload(int numConnPerDownload)
    {
        this.numConnPerDownload = numConnPerDownload;
    }

    public Downloader getDownload(int index)
    {
        return downloadList.get(index);
    }

    public void removeDownload(int index)
    {
        downloadList.remove(index);
    }

    public List<Downloader> getDownloadList()
    {
        return downloadList;
    }

    public Downloader createDownload(URL url, String folder, String fileName, String videoType, DirectLink directLink)
    {
        HttpDownloader fd = new HttpDownloader(url, folder, fileName, videoType, numConnPerDownload, directLink);
        downloadList.add(fd);
        return fd;
    }

    public static DownloadManager getInstance()
    {
        if (instance == null)
        {
            instance = new DownloadManager();
        }

        return instance;
    }


}
