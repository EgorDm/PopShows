package net.egordmitriev.popshows.pojo.tmdb;

import android.content.Context;
import android.os.Parcel;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.Logger;

import net.egordmitriev.popshows.api.TMDBServiceHelper;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.pojo.BaseModel;
import net.egordmitriev.popshows.pojo.DetailedModel;
import net.egordmitriev.popshows.ui.modelviews.SeasonView;
import net.egordmitriev.popshows.ui.modelviews.base.AModelView;
import net.egordmitriev.popshows.utils.APIUtils;

import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class SeasonModel extends DetailedModel<SeasonModel.Base, SeasonModel.Detail> {
    public static final int TYPE = 10;

    public static SeasonModel[] createArray(SeasonModel.Base[] models) {
        SeasonModel[] ret = new SeasonModel[models.length];
        for (int i = 0; i < models.length; i++) {
            ret[i] = new SeasonModel(models[i], null);
        }
        return ret;
    }

    public SeasonModel() {
        super(TYPE);
    }

    public SeasonModel(SeasonModel.Base base, SeasonModel.Detail detail) {
        super(TYPE, base, detail);
    }

    @Override
    public void requestDetail(DetailCallback callback) {
        Logger.d("Parent id is " + base.parentID);
        TMDBServiceHelper.sService.getSeason(base.parentID, base.season_number).enqueue(getDetailCallback(callback));
    }

    @Override
    protected void populateModel(JsonObject data) {
        try {
            this.detail = APIUtils.sTMDBParser.fromJson(data, SeasonModel.Detail.class);
        } catch (Exception e) {
            Logger.e(e, "Error happened while populating a season model.\n" + data.toString());
        }
    }

    @Override
    public String getPoster(boolean small) {
        return APIUtils.getTMDBImageUrl(base.poster_path, (small)
                ? APIUtils.Queries.Image.SIZE_SMALL_POSTER
                : APIUtils.Queries.Image.SIZE_MEDIUM_POSTER);
    }

    @Override
    public String getTitle() {
        return "Season " + base.season_number;
    }

    @Override
    public String getDescription() {
        return detail.overview;
    }

    @Override
    public AModelView getModelView() {
        return SeasonView.getInstance();
    }

    @Override
    public MediaCard onCreateCard(Context context, String prefix, boolean small) {
        return SeasonView.onCreateCard(context, this, prefix, small);
    }

    public static class Base extends BaseModel {
        @SerializedName("air_date")
        public Date air_date;
        @SerializedName("episode_count")
        public int episode_count;
        @SerializedName("season_number")
        public int season_number;
        @SerializedName("poster_path")
        public String poster_path;
        public int parentID;

        public Base() {
            super(TYPE);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(this.air_date != null ? this.air_date.getTime() : -1);
            dest.writeInt(this.episode_count);
            dest.writeInt(this.season_number);
            dest.writeString(this.poster_path);
            dest.writeInt(this.parentID);
        }

        protected Base(Parcel in) {
            super(in);
            long tmpAir_date = in.readLong();
            this.air_date = tmpAir_date == -1 ? null : new Date(tmpAir_date);
            this.episode_count = in.readInt();
            this.season_number = in.readInt();
            this.poster_path = in.readString();
            this.parentID = in.readInt();
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
        @SerializedName("name")
        public String name;
        @SerializedName("overview")
        public String overview;
        @SerializedName("episodes")
        public EpisodeModel[] episodes;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.name);
            dest.writeString(this.overview);
            dest.writeTypedArray(this.episodes, flags);
        }

        public Detail() {
            super(TYPE);
        }

        protected Detail(Parcel in) {
            super(in);
            this.name = in.readString();
            this.overview = in.readString();
            this.episodes = in.createTypedArray(EpisodeModel.CREATOR);
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
        return SeasonModel.Base.class.getClassLoader();
    }

    @Override
    protected ClassLoader getDetailLoader() {
        return SeasonModel.Detail.class.getClassLoader();
    }

    protected SeasonModel(Parcel in) {
        super(in);
    }

    public static final Creator<SeasonModel> CREATOR = new Creator<SeasonModel>() {
        @Override
        public SeasonModel createFromParcel(Parcel source) {
            return new SeasonModel(source);
        }

        @Override
        public SeasonModel[] newArray(int size) {
            return new SeasonModel[size];
        }
    };
}
