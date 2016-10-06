package com.example.vincent.appchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vincent.appchat.R;
import com.example.vincent.appchat.listener.OnItemClickListener;
import com.example.vincent.appchat.model.Channel;

import java.util.ArrayList;

/**
 * Created by vincent on 20/6/2016.
 */
public class ChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int CHAT = 0;
    private final int GROUP_CHAT = 1;
    private OnItemClickListener listener;
    private ArrayList<Channel> channels = new ArrayList<>();

    public ChannelAdapter(ArrayList<Channel> data){
        this.channels = new ArrayList<>(data);
    }

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        Channel channel = channels.get(position);
        int players = channel.getPlayers();
        return players > 2 ? GROUP_CHAT : CHAT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case CHAT: return new ChatHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat, parent, false));
            case GROUP_CHAT: return new GroupChatHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.group, parent, false));
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder != null){
            Channel channel = channels.get(position);
            if(holder instanceof ChatHolder){
                ChatHolder chatHolder = (ChatHolder)holder;
                if(channel.getName() != null){
                    chatHolder.textView.setText(channel.getName());
                }else{
                    chatHolder.textView.setText("User Name");
                }
            }else if(holder instanceof GroupChatHolder){
                GroupChatHolder groupChatHolder = (GroupChatHolder)holder;
                if(channel.getName() != null){
                    groupChatHolder.textView.setText(channel.getName());
                }else{
                    groupChatHolder.textView.setText("User Name");
                }
            }
        }
    }

    private class ChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView textView;

        public ChatHolder(View view) {
            super(view);

            textView = (TextView)view.findViewById(R.id.title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listener != null) {
                Channel channel = channels.get(getLayoutPosition());
                listener.onItemClick(channel);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    private class GroupChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textView;

        public GroupChatHolder(View view) {
            super(view);

            textView = (TextView)view.findViewById(R.id.title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listener != null) {
                Channel channel = channels.get(getLayoutPosition());
                listener.onItemClick(channel);
            }
        }
    }

}
