package com.example.vincent.samplesqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.vincent.samplesqlite.tools.Constants;
import com.example.vincent.samplesqlite.tools.SQLiteHandler;
import com.example.vincent.samplesqlite.tools.SQLiteObserver;
import com.example.vincent.samplesqlite.tools.UserColumnInfo;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = MainActivity.class.getSimpleName();
    private ListView mListView;
    private SimpleCursorAdapter mAdapter;
    private Button mUpdateBtn, mDropBtn;
    private Timer mTimer = new Timer();
    private SQLiteObserver mContentObserver;
    private String mUserUri = "content://com.example/users";
    private String[] mNames = new String[]{"ADELIA", "ALVA", "ROB", "MOISES", "ARMAND", "RORY", "QUINCY", "EMERY", "SANFORD", "ART", "JOHNATHON", "SHELTON", "CHADWICK", "TRACEY", "RODRICK", "JULES", "KIETH"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContentObserver = new SQLiteObserver(new Handler());

        mListView = (ListView)findViewById(R.id.listview);
        mDropBtn = (Button)findViewById(R.id.drop);
        mUpdateBtn = (Button)findViewById(R.id.update);

        mDropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTimer != null) mTimer.cancel();
                getContentResolver().delete(Uri.parse(mUserUri + "/drop"), null, null);
            }
        });

        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {

                        ContentValues value = new ContentValues();
                        value.put("name", "New One");
                        value.put("user_id", 1001);
                        value.put("phone", 89202022);
                        value.put("address", "New Address No.1");
                        value.put("created_at", new Date().toString());
                        getContentResolver().insert(Uri.parse(mUserUri + "/add"), value);

//                        SQLiteHandler.writeSingle(MainActivity.this, new SQLiteHandler.SQLiteLoader<Object>() {
//                            @Override
//                            public Object process(SQLiteDatabase db) {
//                                ContentValues value = new ContentValues();
//                                value.put("name", "New One");
//                                value.put("user_id", 1001);
//                                value.put("phone", 89202022);
//                                value.put("address", "New Address No.1");
//                                value.put("created_at", new Date().toString());
//                                db.insert(UserColumnInfo.TABLE_NAME, null, value);
//                                return null;
//                            }
//                        });
                    }
                }, 0, 1000);
            }
        });

        SQLiteHandler.write(this, new SQLiteHandler.SQLiteLoader<Object>() {
            @Override
            public Object process(SQLiteDatabase db) {
                for (int i = 0; i < mNames.length; i++) {
                    int userId = 101 + i;
                    Cursor c = db.rawQuery("SELECT _id FROM users WHERE user_id = ? LIMIT 1", new String[]{String.valueOf(userId)});
                    if (c.getCount() == 0) {
                        ContentValues value = new ContentValues();
                        value.put("user_id", userId);
                        value.put("name", mNames[i]);
                        value.put("phone", "9201020" + i);
                        value.put("address", "Address No. " + i);
                        value.put("created_at", new Date().toString());
                        //getContentResolver().insert(Uri.parse(_userUri + "/add"), value);
                        db.insert(UserColumnInfo.TABLE_NAME, null, value);
                    } else {
                        Log.i(TAG, "Exists User Id : " + userId);
                    }

                    c.close();
                }
                return null;
            }
        });

//        User user = SQLiteHandler.read(this, new SQLiteHandler.SQLiteLoader<User>() {
//            @Override
//            public User process(SQLiteDatabase db) {
//                Cursor c = db.rawQuery("SELECT * FROM users WHERE _id = ?", new String[]{"1"});
//                User user = null;
//                if (c.moveToFirst()) {
//                    user = new User(c.getInt(c.getColumnIndex("_id")),
//                            c.getString(c.getColumnIndex("name")),
//                            c.getInt(c.getColumnIndex("phone")),
//                            c.getString(c.getColumnIndex("address")),
//                            c.getString(c.getColumnIndex("created_at")));
//                    c.close();
//                }
//                return user;
//            }
//        });
//
//        Log.i(TAG, "User : " + user.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle params = new Bundle();
        params.putInt("id", 1);
        getSupportLoaderManager().initLoader(Constants.USER_LOADER_ID, params, this);

        getContentResolver().registerContentObserver(
                Uri.parse(mUserUri),
                true, //only notify descendants
                mContentObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mTimer != null){
            mTimer.cancel();
        }

        getSupportLoaderManager().destroyLoader(Constants.USER_LOADER_ID);
        getContentResolver().unregisterContentObserver(mContentObserver);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int userId = args.getInt("id", -1);
        Log.i(TAG, "User ID : " + userId);
        return new CursorLoader(this, Uri.parse(mUserUri), new String[]{"Params 1", "Params 2"}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i(TAG, "Cursor Load Completed [size : " + cursor.getCount() + "]");

        if (mListView.getAdapter() == null) {
            mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"name"}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            mListView.setAdapter(mAdapter);
        }

        mAdapter.changeCursor(cursor);

        while (cursor.moveToNext()) {
            Log.i(TAG, "User Id " + cursor.getString(cursor.getColumnIndex("user_id")));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) mAdapter.swapCursor(null);
    }
}
