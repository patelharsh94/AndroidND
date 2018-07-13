package com.androidnd.harshpatel.movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class FavMoviesViewModel extends ViewModel implements FavMovieDBResultGetter {

    private MutableLiveData<ArrayList<FavoriteMovie>> favMovieList;
    private String API_URL;
    private FavMovieDataOpsTask favMovieDataOpsTask;

    public FavMoviesViewModel() {
        super();
    }

    public void insertFavMovie(FavoriteMovie favoriteMovie,Context root) {
        favMovieDataOpsTask = new FavMovieDataOpsTask(root);
        favMovieDataOpsTask.resultGetter = this;

        favMovieDataOpsTask.execute(root.getString(R.string.insert_fav_movie), favoriteMovie.getId(), favoriteMovie.getTitle());
    }

    public void deleteFavMovie(FavoriteMovie favoriteMovie, Context root) {
        favMovieDataOpsTask = new FavMovieDataOpsTask(root);
        favMovieDataOpsTask.resultGetter = this;

        favMovieDataOpsTask.execute(root.getString(R.string.delete_fav_movie), favoriteMovie.getId(), favoriteMovie.getTitle());
    }

    public LiveData<ArrayList<FavoriteMovie>> getFavMovies(Context root) {

        if(favMovieList == null) {
            favMovieList = new MutableLiveData<ArrayList<FavoriteMovie>>();
        }
        loadMovies(root);

        return favMovieList;
    }

    @Override
    public void getFavMovieData(ArrayList<FavoriteMovie> favoriteMovies) {

        if(this.favMovieList != null) {
            this.favMovieList.setValue(favoriteMovies);
        }
    }

    private void loadMovies(Context root) {
        favMovieDataOpsTask = new FavMovieDataOpsTask(root);
        favMovieDataOpsTask.resultGetter = this;

        favMovieDataOpsTask.execute(root.getString(R.string.get_all_fav_movie), "", "");
    }
}
