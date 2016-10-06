package com.example.vincent.servicetransfers.service;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;

/**
 * Created by vincent on 26/7/2016.
 */

public class TransferExecutor {

    private HandlerThread mHandlerThread;
    private JobHandler mJobHandler;
    private PowerManager.WakeLock mWakeLock;

    private OnTransferListener mTransferListener;
    public interface OnTransferListener{
        void onTransferComplete(String reponse);
        void onTransferError(Exception e);
    }

    private class JobHandler extends Handler {
        public JobHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Jobs queue

        }
    }

    public void addTransferListener(OnTransferListener listener){
        mTransferListener = listener;
    }

    public void onCreate(){
        mHandlerThread = new HandlerThread("TransferExecutor", Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mJobHandler = new JobHandler(mHandlerThread.getLooper());
    }

    public void onDestory(){

    }


}
