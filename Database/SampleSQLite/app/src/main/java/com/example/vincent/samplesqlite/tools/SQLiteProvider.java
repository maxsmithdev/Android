package com.example.vincent.samplesqlite.tools;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by vincent on 27/6/2016.
 */
public class SQLiteProvider extends ContentProvider {

    private final String TAG = SQLiteProvider.class.getSimpleName();
    public static final String AUTHORITY = "com.example";

    static UriMatcher mUriMatcher;
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, UserColumnInfo.PATH + "/add", Constants.USER_ADD);
        mUriMatcher.addURI(AUTHORITY, UserColumnInfo.PATH + "/delete", Constants.USER_DELETE);
        mUriMatcher.addURI(AUTHORITY, UserColumnInfo.PATH + "/drop", Constants.USER_DROP);
        mUriMatcher.addURI(AUTHORITY, UserColumnInfo.PATH, Constants.USER_ALL);
    }

    @Override
    public boolean onCreate() {
        Log.i(TAG, "ContentProvider Created.");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int action = mUriMatcher.match(uri);
        Log.i(TAG, "SQLiteProvider Query [uri : "+uri+", action : "+action+"]");

        if(null != projection){
            for(String param : projection) {
                Log.i(TAG, "[projection : " + param + "]");
            }
        }

        switch (action){
            case Constants.USER_ALL:
                return SQLiteHandler.getCursor(getContext(), "SELECT * FROM users");
        }

        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, final ContentValues contentValues) {
        int action = mUriMatcher.match(uri);
        SQLiteDatabase db = new SQLiteHelper(getContext()).getWritableDatabase();
        Log.i(TAG, "SQLiteProvider Insert [uri : "+uri+", action : "+action+"]");
        switch (action){
            case Constants.USER_ADD:
                db.insert(UserColumnInfo.TABLE_NAME, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int action = mUriMatcher.match(uri);
        SQLiteDatabase db = new SQLiteHelper(getContext()).getWritableDatabase();
        Log.i(TAG, "SQLiteProvider Delete [uri : "+uri+", action : "+action+"]");
        switch (action){
            case Constants.USER_DROP:
                db.delete(UserColumnInfo.TABLE_NAME, "", null);
                //getContext().getContentResolver().notifyChange(uri, null);
                break;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = new SQLiteHelper(getContext()).getWritableDatabase();
        db.beginTransaction();
        int length = values.length;
        try{
            for(ContentValues value : values){
                insert(uri, value);
            }
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
            db.close();
        }

        return length;
    }
}
