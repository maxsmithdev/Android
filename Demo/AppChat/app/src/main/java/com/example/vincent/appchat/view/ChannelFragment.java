package com.example.vincent.appchat.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.vincent.appchat.R;
import com.example.vincent.appchat.adapter.ChannelAdapter;
import com.example.vincent.appchat.http.ChannelService;
import com.example.vincent.appchat.listener.OnItemClickListener;
import com.example.vincent.appchat.model.Channel;
import com.example.vincent.appchat.model.ChatResult;
import com.example.vincent.appchat.utils.Constants;
import com.example.vincent.appchat.utils.Session;

/**
 * Created by vincent on 19/6/2016.
 */
public class ChannelFragment extends Fragment implements OnItemClickListener {

    private final String TAG = ChannelFragment.class.getSimpleName();
    private Toolbar toolbar;
    private Intent serviceIntent;
    private RecyclerView recyclerView;
    private ChannelAdapter channelAdapter;
    private LinearLayoutManager layoutManager;

    private BroadcastReceiver receiver = null;
    private BroadcastReceiver getReceiver(){
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(Constants.ACTION_DOWNLOAD_CHANNEL.equalsIgnoreCase(action)) {
                    if (intent.hasExtra("channels")) {
                        ChatResult chatResult = (ChatResult) intent.getSerializableExtra("channels");
                        if (recyclerView.getAdapter() == null) {
                            channelAdapter = new ChannelAdapter(chatResult.channels);
                            channelAdapter.setListener(ChannelFragment.this);
                            recyclerView.setAdapter(channelAdapter);
                        }
                        Log.i(TAG, "Channel by " + chatResult.channels.get(0).created_at);
                    }
                }else if(Constants.ACTION_DELETE_CHANNEL.equalsIgnoreCase(action)){

                }
            }
        };
    }

    public static ChannelFragment getInstance(int id){
        final ChannelFragment f = new ChannelFragment();
        final Bundle p = new Bundle();
        p.putInt("id", id);
        f.setArguments(p);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_channel, container, false);

        serviceIntent = new Intent(getContext(), ChannelService.class);
        serviceIntent.putExtra("playerId", Session.getInstance(getContext()).getId());

        toolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(Session.getInstance(getContext()).getName());
        toolbar.inflateMenu(R.menu.menu_channel);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add:
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setItems(Constants.names, new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final Intent intent = new Intent(getContext(), ChatRoomActivity.class);
                                final int[] playerIds = new int[3];
                                playerIds[0] = Session.getInstance(getContext()).getId();
                                playerIds[1] = Constants.ids[i];
                                playerIds[2] = 22;

                                intent.putExtra("channelId", "com.channel.68.1466411782");
                                //intent.putExtra("channelName", "Chat Room 1");
                                intent.putExtra("playerIds", playerIds);
                                getContext().startActivity(intent);
                            }
                        });

                        builder.create().show();
                        break;
                }
                return false;
            }
        });

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();

        if(receiver == null){
            receiver = getReceiver();
        }

        getContext().registerReceiver(receiver, new IntentFilter(Constants.RECEIVER_CHANNEL_TAG));
        serviceIntent.setAction(Constants.ACTION_DOWNLOAD_CHANNEL);
        getContext().startService(serviceIntent);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(receiver != null){
            getContext().unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void onItemClick(Channel channel) {
        final Intent intent = new Intent(getContext(), ChatRoomActivity.class);
        //final int[] players = new int[3];
        //players[0] = ;
        //players[1] = Constants.ids[i];
        //players[2] = 22;

        String[] ids = channel.getPlayerIds().split(",");
        int[] playerIds = new int[ids.length];
        for(int i=0;i<ids.length;i++) playerIds[i] = Integer.parseInt(ids[i]);

        intent.putExtra("channelId", channel.getChannelId());
        intent.putExtra("channelName", channel.getName());
        intent.putExtra("playerIds", playerIds);
        getContext().startActivity(intent);
    }

    @Override
    public void onItemDelete(Channel channel) {
        serviceIntent.setAction(Constants.ACTION_DELETE_CHANNEL);
        serviceIntent.putExtra("channelId", channel.getChannelId());
        getContext().startService(serviceIntent);
    }
}
