package com.example.vincent.appchat.http;

/**
 * Created by vincent on 19/6/2016.
 */
public class CacheUpload implements Runnable{

    private CachUploadInterface mUploadListener;
    public interface CachUploadInterface{
        void setUploadHost(String url);
        String getUploadHost();
        void setByteBuffer(byte[] buffer);
        byte[] getByteBuffer();
        void handleState(int state);
    }

    public CacheUpload(CachUploadInterface listener){
        mUploadListener = listener;
    }

    @Override
    public void run() {

    }

}
