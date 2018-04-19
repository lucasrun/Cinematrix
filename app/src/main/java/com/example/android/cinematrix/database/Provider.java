package com.example.android.cinematrix.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.example.android.cinematrix.database.Contract.CONTENT_AUTHORITY;
import static com.example.android.cinematrix.database.Contract.Entry;
import static com.example.android.cinematrix.database.Contract.PATH_MOVIE;

/**
 * Created by mhesah on 2018-04-07.
 */

public class Provider extends ContentProvider {

    public static final String LOG_TAG = Provider.class.getSimpleName();
    private static final int MOVIES = 100;
    private static final int MOVIES_ID = 101;
    private static final String UNKNOWN_URI = "Query failed, unknown URI: ";
    private static final String MATCH = ". Match: ";
    private static final String INSERTION_NOT_SUPPORTED = "Insertion is not supported for ";
    private static final String UPDATE_NOT_SUPPORTED = "Update is not supported for ";
    private static final String FAILED_TO_INSERT_ROW = "Failed to insert row for ";
    private static final String FIRST_ROW = "1";
    private static final String DB_SIGN = "=?";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE, MOVIES);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_MOVIE + "/#", MOVIES_ID);
    }

    private Helper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new Helper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES: {
                cursor = database.query(
                        Entry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            }
            case MOVIES_ID:
                selection = Entry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(Entry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                Log.e(LOG_TAG, UNKNOWN_URI + uri);
        }

        if (cursor != null) cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return insertItem(uri, values);
            default:
                Log.e(LOG_TAG, INSERTION_NOT_SUPPORTED  + uri);
                return null;
        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {

        SQLiteDatabase database = mHelper.getWritableDatabase();

        long id = database.insert(Contract.Entry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, FAILED_TO_INSERT_ROW + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return Contract.Entry.buildMovieUri(id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return updateItem(uri, values, selection, selectionArgs);
            case MOVIES_ID:
                selection = Entry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, values, selection, selectionArgs);
            default:
                Log.e(LOG_TAG, UPDATE_NOT_SUPPORTED + uri);
                return -1;
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mHelper.getWritableDatabase();
        int numOfRows = database.update(Contract.Entry.TABLE_NAME, values, selection, selectionArgs);
        if (numOfRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numOfRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase database = mHelper.getWritableDatabase();

        if (null == selection) selection = FIRST_ROW;
        switch (match) {
            case MOVIES:
                int rowsDeleted = database.delete(Contract.Entry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case MOVIES_ID:
                selection = Entry._ID + DB_SIGN;
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int rowDeleted = database.delete(Entry.TABLE_NAME, selection, selectionArgs);
                if (rowDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowDeleted;
            default:
                Log.e(LOG_TAG, UNKNOWN_URI + uri);
                return -1;
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return Contract.Entry.CONTENT_TYPE;
            case MOVIES_ID:
                return Entry.CONTENT_ITEM_TYPE;
            default:
                Log.e(LOG_TAG, UNKNOWN_URI + uri + MATCH + match);
                return null;
        }
    }
}