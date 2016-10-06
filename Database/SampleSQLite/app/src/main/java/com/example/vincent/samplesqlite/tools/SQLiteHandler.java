package com.example.vincent.samplesqlite.tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by vincent on 27/6/2016.
 */
public class SQLiteHandler {

    public interface SQLiteLoader<T>{
        T process(SQLiteDatabase db);
    }

    public static Cursor getCursor(Context context, String sql){
        SQLiteDatabase db = new SQLiteHelper(context).getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public static <T> T read(Context context, SQLiteLoader<T> loader){
        SQLiteDatabase db = new SQLiteHelper(context).getReadableDatabase();
        T obj = null;
        try{
            obj = loader.process(db);
        }finally{
            db.close();
        }

        return obj;
    }

    public static void writeSingle(Context context, SQLiteLoader<Object> loader){
        SQLiteDatabase db = new SQLiteHelper(context).getWritableDatabase();
        try{
            loader.process(db);
        }finally{
            db.close();
        }
    }

    public static void write(Context context, SQLiteLoader<Object> loader){
        SQLiteDatabase db = new SQLiteHelper(context).getWritableDatabase();
        db.beginTransaction();
        try{
            loader.process(db);
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
            db.close();
        }
    }

    private SQLiteHelper _sqliteHelper;

    public SQLiteHandler(Context context){
        _sqliteHelper = new SQLiteHelper(context);
    }

    public SQLiteDatabase getReadable(){
        return _sqliteHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWriteable(){
        return _sqliteHelper.getWritableDatabase();
    }

    public void disconnect(){
        _sqliteHelper.close();
        _sqliteHelper = null;
    }

}
