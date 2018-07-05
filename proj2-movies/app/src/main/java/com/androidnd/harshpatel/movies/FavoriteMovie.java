package com.androidnd.harshpatel.movies;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

@Entity
public class FavoriteMovie {

    @NonNull
    @PrimaryKey
    private String id;
    private String title;

    public FavoriteMovie() {
    }

    public String getId() { return id; }
    public String getTitle() { return title; }

    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        super.equals(obj);

        // if the same location in memory
        if (this == obj) {
            return true;
        }

        if ((obj == null) && (obj.getClass() != this.getClass())) {
            return false;
        }

        FavoriteMovie favMov = (FavoriteMovie) obj;

        if (favMov.getId().equals(id) && favMov.getTitle().equals(title)) {
            Log.i("EQUALS", "FOUND EQUAlS: " + favMov.toString());
        } else {
            Log.i("NOT EQUALS", favMov.toString());
        }
        return favMov.getId().equals(id) && favMov.getTitle().equals(title);
    }

    @Override
    public String toString() {
        return id + " : " + title;
    }
}
