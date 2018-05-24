package com.androidnd.harshpatel.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by harsh.patel on 5/11/18.
 */

// The Movie Class to store movie data

public class Movie implements Parcelable
{
    private String title, posterUrl, thumbnailUrl, releaseData, summary;

    private Movie(Parcel in) {
        title = in.readString();
        posterUrl = in.readString();
        thumbnailUrl = in.readString();
        releaseData = in.readString();
        summary = in.readString();
    }

    public Movie(String title, String posterUrl, String thumbnailUrl, String releaseData, String summary) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.releaseData = releaseData;
        this.summary = summary;
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

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", releaseData='" + releaseData + '\'' +
                ", summary='" + summary + '\'' +
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
