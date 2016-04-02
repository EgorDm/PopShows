package net.egordmitriev.watchall.pojo.tmdb;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import net.egordmitriev.watchall.pojo.BaseModel;

import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class EpisodeModel extends BaseModel {
    public static final int TYPE = 7;

    @SerializedName("name")
    public String name;
    @SerializedName("air_date")
    public Date air_date;
    @SerializedName("overview")
    public String sypnose;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeLong(air_date != null ? air_date.getTime() : -1);
        dest.writeString(this.sypnose);
    }

    public EpisodeModel() {
        super(TYPE);
    }

    protected EpisodeModel(Parcel in) {
        super(in);
        this.name = in.readString();
        long tmpAir_date = in.readLong();
        this.air_date = tmpAir_date == -1 ? null : new Date(tmpAir_date);
        this.sypnose = in.readString();
    }

    public static final Creator<EpisodeModel> CREATOR = new Creator<EpisodeModel>() {
        @Override
        public EpisodeModel createFromParcel(Parcel source) {
            return new EpisodeModel(source);
        }

        @Override
        public EpisodeModel[] newArray(int size) {
            return new EpisodeModel[size];
        }
    };
}
