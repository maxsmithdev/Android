package com.example.vincent.appchat.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by vincent on 19/6/2016.
 */
public class Cache {

    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    private static final int KEEP_ALIVE_TIME = 1; //wait for one task
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAXIMUM_POOL_SIZE = 8;

    private Handler mHandler;
    private final ThreadPoolExecutor mDownloadPool;
    private BlockingQueue<Runnable> mCacheDownload;

    private static Cache sInstance;
    static {
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        sInstance = new Cache();
    }

    public static synchronized Cache getInstance(){
        return sInstance;
    }

    private Cache(){
        mCacheDownload = new LinkedBlockingQueue<>();
        mDownloadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
                mCacheDownload);

        mHandler = new Handler(Looper.getMainLooper()){ //main looper for UI

            @Override
            public void handleMessage(Message msg) {

            }
        };
    }

    public static void startDownload(String url){
        //create a worker to download json
        CacheWorker worker = new CacheWorker();
    }

    public static void startUpload(){

    }

    public void handleState(CacheWorker worker, int state){
        mHandler.obtainMessage(state, worker).sendToTarget();
    }
}
