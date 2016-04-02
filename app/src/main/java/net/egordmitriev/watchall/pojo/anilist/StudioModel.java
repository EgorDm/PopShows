package net.egordmitriev.watchall.pojo.anilist;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import net.egordmitriev.watchall.pojo.BaseModel;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class StudioModel extends BaseModel {
    public static final int TYPE = 5;

    @SerializedName("studio_name")
    public String studio_name;
    @SerializedName("studio_wiki")
    public String studio_wiki;
    @SerializedName("main_studio")
    public int main_studio;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.studio_name);
        dest.writeString(this.studio_wiki);
        dest.writeInt(this.main_studio);
    }

    public StudioModel() {
        super(TYPE);
    }

    protected StudioModel(Parcel in) {
        super(in);
        this.studio_name = in.readString();
        this.studio_wiki = in.readString();
        this.main_studio = in.readInt();
    }

    public static final Creator<StudioModel> CREATOR = new Creator<StudioModel>() {
        @Override
        public StudioModel createFromParcel(Parcel source) {
            return new StudioModel(source);
        }

        @Override
        public StudioModel[] newArray(int size) {
            return new StudioModel[size];
        }
    };
}
