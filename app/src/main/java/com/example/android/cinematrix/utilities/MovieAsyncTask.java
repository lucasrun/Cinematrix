package com.example.android.cinematrix.utilities;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.cinematrix.BuildConfig;
import com.example.android.cinematrix.interfaces.MovieTaskCompleted;
import com.example.android.cinematrix.models.Movie;

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
import java.util.ArrayList;

/**
 * Created by mhesah on 2018-04-01.
 */

public class MovieAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private static final String PERSONAL_API_KEY = BuildConfig.HIDDEN_API_KEY;
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

    private MovieTaskCompleted mListener;

    public MovieAsyncTask(MovieTaskCompleted listener) {
        mListener = listener;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
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

    private ArrayList<Movie> getJsonData(String json) throws JSONException {

        final String RESULTS = "results";

        JSONObject root = new JSONObject(json);
        JSONArray result = root.getJSONArray(RESULTS);

        ArrayList<Movie> movie = new ArrayList<>();

        for (int i = 0; i < result.length(); i++) {
            JSONObject jsonMovie = result.getJSONObject(i);
            Movie movieExtracted = new Movie(jsonMovie);
            movie.add(movieExtracted);
        }
        return movie;

    }

    private URL createUrl(String[] sort_param) {

        Uri builtUri = Uri.parse(HTTP_ENTRY_URL).buildUpon()
                .appendPath(sort_param[0])
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
    protected void onPostExecute(ArrayList<Movie> movie) {

        super.onPostExecute(movie);
        mListener.movieTaskCompleted(movie);

    }
}