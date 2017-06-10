package net.egordmitriev.popshows.pojo.anilist;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import net.egordmitriev.popshows.pojo.BaseModel;

import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class ReviewModel extends BaseModel {
    public static final int TYPE = 3;

    @SerializedName("date")
    public Date date;
    @SerializedName("rating")
    public int rating;
    @SerializedName("rating_amount")
    public int rating_amount;
    @SerializedName("summary")
    public String summary;
    @SerializedName("text")
    public String text;
    @SerializedName("user_rating")
    public double user_rating;
    @SerializedName("score")
    public double score;
    @SerializedName("user")
    public UserModel user;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeInt(this.rating);
        dest.writeInt(this.rating_amount);
        dest.writeString(this.summary);
        dest.writeString(this.text);
        dest.writeDouble(this.user_rating);
        dest.writeDouble(this.score);
        dest.writeParcelable(this.user, flags);
    }

    public ReviewModel() {
        super(TYPE);
    }

    protected ReviewModel(Parcel in) {
        super(in);
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.rating = in.readInt();
        this.rating_amount = in.readInt();
        this.summary = in.readString();
        this.text = in.readString();
        this.user_rating = in.readDouble();
        this.score = in.readDouble();
        this.user = in.readParcelable(UserModel.class.getClassLoader());
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
