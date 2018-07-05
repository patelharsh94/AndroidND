package com.androidnd.harshpatel.movies;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by harsh.patel on 5/17/18.
 */

public class MovieGridFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MovieGridFragmentPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position){

            case 0:
                return new PopularMovieGrid();
            case 1:
                return new TopRatedMovieGrid();
            case 2:
                return new FavoriteMovieGrid();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return mContext.getString(R.string.most_popular_title);
            case 1:
                return  mContext.getString(R.string.top_rated_title);
            case 2: 
                return mContext.getString(R.string.favorite_movie_title);
            default:
                return "DEFAULT";
        }
    }
}
