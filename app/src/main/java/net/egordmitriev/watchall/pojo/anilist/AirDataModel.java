package net.egordmitriev.watchall.pojo.anilist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class AirDataModel implements Parcelable {

    @SerializedName("time")
    public Date time;
    @SerializedName("countdown")
    public long countdown;
    @SerializedName("next_episode")
    public int next_episode;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time != null ? time.getTime() : -1);
        dest.writeLong(this.countdown);
        dest.writeInt(this.next_episode);
    }

    public AirDataModel() {
    }

    protected AirDataModel(Parcel in) {
        long tmpTime = in.readLong();
        this.time = tmpTime == -1 ? null : new Date(tmpTime);
        this.countdown = in.readLong();
        this.next_episode = in.readInt();
    }

    public static final Creator<AirDataModel> CREATOR = new Creator<AirDataModel>() {
        @Override
        public AirDataModel createFromParcel(Parcel source) {
            return new AirDataModel(source);
        }

        @Override
        public AirDataModel[] newArray(int size) {
            return new AirDataModel[size];
        }
    };
}
