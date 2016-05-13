package net.egordmitriev.watchall.api.database.tables.base;

import android.content.ContentValues;
import android.database.Cursor;

import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.MainApplication;
import net.egordmitriev.watchall.utils.APIUtils;

import java.lang.reflect.Array;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class BaseTable {
    protected static final String COLUMN_ID = "id";
    protected static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    protected static final String QUERY_DROP_TABLE = "DROP TABLE IF EXISTS ";

    protected static void createTable(String tableName, String columns) {
        MainApplication.getDatabase().execSQL(QUERY_CREATE_TABLE + tableName + columns);
    }

    protected static boolean upsert(String tableName, ContentValues values, String selection, String[] selectionArgs) {
        if(itemExists(tableName, selection, selectionArgs) == -1) {
            return (MainApplication.getDatabase().insert(tableName, null, values) != -1);
        } else {
            return (MainApplication.getDatabase().update(tableName, values, selection, selectionArgs) > 0);
        }
    }

    protected static long lastInsertID(String tableName) {
        long index = 0;
        Cursor cursor = MainApplication.getDatabase().query(
                "sqlite_sequence",
                new String[]{"seq"},
                "name = ?",
                new String[]{tableName},
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            index = cursor.getLong(cursor.getColumnIndex("seq"));
        }
        cursor.close();
        return index;
    }

    protected static int itemExists(String tableName, String selection, String[] selectionArgs) {
        int ret = -1;
        String[] columns = {COLUMN_ID};
        Cursor cursor = MainApplication.getDatabase().query(tableName, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            ret = cursor.getInt(0);
            cursor.close();
        }
        return ret;
    }

    protected static boolean delete(String tableName, int identifier) {
        String[] selectionArgs = {Integer.toString(identifier)};
        return (MainApplication.getDatabase().delete(tableName, "id=?", selectionArgs) > 0);
    }

    protected static <T> T getJsonFirst(String tableName, String[] columns, String selection, String[] selectionArgs, Class<T> type) {
        Cursor cursor = MainApplication.getDatabase().query(tableName, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                return APIUtils.sGlobalParser.fromJson(cursor.getString(0), type);
            } catch (Exception e) {
                Logger.e(e, "Error while retrieving detail for json item select: " + selectionArgs[0]);
            } finally {
                cursor.close();
            }
        }
        return null;
    }
    protected static <T> T[] getJsonAll(String tableName, String[] columns, String selection, String[] selectionArgs, Class<T> type) {
        Cursor cursor = MainApplication.getDatabase().query(tableName, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressWarnings("unchecked")
            T[] ret = (T[]) Array.newInstance(type, cursor.getCount());
            int i = 0;
            while (!cursor.isAfterLast()) {
                try {
                    ret[i] = APIUtils.sGlobalParser.fromJson(cursor.getString(0), type);
                } catch (Exception e) {
                    Logger.e(e, "Error while retrieving detail for json items select: " + selectionArgs[0]);
                }
                i++;
                cursor.moveToNext();
            }
            cursor.close();
            return ret;
        }
        return null;
    }
    protected static <T> boolean saveJsonObject(String tableName, String column, int identifier, T item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column, APIUtils.sGlobalParser.toJson(item));
        return (MainApplication.getDatabase().update(tableName, contentValues, "id=?", new String[]{Integer.toString(identifier)}) > 0);
    }

    protected static void drop(String tableName) {
        MainApplication.getDatabase().execSQL(QUERY_DROP_TABLE + tableName);
    }
}
