package com.example.vincent.appchat.http;

/**
 * Created by vincent on 19/6/2016.
 */
public class CacheWorker implements CacheDownload.CacheDownloadInteface, CacheUpload.CachUploadInterface {

    private static Cache sCache;
    private CacheDownload sDownload;
    private CacheUpload sUpload;

    private String mDownloadUrl;
    private String mUploadUrl;

    private byte[] mUploadFile;
    private String mResponse;

    CacheWorker(){
        sDownload = new CacheDownload(this);
        sUpload = new CacheUpload(this);
        sCache = Cache.getInstance();
    }

    @Override
    public void setUploadHost(String url) {
        mUploadUrl = url;
    }

    @Override
    public String getUploadHost() {
        return mUploadUrl;
    }

    @Override
    public void setByteBuffer(byte[] buffer) {
        mUploadFile = buffer;
    }

    @Override
    public byte[] getByteBuffer() {
        return mUploadFile;
    }

    @Override
    public void setDownloadUrl(String url) {
        mDownloadUrl = url;
    }

    @Override
    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    @Override
    public void setResponse(String data) {
        mResponse = data;
    }

    @Override
    public String getResponse() {
        return mResponse;
    }

    @Override
    public void handleState(int state){
        sCache.handleState(this, state);
    }

}
