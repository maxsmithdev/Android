package com.example.vincent.appchat.http;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.vincent.appchat.model.ChatResult;
import com.example.vincent.appchat.model.Message;
import com.example.vincent.appchat.utils.Constants;
import com.example.vincent.appchat.utils.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 20/6/2016.
 */
public class MessageService extends IntentService {

    private final String TAG = ChannelService.class.getSimpleName();
    private Intent sendIntent = new Intent(Constants.RECEIVER_MESSAGE_TAG);

    public MessageService() {
        super("MessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null && intent.hasExtra("channelId")){
            int playerId = intent.getIntExtra("playerId", -1);
            String channelId = intent.getStringExtra("channelId");
            String apiUrl = "http://"+ Constants.SOCKET_IP +":8080/ratchet/v1/api/channel/"+playerId+"/" + channelId;
            String json = Tools.getJSONFromURL(apiUrl);
            Log.i(TAG, "Result : ("+apiUrl+") " + json);

            if(json != null && !json.trim().equals("")) {
                Type collectionType = new TypeToken<List<Message>>() {}.getType();
                ArrayList<Message> messages = new Gson().fromJson(json, collectionType);
                ChatResult result = new ChatResult();
                result.messages = messages;
                sendIntent.putExtra("messages", result);
                sendBroadcast(sendIntent);
                //Log.i(TAG, "Channel by " + channels.get(0).created_at);
            }

        }
    }
}
