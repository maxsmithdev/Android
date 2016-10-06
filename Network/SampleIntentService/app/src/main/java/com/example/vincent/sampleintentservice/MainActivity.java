package com.example.vincent.sampleintentservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.vincent.sampleintentservice.model.DribbbleShots;
import com.example.vincent.sampleintentservice.service.HttpService;
import com.example.vincent.sampleintentservice.tools.Constants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private final String mUri = "https://api.dribbble.com/v1/shots?access_token=06428b459e81637505938870033865c924800dc8cf711e5e5194ce26c6f00df8";

    private Intent serviceIntent;
    private BroadcastReceiver receiver = null;
    private BroadcastReceiver getReceiver(){
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int state = intent.getIntExtra("state", -1);
                switch (state){
                    case Constants.HTTP_START:
                        Log.i(TAG, "Request Start.");
                        break;
                    case Constants.HTTP_COMPLETE:
                        List<DribbbleShots> shots = (List<DribbbleShots>)intent.getSerializableExtra("dribbleShots");
                        Log.i(TAG, "Request Complete [size : "+shots.size()+"]");
                        break;
                    case Constants.HTTP_ERROR:
                        Log.e(TAG, "Request Error.");
                        break;
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent(this, HttpService.class);
        serviceIntent.putExtra("uri", mUri);
        serviceIntent.putExtra("method", "GET");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(receiver == null) receiver = getReceiver();
        registerReceiver(receiver, new IntentFilter(Constants.HTTP_SERVICE_RECEIVER));
        startService(serviceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver =  null;
        }

        if(serviceIntent != null){
            HttpService.stop(this, serviceIntent);
        }
    }
}
