package net.egordmitriev.watchall.pojo.watchall;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import net.egordmitriev.watchall.pojo.BaseModel;

import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class ActivityModel extends BaseModel{
    public static final int TYPE = 12;

    @SerializedName("action_type")
    public int action_type;
    @SerializedName("created")
    public Date created;
    @SerializedName("data_id")
    public int data_id;
    @SerializedName("name")
    public String name;
    @SerializedName("poster")
    public String poster;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.action_type);
        dest.writeLong(created != null ? created.getTime() : -1);
        dest.writeInt(this.data_id);
        dest.writeString(this.name);
        dest.writeString(this.poster);
    }

    public ActivityModel() {
        super(TYPE);
    }

    protected ActivityModel(Parcel in) {
        super(in);
        this.action_type = in.readInt();
        long tmpCreated = in.readLong();
        this.created = tmpCreated == -1 ? null : new Date(tmpCreated);
        this.data_id = in.readInt();
        this.name = in.readString();
        this.poster = in.readString();
    }

    public static final Creator<ActivityModel> CREATOR = new Creator<ActivityModel>() {
        @Override
        public ActivityModel createFromParcel(Parcel source) {
            return new ActivityModel(source);
        }

        @Override
        public ActivityModel[] newArray(int size) {
            return new ActivityModel[size];
        }
    };
}
