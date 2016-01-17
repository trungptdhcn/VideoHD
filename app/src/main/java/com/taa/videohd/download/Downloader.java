package com.taa.videohd.download;

import android.app.NotificationManager;
import android.support.v7.app.NotificationCompat;
import com.taa.videohd.ui.model.DirectLink;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trung on 12/21/2015.
 */
public abstract class Downloader implements Runnable
{
    protected URL url;
    protected String fileName;
    public String folder;
    protected String videoType;
    protected int numberConnections;
    protected DirectLink directLink;
    protected int fileSize;
    protected int state;
    private int downloaded;
    protected List<DownloadThread> listDownloadThread;
    protected static final int BLOCK_SIZE = 4096;
    protected static final int BUFFER_SIZE = 4096;
    protected static final int MIN_DOWNLOAD_SIZE = BLOCK_SIZE * 100;
    public static final String STATUSES[] = {"Downloading",
            "Paused", "Complete", "Cancelled", "Error"};

    private UpdateProgress updateProgress;
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETED = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    public Downloader(URL url, String folder, String fileName, String videoType, int numberConnections, DirectLink directLink)
    {
        this.url = url;
        this.folder = folder;
        this.fileName = fileName;
        this.videoType = videoType;
        this.directLink = directLink;
        this.numberConnections = numberConnections;
        this.fileSize = -1;
        this.state = DOWNLOADING;
        downloaded = 0;
        listDownloadThread = new ArrayList<>();
    }

    public void pause()
    {
        setState(PAUSED);
    }

    public void resume()
    {
        setState(DOWNLOADING);
        download();
    }

    public void cancel()
    {
        setState(CANCELLED);
    }

    public String getURL()
    {
        return url.toString();
    }

    public int getFileSize()
    {
        return fileSize;
    }

    public float getProgress()
    {
        return ((float) downloaded / fileSize) * 100;
    }

    public int getState()
    {
        return state;
    }

    public DirectLink getDirectLink()
    {
        return directLink;
    }

    public void setDirectLink(DirectLink directLink)
    {
        this.directLink = directLink;
    }

    protected void setState(int value)
    {
        state = value;
        stateChanged();
    }

    protected void download()
    {
        Thread t = new Thread(this);
        t.start();
    }

    protected synchronized void downloaded(int value)
    {
        downloaded += value;
        stateChanged();
    }

    protected void stateChanged()
    {
        if (updateProgress != null)
        {
            updateProgress.update((int) (getProgress()));
        }
    }

    public void setUpdateProgress(UpdateProgress updateProgress)
    {
        this.updateProgress = updateProgress;
    }

    protected abstract class DownloadThread implements Runnable
    {
        protected int threadId;
        protected URL url;
        protected String fileName;
        protected int startByte;
        protected int endByte;
        protected boolean isFinish;
        protected Thread thread;

        public DownloadThread(int threadId, URL url, String fileName, int startByte, int endByte)
        {
            this.threadId = threadId;
            this.url = url;
            this.fileName = fileName;
            this.startByte = startByte;
            this.endByte = endByte;
            this.isFinish = false;

            download();
        }

        /**
         * Get whether the thread is finished download the part of file
         */
        public boolean isFinished()
        {
            return isFinish;
        }

        /**
         * Start or resume the download
         */
        public void download()
        {
            thread = new Thread(this);
            thread.start();
        }

        /**
         * Waiting for the thread to finish
         *
         * @throws InterruptedException
         */
        public void waitFinish() throws InterruptedException
        {
            thread.join();
        }

    }
}
