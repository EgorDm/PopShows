package net.egordmitriev.watchall.api.database.tables;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.MainApplication;
import net.egordmitriev.watchall.api.database.tables.base.BaseTable;
import net.egordmitriev.watchall.pojo.watchall.WatchlistModel;
import net.egordmitriev.watchall.utils.APIUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class WatchlistsTable extends BaseTable {
    private static final String sTableName = "watchlists";
    private static boolean sCreated = false;

    private static final String COLUMN_SERVER_ID = "server_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_MODIFIED = "modified";
    private static final String COLUMN_BASE_DATA = "base_data";
    private static final String COLUMN_DETAIL_DATA = "detail_data";

    private static final String QUERY_COLUMNS = " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_SERVER_ID + " INTEGER DEFAULT 0," +
            COLUMN_TITLE + " VARCHAR(255) ," +
            COLUMN_BASE_DATA + " TEXT ," +
            COLUMN_DETAIL_DATA + " TEXT," +
            COLUMN_MODIFIED + " INTEGER)";

    public static boolean createTable() {
        if (sCreated) return false;
        createTable(sTableName, QUERY_COLUMNS);
        sCreated = true;
        return true;
    }

    public static boolean upsert(WatchlistModel watchlist, int identifier) {
        ContentValues insertValues = new ContentValues();
        if (identifier == -1) {
            identifier = ((int) lastInsertID(sTableName)) + 1;
        }
        watchlist.setID(identifier);
        insertValues.put(COLUMN_TITLE, watchlist.base.title);
        insertValues.put(COLUMN_BASE_DATA, APIUtils.sGlobalParser.toJson(watchlist.base));
        insertValues.put(COLUMN_DETAIL_DATA, APIUtils.sGlobalParser.toJson(watchlist.detail));
        insertValues.put(COLUMN_MODIFIED, new Date().getTime());
        if (watchlist.server_id != 0) {
            insertValues.put(COLUMN_SERVER_ID, watchlist.server_id);
            return upsert(sTableName, insertValues, "server_id=?", new String[]{Integer.toString(watchlist.server_id)});
        } else {
            return upsert(sTableName, insertValues, "id=?", new String[]{Integer.toString(identifier)});
        }
    }

    public static boolean delete(int identifier) {
        return delete(sTableName, identifier);
    }

    public static WatchlistModel get(int identifier) {
        return get("id=?", new String[]{Integer.toString(identifier)});
    }

    public static WatchlistModel get(String selection, String[] selectionArgs) {
        String[] columns = new String[]{COLUMN_BASE_DATA, COLUMN_DETAIL_DATA, COLUMN_SERVER_ID, COLUMN_MODIFIED};
        Cursor cursor = MainApplication.getDatabase().query(sTableName, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                WatchlistModel ret = new WatchlistModel(
                        APIUtils.sGlobalParser.fromJson(cursor.getString(0), WatchlistModel.Base.class),
                        APIUtils.sGlobalParser.fromJson(cursor.getString(1), WatchlistModel.Detail.class));
                ret.server_id = cursor.getInt(2);
                ret.modified = cursor.getLong(3);
                return ret;
            } catch (Exception e) {
                Logger.e(e, "Error while retrieving detail for WatchlistModel item select: " + selectionArgs[0]);
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public static WatchlistModel[] getAll(String selection, String[] selectionArgs) {
        String[] columns = new String[]{COLUMN_BASE_DATA, COLUMN_DETAIL_DATA, COLUMN_SERVER_ID, COLUMN_MODIFIED};
        Cursor cursor = MainApplication.getDatabase().query(sTableName, columns, selection, selectionArgs, null, null, COLUMN_MODIFIED + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            WatchlistModel[] ret = new WatchlistModel[cursor.getCount()];
            int i = 0;
            while (!cursor.isAfterLast()) {
                try {
                    ret[i] = new WatchlistModel(
                            APIUtils.sGlobalParser.fromJson(cursor.getString(0), WatchlistModel.Base.class),
                            APIUtils.sGlobalParser.fromJson(cursor.getString(1), WatchlistModel.Detail.class));
                    ret[i].base.is_local = true;
                    ret[i].server_id = cursor.getInt(2);
                    ret[i].modified = cursor.getLong(3);
                } catch (Exception e) {
                    Logger.e(e, "Error while retrieving detail for watchlist items select: " + selectionArgs[0]);
                }
                i++;
                cursor.moveToNext();
            }
            cursor.close();
            return ret;
        }
        return null;
    }

    public static WatchlistModel.Base[] getAllBase(String selection, String[] selectionArgs) {
        WatchlistModel.Base[] ret = getJsonAll(sTableName, new String[]{COLUMN_BASE_DATA}, selection, selectionArgs, WatchlistModel.Base.class);
        if(ret != null) {
            for (WatchlistModel.Base aRet : ret) {
                aRet.is_local = true; //TODO: I doubt it will work
            }
        }
        return ret;
    }

    public static WatchlistModel.Base getBase(int identifier) {
        String[] columns = {COLUMN_BASE_DATA};
        WatchlistModel.Base ret = getJsonFirst(sTableName, columns, "id=?", new String[]{Integer.toString(identifier)}, WatchlistModel.Base.class);
        if(ret != null) ret.is_local = true;
        return ret;
    }

    public static WatchlistModel.Detail getDetail(int identifier) {
        String[] columns = {COLUMN_DETAIL_DATA};
        return getJsonFirst(sTableName, columns, "id=?", new String[]{Integer.toString(identifier)}, WatchlistModel.Detail.class);
    }

    public static boolean addMedia(JsonObject media, int identifier) {
        WatchlistModel.Detail detail = getDetail(identifier);
        if (detail == null) return false;
        if (detail.list_contents == null) {
            detail.list_contents = new ArrayList<>();
        } else {
            int objId = media.get("id").getAsInt();
            for (JsonObject child : detail.list_contents) {
                if (child != null && child.get("id").getAsInt() == objId) {
                    return true; //Already has this one.
                }
            }
        }
        detail.list_contents.add(0, media);
        return saveJsonObject(sTableName, COLUMN_DETAIL_DATA, identifier, detail);
    }

    public static boolean removeMedia(int mediaId, int identifier) {
        WatchlistModel.Detail detail = getDetail(identifier);
        if (detail == null || detail.list_contents == null || detail.list_contents.size() < 1)
            return false;

        boolean ret = false;
        for (int i = detail.list_contents.size() - 1; i >= 0; i--) {
            if (detail.list_contents.get(i).get("id").getAsInt() == mediaId) {
                detail.list_contents.remove(i); //Removed!
                ret = true;
            }
        }
        return ret && saveJsonObject(sTableName, COLUMN_DETAIL_DATA, identifier, detail);
    }

    public static int getServerId(int identifier) {
        Cursor cursor = MainApplication.getDatabase().query(sTableName, new String[]{COLUMN_SERVER_ID}, "id=?",
                new String[]{Integer.toString(identifier)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return cursor.getInt(0);
            } catch (Exception e) {
                Logger.e(e, "Error while retrieving server_id for watchlist items select.");
            } finally {
                cursor.close();
            }
        }
        return -1;
    }

    public static int getId(String selection, String[] selectionArgs) {
        Cursor cursor = MainApplication.getDatabase().query(sTableName, new String[]{COLUMN_ID}, selection,
                selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return cursor.getInt(0);
            } catch (Exception e) {
                Logger.e(e, "Error while retrieving server_id for watchlist items select.");
            } finally {
                cursor.close();
            }
        }
        return -1;
    }

    public static Date getModified(int identifier) {
        Cursor cursor = MainApplication.getDatabase().query(sTableName, new String[]{COLUMN_MODIFIED}, "id=?",
                new String[]{Integer.toString(identifier)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return new Date(cursor.getLong(0));
            } catch (Exception e) {
                Logger.e(e, "Error while retrieving modified for watchlist items select.");
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public static void drop() {
        drop(sTableName);
        sCreated = false;
    }
}
