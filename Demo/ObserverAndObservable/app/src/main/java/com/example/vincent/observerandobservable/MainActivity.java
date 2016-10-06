package com.example.vincent.observerandobservable;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.vincent.observerandobservable.classes.NetworkObserver;
import com.example.vincent.observerandobservable.receiver.NetworkChangedReceiver;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    private final String TAG = MainActivity.class.getSimpleName();
    private NetworkChangedReceiver mNetworkChangedReceiver = new NetworkChangedReceiver();
    private IntentFilter mNetworkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.vincent.observerandobservable.R.layout.activity_main);
        NetworkObserver.getInstance().addObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mNetworkChangedReceiver, mNetworkFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNetworkChangedReceiver);
    }

    @Override
    public void update(Observable observable, Object o) {
        if(observable instanceof NetworkObserver){
            NetworkObserver networkObserver = (NetworkObserver)observable;
            Log.i(TAG, "NetworkObserver Changed : " + (networkObserver.isConnected() ? "is connected" : "is disconnect"));
        }
    }
}
