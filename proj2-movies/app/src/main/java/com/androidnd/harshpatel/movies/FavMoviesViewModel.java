package com.androidnd.harshpatel.movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class FavMoviesViewModel extends ViewModel implements FavMovieDBResultGetter {

    private LiveData<FavoriteMovie []> favMovieList;
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

    public LiveData<FavoriteMovie []> getFavMovies(Context root) {

        if(favMovieList == null) {
            favMovieList = new MutableLiveData<>();
        }
        loadMovies(root);

        return favMovieList;
    }

    @Override
    public void getFavMovieData(LiveData<FavoriteMovie []> favoriteMovies) {

        if(favoriteMovies != null) {
            Log.i("TAG_G", "FOUND MOVIES: "  + favoriteMovies.getValue());
            this.favMovieList = favoriteMovies;
        }
    }

    private void loadMovies(Context root) {
        favMovieDataOpsTask = new FavMovieDataOpsTask(root);
        favMovieDataOpsTask.resultGetter = this;

        favMovieDataOpsTask.execute(root.getString(R.string.get_all_fav_movie), "", "");
    }
}
