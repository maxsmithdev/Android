package com.example.vincent.samplehttpnetwork.tools;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by vincent on 25/6/2016.
 */
public class HttpManager {

    private static final String TAG = HttpManager.class.getSimpleName();

    private static final int CORE_POOL_SIZE = 8;
    private static final int MAXIMUM_POOL_SIZE = 8;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    // wait for a task before terminating
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

    private BlockingQueue<Runnable> mQueueMiddleware;
    private Queue<HttpTask> mRecycleTasks;
    private List<HttpTask> mTasks;
    private ThreadPoolExecutor mThreadPool;

    private Handler mHandler;

    private final static HttpManager _sInstance;
    static {
        KEEP_ALIVE_TIME_UNIT = TimeUnit.MICROSECONDS;
        _sInstance = new HttpManager();
    }

    public static HttpManager getInstance(){
        return _sInstance;
    }

    private Queue<HttpTask> getRecycleTasks(){
        return mRecycleTasks;
    }

    private List<HttpTask> getAllTasks(){
        return mTasks;
    }

    private void setTask(HttpTask task){
        mTasks.add(task);
    }

    private ThreadPoolExecutor getThreadPool(){
        return mThreadPool;
    }

    private HttpManager(){

        mQueueMiddleware = new LinkedBlockingQueue<Runnable>();
        mRecycleTasks = new LinkedBlockingQueue<HttpTask>();
        mTasks = new ArrayList<HttpTask>();
        mThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mQueueMiddleware);

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                HttpTask task = (HttpTask) msg.obj;
                switch (msg.what){
                    case HttpTask.HTTP_START:
                        Log.i(TAG, "HTTP Request Start.");
                        break;
                    case HttpTask.HTTP_COMPLETE:
                        Log.i(TAG, "Response : " + task.getResponse());
                        mTasks.remove(task);
                        break;
                    case HttpTask.HTTP_CANCEL:
                        Log.i(TAG, "HTTP Request Cancel.");
                        mTasks.remove(task);
                        break;
                    case HttpTask.HTTP_ERROR:
                        Log.e(TAG, "HTTP Request Error.");
                        mTasks.remove(task);
                        break;
                }
            }
        };
    }

    /*
     * HttpTask (caller)
     * Handler (callback)
     */
    public void handleState(HttpTask task, int state){
        Message completeMessage = mHandler.obtainMessage(state, task);
        completeMessage.sendToTarget();
    }

    public static HttpTask request(String method, String uri, Bundle params){
        HttpTask task = _sInstance.getRecycleTasks().poll();
        if(task == null){
            task = new HttpTask();
        }

        String data = task.getResponse();
        if(null == data){
            task.init(_sInstance, method, uri, params);
            _sInstance.setTask(task);
            _sInstance.getThreadPool().execute(task.getHttpRunnable());
        }

        return task;
    }

    public static HttpTask get(String uri){
        return request("GET", uri, null);
    }

    public static HttpTask post(String uri, Bundle params){
        return request("POST", uri, params);
    }

    public static void cancel(HttpTask httpTask, String uri){
        if(null != httpTask && httpTask.getUri().equals(uri)){
            Thread currentThread = httpTask.getCurrentThread();
            if(null != currentThread) currentThread.interrupt();
            _sInstance.getThreadPool().remove(httpTask.getHttpRunnable());
        }
    }

    public static void stop(){
        //int size = _sInstance.getQueueMiddleware().size();
        //Log.i(TAG, "Queue Size : " + size);

        //HttpTask[] tasks = new HttpTask[_sInstance.getThreadPool().getQueue().size()];
        //_sInstance.getThreadPool().getQueue().toArray(tasks);

        //_threadPool.getQueue();
        List<HttpTask> allTasks = _sInstance.getAllTasks();
        Log.i(TAG, "HttpTask Size : " + allTasks.size());

        synchronized (_sInstance) {
            Iterator<HttpTask> tasks = allTasks.iterator();
            while (tasks.hasNext()) {
                Log.i(TAG, "Task has found.");
                HttpTask task = tasks.next();
                Thread thread = task.getCurrentThread();
                if(null != thread){
                    Log.i(TAG, "It had found a thread that it was interrupt.");
                    thread.interrupt();
                }

                tasks.remove();
            }
        }


        Log.i(TAG, "HttpTask Size (after): " + allTasks.size());
    }

    private void recycle(HttpTask task){
        task.recycle();
        mRecycleTasks.offer(task);
    }
}
