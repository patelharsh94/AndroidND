package com.androidnd.harshpatel.movies;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;

@Dao
public interface FavMovieDBDaoAccess {

    @Insert
    void insertSingleMovie(FavoriteMovie movie);

    @Query("Select * from FavoriteMovie where id = :id")
    LiveData<FavoriteMovie []> fetchSingleMovie(String id);

    @Query("Select * from FavoriteMovie")
    LiveData<FavoriteMovie []> fetchAllFavMovies();

    @Update
    void updateMovie(FavoriteMovie movie);

    @Delete
    void deleteMovie(FavoriteMovie movie);

}
