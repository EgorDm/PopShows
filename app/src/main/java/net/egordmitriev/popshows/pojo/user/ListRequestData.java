package net.egordmitriev.popshows.pojo.user;

import android.os.Parcel;
import android.os.Parcelable;

import net.egordmitriev.popshows.pojo.data.Category;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class ListRequestData implements Parcelable {

    public int type;
    public int actionType;
    public Category[] genres;
    public int page = 1;

    public ListRequestData() {
    }

    public ListRequestData(int actionType, int type, Category[] genres) {
        this.actionType = actionType;
        this.genres = genres;
        this.type = type;
    }

    public ListRequestData(int actionType, int type, Category genres) {
        this.actionType = actionType;
        this.genres = new Category[]{genres};
        this.type = type;
    }

    public ListRequestData(int actionType, int type) {
        this.actionType = actionType;
        this.genres = null;
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.actionType);
        dest.writeTypedArray(this.genres, flags);
        dest.writeInt(this.page);
    }

    protected ListRequestData(Parcel in) {
        this.type = in.readInt();
        this.actionType = in.readInt();
        this.genres = in.createTypedArray(Category.CREATOR);
        this.page = in.readInt();
    }

    public static final Creator<ListRequestData> CREATOR = new Creator<ListRequestData>() {
        @Override
        public ListRequestData createFromParcel(Parcel source) {
            return new ListRequestData(source);
        }

        @Override
        public ListRequestData[] newArray(int size) {
            return new ListRequestData[size];
        }
    };
}
