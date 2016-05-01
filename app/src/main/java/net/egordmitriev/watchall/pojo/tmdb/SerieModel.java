package net.egordmitriev.watchall.pojo.tmdb;

import android.os.Parcel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.api.TMDBServiceHelper;
import net.egordmitriev.watchall.pojo.BaseModel;
import net.egordmitriev.watchall.pojo.DetailedModel;
import net.egordmitriev.watchall.pojo.tmdb.response.SerieListResponse;
import net.egordmitriev.watchall.utils.APIUtils;

import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class SerieModel extends DetailedModel<SerieModel.Base, SerieModel.Detail> {
    public static final int TYPE = 11;
    public boolean lite = false;

    public SerieModel() {
        super(TYPE);
    }

    public SerieModel(SerieModel.Base base, SerieModel.Detail detail) {
        super(TYPE, base, detail);
        if (base.genre_ids == null && base.overview == null && base.backdrop_path == null) {
            lite = true;
        }
    }

    @Override
    public void requestDetail(DetailCallback callback) {
        TMDBServiceHelper.sService.getSerie(this.id, APIUtils.APPEND_TO_RESPONSE_MOVIE).enqueue(getDetailCallback(callback));
    }

    @Override
    protected void populateModel(JsonObject data) {
        try {
            if (lite) {
                lite = false;
                if (data.has("overview")) this.base.overview = data.get("overview").getAsString();
                if (data.has("popularity"))
                    this.base.popularity = data.get("popularity").getAsFloat();
                if (data.has("vote_average"))
                    this.base.vote_average = data.get("vote_average").getAsFloat();
                this.base.vote_count = data.get("vote_count").getAsInt();
                this.base.backdrop_path = data.get("backdrop_path").getAsString();
                JsonArray genres = data.get("genres").getAsJsonArray();
                this.base.genre_ids = new int[genres.size()];
                for (int i = 0; i < genres.size(); i++) {
                    this.base.genre_ids[i] = genres.get(i).getAsJsonObject().get("id").getAsInt();
                }
            }
            this.detail = APIUtils.sTMDBParser.fromJson(data, SerieModel.Detail.class);
        } catch (Exception e) {
            Logger.e(e, "Error happened while populating a serie model.\n" + data.toString());
        }
    }

    public static class Base extends BaseModel {
        @SerializedName("name")
        public String name;
        @SerializedName("original_name")
        public String original_name;
        @SerializedName("overview")
        public String overview;

        @SerializedName("first_air_date")
        public Date first_air_date;
        @SerializedName("popularity")
        public float popularity;
        @SerializedName("genre_ids")
        public int[] genre_ids;

        @SerializedName("vote_average")
        public float vote_average = -1f;
        @SerializedName("vote_count")
        public int vote_count;

        @SerializedName("backdrop_path")
        public String backdrop_path;
        @SerializedName("poster_path")
        public String poster_path;

        @SerializedName("episode_count")
        public int episode_count;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.name);
            dest.writeString(this.original_name);
            dest.writeString(this.overview);
            dest.writeLong(first_air_date != null ? first_air_date.getTime() : -1);
            dest.writeFloat(this.popularity);
            dest.writeIntArray(this.genre_ids);
            dest.writeFloat(this.vote_average);
            dest.writeInt(this.vote_count);
            dest.writeString(this.backdrop_path);
            dest.writeString(this.poster_path);
            dest.writeInt(this.episode_count);
        }

        public Base() {
            super(TYPE);
        }

        protected Base(Parcel in) {
            super(in);
            this.name = in.readString();
            this.original_name = in.readString();
            this.overview = in.readString();
            long tmpFirst_air_date = in.readLong();
            this.first_air_date = tmpFirst_air_date == -1 ? null : new Date(tmpFirst_air_date);
            this.popularity = in.readFloat();
            this.genre_ids = in.createIntArray();
            this.vote_average = in.readFloat();
            this.vote_count = in.readInt();
            this.backdrop_path = in.readString();
            this.poster_path = in.readString();
            this.episode_count = in.readInt();
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
        @SerializedName("imdb_id")
        public String imdb_id;

        @SerializedName("number_of_seasons")
        public int numberOfSeasons;
        @SerializedName("number_of_episodes")
        public int numberOfEpisodes;

        @SerializedName("in_production")
        public boolean inProduction;
        @SerializedName("episode_run_time")
        public int[] runtime;
        @SerializedName("origin_country")
        public String[] origin_country;
        @SerializedName("homepage")
        public String homepage;

        @SerializedName("first_air_date")
        public Date firstAirDate;
        @SerializedName("last_air_date")
        public Date last_air_date;
        @SerializedName("status")
        public String status;

        @SerializedName("seasons")
        public SeasonModel.Base[] seasons;
        @SerializedName("credits")
        public CreditsModel credits;
        @SerializedName("similar")
        public SerieListResponse similar;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.imdb_id);
            dest.writeInt(this.numberOfSeasons);
            dest.writeInt(this.numberOfEpisodes);
            dest.writeByte(inProduction ? (byte) 1 : (byte) 0);
            dest.writeIntArray(this.runtime);
            dest.writeStringArray(this.origin_country);
            dest.writeString(this.homepage);
            dest.writeLong(firstAirDate != null ? firstAirDate.getTime() : -1);
            dest.writeLong(last_air_date != null ? last_air_date.getTime() : -1);
            dest.writeString(this.status);
            dest.writeTypedArray(this.seasons, flags);
            dest.writeParcelable(this.credits, flags);
            dest.writeParcelable(this.similar, flags);
        }

        public Detail() {
            super(TYPE);
        }

        protected Detail(Parcel in) {
            super(in);
            this.imdb_id = in.readString();
            this.numberOfSeasons = in.readInt();
            this.numberOfEpisodes = in.readInt();
            this.inProduction = in.readByte() != 0;
            this.runtime = in.createIntArray();
            this.origin_country = in.createStringArray();
            this.homepage = in.readString();
            long tmpFirstAirDate = in.readLong();
            this.firstAirDate = tmpFirstAirDate == -1 ? null : new Date(tmpFirstAirDate);
            long tmpLast_air_date = in.readLong();
            this.last_air_date = tmpLast_air_date == -1 ? null : new Date(tmpLast_air_date);
            this.status = in.readString();
            this.seasons = in.createTypedArray(SeasonModel.Base.CREATOR);
            this.credits = in.readParcelable(CreditsModel.class.getClassLoader());
            this.similar = in.readParcelable(SerieListResponse.class.getClassLoader());
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
        return SerieModel.Base.class.getClassLoader();
    }

    @Override
    protected ClassLoader getDetailLoader() {
        return SerieModel.Detail.class.getClassLoader();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(lite ? (byte) 1 : (byte) 0);
    }

    protected SerieModel(Parcel in) {
        super(in);
        this.lite = in.readByte() != 0;
    }

    public static final Creator<SerieModel> CREATOR = new Creator<SerieModel>() {
        @Override
        public SerieModel createFromParcel(Parcel source) {
            return new SerieModel(source);
        }

        @Override
        public SerieModel[] newArray(int size) {
            return new SerieModel[size];
        }
    };
}
