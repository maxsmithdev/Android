package com.example.vincent.appchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vincent.appchat.R;
import com.example.vincent.appchat.listener.OnItemClickListener;
import com.example.vincent.appchat.model.Message;

import java.util.ArrayList;

/**
 * Created by vincent on 16/6/2016.
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int SENEDER_TYPE = 0;
    private final int RECEIVER_TYPE = 1;
    private int currentId = -1;
    private ArrayList<Message> messages = new ArrayList<>();

    public MessageAdapter(ArrayList<Message> data){
        messages = new ArrayList<>(data);
    }

    public void setMessage(Message message){
        messages.add(message);
        notifyDataSetChanged();
    }

    public void setId(int playerId){
        this.currentId = playerId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return currentId == message.getPlayerId() ? SENEDER_TYPE : RECEIVER_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case SENEDER_TYPE: return new SenderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sender, parent, false));
            case RECEIVER_TYPE: return new ReceiverHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder != null){
            final Message message = messages.get(position);

            if(holder instanceof SenderHolder) {
                SenderHolder senderHolder = (SenderHolder)holder;
                senderHolder.textView.setText(message.getText());

            }else if(holder instanceof ReceiverHolder){
                ReceiverHolder receiverHolder = (ReceiverHolder)holder;
                receiverHolder.textView.setText(message.getText());
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private class SenderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textView;

        public SenderHolder(View view) {
            super(view);

            textView = (TextView)view.findViewById(R.id.sender_message);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private static class ReceiverHolder extends RecyclerView.ViewHolder{

        public TextView textView;

        public ReceiverHolder(View view) {
            super(view);

            textView = (TextView)view.findViewById(R.id.receiver_message);
        }
    }

}
