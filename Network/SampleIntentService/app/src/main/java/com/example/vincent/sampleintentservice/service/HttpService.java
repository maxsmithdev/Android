package com.example.vincent.sampleintentservice.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.vincent.sampleintentservice.model.DribbbleShots;
import com.example.vincent.sampleintentservice.tools.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 26/6/2016.
 */
public class HttpService extends IntentService {

    private final String TAG = HttpService.class.getSimpleName();
    private final Object mIsLock = new Object();
    private final Intent mSendIntent = new Intent(Constants.HTTP_SERVICE_RECEIVER);

    public static boolean mInterrupted = false;

    public HttpService() {
        super("HttpService");
    }

    public static void stop(Context context, Intent intent){
        mInterrupted = true;
        context.stopService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInterrupted = false;
    }

    private boolean isInterrupted(){
        synchronized (mIsLock){
            return mInterrupted;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service terminal.");
        if(mInterrupted) stopSelf();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(null != intent && intent.hasExtra("uri") && intent.hasExtra("method")) {
            int state = Constants.HTTP_START;
            mSendIntent.putExtra("state", state);
            sendBroadcast(mSendIntent);

            String uri = intent.getStringExtra("uri");
            String method = intent.getStringExtra("method");

            try {
                Thread.sleep(5000);

                String json = getResponse(method, uri, null);
                Type collectionType = new TypeToken<List<DribbbleShots>>() {}.getType();
                //json = "[{\"id\":2799816, \"title\":\"Onboarding\", \"images\": {" +
                //        "      \"hidpi\": \"https://d13yacurqjgara.cloudfront.net/users/116739/screenshots/2799816/drb-test2.png\"," +
                //        "      \"normal\": \"https://d13yacurqjgara.cloudfront.net/users/116739/screenshots/2799816/drb-test2_1x.png\"," +
                //        "      \"teaser\": \"https://d13yacurqjgara.cloudfront.net/users/116739/screenshots/2799816/drb-test2_teaser.png\"" +
                //        "    }}]";

                //Log.i(TAG, json);
                ArrayList<DribbbleShots> shots = new Gson().fromJson(json, collectionType);
                mSendIntent.putExtra("dribbleShots", shots);
                
                state = Constants.HTTP_COMPLETE;
            } catch (IOException e) {
                e.printStackTrace();
                state = Constants.HTTP_ERROR;
            } catch (InterruptedException e) {
                e.printStackTrace();
                state = Constants.HTTP_ERROR;
            }

            mSendIntent.putExtra("state", state);
            sendBroadcast(mSendIntent);
        }
    }

    private String getResponse(String method, String uri, Bundle params) throws IOException, InterruptedException {
        InputStream is = null;
        String response = null;
        HttpURLConnection conn = null;

        try {
            if(isInterrupted()){
                throw new InterruptedException();
            }

            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int state = conn.getResponseCode();
            Log.d(TAG, "The response is: " + state);
            if(state != 200){
                throw new InterruptedException();
            }

            if(isInterrupted()){
                throw new InterruptedException();
            }

            is = conn.getInputStream();

            // Convert the InputStream into a string
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            response = sb.toString();
            conn.disconnect();
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if(null != conn) conn.disconnect();
            if(null != is) is.close();
        }

        return response;
    }

}
