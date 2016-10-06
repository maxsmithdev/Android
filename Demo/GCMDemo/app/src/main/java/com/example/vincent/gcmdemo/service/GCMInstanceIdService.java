package com.example.vincent.gcmdemo.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by vincent on 30/6/2016.
 */
public class GCMInstanceIdService extends InstanceIDListenerService {

    private final String TAG = GCMInstanceIdService.class.getSimpleName();

    public GCMInstanceIdService() {
        super();
    }

//    public static void start(Context context){
//        context.startService(new Intent(context, GCMInstanceIdService.class));
//    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Log.i(TAG, "onTokenRefresh()");

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
