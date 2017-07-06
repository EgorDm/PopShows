package net.egordmitriev.popshows.utils;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;

import net.egordmitriev.popshows.api.WatchAllServiceHelper;
import net.egordmitriev.popshows.pojo.BaseModel;
import net.egordmitriev.popshows.pojo.anilist.AnimeModel;
import net.egordmitriev.popshows.pojo.tmdb.MovieModel;
import net.egordmitriev.popshows.pojo.tmdb.SerieModel;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by EgorDm on 4/1/2016.
 */
public class APIUtils {

    //CURRENT MAX MEDIA ID IS 17

    public static final Gson sGlobalParser = new GsonBuilder()
            .registerTypeAdapter(Date.class, new GlobalDateHandler())
            .create();
    public static final Gson sTMDBParser = new GsonBuilder()
            .registerTypeAdapter(Date.class, new TMDBDateHandler())
            .create();
    public static final Gson sAnilistParser = new GsonBuilder()
            .registerTypeAdapter(Date.class, new AnilistDateHandler())
            .create();

    public static final DateFormat sFriendlyDateFormat = DateFormat.getDateInstance();

    @SuppressLint("SimpleDateFormat")
    public static final DateFormat sTMDBDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static class GlobalDateHandler implements JsonDeserializer<Date>, JsonSerializer<Date> {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            try {

                return Utils.convertUnixToDate(json.getAsLong());
            } catch (Exception e) {
                Logger.e("Error occured while parsing date of global api. \nRaw Element: " + json.getAsString() + "\nParse Format: unix");
                return null;
            }
        }

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }
    }

    public static class TMDBDateHandler implements JsonDeserializer<Date>, JsonSerializer<Date> {

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            try {
                return sTMDBDateFormat.parse(json.getAsString());
            } catch (ParseException e) {
                Logger.e("Error occured while parsing date of tmdb api. \nRaw Element: " + json.getAsString() + "\nParse Format: yyyy-MM-dd");
                return null;
            }
        }

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }
    }

    public static class AnilistDateHandler implements JsonDeserializer<Date>, JsonSerializer<Date> {
        @SuppressLint("SimpleDateFormat")
        private static final SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            try {
                return sFormat.parse(json.getAsString());
            } catch (ParseException e) {
                try {
                    long temp = json.getAsLong();
                    if (temp <= 4000L) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.MONTH, Calendar.DECEMBER);
                        cal.set(Calendar.DAY_OF_MONTH, 31);
                        cal.set(Calendar.YEAR, (int) temp);
                        return cal.getTime();
                    }
                    return new Date(temp * 1000L);
                } catch (Exception ex) {
                    Logger.e("Error occured while parsing date of anilist api. \nRaw Element: " + json.getAsString() + "\nParse Format: yyyy-MM-dd'T'HH:mm:ssZ || year || unix");
                    return null;
                }
            }
        }

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }
    }

    public static final String APPEND_TO_RESPONSE_MOVIE = "credits,similar";
    public static final String APPEND_TO_RESPONSE_PERSON = "movie_credits,tv_credits";
    public static final String TMDB_BASEURL_IMAGES = "http://image.tmdb.org/t/p/";
    public static final int TMDB_RESULTS_PERPAGE = 20;

    public static BaseModel[] mediaFromJson(List<JsonObject> results) {
        if (results != null && results.size() > 0) {
            BaseModel[] ret = new BaseModel[results.size()];
            for (int i = 0; i < ret.length; i++) {
                try {
                    switch (results.get(i).get("model_type").getAsInt()) {
                        case MovieModel.TYPE:
                            ret[i] = APIUtils.sGlobalParser.fromJson(results.get(i), MovieModel.Base.class);
                            break;
                        case SerieModel.TYPE:
                            ret[i] = APIUtils.sGlobalParser.fromJson(results.get(i), SerieModel.Base.class);
                            break;
                        case AnimeModel.TYPE:
                            ret[i] = APIUtils.sGlobalParser.fromJson(results.get(i), AnimeModel.Base.class);
                            break;
                        default:
                            Logger.e("Unknown mediatype: " + results.get(i).get("model_type").getAsInt());
                            break;
                    }
                } catch (JsonSyntaxException ignored) {
                }
            }
            return ret;
        }
        return null;
    }

    public static final String ANILIST_GRANTTYPE = "client_credentials";
    public static final int ANILIST_RESULTS_PERPAGE = 40;

    public static Interceptor getDebugInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }

    public static final int NETWORK_STATE_DISCONNECTED = 0;
    public static final int NETWORK_STATE_WIFI = 1;
    public static final int NETWORK_STATE_MOBILE = 2;

    public static final int OUTDATED_TIME = 2; //Two hours

    public static class Queries {
        public static class Movie {
            public static final String INCLUDE_ADULT = "include_adult";
            public static final String INCLUDE_VIDEO = "include_video";
            public static final String LANGUAGE = "language";
            public static final String PAGE = "page";
            public static final String PRIMARY_RELEASE_YEAR = "primary_release_year";
            public static final String PRIMARY_RELEASE_DATE_SORT = "primary_release_date";
            public static final String VOTE_COUNT_SORT = "vote_count";
            public static final String VOTE_AVERAGE_SORT = "vote_average";
            public static final String SORT_BY = "sort_by";
            public static final String WITH_GENRES = "with_genres";

            public static class Sorting {
                public static final String POPULARITY = "popularity";
                public static final String RELEASE_DATE = "release_date";
                public static final String REVENUE = "revenue";
                public static final String TITLE = "original_title";
                public static final String VOTE_AVERAGE = "vote_average";
                public static final String VOTE_COUNT = "vote_count";
            }

            public static String setOrderSuffix(String value, boolean desc) {
                return value + "." + ((desc) ? APIUtils.Queries.DESC : APIUtils.Queries.ASC);
            }

            public static String setFilterSuffix(String key, boolean gte) {
                return key + "." + ((gte) ? APIUtils.Queries.GTE : APIUtils.Queries.LTE);
            }
        }

        public static class Serie {
            public static final String AIR_DATE = "air_date";
            public static final String FIRST_AIR_DATE = "first_air_date";
            public static final String FIRST_AIR_DATE_YEAR = "first_air_date_year";
            public static final String LANGUAGE = "language";
            public static final String PAGE = "page";
            public static final String SORT_BY = "sort_by";
            public static final String TIMEZONE = "timezone";

            public static final String VOTE_COUNT_SORT = "vote_count";
            public static final String VOTE_AVERAGE_SORT = "vote_average";

            public static final String WITH_GENRES = "with_genres";
            public static final String WITH_NETWORKS = "with_networks";

            public static final String DESC = "desc";
            public static final String ASC = "asc";

            public static final String GTE = "gte";
            public static final String LTE = "lte";

            public static class Sorting {
                public static final String POPULARITY = "popularity";
                public static final String FIRST_AIR_DATE = "first_air_date";
                public static final String VOTE_AVERAGE = "vote_average";
            }
        }

        public static class Anime {
            public static final String PAGE = "page";
            public static final String YEAR = "year";
            public static final String SEASON = "season";
            public static final String TYPE = "type";
            public static final String STATUS = "status";
            public static final String GENRES = "genres";
            public static final String GENRES_EXCLUDE = "genres_exclude";
            public static final String SORT = "sort";

            public static final String AIRING_DATA = "airing_data";
            public static final String FULL_PAGE = "full_page";

            public static class Sorting {
                public static final String ID = "id";
                public static final String SCORE = "score";
                public static final String POPULARITY = "popularity";
                public static final String START_DATE = "start date";
                public static final String END_DATE = "end date";
            }

            public static class Status {
                public static final String NOT_YET_AIRED = "Not Yet Aired";
                public static final String CURRENTLY_AIRING = "Currently Airing";
                public static final String FINISHED_AIRING = "Finished Airing";
                public static final String CANCELLED = "Cancelled";
            }

            public static class Season {
                public static final String WINTER = "winter";
                public static final String SPRING = "spring";
                public static final String SUMMER = "summer";
                public static final String FALL = "fall";
            }

            public static class Type {
                public static final String TV = "Tv";
                public static final String MOVIE = "Movie";
                public static final String SPECIAL = "Special";
                public static final String OVA = "OVA";
                public static final String ONA = "ONA";
                public static final String TV_SHORT = "Tv Short";

            }

            public static String setOrderSuffix(String value, boolean desc) {
                return value + "-" + ((desc) ? APIUtils.Queries.DESC : APIUtils.Queries.ASC);
            }
        } //TODO: make anime also go per year season.

        public static class Image {
            public static final String SIZE_SMALL_POSTER = "w92";
            public static final String SIZE_MEDIUM_POSTER = "w154";
            public static final String SIZE_MEDIUM_BACKDROP = "w1280";
        }

        public static final String DESC = "desc";
        public static final String ASC = "asc";

        public static final String GTE = "gte";
        public static final String LTE = "lte";
    }

    public static final String FAVOURITES_WATCHLIST_NAME = "WAW_C4L"; //WatchAll with cookies 4 life :3

    public static String getAvatarUrl(int id) {
        return WatchAllServiceHelper.API_BASE_URL + "users/avatar/" + id;
    }

    public static String getTMDBImageUrl(String imagePath, String imageSize) {
        return APIUtils.TMDB_BASEURL_IMAGES + imageSize + imagePath;
    }

    public static String posterFromType(int type, String poster) {
        if (type == AnimeModel.TYPE) {
            return poster;
        } else {
            return getTMDBImageUrl(poster, Queries.Image.SIZE_SMALL_POSTER);
        }
    }

    public static final int MEDIA_LIST_DEFAULT = 0;
    public static final int MEDIA_LIST_SECTIONED = 1;
    public static final int MEDIA_LIST_SPLIT = 2;

}
