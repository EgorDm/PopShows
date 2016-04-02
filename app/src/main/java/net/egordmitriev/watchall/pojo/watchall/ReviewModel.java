package net.egordmitriev.watchall.pojo.watchall;

import android.os.Parcel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import net.egordmitriev.watchall.pojo.BaseModel;

import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class ReviewModel extends BaseModel {
    public static final int TYPE = 13;

    @SerializedName("created")
    public Date created;
    @SerializedName("user_rating")
    public float user_rating;
    @SerializedName("summary")
    public String summary;
    @SerializedName("content")
    public String content;
    @SerializedName("user")
    public UserModel user;
    @SerializedName("data")
    public JsonObject data;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(created != null ? created.getTime() : -1);
        dest.writeFloat(this.user_rating);
        dest.writeString(this.summary);
        dest.writeString(this.content);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.data.toString());
    }

    public ReviewModel() {
        super(TYPE);
    }

    protected ReviewModel(Parcel in) {
        super(in);
        long tmpCreated = in.readLong();
        this.created = tmpCreated == -1 ? null : new Date(tmpCreated);
        this.user_rating = in.readFloat();
        this.summary = in.readString();
        this.content = in.readString();
        this.user = in.readParcelable(UserModel.class.getClassLoader());
        this.data = new JsonParser().parse(in.readString()).getAsJsonObject();
    }

    public static final Creator<ReviewModel> CREATOR = new Creator<ReviewModel>() {
        @Override
        public ReviewModel createFromParcel(Parcel source) {
            return new ReviewModel(source);
        }

        @Override
        public ReviewModel[] newArray(int size) {
            return new ReviewModel[size];
        }
    };
}
