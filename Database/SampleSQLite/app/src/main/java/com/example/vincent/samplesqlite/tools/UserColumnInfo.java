package com.example.vincent.samplesqlite.tools;

import android.provider.BaseColumns;

/**
 * Created by vincent on 27/6/2016.
 */
public class UserColumnInfo implements BaseColumns {

    public static final String PATH = "users";
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_CREATED_AT = "created_at";

    public static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +"("+
            BaseColumns._ID +" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+ //PRIMARY KEY = AUTOINCREMENT
            COLUMN_USER_ID + " INT(11) DEFAULT -1," +
            COLUMN_NAME + " VARCHAR(255) NULL," + //varchar maximum 255 length
            COLUMN_PHONE + " INT(11) DEFAULT 0," +
            COLUMN_ADDRESS + " TEXT NULL," /* DEFAULT 'No Address' */ + //text maximum 65536 legnth
            COLUMN_CREATED_AT + " VARCHAR(255) NULL" + ")";

}
