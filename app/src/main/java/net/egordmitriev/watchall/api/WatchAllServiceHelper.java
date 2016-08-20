package net.egordmitriev.watchall.api;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.adapters.WatchAllAuthenticator;
import net.egordmitriev.watchall.api.base.APIError;
import net.egordmitriev.watchall.api.base.ServiceHelperBase;
import net.egordmitriev.watchall.api.database.tables.WatchlistsTable;
import net.egordmitriev.watchall.api.services.WatchAllService;
import net.egordmitriev.watchall.helpers.AMediaCardRecyclerHelper;
import net.egordmitriev.watchall.pojo.DetailedModel;
import net.egordmitriev.watchall.pojo.watchall.ClientCredentials;
import net.egordmitriev.watchall.pojo.watchall.UserModel;
import net.egordmitriev.watchall.pojo.watchall.WatchlistModel;
import net.egordmitriev.watchall.ui.dialogs.WatchlistAddToDialog;
import net.egordmitriev.watchall.utils.APIUtils;
import net.egordmitriev.watchall.utils.DataCallback;
import net.egordmitriev.watchall.utils.ErrorUtils;

import java.io.IOException;
import java.util.ArrayList;
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
 * Created by EgorDm on 4/1/2016.
 */
public class WatchAllServiceHelper extends ServiceHelperBase {

    public static final String API_BASE_URL = "http://watchit.egordmitriev.net/api/";
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());
                    if (!original.url().pathSegments().contains("token")) {
                        if (getAuthToken() != null) {
                            requestBuilder.header("Authorization", "Bearer " + sCredentials.token);
                        }
                    }
                    return chain.proceed(requestBuilder.build());
                }
            })
            .addInterceptor(APIUtils.getDebugInterceptor());
    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(APIUtils.sGlobalParser));

    public static final WatchAllService sService = createService(WatchAllService.class, builder, httpClient);

    public static ClientCredentials sCredentials;

    /**
     * WARNING! This method is synchronous!
     *
     * @return Authentication token
     */
    private static String getAuthToken() {
        if (sCredentials != null && sCredentials.exp.after(new Date())) {
            return sCredentials.token;
        }
        try {
            sCredentials = WatchAllAuthenticator.refreshCredentials();
            return sCredentials.token;
        } catch (Exception e) {
            Logger.e(e, "Error while requesting a access token.");
            e.printStackTrace();
        }
        return null;
    }

    public WatchAllServiceHelper() {
    }

    public static WatchlistModel getFavourites() {
        WatchlistModel ret = WatchlistsTable.get("title=?", new String[]{APIUtils.FAVOURITES_WATCHLIST_NAME});
        if (ret != null) {
            return ret;
        }
        ret = new WatchlistModel(new WatchlistModel.Base(), new WatchlistModel.Detail());
        ret.base.title = APIUtils.FAVOURITES_WATCHLIST_NAME;
        ret.base.creator = 42; //TODO: Add the actual user id
        ret.base.color = 0;
        ret.base.is_public = false;
        ret.base.is_local = true;
        ret.detail.description = "<3 Cookies";
        ret.detail.list_contents = new ArrayList<>();
        WatchlistsTable.upsert(ret, -1, 0);
        return ret;
    }

    public static int getFavouritesID() {
        int ret = WatchlistsTable.getId("title=?", new String[]{APIUtils.FAVOURITES_WATCHLIST_NAME});
        if (ret != -1) {
            return ret;
        }

        WatchlistModel favs = new WatchlistModel(new WatchlistModel.Base(), new WatchlistModel.Detail());
        favs.base.title = APIUtils.FAVOURITES_WATCHLIST_NAME;
        favs.base.creator = 42; //TODO: Add the actual user id
        favs.base.color = 0;
        favs.base.is_public = false;
        favs.base.is_local = true;
        favs.detail.description = "<3 Cookies";
        favs.detail.list_contents = new ArrayList<>();
        if (WatchlistsTable.upsert(favs, -1, 0)) {
            ret = WatchlistsTable.getId("title=?", new String[]{APIUtils.FAVOURITES_WATCHLIST_NAME});
        }
        Logger.d("Returning fav id = " + ret);
        return ret;
    }

    public static WatchlistModel.Base[] getMyWatchlists() {
        return WatchlistsTable.getAllBase("title<>?", new String[]{APIUtils.FAVOURITES_WATCHLIST_NAME});
    }

    public static boolean deleteWatchlist(int identifier) {
        boolean ret;
        if (WatchAllAuthenticator.getAccount() != null) {
            int server_id = WatchlistsTable.getServerId(identifier);
            ret = WatchlistsTable.delete(identifier);
            if (ret && server_id != -1) {
                Logger.d("Deleting onlline " + server_id);
                sService.deleteWatchlist(server_id).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        } else {
            ret = WatchlistsTable.delete(identifier);
        }
        Logger.d("Deleting " + identifier);

        return ret;
    }

    public static void getUser(final DataCallback<UserModel> callback, int userID) {
        sService.getUser(userID).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                APIError error = ErrorUtils.checkError(response);
                if (error != null) {
                    callback.failure(error);
                }
                try {
                    UserModel ret = APIUtils.sGlobalParser.fromJson(response.body().get("data").getAsJsonObject(), UserModel.class);
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

    public static void getMyProfile(final DataCallback<UserModel> callback, boolean invalidate) {
        if (WatchAllAuthenticator.getAccount() == null) {
            callback.failure(null);
            return;
        }

        if (!invalidate) {
            UserModel ret = getMyProfileFromCache();
            if (ret.id != -1) {
                callback.success(ret);
                return;
            }
        }

        sService.getProfile().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                APIError error = ErrorUtils.checkError(response);
                if (error != null) {
                    callback.failure(error);
                }
                try {
                    UserModel ret = APIUtils.sGlobalParser.fromJson(response.body().get("data").getAsJsonObject(), UserModel.class);
                    callback.success(ret);
                    cacheMyProfile(ret);
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

    private static void cacheMyProfile(UserModel user) {
        PreferencesHelper.getInstance().build()
                .setInt(R.string.pref_account_user_id, user.id)
                .setString(R.string.pref_account_user_name, user.fullname)
                .setString(R.string.pref_account_user_email, user.email)
                .setString(R.string.pref_account_user_avatar, user.avatar)
                .setInt(R.string.pref_account_user_color, user.profile_color)
                .commit();
    }

    private static UserModel getMyProfileFromCache() {
        return new UserModel(
                PreferencesHelper.getInstance().getInt(R.string.pref_account_user_id, -1),
                PreferencesHelper.getInstance().getString(R.string.pref_account_user_name),
                PreferencesHelper.getInstance().getString(R.string.pref_account_user_email),
                PreferencesHelper.getInstance().getString(R.string.pref_account_user_avatar),
                PreferencesHelper.getInstance().getInt(R.string.pref_account_user_color, -1)
        );
    }

    public static <T extends DetailedModel> AMediaCardRecyclerHelper.IMediaItemMenuListener<T> getMediaMenuListener(final FragmentManager manager, final Activity activity) {
        return new AMediaCardRecyclerHelper.IMediaItemMenuListener<T>() {
            @Override
            public void onMenuItemClick(MenuItem menuItem, T item) {
                switch (menuItem.getItemId()) {
                    case R.id.media_addtolist:
                        WatchlistAddToDialog dialog = WatchlistAddToDialog.newInstance(item);
                        dialog.show(manager, "Add to list");
                        break;
                    case R.id.media_addtofav:
                        if (WatchlistsTable.addMedia(APIUtils.sGlobalParser.toJsonTree(item.base).getAsJsonObject(), WatchAllServiceHelper.getFavouritesID())) {
                            Toast.makeText(activity, String.format(activity.getString(R.string.toast_added_to_list), "Favourites"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, R.string.toast_unknown_error, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };
    }
}
