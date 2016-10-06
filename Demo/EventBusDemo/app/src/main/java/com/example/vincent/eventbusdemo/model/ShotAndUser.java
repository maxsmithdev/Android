package com.example.vincent.eventbusdemo.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.vincent.eventbusdemo.utils.ObjectSerializer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vincent on 27/7/2016.
 */

public class ShotAndUser implements Serializable{

    public ArrayList<Shots> shots;
    public User user;

    public ShotAndUser(ArrayList<Shots> shots, User user){
        this.shots = shots;
        this.user = user;
    }

    public static void save(Context context, ArrayList<Shots> shots, User user){
        SharedPreferences pref = context.getSharedPreferences("dribbble", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("shots", ObjectSerializer.serialize(shots));
        editor.putString("user", ObjectSerializer.serialize(user));
        editor.apply();
    }

    public static ArrayList<Shots> getShotAll(Context context) {
        SharedPreferences pref = context.getSharedPreferences("dribbble", Context.MODE_PRIVATE);
        return (ArrayList<Shots>)ObjectSerializer.deserialize(pref.getString("shots", null));
    }

    public static User getUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences("dribbble", Context.MODE_PRIVATE);
        return (User)ObjectSerializer.deserialize(pref.getString("user", null));
    }
}
