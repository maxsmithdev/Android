package com.example.vincent.appchat.http;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by vincent on 20/6/2016.
 */
public class ChatFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private String TAG = ChatFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refresh token : " + deviceToken);
        sendRegistrationServer(deviceToken);
    }

    public void sendRegistrationServer(String deviceToken){

    }
}
