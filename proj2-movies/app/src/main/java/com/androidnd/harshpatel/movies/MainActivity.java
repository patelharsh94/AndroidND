package com.androidnd.harshpatel.movies;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewParent;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager =  findViewById(R.id.tab_view_pager);

        MovieGridFragmentPagerAdapter movieGridFragmentPagerAdapter = new MovieGridFragmentPagerAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(movieGridFragmentPagerAdapter);

        TabLayout movieTab = findViewById(R.id.movieTab);
        movieTab.setupWithViewPager(viewPager);
    }
}
