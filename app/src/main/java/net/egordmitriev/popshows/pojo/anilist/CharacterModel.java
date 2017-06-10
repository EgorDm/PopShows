package net.egordmitriev.popshows.pojo.anilist;

import android.content.Context;
import android.os.Parcel;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.Logger;

import net.egordmitriev.popshows.api.AnilistServiceHelper;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.pojo.BaseModel;
import net.egordmitriev.popshows.pojo.DetailedModel;
import net.egordmitriev.popshows.ui.modelviews.CharacterView;
import net.egordmitriev.popshows.ui.modelviews.base.AModelView;
import net.egordmitriev.popshows.utils.APIUtils;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class CharacterModel extends DetailedModel<CharacterModel.Base, CharacterModel.Detail> {
    public static final int TYPE = 2;

    public static CharacterModel[] createArray(CharacterModel.Base[] models) {
        CharacterModel[] ret = new CharacterModel[models.length];
        for (int i = 0; i < models.length; i++) {
            ret[i] = new CharacterModel(models[i], null);
        }
        return ret;
    }

    public CharacterModel() {
        super(TYPE);
    }

    public CharacterModel(CharacterModel.Base base, CharacterModel.Detail detail) {
        super(TYPE, base, detail);
    }

    @Override
    public void requestDetail(DetailCallback callback) {
        AnilistServiceHelper.sService.getCharacter(this.id).enqueue(getDetailCallback(callback));
    }

    @Override
    protected void populateModel(JsonObject data) {
        try {
            this.detail = APIUtils.sAnilistParser.fromJson(data, CharacterModel.Detail.class);
        } catch (Exception e) {
            Logger.e(e, "Error happened while populating a character model.\n" + data.toString());
        }
    }

    @Override
    public String getPoster(boolean small) {
        return base.poster;
    }

    @Override
    public String getTitle() {
        return (base.name_last != null) ? base.name_first + " " + base.name_last : base.name_first;
    }

    @Override
    public String getDescription() {
        return detail.info;
    }

    @Override
    public AModelView getModelView() {
        return CharacterView.getInstance();
    }

    @Override
    public MediaCard onCreateCard(Context context, String prefix, boolean small) {
        return CharacterView.onCreateCard(context, this, prefix, small);
    }

    public static class Base extends BaseModel {
        @SerializedName("name_first")
        public String name_first;
        @SerializedName("name_last")
        public String name_last;
        @SerializedName("image_url_lge")
        public String poster;
        @SerializedName("id_actor")
        public int id_actor;
        @SerializedName("role")
        public String role;
        @SerializedName("actor")
        public StaffModel.Base[] actor;

        public Base() {
            super(CharacterModel.TYPE);
        }

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
            dest.writeInt(this.id_actor);
            dest.writeString(this.role);
            dest.writeTypedArray(this.actor, flags);
        }

        protected Base(Parcel in) {
            super(in);
            this.name_first = in.readString();
            this.name_last = in.readString();
            this.poster = in.readString();
            this.id_actor = in.readInt();
            this.role = in.readString();
            this.actor = in.createTypedArray(StaffModel.Base.CREATOR);
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
        @SerializedName("info")
        public String info;
        @SerializedName("name_japanese")
        public String name_japanese;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.info);
            dest.writeString(this.name_japanese);
        }

        public Detail() {
            super(TYPE);
        }

        protected Detail(Parcel in) {
            super(in);
            this.info = in.readString();
            this.name_japanese = in.readString();
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
        return CharacterModel.Base.class.getClassLoader();
    }

    @Override
    protected ClassLoader getDetailLoader() {
        return CharacterModel.Detail.class.getClassLoader();
    }

    protected CharacterModel(Parcel in) {
        super(in);
    }

    public static final Creator<CharacterModel> CREATOR = new Creator<CharacterModel>() {
        @Override
        public CharacterModel createFromParcel(Parcel source) {
            return new CharacterModel(source);
        }

        @Override
        public CharacterModel[] newArray(int size) {
            return new CharacterModel[size];
        }
    };
}
