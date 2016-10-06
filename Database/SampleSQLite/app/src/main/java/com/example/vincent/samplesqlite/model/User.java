package com.example.vincent.samplesqlite.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;

/**
 * Created by vincent on 27/6/2016.
 */
public class User implements Serializable {

    public int id;
    public String name;
    public int phone;
    public String address;
    public String createdAt;

    public User(int id, String name, int phone, String address, String createdAt){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
    }

    public static User get(SQLiteDatabase db, int userId){
        Cursor c = db.rawQuery("SELECT * FROM users WHERE _id = ?", new String[]{String.valueOf(userId)});
        User user = null;
        try{
            if(c.moveToFirst()) {
                user = new User(
                        c.getInt(c.getColumnIndex("_id")),
                        c.getString(c.getColumnIndex("name")),
                        c.getInt(c.getColumnIndex("phone")),
                        c.getString(c.getColumnIndex("address")),
                        c.getString(c.getColumnIndex("created_at")));
            }
        }finally{
            c.close();
        }

        return user;
    }

    @Override
    public String toString() {
        return "[_id : "+id+", name : "+name+", phone : "+phone+", address : "+address+", created_at : "+createdAt+"]";
    }
}
