package net.egordmitriev.popshows.pojo.tmdb.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import net.egordmitriev.popshows.pojo.tmdb.MovieModel;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class MovieLiteResponse implements Parcelable {
    @SerializedName("cast")
    public MovieModel.Base[] cast;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.cast, flags);
    }

    public MovieLiteResponse() {
    }

    protected MovieLiteResponse(Parcel in) {
        this.cast = in.createTypedArray(MovieModel.Base.CREATOR);
    }

    public static final Creator<MovieLiteResponse> CREATOR = new Creator<MovieLiteResponse>() {
        @Override
        public MovieLiteResponse createFromParcel(Parcel source) {
            return new MovieLiteResponse(source);
        }

        @Override
        public MovieLiteResponse[] newArray(int size) {
            return new MovieLiteResponse[size];
        }
    };
}
