package com.androidnd.harshpatel.movies;

import android.arch.lifecycle.LiveData;


public interface FavMovieDBResultGetter {
    void getFavMovieData(LiveData<FavoriteMovie []> favoriteMovies);
}
