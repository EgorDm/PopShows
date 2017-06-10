package net.egordmitriev.popshows.pojo.tmdb.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import net.egordmitriev.popshows.pojo.tmdb.MovieModel;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class MovieListResponse implements Parcelable {

    @SerializedName("results")
    public MovieModel.Base[] results;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.results, flags);
    }

    public MovieListResponse() {
    }

    protected MovieListResponse(Parcel in) {
        this.results = in.createTypedArray(MovieModel.Base.CREATOR);
    }

    public static final Creator<MovieListResponse> CREATOR = new Creator<MovieListResponse>() {
        @Override
        public MovieListResponse createFromParcel(Parcel source) {
            return new MovieListResponse(source);
        }

        @Override
        public MovieListResponse[] newArray(int size) {
            return new MovieListResponse[size];
        }
    };
}
