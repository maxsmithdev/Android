package com.example.vincent.appchat.http;

/**
 * Created by vincent on 19/6/2016.
 */
public class CacheDownload implements Runnable {

    private CacheDownloadInteface mDownloadListener;
    public interface CacheDownloadInteface {
        void setDownloadUrl(String url);
        String getDownloadUrl();
        void handleState(int state);
        void setResponse(String data);
        String getResponse();
    }

    public CacheDownload(CacheDownloadInteface listener){
        mDownloadListener = listener;
    }

    @Override
    public void run() {

    }
}
