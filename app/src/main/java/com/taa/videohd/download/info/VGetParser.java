package com.taa.videohd.download.info;

import com.github.axet.wget.info.DownloadInfo;
import com.github.axet.wget.info.ex.DownloadInterruptedError;

import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class VGetParser {

    public abstract VideoInfo info(URL web);

    public void info(VideoInfo info, AtomicBoolean stop, Runnable notify) {
        try {
            DownloadInfo dinfo = extract(info, stop, notify);

            info.setInfo(dinfo);

//            dinfo.setRefer(info.getWeb());

            dinfo.extract(stop, notify);
        } catch (DownloadInterruptedError e) {
            info.setState(VideoInfo.States.STOP, e);

            throw e;
        } catch (RuntimeException e) {
            info.setState(VideoInfo.States.ERROR, e);

            throw e;
        }
    }

    public abstract DownloadInfo extract(final VideoInfo vinfo, final AtomicBoolean stop, final Runnable notify);

}
