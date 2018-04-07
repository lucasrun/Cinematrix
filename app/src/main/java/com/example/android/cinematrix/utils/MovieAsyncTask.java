package com.example.android.cinematrix.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.cinematrix.BuildConfig;
import com.example.android.cinematrix.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mhesah on 2018-04-01.
 */

public class MovieAsyncTask extends AsyncTask<String, Void, Movie[]> {

    //
    // insert your personal API KEY
    private static final String PERSONAL_API_KEY = BuildConfig.HIDDEN_API_KEY;
    // thank You.
    //

    private static final String LOG_TAG = MovieAsyncTask.class.getSimpleName();
    private static final int TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int RESPONSE_CODE = 200;
    private static final String REQUEST_METHOD = "GET";
    private static final String ERROR_RETRIEVING_DATA = "Error retrieving data";
    private static final String ERROR_CLOSING_STREAM = "Error closing stream";
    private static final String ERROR_RESPONSE_CODE = "Error response code: ";
    private static final String HTTP_ENTRY_URL = "https://api.themoviedb.org/3/movie";
    private static final String API_KEY = "api_key";

    private OnTaskCompleted mListener;

    public MovieAsyncTask(OnTaskCompleted listener) {
        mListener = listener;
    }

    @Override
    protected Movie[] doInBackground(String... params) {
        BufferedReader reader = null;
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {

            URL url = createUrl(params);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(REQUEST_METHOD);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                StringBuilder output = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }

                if (output.length() == 0) {
                    return null;
                }

                jsonResponse = output.toString();

            } else {
                Log.e(LOG_TAG, ERROR_RESPONSE_CODE + urlConnection.getResponseCode());
            }

        } catch (IOException e) {

            Log.e(LOG_TAG, ERROR_RETRIEVING_DATA, e);
            return null;

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, ERROR_CLOSING_STREAM, e);
                }
            }

        }

        try {
            return getJsonData(jsonResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private Movie[] getJsonData(String json) throws JSONException {

        final String POSTER = "poster_path";
        final String TITLE = "original_title";
        final String VOTE = "vote_average";
        final String RELEASE = "release_date";
        final String PLOT = "overview";
        final String RESULTS = "results";

        JSONObject root = new JSONObject(json);
        JSONArray result = root.getJSONArray(RESULTS);

        Movie[] movie = new Movie[result.length()];

        for (int i = 0; i < result.length(); i++) {
            movie[i] = new Movie();
            JSONObject jsonMovie = result.getJSONObject(i);
            movie[i].setPoster(jsonMovie.getString(POSTER));
            movie[i].setTitle(jsonMovie.getString(TITLE));
            movie[i].setVote(jsonMovie.getDouble(VOTE));
            movie[i].setRelease(jsonMovie.getString(RELEASE));
            movie[i].setPlot(jsonMovie.getString(PLOT));
        }
        return movie;

    }

    private URL createUrl(String[] sort_by_param) {

        Uri builtUri = Uri.parse(HTTP_ENTRY_URL).buildUpon()
                .appendPath(sort_by_param[0])
                .appendQueryParameter(API_KEY, PERSONAL_API_KEY)
                .build();
        try {
            URL url = new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Movie[] movie) {

        super.onPostExecute(movie);
        mListener.onTaskCompleted(movie);

    }
}