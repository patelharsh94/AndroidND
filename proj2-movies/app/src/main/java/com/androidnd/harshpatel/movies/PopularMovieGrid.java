package com.androidnd.harshpatel.movies;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class PopularMovieGrid extends Fragment implements MovieDBAPIResultGetter {

    private String API_URL;
    private MovieApiCallTask apiCallTask;
    private ArrayList<Movie> movieList = null;

    public PopularMovieGrid() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_popular_movie_grid, container, false);
        API_URL =  this.getResources().getString(R.string.api_url_popular_movie);
        apiCallTask = new MovieApiCallTask(root);
        apiCallTask.resultGetter = this;
        apiCallTask.execute(API_URL, String.valueOf(R.string.type_popular));

        return root;
    }

    @Override
    public void getMovieApiResult(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

}
   