package net.egordmitriev.watchall.pojo.watchall;

import android.content.Context;
import android.os.Parcel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.pojo.CardedModel;
import net.egordmitriev.watchall.ui.widgets.cards.MediaCardExpand;
import net.egordmitriev.watchall.utils.APIUtils;

import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class ReviewModel extends CardedModel {
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

    public ReviewModel(CardedModel data, Date created, String summary, String content, UserModel user, float user_rating) {
        super(TYPE);
        //this.data = data;
        this.created = created;
        this.summary = summary;
        this.content = content;
        this.user = user;
        this.user_rating = user_rating;
    }

    @Override
    public MediaCard onCreateCard(Context context, String prefix, boolean small) {
        return new MediaCardExpand(context, type, summary, "By " + user.fullname, user_rating, getMediaThumbnail(), null, content);
    }

    private String getMediaThumbnail() {
        if(data == null) return null;
        if(data.has("image_url_med")) {
            return data.get("image_url_med").getAsString();
        } else if(data.has("poster_path")) {
            return APIUtils.getTMDBImageUrl(data.get("poster_path").getAsString(), APIUtils.Queries.Image.SIZE_SMALL_POSTER);
        }
        return null;
    }

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
        dest.writeString((this.data != null) ? this.data.toString() : null);
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
