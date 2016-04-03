package net.egordmitriev.watchall.pojo.user;

import android.os.Parcel;

import net.egordmitriev.watchall.pojo.BaseModel;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class Category extends BaseModel {
    public static final int TYPE = 16;

    public String title;

    public Category() {
        super(TYPE);
    }

    public Category(String title, int id, int type) {
        super(type);
        this.title = title;
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.title);
    }

    protected Category(Parcel in) {
        super(in);
        this.title = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
