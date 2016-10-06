package com.example.vincent.gcmdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.vincent.gcmdemo.service.RegistrationIntentService;
import com.example.vincent.gcmdemo.tools.Constants;
import com.example.vincent.gcmdemo.service.GCMInstanceIdService;
import com.example.vincent.gcmdemo.tools.Device;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    /*
     * # api_key=YOUR_API_KEY
     * curl --header "Authorization: key=$api_key" \
     * --header Content-Type:"application/json" \
     * https://gcm-http.googleapis.com/gcm/send \
     * -d "{\"registration_ids\":[\"ABC\"]}"
     */

    private BroadcastReceiver mTokenReceiver = null;
    private BroadcastReceiver getTokenReceiver(){
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "");
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mTokenReceiver == null){
            mTokenReceiver = getTokenReceiver();
        }

        if(Device.checkPlayServices(this)) {
            RegistrationIntentService.start(this);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mTokenReceiver, new IntentFilter(Constants.RECEIVER_REGISTRATION_GCM));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mTokenReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mTokenReceiver);
            mTokenReceiver = null;
        }
    }
}
