package net.egordmitriev.watchall.pojo.data;

import android.os.Parcel;

import net.egordmitriev.watchall.pojo.BaseModel;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class Category extends BaseModel {
    public static final int TYPE = 16;

    public static final int GENRE_MOVIE_ACTION = 28;
    public static final int GENRE_MOVIE_ADVENTURE = 12;
    public static final int GENRE_MOVIE_ANIMATION = 16;
    public static final int GENRE_MOVIE_COMEDY = 35;
    public static final int GENRE_MOVIE_CRIME = 80;
    public static final int GENRE_MOVIE_DOCUMENTARY = 99;
    public static final int GENRE_MOVIE_DRAMA = 18;
    public static final int GENRE_MOVIE_FAMILY = 10751;
    public static final int GENRE_MOVIE_FANTASY = 14;
    public static final int GENRE_MOVIE_FOREIGN = 10769;
    public static final int GENRE_MOVIE_HISTORY = 36;
    public static final int GENRE_MOVIE_HORROR = 27;
    public static final int GENRE_MOVIE_MUSIC = 10402;
    public static final int GENRE_MOVIE_MYSTERY = 9648;
    public static final int GENRE_MOVIE_ROMANCE = 10749;
    public static final int GENRE_MOVIE_SCIFI = 878;
    public static final int GENRE_MOVIE_TVMOVIE = 10770;
    public static final int GENRE_MOVIE_THRILLER = 53;
    public static final int GENRE_MOVIE_WAR = 10752;
    public static final int GENRE_MOVIE_WESTERN = 37;
    public static final int GENRE_SERIE_ACTIONADVENTURE = 10759;
    public static final int GENRE_SERIE_EDUCATION = 10761;
    public static final int GENRE_SERIE_KIDS = 10762;
    public static final int GENRE_SERIE_NEWS = 10763;
    public static final int GENRE_SERIE_REALITY = 10764;
    public static final int GENRE_SERIE_SCIFIFANTASY = 10765;
    public static final int GENRE_SERIE_SOAP = 10766;
    public static final int GENRE_SERIE_TALK = 10767;
    public static final int GENRE_SERIE_WARPOLITICS = 10768;

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
