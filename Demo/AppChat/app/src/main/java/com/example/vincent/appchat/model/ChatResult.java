package com.example.vincent.appchat.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vincent on 20/6/2016.
 */
public class ChatResult implements Serializable {

    public ArrayList<Channel> channels = new ArrayList<>();
    public ArrayList<Message> messages = new ArrayList<>();

}
