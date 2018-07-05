package com.androidnd.harshpatel.movies;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class FavMovieDataOpsTask extends AsyncTask<String, Integer, ArrayList<FavoriteMovie>> {

    Context root;
    private FavMovieDB favMovieDB;
    FavMovieDBResultGetter resultGetter = null;


    FavMovieDataOpsTask(Context root) {
        this.root = root;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<FavoriteMovie> favoriteMovies) {
        super.onPostExecute(favoriteMovies);
        resultGetter.getFavMovieData(favoriteMovies);
    }

    @Override
    protected ArrayList<FavoriteMovie> doInBackground(String... strings) {

        ArrayList<FavoriteMovie> favoriteMovies = new ArrayList<>();


        favMovieDB = Room.databaseBuilder(root,
                FavMovieDB.class,
                root.getApplicationContext().getString(R.string.database_name))
                .fallbackToDestructiveMigration()
                .build();
        try {

            String type = strings[0];
            String movie_name, movie_id;
            FavoriteMovie movie = new FavoriteMovie();


            if (type != null) {
                Log.i("TAG", "GOT TYPE: " + type);
                movie_id = strings[1];
                movie_name = strings[2];
                movie.setId(movie_id);
                movie.setTitle(movie_name);

                if(type.equals(root
                        .getString(R.string.update_fav_movie_list))) {
                    favMovieDB.daoAccess().updateMovie(movie);
                } else if (type.equals(root
                        .getString(R.string.insert_fav_movie))) {
                    favMovieDB.daoAccess().insertSingleMovie(movie);
                } else if (type.equals(root
                        .getString(R.string.get_fav_movie_by_id))){
                    favoriteMovies.add(favMovieDB.daoAccess().fetchSingleMovie(movie_id));
                } else if (type.equals(root
                        .getString(R.string.delete_fav_movie))) {
                    favMovieDB.daoAccess().deleteMovie(movie);
                }
                else if (type.equals(root
                        .getString(R.string.get_all_fav_movie))){
                    favoriteMovies.addAll(Arrays.asList(favMovieDB.daoAccess().fetchAllFavMovies()));
                }

            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return favoriteMovies;
    }
}
