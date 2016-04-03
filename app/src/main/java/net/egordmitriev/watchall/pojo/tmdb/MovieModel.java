package net.egordmitriev.watchall.pojo.tmdb;

import android.os.Parcel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.api.TMDBServiceHelper;
import net.egordmitriev.watchall.pojo.BaseModel;
import net.egordmitriev.watchall.pojo.DetailedModel;
import net.egordmitriev.watchall.pojo.tmdb.response.MovieListResponse;
import net.egordmitriev.watchall.utils.APIUtils;

import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class MovieModel extends DetailedModel<MovieModel.Base, MovieModel.Detail> {
    public static final int TYPE = 8;
    public boolean lite = false;

    public MovieModel() {
        super(TYPE);
    }

    public MovieModel(MovieModel.Base base, MovieModel.Detail detail) {
        super(TYPE, base, detail);
        if (base.genre_ids == null && base.overview == null && base.backdrop_path == null) {
            lite = true;
        }
    }

    @Override
    public void requestDetail(final DetailCallback callback) {
        TMDBServiceHelper.sService.getMovie(this.id, APIUtils.APPEND_TO_RESPONSE_MOVIE).enqueue(getDetailCallback(callback));
    }

    @Override
    protected void populateModel( JsonObject data) {
        try {
            if(lite) {
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
            this.detail = APIUtils.sTMDBParser.fromJson(data, MovieModel.Detail.class);
        } catch (Exception e) {
            Logger.e(e, "Error happened while populating a movie model.\n"+data.toString());
        }
    }

    public static class Base extends BaseModel {
        @SerializedName("title")
        public String title;
        @SerializedName("original_title")
        public String original_title;
        @SerializedName("overview")
        public String overview;

        @SerializedName("release_date")
        public Date release_date;
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


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.title);
            dest.writeString(this.original_title);
            dest.writeString(this.overview);
            dest.writeLong(release_date != null ? release_date.getTime() : -1);
            dest.writeFloat(this.popularity);
            dest.writeIntArray(this.genre_ids);
            dest.writeFloat(this.vote_average);
            dest.writeInt(this.vote_count);
            dest.writeString(this.backdrop_path);
            dest.writeString(this.poster_path);
        }

        public Base() {
            super(TYPE);
        }

        protected Base(Parcel in) {
            super(in);
            this.title = in.readString();
            this.original_title = in.readString();
            this.overview = in.readString();
            long tmpRelease_date = in.readLong();
            this.release_date = tmpRelease_date == -1 ? null : new Date(tmpRelease_date);
            this.popularity = in.readFloat();
            this.genre_ids = in.createIntArray();
            this.vote_average = in.readFloat();
            this.vote_count = in.readInt();
            this.backdrop_path = in.readString();
            this.poster_path = in.readString();
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

        @SerializedName("runtime")
        public int runtime;
        @SerializedName("revenue")
        public long revenue;
        @SerializedName("budget")
        public long budget;
        @SerializedName("homepage")
        public String homepage;

        @SerializedName("status")
        public String status;
        @SerializedName("tagline")
        public String tagline;
        @SerializedName("credits")
        public CreditsModel credits;
        @SerializedName("similar")
        public MovieListResponse similar;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.imdb_id);
            dest.writeInt(this.runtime);
            dest.writeLong(this.revenue);
            dest.writeLong(this.budget);
            dest.writeString(this.homepage);
            dest.writeString(this.status);
            dest.writeString(this.tagline);
            dest.writeParcelable(this.credits, flags);
            dest.writeParcelable(this.similar, flags);
        }

        public Detail() {
            super(TYPE);
        }

        protected Detail(Parcel in) {
            super(in);
            this.imdb_id = in.readString();
            this.runtime = in.readInt();
            this.revenue = in.readLong();
            this.budget = in.readLong();
            this.homepage = in.readString();
            this.status = in.readString();
            this.tagline = in.readString();
            this.credits = in.readParcelable(CreditsModel.class.getClassLoader());
            this.similar = in.readParcelable(MovieListResponse.class.getClassLoader());
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
        return MovieModel.Base.class.getClassLoader();
    }

    @Override
    protected ClassLoader getDetailLoader() {
        return MovieModel.Detail.class.getClassLoader();
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

    protected MovieModel(Parcel in) {
        super(in);
        this.lite = in.readByte() != 0;
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel source) {
            return new MovieModel(source);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
}
