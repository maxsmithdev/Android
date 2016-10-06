package com.example.vincent.appchat.http;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by vincent on 20/6/2016.
 */
public class ChatFirebaseMessageService extends FirebaseMessagingService {

    private final String TAG = ChatFirebaseMessageService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From : " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body : " + remoteMessage.getNotification().getBody());
    }

}
