package com.example.vincent.appchat.model;

import java.io.Serializable;

/**
 * Created by vincent on 20/6/2016.
 */
public class Message implements Serializable {

    public int player_id;
    public String channel_id;
    public String text;
    public String created_at;

    public Message(String channelId, int playerId, String text, String createdAt){
        this.channel_id = channelId;
        this.player_id = playerId;
        this.text = text;
        this.created_at = createdAt;
    }

    public void setChannelId(String channelId){
        this.channel_id = channelId;
    }

    public String getChannelId(){
        return this.channel_id;
    }

    public void setPlayerId(int playerId){
        this.player_id = playerId;
    }

    public int getPlayerId(){
        return this.player_id;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}
