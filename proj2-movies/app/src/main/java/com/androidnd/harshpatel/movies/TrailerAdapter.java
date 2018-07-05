package com.androidnd.harshpatel.movies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    public ArrayList<String> trailerTitleList;
    public ArrayList<String> keyList;

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        TextView trailerTitle;
        String key;

        public TrailerViewHolder(View view) {
            super(view);
            trailerTitle = view.findViewById(R.id.trailer_title);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*
                    * Reference:
                    * https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
                    * Author: Roger Garzon Nieto */
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + key));
                    try {
                        view.getContext().startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        view.getContext().startActivity(webIntent);
                    }
                    // -----
                    
                }
            });
        }


    }

    public TrailerAdapter(HashMap<String, String> trailerTitleMap) {

        trailerTitleList =  new ArrayList<>();
        keyList = new ArrayList<>();
        for(String key : trailerTitleMap.keySet()) {
            this.trailerTitleList.add(trailerTitleMap.get(key));
            this.keyList.add(key);
        }

    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);

        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.trailerTitle.setText(trailerTitleList.get(position));
        holder.key = this.keyList.get(position);
    }

    @Override
    public int getItemCount() {
        return trailerTitleList.size();
    }
}
