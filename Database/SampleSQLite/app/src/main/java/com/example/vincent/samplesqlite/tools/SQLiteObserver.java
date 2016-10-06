package com.example.vincent.samplesqlite.tools;

import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

/**
 * Created by vincent on 27/6/2016.
 */
public class SQLiteObserver extends ContentObserver {

    private final String TAG = SQLiteObserver.class.getSimpleName();

    public SQLiteObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.i(TAG, "SQLite Changed.");
        super.onChange(selfChange);
    }
}
