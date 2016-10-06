package com.example.vincent.appchat.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vincent on 19/6/2016.
 */
public class Session {

    private final String name = "data";
    private static SharedPreferences pref;
    private static Session sInstance;

    public Session(Context context){
        pref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static synchronized Session getInstance(Context context){
        if(sInstance == null) sInstance = new Session(context);
        return sInstance;
    }

    public void setId(int id){
        pref.edit().putInt("id", id).commit();
    }

    public int getId(){
        return pref.getInt("id", -1);
    }

    public void setName(String name){
        pref.edit().putString("name", name).commit();
    }

    public String getName(){
        return pref.getString("name", null);
    }
}
