package net.egordmitriev.popshows.api;

import com.google.gson.JsonArray;
import com.orhanobut.logger.Logger;

import net.egordmitriev.popshows.api.base.APIError;
import net.egordmitriev.popshows.api.base.ServiceHelperBase;
import net.egordmitriev.popshows.api.database.tables.AnimesTable;
import net.egordmitriev.popshows.api.services.AnilistService;
import net.egordmitriev.popshows.pojo.anilist.AnimeModel;
import net.egordmitriev.popshows.pojo.anilist.ClientCredentials;
import net.egordmitriev.popshows.pojo.user.ListRequestData;
import net.egordmitriev.popshows.utils.APIUtils;
import net.egordmitriev.popshows.utils.Credentials;
import net.egordmitriev.popshows.utils.DataCallback;
import net.egordmitriev.popshows.utils.ErrorUtils;
import net.egordmitriev.popshows.utils.MediaUtils;
import net.egordmitriev.popshows.utils.QueryBuilder;

import java.io.IOException;
import java.util.Date;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class AnilistServiceHelper extends ServiceHelperBase {
    public static final String API_BASE_URL = "https://anilist.co/";
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());

                    if (!original.url().pathSegments().get(1).equals("auth")) {
                        requestBuilder.header("Authorization", "Bearer " + getAuthToken());
                    }
                    return chain.proceed(requestBuilder.build());
                }
            })
            .addInterceptor(APIUtils.getDebugInterceptor());
    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(APIUtils.sAnilistParser));

    public static final AnilistService sService = createService(AnilistService.class, builder, httpClient);

    private static ClientCredentials credentials;

    /**
     * WARNING! This method is synchronous!
     *
     * @return Authentication token
     */
    private static String getAuthToken() {
        if (credentials != null && credentials.expires.after(new Date())) {
            return credentials.access_token;
        }
        try {
            credentials = sService.requestAccessToken(APIUtils.ANILIST_GRANTTYPE, Credentials.ANILIST_CLIENTID, Credentials.ANILIST_CLIENTSECRET).execute().body();
            credentials.save();
            return credentials.access_token;
        } catch (Exception e) {
            Logger.e(e, "Error while requesting a access token.");
            e.printStackTrace();
        }
        return null;
    }

    public AnilistServiceHelper() {
    }

    public static void setup() {
        credentials = ClientCredentials.load();
    }

    public static void getAnimeList(final ListRequestData request, final DataCallback<AnimeModel.Base[]> callback, boolean tryLocal) throws Exception {
        if (tryLocal) {
            GlobalHelper.updateNetworkState();
            AnimeModel.Base[] ret = AnimesTable.tryGetLocal(request.actionType);
            if (ret != null) {
                callback.success(ret);
                return;
            } else if (GlobalHelper.getNetworkState() == APIUtils.NETWORK_STATE_DISCONNECTED) {
                callback.failure(new APIError(1337, "No network connection!"));
                return;
            }
        }
        getAnimeListMethod(request).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, retrofit2.Response<JsonArray> response) {
                APIError error = ErrorUtils.checkError(response);
                if (error != null) {
                    callback.failure(error);
                }
                try {
                    AnimeModel.Base[] data = APIUtils.sAnilistParser.fromJson(response.body(), AnimeModel.Base[].class);
                    callback.success(data);
                    AnimesTable.upsert(data, request.actionType);
                } catch (Exception e) {
                    callback.failure(new APIError(1337, e.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                callback.failure(new APIError(1337, t.getMessage()));
            }
        });
    }

    private static Call<JsonArray> getAnimeListMethod(ListRequestData request) throws Exception {
        QueryBuilder queryBuild;
        switch (request.actionType) {
            case MediaUtils.ACTION_TOPAIRING:
                queryBuild = QueryBuilder.useDefaultAnimeQuery(request.genres, request.page)
                        .setString(APIUtils.Queries.Anime.SORT, APIUtils.Queries.Anime.setOrderSuffix(APIUtils.Queries.Anime.Sorting.POPULARITY, true))
                        .setString(APIUtils.Queries.Anime.STATUS, APIUtils.Queries.Anime.Status.CURRENTLY_AIRING);
                return sService.getAnimeDiscover(queryBuild.build());
            case MediaUtils.ACTION_JUSTADDED:
                queryBuild = QueryBuilder.useDefaultAnimeQuery(request.genres, request.page)
                        .setString(APIUtils.Queries.Anime.SORT, APIUtils.Queries.Anime.setOrderSuffix(APIUtils.Queries.Anime.Sorting.ID, true));
                return sService.getAnimeDiscover(queryBuild.build());
            case MediaUtils.ACTION_POPULAR:
                queryBuild = QueryBuilder.useDefaultAnimeQuery(request.genres, request.page)
                        .setString(APIUtils.Queries.Anime.SORT, APIUtils.Queries.Anime.setOrderSuffix(APIUtils.Queries.Anime.Sorting.POPULARITY, true));
                return sService.getAnimeDiscover(queryBuild.build());
            case MediaUtils.ACTION_TOPRATED:
                queryBuild = QueryBuilder.useDefaultAnimeQuery(request.genres, request.page)
                        .setString(APIUtils.Queries.Anime.SORT, APIUtils.Queries.Anime.setOrderSuffix(APIUtils.Queries.Anime.Sorting.SCORE, true));
                return sService.getAnimeDiscover(queryBuild.build());
            case MediaUtils.ACTION_UPCOMING:
                queryBuild = QueryBuilder.useDefaultAnimeQuery(request.genres, request.page)
                        .setString(APIUtils.Queries.Anime.SORT, APIUtils.Queries.Anime.setOrderSuffix(APIUtils.Queries.Anime.Sorting.POPULARITY, true))
                        .setString(APIUtils.Queries.Anime.STATUS, APIUtils.Queries.Anime.Status.NOT_YET_AIRED);
                return sService.getAnimeDiscover(queryBuild.build());
        }

        throw new Exception("No such anime action type");
    }

    public static void search(String query, int page, final DataCallback<AnimeModel.Base[]> callback) {
        sService.searchAnime(query, page).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, retrofit2.Response<JsonArray> response) {
                APIError error = ErrorUtils.checkError(response);
                if (error != null) {
                    callback.failure(error);
                }
                try {
                    callback.success(APIUtils.sAnilistParser.fromJson(response.body(), AnimeModel.Base[].class));
                } catch (Exception e) {
                    callback.failure(new APIError(1337, e.getMessage()));
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                callback.failure(new APIError(1337, t.getMessage()));
            }
        });
    }
}
