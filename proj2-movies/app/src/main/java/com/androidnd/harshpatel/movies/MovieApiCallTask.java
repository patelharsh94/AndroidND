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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by harsh.patel on 5/10/18.
 * Just a Java class made to handle movie data.
 */

public class MovieApiCallTask extends AsyncTask<String, Integer, ArrayList<Movie>> {

    private static String MOVIEAPICALLTASKTAG = "MOVIE_API_CALL";
    MovieDBAPIResultGetter resultGetter = null;
    View root;
    String type;

    MovieApiCallTask(View root) {
        this.root = root;
    }

    // A method to parse json from the movie string.
    private ArrayList<Movie> parseJsonFromMovie(String movieData) {

        ArrayList<Movie> finalMovieList = new ArrayList<>();
        try {
            int movieIndex = 0;
            int movieListLength;

            JSONObject movieJSON = new JSONObject(movieData);
            JSONObject currMovie;
            JSONArray movieList = movieJSON.getJSONArray("results");

            movieListLength = movieList.length();

            // Gather all movie data into a list of movies.
            while (movieIndex < movieListLength ) {

                currMovie = movieList.getJSONObject(movieIndex);
                finalMovieList.add(new Movie(   currMovie.getString("title"),
                                                currMovie.getString("backdrop_path"),
                                                currMovie.getString("poster_path"),
                                                currMovie.getString("release_date"),
                                                currMovie.getString("overview")
                                            ));
                movieIndex ++;
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
            URL url = new URL(urls[0]);
            this.type = urls[1];
            Log.i(MOVIEAPICALLTASKTAG, url.toString());
            String movieData;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");


            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            Log.i(MOVIEAPICALLTASKTAG, "Output from Server .... ");

            while ((movieData = br.readLine()) != null) {
                finalMovieList = parseJsonFromMovie(movieData);
            }

            conn.disconnect();

        } catch (MalformedURLException e)
        {
            Log.e("TAG", e.toString());
        } catch (IOException e)
        {
            Log.e("TAG", e.toString());
        }


        return finalMovieList;
    }

    @Override
    protected void onPostExecute(final ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        resultGetter.getMovieApiResult(movies);
        GridView movie_grid = new GridView(root.getContext());


        if (this.type.equals(String.valueOf(R.string.type_top_rated))) {
            movie_grid =  root.findViewById(R.id.top_rated_grid);
        } else if (this.type.equals(String.valueOf(R.string.type_popular))) {
            movie_grid =  root.findViewById(R.id.most_popular_grid);
        }

        movie_grid.setAdapter(new MovieImageAdapter(root.getContext(), movies));
        movie_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(MOVIEAPICALLTASKTAG, "CLICK ON MOVIE: " + movies.get(i).getTitle());
                Intent movieShowCase = new Intent(view.getContext(), MovieShowCase.class);
                movieShowCase.putExtra(String.valueOf(R.string.movie_data_extra), movies.get(i));
                view.getContext().startActivity(movieShowCase);
            }
        });
    }
}
