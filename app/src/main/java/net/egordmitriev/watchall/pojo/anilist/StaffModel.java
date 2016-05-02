package net.egordmitriev.watchall.pojo.anilist;

import android.content.Context;
import android.os.Parcel;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.api.AnilistServiceHelper;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.pojo.BaseModel;
import net.egordmitriev.watchall.pojo.DetailedModel;
import net.egordmitriev.watchall.utils.APIUtils;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class StaffModel extends DetailedModel<StaffModel.Base, StaffModel.Detail>  {
    public static final int TYPE = 4;

    public StaffModel() {
        super(TYPE);
    }

    public StaffModel(StaffModel.Base base, StaffModel.Detail detail) {
        super(TYPE, base, detail);
    }

    @Override
    public void requestDetail(DetailCallback callback) {
        AnilistServiceHelper.sService.getStaff(this.id).enqueue(getDetailCallback(callback));
    }

    @Override
    protected void populateModel(JsonObject data) {
        try {
            this.detail = APIUtils.sAnilistParser.fromJson(data, StaffModel.Detail.class);
        } catch (Exception e) {
            Logger.e(e, "Error happened while populating a staff model.\n"+data.toString());
        }
    }

    @Override
    public MediaCard onCreateCard(Context context, String prefix, boolean small) {
        //TODO: implement card
        return null;
    }

    public static class Base extends BaseModel {
        @SerializedName("name_first")
        public String name_first;
        @SerializedName("name_last")
        public String name_last;
        @SerializedName("image_url_lge")
        public String poster;
        @SerializedName("language")
        public String language;
        @SerializedName("role")
        public String role;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.name_first);
            dest.writeString(this.name_last);
            dest.writeString(this.poster);
            dest.writeString(this.language);
            dest.writeString(this.role);
        }

        public Base() {
            super(TYPE);
        }

        protected Base(Parcel in) {
            super(in);
            this.name_first = in.readString();
            this.name_last = in.readString();
            this.poster = in.readString();
            this.language = in.readString();
            this.role = in.readString();
        }

        public static final Creator<Base> CREATOR = new Creator<Base>() {
            @Override
            public Base createFromParcel(Parcel source) {
                return new Base(source);
            }

            @Override
            public Base[] newArray(int size) {
                return new Base[size];
            }
        };
    }

    public static class Detail extends BaseModel {
        @SerializedName("website")
        public String website;
        @SerializedName("info")
        public String info;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.website);
            dest.writeString(this.info);
        }

        public Detail() {
            super(TYPE);
        }

        protected Detail(Parcel in) {
            super(in);
            this.website = in.readString();
            this.info = in.readString();
        }

        public static final Creator<Detail> CREATOR = new Creator<Detail>() {
            @Override
            public Detail createFromParcel(Parcel source) {
                return new Detail(source);
            }

            @Override
            public Detail[] newArray(int size) {
                return new Detail[size];
            }
        };
    }

    @Override
    protected ClassLoader getBaseLoader() {
        return StaffModel.Base.class.getClassLoader();
    }

    @Override
    protected ClassLoader getDetailLoader() {
        return StaffModel.Detail.class.getClassLoader();
    }

    protected StaffModel(Parcel in) {
        super(in);
    }

    public static final Creator<StaffModel> CREATOR = new Creator<StaffModel>() {
        @Override
        public StaffModel createFromParcel(Parcel source) {
            return new StaffModel(source);
        }

        @Override
        public StaffModel[] newArray(int size) {
            return new StaffModel[size];
        }
    };
}
