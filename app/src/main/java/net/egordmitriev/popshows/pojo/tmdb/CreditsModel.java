package net.egordmitriev.popshows.pojo.tmdb;

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

    public String getActors(int count) {
        StringBuilder ret = null;
        if (cast != null && cast.length > 0) {
            ret = new StringBuilder(cast[0].name);
            if (count > cast.length) count = cast.length;
            for (int i = 1; i < count; i++) {
                ret.append(", ").append(cast[i].name);
            }
        }

        return (ret != null) ? ret.toString() : null;
    }

    public String getCrew(String jobName, int count) {
        StringBuilder ret = null;
        if (crew != null && crew.length > 0) {
            ret = new StringBuilder();
            if (count > cast.length) count = cast.length;
            int index = 0;
            for (PersonModel.Base person : crew) {
                if (index < count) {
                    if (person.job.contentEquals(jobName)) {
                        if (index == 0) {
                            ret.append(person.name);
                        } else {
                            ret.append(", ").append(person.name);
                        }
                        index++;
                    }
                } else {
                    break;
                }
            }
        }

        return (ret != null) ? ret.toString() : null;
    }

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
