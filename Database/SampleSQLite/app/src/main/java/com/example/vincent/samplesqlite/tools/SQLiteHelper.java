package com.example.vincent.samplesqlite.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by vincent on 27/6/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cachedb";
    private static final int DB_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(UserColumnInfo.CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // If you need to add a column
        // if (newVersion > oldVersion) {
        //     db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
        // }

        // if you need empty the database
        // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS sample");
        // onCreate(sqLiteDatabase);
    }
}
