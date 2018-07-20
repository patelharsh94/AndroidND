package com.androidnd.harshpatel.movies;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {FavoriteMovie.class}, version = 1, exportSchema = false)

public abstract class FavMovieDB extends RoomDatabase {
    public abstract FavMovieDBDaoAccess daoAccess();
}
