package com.example.vincent.observerandobservable.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import com.example.vincent.observerandobservable.classes.NetworkObserver;

/**
 * Created by vincent on 21/7/2016.
 */

public class NetworkChangedReceiver extends BroadcastReceiver {

    private final String TAG = NetworkChangedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Detected network receiver.");
        if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
            Log.i(TAG, "NETWORK_STATE_CHANGED_ACTION");
        }else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            Log.i(TAG, "CONNECTIVITY_ACTION");
        }

        NetworkObserver.getInstance().setState(isConnected(context));
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
        if(Build.VERSION.SDK_INT >= 23) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }else{
            NetworkInfo mobileNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return !mobileNetInfo.isConnected() && !wifiNetInfo.isConnected();
        }
    }

}
