package net.egordmitriev.watchall.pojo.watchall;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import net.egordmitriev.watchall.pojo.BaseModel;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class UserModel extends BaseModel {
    public static final int TYPE = 14;

    @SerializedName("fullname")
    public String fullname;
    @SerializedName("email")
    public String email;
    @SerializedName("active")
    public String active;
    @SerializedName("tagline")
    public String tagline;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("backdrop")
    public String backdrop;
    @SerializedName("profile_color")
    public int profile_color;

    @SerializedName("reviews")
    public ReviewModel[] reviews;
    @SerializedName("activities")
    public ActivityModel[] activities;
    @SerializedName("watchlists")
    public WatchlistModel.Base[] watchlists;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.fullname);
        dest.writeString(this.email);
        dest.writeString(this.active);
        dest.writeString(this.tagline);
        dest.writeString(this.avatar);
        dest.writeString(this.backdrop);
        dest.writeInt(this.profile_color);
        dest.writeTypedArray(this.reviews, flags);
        dest.writeTypedArray(this.activities, flags);
        dest.writeTypedArray(this.watchlists, flags);
    }

    public UserModel() {
        super(TYPE);
    }

    public UserModel(int id, String fullname, String email, String avatar, int profile_color) {
        super(TYPE);
        this.id= id;
        this.fullname = fullname;
        this.email = email;
        this.avatar = avatar;
        this.profile_color = profile_color;
    }

    protected UserModel(Parcel in) {
        super(in);
        this.fullname = in.readString();
        this.email = in.readString();
        this.active = in.readString();
        this.tagline = in.readString();
        this.avatar = in.readString();
        this.backdrop = in.readString();
        this.profile_color = in.readInt();
        this.reviews = in.createTypedArray(ReviewModel.CREATOR);
        this.activities = in.createTypedArray(ActivityModel.CREATOR);
        this.watchlists = in.createTypedArray(WatchlistModel.Base.CREATOR);
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
