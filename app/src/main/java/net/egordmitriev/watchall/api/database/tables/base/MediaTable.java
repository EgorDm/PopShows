package net.egordmitriev.watchall.api.database.tables.base;

import android.content.ContentValues;
import android.database.Cursor;

import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.MainApplication;
import net.egordmitriev.watchall.utils.APIUtils;

import java.util.Date;

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

    protected <T> T[] get(String tableName, int identifier, Class<T[]> classType) {
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
}
