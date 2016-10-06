package com.example.vincent.appchat.model;

import java.io.Serializable;

/**
 * Created by vincent on 20/6/2016.
 */
public class Channel implements Serializable {

    public int player_id;
    public String channel_id;
    public String name;
    public String text;
    public int count;
    public int role;
    public int players;
    public String player_ids;
    public String created_at;

    public Channel(String channelId, String name, int playerId, int role, int players, int count, String playerIds, String text, String createdAt){
        this.channel_id = channelId;
        this.name = name;
        this.text = text;
        this.player_id = playerId;
        this.role = role;
        this.count = count;
        this.players = players;
        this.player_ids = playerIds;
        this.created_at = createdAt;
    }

    public String getChannelId(){
        return channel_id;
    }

    public void setChannelId(String channelId){
        this.channel_id = channelId;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPlayers(int players){
        this.players = players;
    }

    public int getPlayers(){
        return this.players;
    }

    public void setPlayerIds(String ids){
        this.player_ids = ids;
    }

    public String getPlayerIds(){
        return this.player_ids;
    }

    public void setCount(int count){
        this.count = count;
    }

    public int getCount(){
        return this.count;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public int getRole(){
        return role;
    }

    public void setRole(int role){
        this.role = role;
    }

    public String getCreatedAt(){
        return this.created_at;
    }

    public void setCreatedAt(String createdAt){
        this.created_at = createdAt;
    }
}
