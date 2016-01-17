package com.taa.videohd.download;


import com.taa.videohd.ui.model.DirectLink;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by trung on 12/21/2015.
 */
public class HttpDownloader extends Downloader
{
    public HttpDownloader(URL url, String folder, String fileName, String videoType, int numConnections, DirectLink directLink)
    {
        super(url, folder, fileName, videoType, numConnections,directLink);
        download();
    }

    private void error()
    {
        System.out.println("ERROR");
        setState(ERROR);
    }

    @Override
    public void run()
    {
        HttpURLConnection conn = null;
        try
        {
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.connect();
            if (conn.getResponseCode() / 100 != 2)
            {
                error();
            }
            int contentLength = conn.getContentLength();
            if (contentLength < 1)
            {
                error();
            }
            if (fileSize == -1)
            {
                fileSize = contentLength;
                stateChanged();
            }
            if (state == DOWNLOADING)
            {
                if (listDownloadThread.size() == 0)
                {
                    if (fileSize > MIN_DOWNLOAD_SIZE)
                    {
                        int partSize = Math.round(((float) fileSize / numberConnections) / BLOCK_SIZE) * BLOCK_SIZE;
                        int startByte = 0;
                        int endByte = partSize - 1;
                        HttpDownloadThread thread = new HttpDownloadThread(1, url, folder + fileName + videoType, startByte, endByte);
                        listDownloadThread.add(thread);
                        int i = 2;
                        while (endByte < fileSize)
                        {
                            startByte = endByte + 1;
                            endByte += partSize;
                            thread = new HttpDownloadThread(i, url, folder + fileName + videoType, startByte, endByte);
                            listDownloadThread.add(thread);
                            ++i;
                        }
                    }
                    else
                    {
                        HttpDownloadThread thread = new HttpDownloadThread(1, url, folder + fileName + videoType, 0, fileSize);
                        listDownloadThread.add(thread);
                    }
                }
                else
                {
                    for (int i = 0; i < listDownloadThread.size(); ++i)
                    {
                        if (!listDownloadThread.get(i).isFinished())
                        {
                            listDownloadThread.get(i).download();
                        }
                    }
                }
                for (int i = 0; i < listDownloadThread.size(); i++)
                {
                    listDownloadThread.get(i).waitFinish();
                }
                if (state == DOWNLOADING)
                {
                    setState(COMPLETED);
                }
            }
        }
        catch (Exception e)
        {
            error();
        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
        }
    }

    private class HttpDownloadThread extends DownloadThread
    {

        public HttpDownloadThread(int threadId, URL url, String fileName, int startByte, int endByte)
        {
            super(threadId, url, fileName, startByte, endByte);
        }

        @Override
        public void run()
        {
            BufferedInputStream in = null;
            RandomAccessFile raf = null;
            try
            {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                String byteRange = startByte + "-" + endByte;
                conn.setRequestProperty("Range", "bytes=" + byteRange);
                conn.connect();
                if (conn.getResponseCode() / 100 != 2)
                {
                    error();
                }
                in = new BufferedInputStream(conn.getInputStream());
                raf = new RandomAccessFile(fileName, "rw");
                raf.seek(startByte);
                byte data[] = new byte[BUFFER_SIZE];
                int numRead;
                while ((state == DOWNLOADING) && (numRead = in.read(data, 0, BUFFER_SIZE)) != -1)
                {
                    raf.write(data, 0, numRead);
                    startByte += numRead;
                    downloaded(numRead);
                }
                if (state == DOWNLOADING)
                {
                    isFinish = true;
                }
            }
            catch (Exception e)
            {
                error();
            }
            finally
            {
                if (raf != null)
                {
                    try
                    {
                        raf.close();
                    }
                    catch (IOException e)
                    {
                    }
                }

                if (in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch (IOException e)
                    {
                    }
                }
            }
        }
    }
}
