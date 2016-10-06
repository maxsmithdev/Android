package com.example.vincent.appchat.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vincent.appchat.R;
import com.example.vincent.appchat.adapter.ChannelAdapter;
import com.example.vincent.appchat.adapter.MessageAdapter;
import com.example.vincent.appchat.http.ChannelService;
import com.example.vincent.appchat.http.MessageService;
import com.example.vincent.appchat.model.ChatResult;
import com.example.vincent.appchat.model.Message;
import com.example.vincent.appchat.utils.Constants;
import com.example.vincent.appchat.utils.Session;

import java.util.HashMap;

import de.tavendo.autobahn.Autobahn;
import de.tavendo.autobahn.AutobahnConnection;

/**
 * Created by vincent on 20/6/2016.
 */
public class ChatRoomActivity extends AppCompatActivity {

    private final String TAG = ChatRoomActivity.class.getSimpleName();
    private final AutobahnConnection mWampConn = new AutobahnConnection();

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MessageAdapter messageAdapter;
    private Button send;
    private EditText messageInput;
    private int playerId;
    private int[] playerIds;
    private String channelId = null;
    private String channelName = null;
    private String wsuri = null;
    private Intent serviceIntent;
    private BroadcastReceiver receiver = null;
    private BroadcastReceiver getReceiver(){
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.hasExtra("messages")) {
                    ChatResult chatResult = (ChatResult) intent.getSerializableExtra("messages");
                    if(recyclerView.getAdapter() == null){
                        messageAdapter = new MessageAdapter(chatResult.messages);
                        messageAdapter.setId(playerId);
                        recyclerView.setAdapter(messageAdapter);
                    }
                    //Log.i(TAG, "Message by " + chatResult.channels.get(0).created_at);
                }else if(intent.hasExtra("message")){
                    Message message = (Message)intent.getSerializableExtra("messages");
                    if(messageAdapter != null)messageAdapter.setMessage(message);
                }
            }
        };
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        final Intent args = getIntent();
        if(args != null){
            channelId = args.getStringExtra("channelId");
            channelName = args.getStringExtra("channelName");
            playerIds = args.getIntArrayExtra("playerIds");
        }

        playerId = Session.getInstance(this).getId();
        serviceIntent = new Intent(this, MessageService.class);
        serviceIntent.putExtra("channelId", channelId);
        serviceIntent.putExtra("playerId", playerId);

        wsuri = "ws://" + Constants.SOCKET_IP + ":" + Constants.SOCKET_PORT; // + "/" + route;
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);

        messageInput = (EditText)findViewById(R.id.message);
        send = (Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = messageInput.getText().toString();
                if(!text.trim().equals("")) {
                    messageInput.setText("");
                    sendMessage(text);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(receiver == null){
            receiver = getReceiver();
        }

        registerReceiver(receiver, new IntentFilter(Constants.RECEIVER_MESSAGE_TAG));

        if(!mWampConn.isConnected()) {
            Log.i(TAG, "WAMP connection.");
            mWampConn.connect(wsuri, new Autobahn.SessionHandler() {

                @Override
                public void onOpen() {
                    Log.i(TAG, "Socket opening.");
                    if(channelId == null){
                        final Long timestamp = System.currentTimeMillis() / 1000;
                        channelId = "com.channel." + playerIds[0] + "." + timestamp;
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("playerIds", playerIds);
                        map.put("channel_id", channelId);
                        if(channelName != null) map.put("channel_name", channelName);

                        Log.i(TAG, "Socket -- onCall("+map.toString()+")");
                        mWampConn.call(channelId,
                                HashMap.class,
                                new Autobahn.CallHandler() {

                                    @Override
                                    public void onResult(Object result) {
                                        Log.i(TAG, "onCall -- onResult()");
                                        //subcribe();
                                    }

                                    @Override
                                    public void onError(String error, String info) {
                                        Log.i(TAG, "onCall -- onError()");
                                    }
                                }, map);

                        //subcribe();
                    }else{
                        subcribe();
                    }

                    startService(serviceIntent);
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.i(TAG, "Socket Close.");
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        if(mWampConn.isConnected()){
            mWampConn.disconnect();
            mWampConn.unsubscribe();
        }

        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private void subcribe(){
        Log.i(TAG, "Socket -- subscribe()");
        mWampConn.subscribe(channelId, HashMap.class, new Autobahn.EventHandler() {
            @Override
            public void onEvent(String s, Object o) {
                HashMap<String, Object> map = (HashMap<String, Object>)o;
                messageAdapter.setMessage(new Message((String)map.get("channel_id"), (int)map.get("player_id"), (String)map.get("text"), (String)map.get("created_at")));
                Log.i(TAG, "Result : " + map.toString());
            }
        });
    }

    private void sendMessage(String text){
        if(mWampConn.isConnected()){
            HashMap<String, Object> message = new HashMap<>();
            message.put("channel_id", channelId);
            message.put("text", text);
            message.put("player_id", playerIds[0]);
            mWampConn.publish(channelId, message);
        }
    }

}
