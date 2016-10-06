package com.example.vincent.eventbusdemo.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.vincent.eventbusdemo.utils.ObjectSerializer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vincent on 27/7/2016.
 */

public class Shots implements Serializable {

    public String id;
    public String title;
    public String description;
    public int width;
    public int height;

    public Images images;
    public int views_count;
    public User user;

    public static void save(Context context, ArrayList<Shots> shots){
        SharedPreferences pref = context.getSharedPreferences("dribbble", Context.MODE_PRIVATE);
        pref.edit().putString("shots", ObjectSerializer.serialize(shots)).apply();
    }

    public static ArrayList<Shots> getAll(Context context) {
        SharedPreferences pref = context.getSharedPreferences("dribbble", Context.MODE_PRIVATE);
        return (ArrayList<Shots>)ObjectSerializer.deserialize(pref.getString("shots", null));
    }

    public class Images implements Serializable{
        public String hidpi;
        public String normal;
        public String teaser;
    }

    public class Links implements Serializable{
        public String web;
        public String twitter;
    }

}
