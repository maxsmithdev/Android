package com.example.vincent.observerandobservable.classes;

import java.util.Observable;

/**
 * Created by vincent on 21/7/2016.
 */

public class NetworkObserver extends Observable {

    private boolean mIsConnected;
    private final static NetworkObserver sInstance;
    static{
        sInstance = new NetworkObserver();
    }

    private NetworkObserver(){

    }

    public static synchronized NetworkObserver getInstance(){
        synchronized (sInstance){
            return sInstance;
        }
    }

    public void setState(boolean isConnected){
        //if(mIsConnected != isConnected) setChanged();
        synchronized (this) {
            mIsConnected = isConnected;
            setChanged();
            notifyObservers();
            //notifyObservers(obj);
        }
    }

    public boolean isConnected(){
        synchronized (sInstance){
            return mIsConnected;
        }
    }

}
