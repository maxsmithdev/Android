package com.example.vincent.gcmdemo.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.vincent.gcmdemo.tools.Constants;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by vincent on 28/6/2016.
 */
public class RegistrationIntentService extends IntentService {

    private final String TAG = RegistrationIntentService.class.getSimpleName();

    public RegistrationIntentService() {
        super("RegistrationIntentService");
    }

    public static void start(Context context){
        context.startService(new Intent(context, RegistrationIntentService.class));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(Constants.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "Device Token : " + token);
            Intent i = new Intent(Constants.RECEIVER_REGISTRATION_GCM);
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
