package net.egordmitriev.watchall.utils;

import net.egordmitriev.watchall.pojo.BaseModel;
import net.egordmitriev.watchall.pojo.user.Category;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class QueryBuilder {

    private Map<String, String> mQuery;

    public QueryBuilder() {
        mQuery = new HashMap<>();
    }

    public QueryBuilder setInt(String key, int value) {
        mQuery.put(key, Integer.toString(value));
        return this;
    }

    public QueryBuilder setString(String key, String value) {
        mQuery.put(key, value);
        return this;
    }

    public QueryBuilder setBoolean(String key, boolean value) {
        mQuery.put(key, Boolean.toString(value));
        return this;
    }

    public QueryBuilder setDate(String key, Date value) {
        mQuery.put(key, APIUtils.sTMDBDateFormat.format(value));
        return this;
    }

    public QueryBuilder setObject(String key, BaseModel value) {
        mQuery.put(key, Integer.toString(value.id));
        return this;
    }

    public QueryBuilder setObjectArray(String key, BaseModel[] value) {
        if (value == null) return this;
        StringBuilder builder = new StringBuilder();
        for (BaseModel obj : value) {
            if (builder.length() > 0) builder.append(",");
            builder.append(obj.id);
        }
        mQuery.put(key, builder.toString());
        return this;
    }

    public QueryBuilder setCategoryArray(String key, Category[] value) {
        if(value == null) return this;
        StringBuilder builder = new StringBuilder();
        for(Category obj : value) {
            if(builder.length() > 0) builder.append(",");
            builder.append(obj.title);
        }
        mQuery.put(key, builder.toString());
        return this;
    }

    public Map<String, String> build() {
        return mQuery;
    }

    public static QueryBuilder useDefaultTMDBQuery(Category[] genres, int page) {
        return new QueryBuilder()
                .setDate(APIUtils.Queries.Movie.setFilterSuffix(APIUtils.Queries.Movie.PRIMARY_RELEASE_DATE_SORT, false), new Date())
                .setDate(APIUtils.Queries.Movie.setFilterSuffix(APIUtils.Queries.Movie.PRIMARY_RELEASE_DATE_SORT, true), new GregorianCalendar(1997, 1, 1).getTime())
                .setInt(APIUtils.Queries.Movie.setFilterSuffix(APIUtils.Queries.Movie.VOTE_COUNT_SORT, true), 10)
                .setObjectArray(APIUtils.Queries.Movie.WITH_GENRES, genres)
                .setInt(APIUtils.Queries.Movie.PAGE, page);
    }

    public static QueryBuilder useDefaultAnimeQuery(Category[] genres, int page) {
        return new QueryBuilder()
                .setString(APIUtils.Queries.Anime.GENRES_EXCLUDE,"Hentai,Adult")
                .setCategoryArray(APIUtils.Queries.Anime.GENRES, genres)
                .setInt(APIUtils.Queries.Anime.PAGE, page);
    }
}
