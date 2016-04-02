package net.egordmitriev.watchall.pojo.tmdb;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class CreditsModel implements Parcelable {
    @SerializedName("cast")
    public PersonModel.Base[] cast;
    @SerializedName("crew")
    public PersonModel.Base[] crew;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.cast, flags);
        dest.writeTypedArray(this.crew, flags);
    }

    public CreditsModel() {
    }

    protected CreditsModel(Parcel in) {
        this.cast = in.createTypedArray(PersonModel.Base.CREATOR);
        this.crew = in.createTypedArray(PersonModel.Base.CREATOR);
    }

    public static final Creator<CreditsModel> CREATOR = new Creator<CreditsModel>() {
        @Override
        public CreditsModel createFromParcel(Parcel source) {
            return new CreditsModel(source);
        }

        @Override
        public CreditsModel[] newArray(int size) {
            return new CreditsModel[size];
        }
    };
}
