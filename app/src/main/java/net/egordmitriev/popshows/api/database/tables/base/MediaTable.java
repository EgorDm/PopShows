package net.egordmitriev.popshows.api.database.tables.base;

import android.content.ContentValues;
import android.database.Cursor;

import com.orhanobut.logger.Logger;

import net.egordmitriev.popshows.MainApplication;
import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.api.GlobalHelper;
import net.egordmitriev.popshows.utils.APIUtils;
import net.egordmitriev.popshows.api.PreferencesHelper;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class MediaTable extends BaseTable {
    private static final String COLUMN_MODIFIED = "modified";
    private static final String COLUMN_CONTENT = "list_data";

    //ID resembles the table type. Easier to avoid duplicates
    private static final String QUERY_COLUMNS = " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_MODIFIED + " INTEGER ," +
            COLUMN_CONTENT + " TEXT)";

    protected static boolean createTable(String tableName) {
        createTable(tableName, QUERY_COLUMNS);
        return true;
    }

    protected static <T> boolean upsert(String tableName, T[] item, int identifier) {
        ContentValues insertValues = new ContentValues();
        insertValues.put(COLUMN_ID, identifier);
        insertValues.put(COLUMN_MODIFIED, new Date().getTime());
        insertValues.put(COLUMN_CONTENT, APIUtils.sGlobalParser.toJson(item));
        return upsert(tableName, insertValues, "id=?", new String[]{Integer.toString(identifier)});
    }

    protected static <T> T[] get(String tableName, int identifier, Class<T[]> classType) {
        Cursor cursor =  MainApplication.getDatabase().query(tableName, new String[]{COLUMN_CONTENT}, "id=?", new String[]{Integer.toString(identifier)}, null, null, null);

        if(cursor != null && cursor.moveToFirst()) {
            try {
                return APIUtils.sGlobalParser.fromJson(cursor.getString(0), classType);
            } catch (Exception e) {
                Logger.e(e.getMessage());
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    protected static  <T> T[] tryGetLocal(String tableName, int identifier, Class<T[]> classType) {
        int networkState = GlobalHelper.getNetworkState();
        if(networkState == 0 || !checkOutdated(tableName, identifier) || (networkState == 2 && !useMobileNetwork())) {
            return get(tableName, identifier, classType);
        }
        return null;
    }

    public static Date getModified(String tableName, int identifier) {
        Cursor cursor = MainApplication.getDatabase().query(tableName, new String[]{COLUMN_MODIFIED}, "id=?",
                new String[]{Integer.toString(identifier)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return new Date(cursor.getLong(0));
            } catch (Exception e) {
                Logger.e(e, "Error while retrieving modified for media items select.");
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    protected static boolean checkOutdated(String tableName, int identifier) {
        Date lastChanged = getModified(tableName, identifier);
        if(lastChanged != null) {
            long diffInMs = new Date().getTime() - lastChanged.getTime();
            return TimeUnit.MILLISECONDS.toHours(diffInMs) > outdatedTime();
        }
        return true;
    }

    protected static int outdatedTime() {
        String result =  PreferencesHelper.getInstance().getString(R.string.pref_datausage_sync_delay);
        return (result != null) ? Integer.parseInt(result) : APIUtils.OUTDATED_TIME;
    }

    protected static boolean useMobileNetwork() {
        return PreferencesHelper.getInstance().getBoolean(R.string.pref_datausage_usenetwork, false);
    }
}
