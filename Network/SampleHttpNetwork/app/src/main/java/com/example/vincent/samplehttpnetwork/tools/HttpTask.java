package com.example.vincent.samplehttpnetwork.tools;

import android.os.Bundle;

import com.example.vincent.samplehttpnetwork.tools.HttpRunnable.HttpImpl;

/**
 * Created by vincent on 25/6/2016.
 */
public class HttpTask implements HttpImpl {

    public static final int HTTP_START = 0;
    public static final int HTTP_COMPLETE = 1;
    public static final int HTTP_CANCEL = 2;
    public static final int HTTP_ERROR = 3;

    Thread mThread;
    private Thread mCurrentThread;

    private HttpRunnable mHttpRunnable;
    private String mUri;
    private String mMethod = "GET";
    private String mData;
    private Bundle mParams;
    private static HttpManager mHttpManager;

    HttpTask(){
        mHttpRunnable = new HttpRunnable(this);
    }

    public void init(HttpManager httpManager, String method, String uri, Bundle params){
        mHttpManager = httpManager;
        mUri = uri;
        mMethod = method;
        mParams = params;
    }

    public HttpRunnable getHttpRunnable(){
        synchronized (this){
            return mHttpRunnable;
        }
    }

    void handleState(int state) {
        mHttpManager.handleState(this, state);
    }

    @Override
    public void setMethod(String method) {
        mMethod = method;
    }

    @Override
    public String getMethod() {
        return mMethod;
    }

    @Override
    public void setUri(String uri) {
        mUri = uri;
    }

    @Override
    public void setParams(Bundle params) {
        mParams = params;
    }

    @Override
    public String getUri() {
        return mUri;
    }

    @Override
    public Bundle getParams() {
        return mParams;
    }

    @Override
    public void setResponse(String data) {
        mData = data;
    }

    @Override
    public String getResponse() {
        return mData;
    }

    @Override
    public void handleHttpState(int state) {
        handleState(state);
    }

    @Override
    public void setCurrentThread(Thread thread) {
        synchronized (HttpTask.class){
            mCurrentThread = thread;
        }
    }

    @Override
    public Thread getCurrentThread() {
        synchronized (HttpTask.class) {
            return mCurrentThread;
        }
    }

    /*
     * force-free objects
     */
    public void recycle(){
        mData = null;
        mUri = null;
    }
}
