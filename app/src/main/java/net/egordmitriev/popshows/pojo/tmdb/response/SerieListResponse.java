package net.egordmitriev.popshows.pojo.tmdb.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import net.egordmitriev.popshows.pojo.tmdb.SerieModel;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class SerieListResponse implements Parcelable {
    @SerializedName("results")
    public SerieModel.Base[] results;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.results, flags);
    }

    public SerieListResponse() {
    }

    protected SerieListResponse(Parcel in) {
        this.results = in.createTypedArray(SerieModel.Base.CREATOR);
    }

    public static final Creator<SerieListResponse> CREATOR = new Creator<SerieListResponse>() {
        @Override
        public SerieListResponse createFromParcel(Parcel source) {
            return new SerieListResponse(source);
        }

        @Override
        public SerieListResponse[] newArray(int size) {
            return new SerieListResponse[size];
        }
    };
}
