package net.egordmitriev.watchall.api.database.tables;

import android.content.ContentValues;
import android.database.Cursor;

import net.egordmitriev.watchall.MainApplication;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class BaseTable {
    protected static final String COLUMN_ID = "id";
    protected static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";

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

}
