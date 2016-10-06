package com.example.vincent.appchat.http;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.vincent.appchat.model.Channel;
import com.example.vincent.appchat.model.ChatResult;
import com.example.vincent.appchat.utils.Constants;
import com.example.vincent.appchat.utils.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 20/6/2016.
 */
public class ChannelService extends IntentService {

    private final String TAG = ChannelService.class.getSimpleName();
    private Intent sendIntent = new Intent(Constants.RECEIVER_CHANNEL_TAG);

    public ChannelService() {
        super("ChannelService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){
            String action = intent.getAction();
            if(Constants.ACTION_DOWNLOAD_CHANNEL.equalsIgnoreCase(action) && intent.hasExtra("playerId")) {
                Log.i(TAG, "Download Action");
                int playerId = intent.getIntExtra("playerId", -1);
                String apiUrl = "http://" + Constants.SOCKET_IP + ":8080/ratchet/v1/api/" + playerId + "/channel";
                String json = Tools.getJSONFromURL(apiUrl);
                Log.i(TAG, "Result : (" + apiUrl + ") " + json);

                JSONObject obj = null;
                try {
                    obj = new JSONObject(json);
                    if (json != null && !obj.has("exception")) {
                        Type collectionType = new TypeToken<List<Channel>>() {
                        }.getType();
                        ArrayList<Channel> channels = new Gson().fromJson(json, collectionType);
                        ChatResult result = new ChatResult();
                        result.channels = channels;
                        sendIntent.putExtra("channels", result);
                        sendBroadcast(sendIntent);
                        Log.i(TAG, "Channel by " + channels.get(0).created_at);
                    }else{
                        Log.i(TAG, "Not found data.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(Constants.ACTION_DELETE_CHANNEL.equalsIgnoreCase(action)){
                Log.i(TAG, "Delete Action");
            }
        }
    }

}
