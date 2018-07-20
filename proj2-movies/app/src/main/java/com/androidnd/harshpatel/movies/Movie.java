package com.androidnd.harshpatel.movies;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harsh.patel on 5/11/18.
 */

// The Movie Class to store movie data

public class Movie implements Parcelable
{
    private String title, posterUrl, thumbnailUrl, releaseData, summary, vote_average, vote_count;
    private int isFavorite;
    private String id;

    private Movie(Parcel in) {
        title = in.readString();
        posterUrl = in.readString();
        thumbnailUrl = in.readString();
        releaseData = in.readString();
        summary = in.readString();
        vote_average = in.readString();
        vote_count = in.readString();
        id = in.readString();
        isFavorite = in.readInt();
    }

    public Movie(String title, String posterUrl, String thumbnailUrl, String releaseData,
                 String summary, String vote_average, String vote_count, String id, int isFavorite) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.releaseData = releaseData;
        this.summary = summary;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.id = id;
        this.isFavorite = isFavorite;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getReleaseData() {
        return releaseData;
    }

    public String getSummary() {
        return summary;
    }

    public String getVote_average() { return vote_average; }

    public String getVote_count() { return vote_count; }

    public String getId() { return id; }

    public int getIsFavorite() { return isFavorite; };

    public void setIsFavorite(int val) {
        this.isFavorite = val;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", releaseData='" + releaseData + '\'' +
                ", summary='" + summary + '\'' +
                ", vote_count='" + vote_count + '\'' +
                ", vote_average='" + vote_average + '\'' +
                ", id='" + id + '\'' +
                ", isFavorite='" + isFavorite + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.posterUrl);
        parcel.writeString(this.thumbnailUrl);
        parcel.writeString(this.releaseData);
        parcel.writeString(this.summary);
        parcel.writeString(this.vote_average);
        parcel.writeString(this.vote_count);
        parcel.writeString(this.id);
        parcel.writeInt(this.isFavorite);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[0];
        }
    };
}
