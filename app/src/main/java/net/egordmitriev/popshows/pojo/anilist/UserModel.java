package net.egordmitriev.popshows.pojo.anilist;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import net.egordmitriev.popshows.pojo.BaseModel;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class UserModel extends BaseModel {
    public static final int TYPE = 6;

    @SerializedName("display_name")
    public String display_name;
    @SerializedName("forum_homepage")
    public String forum_homepage;
    @SerializedName("image_url_med")
    public String poster;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.display_name);
        dest.writeString(this.forum_homepage);
        dest.writeString(this.poster);
    }

    public UserModel() {
        super(TYPE);
    }

    protected UserModel(Parcel in) {
        super(in);
        this.display_name = in.readString();
        this.forum_homepage = in.readString();
        this.poster = in.readString();
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
