package com.example.vincent.appchat.http;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.vincent.appchat.utils.Constants;

import java.util.Date;
import java.util.HashMap;

import de.tavendo.autobahn.Autobahn;
import de.tavendo.autobahn.AutobahnConnection;

/**
 * Created by vincent on 16/6/2016.
 */
public class SocketService extends Service {

    private final String TAG = "Socket";
    private final String route = "pubsub";
    private final AutobahnConnection mWampConn = new AutobahnConnection();
    private String channelId = "com.channel.world";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        mWampConn.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.hasExtra("players")){
            Log.i(TAG, "Socket Require");
            listener(intent);
            if(intent.hasExtra("method")) {
                String method = intent.getStringExtra("method");
                if (method.equalsIgnoreCase("subscribe")) {

                } else if (method.equalsIgnoreCase("unsubscribe")) {

                }
            }
        }

        return START_STICKY;
    }

    private void listener(Intent intent) {
        if(!mWampConn.isConnected()) {
            Log.i(TAG, "WAMP connection.");
            final int[] players = intent.getIntArrayExtra("players");
            final String wsuri = "ws://" + Constants.SOCKET_IP + ":" + Constants.SOCKET_PORT; // + "/" + route;
            mWampConn.connect(wsuri, new Autobahn.SessionHandler() {

                @Override
                public void onOpen() {
                    Log.i(TAG, "Socket opening.");
                    onCall(players);
                    onSubscribe();
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.i(TAG, "Socket Close.");
                }
            });
        }
    }

    private void onCall(final int[] players) {
        final Long timestamp = System.currentTimeMillis() / 1000;
        channelId = "com.channel." + players[0] + "." + players[1] + "." + timestamp;
        HashMap<String, Object> map = new HashMap<>();
        map.put("players", players);
        map.put("channel_id", channelId);
        mWampConn.call(channelId,
                Integer.class,
                new Autobahn.CallHandler() {

                    @Override
                    public void onResult(Object result) {
                        int res = (Integer) result;
                        Log.d(TAG, "calc:add result = " + res);
                    }

                    @Override
                    public void onError(String error, String info) {

                    }
                }, map);


        HashMap<String, Object> message = new HashMap<>();
        message.put("channel_id", channelId);
        message.put("text", "Hello World");
        message.put("player_id", players[0]);

        mWampConn.publish(channelId, message);
    }

    private static class MyEvent1 {
        public int num;
        public String name;
        public boolean flag;
        public Date created;
        public double rand;
    }

    private void onSubscribe() {
        mWampConn.subscribe(channelId,
                MyEvent1.class,
                new Autobahn.EventHandler() {
                    @Override
                    public void onEvent(String topic, Object event) {
                        //MyEvent1 evt = (MyEvent1) event;
                    }
                }
        );
    }
}
