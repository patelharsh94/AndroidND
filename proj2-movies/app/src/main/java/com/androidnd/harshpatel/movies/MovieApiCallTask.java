package com.androidnd.harshpatel.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by harsh.patel on 5/10/18.
 * Just a Java class made to handle movie data.
 */

public class MovieApiCallTask extends AsyncTask<String, Integer, ArrayList<Movie>> {

    private static String MOVIEAPICALLTASKTAG = "MOVIE_API_CALL";
    MovieDBAPIResultGetter resultGetter = null;
    String type;

    MovieApiCallTask() {
    }

    // A method to parse json from the movie string.
    private ArrayList<Movie> parseJsonFromMovie(String movieData) {

        ArrayList<Movie> finalMovieList = new ArrayList<>();
        try {
            int movieIndex = 0;
            int movieListLength;

            JSONObject movieJSON = new JSONObject(movieData);
            JSONObject currMovie;

            if(this.type.equals("SINGLE_URL")) {
                finalMovieList.add(new Movie(movieJSON.getString("title"),
                        movieJSON.getString("backdrop_path"),
                        movieJSON.getString("poster_path"),
                        movieJSON.getString("release_date"),
                        movieJSON.getString("overview"),
                        movieJSON.getString("vote_average"),
                        movieJSON.getString("vote_count"),
                        movieJSON.getString("id"),
                        0
                ));
            } else {

                JSONArray movieList = movieJSON.getJSONArray("results");

                movieListLength = movieList.length();

                // Gather all movie data into a list of movies.
                while (movieIndex < movieListLength ) {

                    currMovie = movieList.getJSONObject(movieIndex);
                    finalMovieList.add(new Movie(   currMovie.getString("title"),
                            currMovie.getString("backdrop_path"),
                            currMovie.getString("poster_path"),
                            currMovie.getString("release_date"),
                            currMovie.getString("overview"),
                            currMovie.getString("vote_average"),
                            currMovie.getString("vote_count"),
                            currMovie.getString("id"),
                            0
                    ));
                    movieIndex ++;
                }

            }

        } catch (JSONException e) {
            Log.e(MOVIEAPICALLTASKTAG, e.toString());
        }

        return finalMovieList;
    }


    @Override
    protected ArrayList<Movie> doInBackground(String... urls) {

        // The final movie list that we want to return.
        ArrayList<Movie> finalMovieList = new ArrayList<>();

        // Get the data
        try
        {
            this.type = urls[urls.length-1];
            ArrayList<String> API_URL_LIST = new ArrayList<>(Arrays.asList(urls));
            API_URL_LIST.remove(urls.length-1);


            for(String curr_url : API_URL_LIST) {

                URL url = new URL(curr_url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                Log.i(MOVIEAPICALLTASKTAG, url.toString());
                String movieData;



                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));


                while ((movieData = br.readLine()) != null) {
                    finalMovieList.addAll(parseJsonFromMovie(movieData));
                }

                conn.disconnect();
            }



        } catch (Exception e)
        {
            Log.e("TAG", e.toString());

        }

        return finalMovieList;
    }

    @Override
    protected void onPostExecute(final ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        resultGetter.getMovieApiResult(movies);
    }
}
