package net.egordmitriev.popshows.pojo.tmdb.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import net.egordmitriev.popshows.pojo.tmdb.SerieModel;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class SerieLiteResponse implements Parcelable {
    @SerializedName("cast")
    public SerieModel.Base[] cast;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.cast, flags);
    }

    public SerieLiteResponse() {
    }

    protected SerieLiteResponse(Parcel in) {
        this.cast = in.createTypedArray(SerieModel.Base.CREATOR);
    }

    public static final Creator<SerieLiteResponse> CREATOR = new Creator<SerieLiteResponse>() {
        @Override
        public SerieLiteResponse createFromParcel(Parcel source) {
            return new SerieLiteResponse(source);
        }

        @Override
        public SerieLiteResponse[] newArray(int size) {
            return new SerieLiteResponse[size];
        }
    };
}
