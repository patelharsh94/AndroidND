package com.androidnd.harshpatel.movies;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class FavMovieDBManipulator implements FavMovieDBResultGetter {

    private ArrayList<FavoriteMovie> favoriteMovies = new ArrayList<>();
    private ArrayList<String> favMovieIds = new ArrayList<>();
    private FavMovieDataOpsTask favMovieDataOpsTask;
    private Context root;

    private void initializeOpsTask(Context root) {
        favMovieDataOpsTask = new FavMovieDataOpsTask(root);
        favMovieDataOpsTask.resultGetter = this;

    }

    public FavMovieDBManipulator(Context root) {
        initializeOpsTask(root);
        favMovieDataOpsTask.execute(root.getString(R.string.get_all_fav_movie),"","");
        this.root = root;
    }

    // to insert
    public void insert_data(FavoriteMovie favoriteMovie) {
        initializeOpsTask(root);
        favMovieDataOpsTask.execute(root.getString(R.string.insert_fav_movie), favoriteMovie.getId(), favoriteMovie.getTitle());
    }

    // to delete
    public void delete_data(FavoriteMovie favoriteMovie) {
        initializeOpsTask(root);
        favMovieDataOpsTask.execute(root.getString(R.string.delete_fav_movie), favoriteMovie.getId(), favoriteMovie.getTitle());
    }

    @Override
    public void getFavMovieData(ArrayList<FavoriteMovie> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }
}
