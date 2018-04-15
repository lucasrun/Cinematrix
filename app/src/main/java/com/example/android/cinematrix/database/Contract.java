package com.example.android.cinematrix.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mhesah on 2018-04-07.
 */

public class Contract {
    public static final String CONTENT_AUTHORITY = "com.example.android.cinematrix";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public Contract() {
    }

    public static final class Entry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // table
        public static final String TABLE_NAME = "movie";

        // table columns
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER = "image";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE = "vote";
        public static final String COLUMN_RELEASE = "release";
        public static final String COLUMN_PLOT = "plot";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}