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
import net.egordmitriev.watchall.ui.modelviews.AnimeView;
import net.egordmitriev.watchall.utils.APIUtils;

import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class AnimeModel extends DetailedModel<AnimeModel.Base, AnimeModel.Detail> {
    public static final int TYPE = 1;

    public AnimeModel() {
        super(TYPE);
    }

    public AnimeModel(AnimeModel.Base base, AnimeModel.Detail detail) {
        super(TYPE, base, detail);
    }

    @Override
    public void requestDetail(DetailCallback callback) {
        AnilistServiceHelper.sService.getAnime(this.id).enqueue(getDetailCallback(callback));
    }

    @Override
    protected void populateModel(JsonObject data) {
        try {
            this.detail = APIUtils.sAnilistParser.fromJson(data, AnimeModel.Detail.class);
        } catch (Exception e) {
            Logger.e(e, "Error happened while populating a anime model.\n"+data.toString());
        }
    }

    @Override
    public String getPoster(boolean small) {
        return (small) ? base.poster_med : base.poster_lge;
    }


    @Override
    public MediaCard onCreateCard(Context context, String prefix, boolean small) {
        return AnimeView.onCreateCard(context, this, prefix, small);
    }

    public static class Base extends BaseModel {
        @SerializedName("title_romaji")
        public String title_romaji;
        @SerializedName("title_japanese")
        public String title_japanese;
        @SerializedName("title_english")
        public String title_english;
        @SerializedName("type")
        public String type_anime;

        @SerializedName("synonyms")
        public String[] synonyms;
        @SerializedName("airing_status")
        public String airing_status;
        @SerializedName("average_score")
        public float average_score = -1f;
        @SerializedName("total_episodes")
        public int total_episodes;
        @SerializedName("popularity")
        public int popularity;

        @SerializedName("image_url_med")
        public String poster_med;
        @SerializedName("image_url_lge")
        public String poster_lge;

        @SerializedName("relation_type")
        public String relation_type;
        @SerializedName("airing")
        public AirDataModel airing;

        public Base() {
            super(AnimeModel.TYPE);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.title_romaji);
            dest.writeString(this.title_japanese);
            dest.writeString(this.title_english);
            dest.writeString(this.type_anime);
            dest.writeStringArray(this.synonyms);
            dest.writeString(this.airing_status);
            dest.writeFloat(this.average_score);
            dest.writeInt(this.total_episodes);
            dest.writeInt(this.popularity);
            dest.writeString(this.poster_med);
            dest.writeString(this.poster_lge);
            dest.writeString(this.relation_type);
            dest.writeParcelable(this.airing, flags);
        }

        protected Base(Parcel in) {
            super(in);
            this.title_romaji = in.readString();
            this.title_japanese = in.readString();
            this.title_english = in.readString();
            this.type_anime = in.readString();
            this.synonyms = in.createStringArray();
            this.airing_status = in.readString();
            this.average_score = in.readFloat();
            this.total_episodes = in.readInt();
            this.popularity = in.readInt();
            this.poster_med = in.readString();
            this.poster_lge = in.readString();
            this.relation_type = in.readString();
            this.airing = in.readParcelable(AirDataModel.class.getClassLoader());
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
        @SerializedName("description")
        public String description;
        @SerializedName("genres")
        public String[] genres;

        @SerializedName("image_url_banner")
        public String backdrop;
        @SerializedName("duration")
        public int duration;

        @SerializedName("start_date")
        public Date start_date;
        @SerializedName("end_date")
        public Date end_date;
        @SerializedName("classification")
        public String classification;

        @SerializedName("characters")
        public CharacterModel.Base[] characters;
        @SerializedName("staff")
        public StaffModel.Base[] staff;
        @SerializedName("relations")
        public AnimeModel.Base[] relations;
        @SerializedName("studio")
        public StudioModel[] studio;
        @SerializedName("reviews")
        public ReviewModel[] reviews;

        public Detail() {
            super(AnimeModel.TYPE);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.description);
            dest.writeStringArray(this.genres);
            dest.writeString(this.backdrop);
            dest.writeInt(this.duration);
            dest.writeLong(start_date != null ? start_date.getTime() : -1);
            dest.writeLong(end_date != null ? end_date.getTime() : -1);
            dest.writeString(this.classification);
            dest.writeTypedArray(this.characters, flags);
            dest.writeTypedArray(this.staff, flags);
            dest.writeTypedArray(this.relations, flags);
            dest.writeTypedArray(this.studio, flags);
            dest.writeTypedArray(this.reviews, flags);
        }

        protected Detail(Parcel in) {
            super(in);
            this.description = in.readString();
            this.genres = in.createStringArray();
            this.backdrop = in.readString();
            this.duration = in.readInt();
            long tmpStart_date = in.readLong();
            this.start_date = tmpStart_date == -1 ? null : new Date(tmpStart_date);
            long tmpEnd_date = in.readLong();
            this.end_date = tmpEnd_date == -1 ? null : new Date(tmpEnd_date);
            this.classification = in.readString();
            this.characters = in.createTypedArray(CharacterModel.Base.CREATOR);
            this.staff = in.createTypedArray(StaffModel.Base.CREATOR);
            this.relations = in.createTypedArray(Base.CREATOR);
            this.studio = in.createTypedArray(StudioModel.CREATOR);
            this.reviews = in.createTypedArray(ReviewModel.CREATOR);
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
        return AnimeModel.Base.class.getClassLoader();
    }

    @Override
    protected ClassLoader getDetailLoader() {
        return AnimeModel.Detail.class.getClassLoader();
    }

    protected AnimeModel(Parcel in) {
        super(in);
    }

    public static final Creator<AnimeModel> CREATOR = new Creator<AnimeModel>() {
        @Override
        public AnimeModel createFromParcel(Parcel source) {
            return new AnimeModel(source);
        }

        @Override
        public AnimeModel[] newArray(int size) {
            return new AnimeModel[size];
        }
    };
}
