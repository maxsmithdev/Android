package com.example.vincent.appchat.listener;

import com.example.vincent.appchat.model.Channel;

/**
 * Created by vincent on 19/6/2016.
 */
public interface OnItemClickListener {
    void onItemClick(Channel channel);
    void onItemDelete(Channel channel);
}
