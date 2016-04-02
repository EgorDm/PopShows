package net.egordmitriev.watchall.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EgorDm on 4/2/2016.
 */
public abstract class BaseModel implements Parcelable {

    @SerializedName("id")
    public int id = -1;

    @SerializedName("model_type")
    public int type;

    public BaseModel(int type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
    }

    protected BaseModel(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
    }
}
