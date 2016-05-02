package net.egordmitriev.watchall.pojo.tmdb;

import android.content.Context;
import android.os.Parcel;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.api.TMDBServiceHelper;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.pojo.BaseModel;
import net.egordmitriev.watchall.pojo.DetailedModel;
import net.egordmitriev.watchall.pojo.tmdb.response.MovieLiteResponse;
import net.egordmitriev.watchall.pojo.tmdb.response.SerieLiteResponse;
import net.egordmitriev.watchall.utils.APIUtils;

import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class PersonModel extends DetailedModel<PersonModel.Base, PersonModel.Detail>  {
    public static final int TYPE = 9;

    public PersonModel() {
        super(TYPE);
    }

    public PersonModel(PersonModel.Base base, PersonModel.Detail detail) {
        super(TYPE, base, detail);
    }

    @Override
    public void requestDetail(DetailCallback callback) {
        TMDBServiceHelper.sService.getPerson(this.id, APIUtils.APPEND_TO_RESPONSE_PERSON).enqueue(getDetailCallback(callback));
    }

    @Override
    protected void populateModel(JsonObject data) {
        try {
            this.detail = APIUtils.sTMDBParser.fromJson(data, PersonModel.Detail.class);
        } catch (Exception e) {
            Logger.e(e, "Error happened while populating a person model.\n"+data.toString());
        }
    }

    @Override
    public MediaCard onCreateCard(Context context, String prefix, boolean small) {
        //TODO: implement card
        return null;
    }

    public static class Base extends BaseModel {
        @SerializedName("order")
        public int order;
        @SerializedName("cast_id")
        public int cast_id;
        @SerializedName("credit_id")
        public String credit_id;

        @SerializedName("name")
        public String name;
        @SerializedName("character")
        public String character;
        @SerializedName("job")
        public String job;
        @SerializedName("profile_path")
        public String profile_path;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.order);
            dest.writeInt(this.cast_id);
            dest.writeString(this.credit_id);
            dest.writeString(this.name);
            dest.writeString(this.character);
            dest.writeString(this.job);
            dest.writeString(this.profile_path);
        }

        public Base() {
            super(TYPE);
        }

        protected Base(Parcel in) {
            super(in);
            this.order = in.readInt();
            this.cast_id = in.readInt();
            this.credit_id = in.readString();
            this.name = in.readString();
            this.character = in.readString();
            this.job = in.readString();
            this.profile_path = in.readString();
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
        @SerializedName("place_of_birth")
        public String place_of_birth;
        @SerializedName("birthday")
        public Date birthday;
        @SerializedName("homepage")
        public String homepage;
        @SerializedName("biography")
        public String biography;
        @SerializedName("movie_credits")
        public MovieLiteResponse movie_credits;
        @SerializedName("tv_credits")
        public SerieLiteResponse tv_credits;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.place_of_birth);
            dest.writeLong(birthday != null ? birthday.getTime() : -1);
            dest.writeString(this.homepage);
            dest.writeString(this.biography);
            dest.writeParcelable(this.movie_credits, flags);
            dest.writeParcelable(this.tv_credits, flags);
        }

        public Detail() {
            super(TYPE);
        }

        protected Detail(Parcel in) {
            super(in);
            this.place_of_birth = in.readString();
            long tmpBirthday = in.readLong();
            this.birthday = tmpBirthday == -1 ? null : new Date(tmpBirthday);
            this.homepage = in.readString();
            this.biography = in.readString();
            this.movie_credits = in.readParcelable(MovieLiteResponse.class.getClassLoader());
            this.tv_credits = in.readParcelable(SerieLiteResponse.class.getClassLoader());
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
        return PersonModel.Base.class.getClassLoader();
    }

    @Override
    protected ClassLoader getDetailLoader() {
        return PersonModel.Detail.class.getClassLoader();
    }

    protected PersonModel(Parcel in) {
        super(in);
    }

    public static final Creator<PersonModel> CREATOR = new Creator<PersonModel>() {
        @Override
        public PersonModel createFromParcel(Parcel source) {
            return new PersonModel(source);
        }

        @Override
        public PersonModel[] newArray(int size) {
            return new PersonModel[size];
        }
    };
}