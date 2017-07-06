package net.egordmitriev.popshows.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;

import net.egordmitriev.popshows.api.base.APIError;
import net.egordmitriev.popshows.api.base.ServiceHelperBase;
import net.egordmitriev.popshows.api.database.tables.MoviesTable;
import net.egordmitriev.popshows.api.database.tables.SeriesTable;
import net.egordmitriev.popshows.api.services.TMDBService;
import net.egordmitriev.popshows.pojo.BaseModel;
import net.egordmitriev.popshows.pojo.tmdb.MovieModel;
import net.egordmitriev.popshows.pojo.tmdb.PersonModel;
import net.egordmitriev.popshows.pojo.tmdb.SerieModel;
import net.egordmitriev.popshows.pojo.user.ListRequestData;
import net.egordmitriev.popshows.utils.APIUtils;
import net.egordmitriev.popshows.utils.DataCallback;
import net.egordmitriev.popshows.utils.ErrorUtils;
import net.egordmitriev.popshows.utils.MediaUtils;
import net.egordmitriev.popshows.utils.QueryBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static net.egordmitriev.popshows.utils.Credentials.TMDB_APIKEY;

/**
 * Created by EgorDm on 4/1/2016.
 */
public class TMDBServiceHelper extends ServiceHelperBase {


    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    HttpUrl url = request.url().newBuilder()
                            .addQueryParameter("api_key", TMDB_APIKEY).build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                }
            })
            .addInterceptor(APIUtils.getDebugInterceptor());
    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(APIUtils.sTMDBParser));


    public static final TMDBService sService = createService(TMDBService.class, builder, httpClient);

    public TMDBServiceHelper() {
    }

    public static void getMovieList(final ListRequestData request, final DataCallback<MovieModel.Base[]> callback, boolean tryLocal) throws Exception {
        if(tryLocal) {
            GlobalHelper.updateNetworkState();
            MovieModel.Base[] ret = MoviesTable.tryGetLocal(request.actionType);
            if (ret != null) {
                callback.success(ret);
                return;
            } else if (GlobalHelper.getNetworkState() == APIUtils.NETWORK_STATE_DISCONNECTED) {
                callback.failure(new APIError(1337, "No network connection!"));
                return;
            }
        }
        getMovieListMethod(request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                APIError error = ErrorUtils.checkError(response);
                if(error != null) {
                    callback.failure(error);
                }
                try {
                    //Logger.json(response.body().toString());
                    MovieModel.Base[] data = APIUtils.sTMDBParser.fromJson(response.body().get("results").getAsJsonArray(), MovieModel.Base[].class);
                    callback.success(data);
                    MoviesTable.upsert(data, request.actionType);
                } catch (Exception e) {
                    callback.failure(new APIError(1337, e.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.failure(new APIError(1337, t.getMessage()));
            }
        });
    }

    private static Call<JsonObject> getMovieListMethod(ListRequestData request) throws Exception {
        switch (request.actionType) {
            case MediaUtils.ACTION_POPULAR:
                if(request.genres == null) {
                    return sService.getMoviePopular(request.page);
                } else {
                    QueryBuilder queryBuild = QueryBuilder.useDefaultTMDBQuery(request.genres, request.page)
                            .setString(APIUtils.Queries.Movie.SORT_BY, APIUtils.Queries.Movie.setOrderSuffix(APIUtils.Queries.Movie.Sorting.POPULARITY, true));
                    return sService.getMovieDiscover(queryBuild.build());
                }
            case MediaUtils.ACTION_NEWEST:
                if (request.genres == null) {
                    return sService.getMovieNewest(request.page);
                } else {
                    QueryBuilder queryBuild = QueryBuilder.useDefaultTMDBQuery(request.genres, request.page)
                            .setString(APIUtils.Queries.Movie.SORT_BY, APIUtils.Queries.Movie.setOrderSuffix(APIUtils.Queries.Movie.Sorting.RELEASE_DATE, true));
                    return sService.getMovieDiscover(queryBuild.build());
                }
            case MediaUtils.ACTION_TOPRATED:
                if (request.genres == null) {
                    return sService.getMovieToprated(request.page);
                } else {
                    QueryBuilder queryBuild = QueryBuilder.useDefaultTMDBQuery(request.genres, request.page)
                            .setString(APIUtils.Queries.Movie.SORT_BY, APIUtils.Queries.Movie.setOrderSuffix(APIUtils.Queries.Movie.Sorting.VOTE_AVERAGE, true));
                    return sService.getMovieDiscover(queryBuild.build());
                }
            case MediaUtils.ACTION_UPCOMING:
                return sService.getMovieUpcoming(request.page);
        }

        throw new Exception("No such movie action type");
    }

    public static void getSerieList(final ListRequestData request, final DataCallback<SerieModel.Base[]> callback, boolean tryLocal) throws Exception {
        if(tryLocal) {
            GlobalHelper.updateNetworkState();
            SerieModel.Base[] ret = SeriesTable.tryGetLocal(request.actionType);
            if (ret != null) {
                callback.success(ret);
                return;
            } else if (GlobalHelper.getNetworkState() == APIUtils.NETWORK_STATE_DISCONNECTED) {
                callback.failure(new APIError(1337, "No network connection!"));
                return;
            }
        }
        getSeriesListMethod(request).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                APIError error = ErrorUtils.checkError(response);
                if(error != null) {
                    callback.failure(error);
                }
                try {
                    SerieModel.Base[] data = APIUtils.sTMDBParser.fromJson(response.body().get("results").getAsJsonArray(), SerieModel.Base[].class);
                    callback.success(data);
                    SeriesTable.upsert(data, request.actionType);
                } catch (Exception e) {
                    callback.failure(new APIError(1337, e.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.failure(new APIError(1337, t.getMessage()));
            }
        });
    }

    private static Call<JsonObject> getSeriesListMethod(ListRequestData request) throws Exception {
        switch (request.actionType) {
            case MediaUtils.ACTION_POPULAR:
                if(request.genres == null) {
                    return sService.getSeriePopular(request.page);
                } else {
                    QueryBuilder queryBuild = QueryBuilder.useDefaultTMDBQuery(request.genres, request.page)
                            .setString(APIUtils.Queries.Movie.SORT_BY, APIUtils.Queries.Movie.setOrderSuffix(APIUtils.Queries.Movie.Sorting.POPULARITY, true));
                    return sService.getSerieDiscover(queryBuild.build());
                }
            case MediaUtils.ACTION_TOPRATED:
                if (request.genres == null) {
                    return sService.getSerieToprated(request.page);
                } else {
                    QueryBuilder queryBuild = QueryBuilder.useDefaultTMDBQuery(request.genres, request.page)
                            .setString(APIUtils.Queries.Movie.SORT_BY, APIUtils.Queries.Movie.setOrderSuffix(APIUtils.Queries.Movie.Sorting.VOTE_AVERAGE, true));
                    return sService.getSerieDiscover(queryBuild.build());
                }
            case MediaUtils.ACTION_AIRINGTODAY:
                return sService.getSerieAiringToday(request.page);
            case MediaUtils.ACTION_ONTHEAIR:
                return sService.getSerieOnAir(request.page);
        }

        throw new Exception("No such serie action type");
    }

    public static void searchKeywords(String query, final DataCallback<String[]> callback) {
        sService.searchKeywords(query, 1).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                APIError error = ErrorUtils.checkError(response);
                if(error != null) {
                    callback.failure(error);
                }
                try {
                    JsonArray results = response.body().get("results").getAsJsonArray();
                    String[] ret = new String[results.size()];
                    for (int i = 0; i < results.size(); i++) {
                        ret[i] = results.get(i).getAsJsonObject().get("name").getAsString();
                    }
                    callback.success(ret);
                } catch (Exception e) {
                    callback.failure(new APIError(1337, e.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.failure(new APIError(1337, t.getMessage()));
            }
        });
    }

    public static void search(String query, int page, final DataCallback<BaseModel[]> callback) {
        sService.searchAll(query, page).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                APIError error = ErrorUtils.checkError(response);
                if(error != null) {
                    callback.failure(error);
                }
                try {
                    callback.success(parseRawResults(response.body().get("results").getAsJsonArray()));
                } catch (Exception e) {
                    callback.failure(new APIError(1337, e.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.failure(new APIError(1337, t.getMessage()));
            }
        });
    }

    protected static BaseModel[] parseRawResults(JsonArray results) {
        if(results != null && results.size() > 0) {
            BaseModel[] ret = new BaseModel[results.size()];
            for (int i = 0; i < ret.length; i++) {
                String mediaType = results.get(i).getAsJsonObject().get("media_type").getAsString();
                try {
                    switch (mediaType) {
                        case "movie":
                            ret[i] = APIUtils.sTMDBParser.fromJson(results.get(i).getAsJsonObject(), MovieModel.Base.class);
                            break;
                        case "tv":
                            ret[i] = APIUtils.sTMDBParser.fromJson(results.get(i).getAsJsonObject(), SerieModel.Base.class);
                            break;
                        case "person":
                            ret[i] = APIUtils.sTMDBParser.fromJson(results.get(i).getAsJsonObject(), PersonModel.Base.class);
                            break;
                        default:
                            Logger.e("Unknown mediatype: " + mediaType);
                            break;
                    }
                } catch (JsonSyntaxException ignored) {
                    Logger.e(ignored.getMessage());
                }
            }
            return ret;
        }
        return null;
    }
}
