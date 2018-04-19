package com.example.android.cinematrix.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.cinematrix.database.Contract.Entry;

/**
 * Created by mhesah on 2018-04-07.
 */
public class Helper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cinematrix.db";

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + Entry.TABLE_NAME + " (" +
                Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Entry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                Entry.COLUMN_POSTER + " TEXT, " +
                Entry.COLUMN_TITLE + " TEXT NOT NULL, " +
                Entry.COLUMN_VOTE + " TEXT, " +
                Entry.COLUMN_RELEASE + " TEXT, " +
                Entry.COLUMN_PLOT + " TEXT);";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + Entry.TABLE_NAME);
        onCreate(db);
    }
}