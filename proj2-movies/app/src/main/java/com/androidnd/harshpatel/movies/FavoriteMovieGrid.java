package com.androidnd.harshpatel.movies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.security.Signature;
import java.util.ArrayList;
import java.util.HashMap;


public class FavoriteMovieGrid extends Fragment implements MovieDBAPIResultGetter, FavMovieDBResultGetter {

    private String API_URL, SINGLE_URL;
    private MovieApiCallTask apiCallTask;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private HashMap<String, Movie> movieHashMap = new HashMap<>();
    private final ArrayList<Movie> finalMovieList = new ArrayList<>();
    private ArraySet<String> movieIdSet = new ArraySet<>();
    private View root;
    private GridView movie_grid;
    private FavMovieDataOpsTask favMovieDataOpsTask;
    private ArrayList<FavoriteMovie> favoriteMovies = new ArrayList<>();
    private int  i = 0;

    public FavoriteMovieGrid() {
        // Required empty public constructor
    }

    public void populateGrid (ArrayList<FavoriteMovie> favMovies) {

        API_URL =  this.getResources().getString(R.string.api_url_single_movie);

        String type = root.getResources().getString(R.string.type_single_url);
        for(FavoriteMovie favoriteMovie : favMovies) {
            SINGLE_URL = String.format(API_URL, favoriteMovie.getId());

            apiCallTask = new MovieApiCallTask(root);
            apiCallTask.resultGetter = this;
            apiCallTask.execute(SINGLE_URL, type);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_favorite_movie_grid, container, false);

        Log.i("TAG_b", "ON_CREATE_VIEW: " + i);
        i++;
        return root;
    }

    @Override
    public void getMovieApiResult( ArrayList<Movie> movies) {

        for(Movie movie : movies) {
            movieHashMap.put(movie.getId(), movie);
            /*
            if(!this.movieIdSet.contains(movie.getId())) {
                this.movieList.add(movie);
            } else {
                Log.i("TAG_b", "MOVIE REMOVED: " + movie.getTitle());
                this.movieList.remove(movie);
                Log.i("TAG_b", "AFTER REMOVED: " + movieList.toString());
            }
            */
        }

        this.movieList = new ArrayList<>();

        for(String key : movieHashMap.keySet()) {
            this.movieList.add(movieHashMap.get(key));
        }
        /*
        for(FavoriteMovie movie : favoriteMovies) {
            this.movieIdSet.add(movie.getId());
        }*/


        Log.i("TAG_B", "GOT SIZE: " + this.movieList.size() + " FAV MOVIES: " + this.favoriteMovies.size());

        movie_grid = root.findViewById(R.id.favorite_movie_grid);
        movie_grid.setAdapter(new MovieImageAdapter(root.getContext(), this.movieList));
        movie_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Movie currMovie = movieList.get(i);

                Intent movieShowCase = new Intent(view.getContext(), MovieShowCase.class);
                movieShowCase.putExtra(String.valueOf(R.string.movie_data_extra), currMovie);
                view.getContext().startActivity(movieShowCase);
            }
        });

        /*
        if (this.movieList.size() == this.favoriteMovies.size())
        {
            movie_grid = root.findViewById(R.id.favorite_movie_grid);
            movie_grid.setAdapter(new MovieImageAdapter(root.getContext(), this.movieList));
            movie_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Movie currMovie = movieList.get(i);

                    Intent movieShowCase = new Intent(view.getContext(), MovieShowCase.class);
                    movieShowCase.putExtra(String.valueOf(R.string.movie_data_extra), currMovie);
                    view.getContext().startActivity(movieShowCase);
                }
            });
        }*/

        i++;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.i("TAG_b", "ON_START: " + i);
        i++;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("TAG_b", "ON_RESUME: " + i);
        favMovieDataOpsTask = new FavMovieDataOpsTask(root.getContext());
        favMovieDataOpsTask.resultGetter = this;

        favMovieDataOpsTask.execute(root.getContext().getString(R.string.get_all_fav_movie), "", "");
        i++;
    }

    @Override
    public void getFavMovieData(ArrayList<FavoriteMovie> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
        Log.i("TAG_b", "GOT FAV: " + favoriteMovies.toString());
        ArrayList<String> removeKeys = new ArrayList<>();
        movieIdSet = new ArraySet<>();
        for(FavoriteMovie movie : this.favoriteMovies) {
            movieIdSet.add(movie.getId());
        }


        for(String key : movieHashMap.keySet()) {
            if(!movieIdSet.contains(key)) {
                removeKeys.add(key);
            }
        }

        for(String key : removeKeys) {
            movieHashMap.remove(key);
        }
        populateGrid(favoriteMovies);
    }

}
