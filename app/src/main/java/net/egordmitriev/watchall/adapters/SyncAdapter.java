package net.egordmitriev.watchall.adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.api.WatchAllServiceHelper;
import net.egordmitriev.watchall.api.base.APIError;
import net.egordmitriev.watchall.api.database.tables.WatchlistsTable;
import net.egordmitriev.watchall.pojo.anilist.AnimeModel;
import net.egordmitriev.watchall.pojo.tmdb.MovieModel;
import net.egordmitriev.watchall.pojo.tmdb.SerieModel;
import net.egordmitriev.watchall.pojo.watchall.WatchlistModel;
import net.egordmitriev.watchall.utils.APIUtils;
import net.egordmitriev.watchall.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Response;

/**
 * Created by EgorDm on 5/14/2016.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        if (account == null) {
            Logger.d("Sync acc is null");
            return;
        }

        WatchlistModel[] data = WatchlistsTable.getAll(null, null);
        JsonObject[] syncData;
        if(data != null) {
            syncData = new JsonObject[data.length];
            for (int i = 0; i < data.length; i++) {
                syncData[i] = watchlistToJson(data[i]);
            }
        } else {
            syncData = new JsonObject[0];
        }
        //Log.d("PRETTYLOGGER", APIUtils.sGlobalParser.toJson(syncData));
        Logger.json(APIUtils.sGlobalParser.toJson(syncData));

        try {
            Response<JsonObject> response = WatchAllServiceHelper.sService.syncWatchlist(syncData).execute();
            APIError error = ErrorUtils.checkError(response);
            if (error != null) {
                return;
            }

            JsonArray responseData = response.body().get("data").getAsJsonArray();
            Logger.json(APIUtils.sGlobalParser.toJson(responseData));
            for (int i = 0; i < responseData.size(); i++) {
                JsonObject object = responseData.get(i).getAsJsonObject();
                if (object.entrySet().size() == 3) {
                    WatchlistsTable.setServerId(object.get("local_id").getAsInt(), object.get("id").getAsInt(), object.get("modified").getAsLong());
                } else if (object.entrySet().size() == 2) {
                    WatchlistsTable.setModified(object.get("id").getAsInt(), object.get("modified").getAsLong());
                } else {
                    WatchlistModel list = watchlistFromJson(object);
                    if (list != null) {
                        list.server_id = list.id;
                        list.setID(-1);
                        int id = WatchlistsTable.getId("server_id=?", new String[]{Integer.toString(list.server_id)});
                        if(list.base.title.equals(APIUtils.FAVOURITES_WATCHLIST_NAME)) {
                            id = WatchAllServiceHelper.getFavouritesID();
                            WatchlistsTable.setServerId(id, list.server_id, new Date().getTime());
                            list.setID(id);
                        }
                        WatchlistsTable.upsert(list, id);
                    }
                }
            }
            Logger.d("The synchronization was a success");
        } catch (Exception e) {
            Logger.e(e.getMessage());
           /* if (e.getResponse() != null && e.getResponse().getBody() != null) {
                Log.d("PRETTYLOGGER", new String(((TypedByteArray) e.getResponse().getBody()).getBytes()));
            }*/
            Logger.d("The synchronization was a failure");
        }
    }

    public static JsonObject watchlistToJson(WatchlistModel data) {
        JsonObject ret = APIUtils.sGlobalParser.toJsonTree(data.base, WatchlistModel.Base.class).getAsJsonObject();
        ret.addProperty("modified", data.modified / 1000);
        ret.remove("id");
        ret.add("local_id", new JsonPrimitive(data.id));
        if(data.server_id > 0) {
            ret.add("id", new JsonPrimitive(data.server_id));
        }
        if (data.detail != null) {
            ret.add("description", new JsonPrimitive(data.detail.description));
            if (data.detail.list_contents != null && !data.detail.list_contents.isEmpty()) {
                JsonArray movies = new JsonArray();
                JsonArray series = new JsonArray();
                JsonArray animes = new JsonArray();
                for (JsonObject obj : data.detail.list_contents) {
                    switch (obj.get("model_type").getAsInt()) {
                        case MovieModel.TYPE:
                            if (obj.has("release_date")) {
                                try {
                                    //TODO: if works, shorten byt adding long directly without creating a date
                                    //Date release_date = APIUtils.sTMDBDateFormat.parse(obj.get("release_date").getAsString());
                                    Date release_date = new Date(obj.get("release_date").getAsLong());
                                    obj.remove("release_date");
                                    obj.addProperty("release_date", release_date.getTime() / 1000);
                                } catch (Exception e) {
                                    Logger.e(e, "Error while parsing release date for a movie!");
                                }
                            }
                            movies.add(obj);
                            break;
                        case SerieModel.TYPE:
                            if (obj.has("first_air_date")) {
                                try {
                                    //Date release_date =  APIUtils.sTMDBDateFormat.parse(obj.get("first_air_date").getAsString());
                                    Date release_date = new Date(obj.get("first_air_date").getAsLong());
                                    obj.remove("first_air_date");
                                    obj.addProperty("first_air_date", release_date.getTime() / 1000);
                                } catch (Exception e) {
                                    Logger.e(e, "Error while parsing release date for a movie!");
                                }
                            }
                            series.add(obj);
                            break;
                        case AnimeModel.TYPE:
                            animes.add(obj);
                            break;
                    }
                }
                ret.add("movies", movies);
                ret.add("series", series);
                ret.add("animes", animes);
            }
        }
        return ret;
    }

    public static WatchlistModel watchlistFromJson(JsonObject data) {
        WatchlistModel.Base base = APIUtils.sGlobalParser.fromJson(data, WatchlistModel.Base.class);
        if (base != null) {
            WatchlistModel.Detail detail = new WatchlistModel.Detail();
            detail.id = base.id;
            detail.description = data.get("description").getAsString();
            detail.list_contents = new ArrayList<>();
            JsonArray movies = data.get("movies").getAsJsonArray();
            JsonArray series = data.get("series").getAsJsonArray();
            JsonArray animes = data.get("animes").getAsJsonArray();
            for (JsonElement element : movies) {
                JsonObject object = element.getAsJsonObject();
                object.addProperty("model_type", MovieModel.TYPE);
                detail.list_contents.add(object);
            }
            for (JsonElement element : series) {
                JsonObject object = element.getAsJsonObject();
                object.addProperty("model_type", SerieModel.TYPE);
                detail.list_contents.add(object);
            }
            for (JsonElement element : animes) {
                JsonObject object = element.getAsJsonObject();
                object.addProperty("model_type", AnimeModel.TYPE);
                detail.list_contents.add(object);
            }
            return new WatchlistModel(base, detail);
        }
        return null;
    }
}
