package net.egordmitriev.watchall.pojo.watchall;

import android.content.Context;
import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCardSmall;
import net.egordmitriev.watchall.pojo.CardedModel;
import net.egordmitriev.watchall.utils.APIUtils;

import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class ActivityModel extends CardedModel {
    public static final int TYPE = 12;

    public static final int ACTION_WATCHED = 0;
    public static final int ACTION_CREATED_LIST = 1;
    public static final int ACTION_ADDED_TO_LIST = 2;
    public static final int ACTION_REVIEWED = 3;

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
    @SerializedName("data_type")
    public int media_type;

    public ActivityModel(int type, int media_type, int action_type, int data_id, Date created, String name, String poster) {
        super(type);
        this.action_type = action_type;
        this.data_id = data_id;
        this.created = created;
        this.name = name;
        this.poster = poster;
        this.media_type = media_type;
    }

    @Override
    public MediaCard onCreateCard(Context context, String prefix, boolean small) {
        return new MediaCardSmall(context, -1, getTitle(), getSubtitle(), APIUtils.posterFromType(media_type, poster), -1f, null);
    }

    private String getTitle() {
        StringBuilder ret = new StringBuilder();
        switch (action_type) {
            case ACTION_WATCHED:
                ret.append("Watched ");
                break;
            case ACTION_ADDED_TO_LIST:
                ret.append("Added to list ");
                break;
            case ACTION_REVIEWED:
                ret.append("Placed a review on ");
                break;
        }
        ret.append(name);
        return ret.toString();
    }

    private String getSubtitle() {
        if(created == null) return null;
        else return APIUtils.sFriendlyDateFormat.format(created);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.action_type);
        dest.writeInt(this.media_type);
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
        this.media_type = in.readInt();
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
