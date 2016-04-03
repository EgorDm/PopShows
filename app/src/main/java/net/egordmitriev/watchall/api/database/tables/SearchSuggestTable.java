package net.egordmitriev.watchall.api.database.tables;

import android.content.ContentValues;
import android.database.Cursor;

import net.egordmitriev.watchall.MainApplication;
import net.egordmitriev.watchall.pojo.user.SearchRecord;

import java.util.Date;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class SearchSuggestTable extends BaseTable {
    private static final String sTableName = "searchrecords";
    private static boolean sCreated = false;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_QUERY = "query";
    private static final String COLUMN_DATE = "date";

    private static final String QUERY_COLUMNS = " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_QUERY + " VARCHAR(255) UNIQUE," +
            COLUMN_DATE + " DATETIME)";

    public static boolean createTable() {
        if (sCreated) return false;
        createTable(sTableName, QUERY_COLUMNS);
        return true;
    }

    public static boolean upsert(SearchRecord record) {
        ContentValues insertValues = new ContentValues();
        insertValues.put(COLUMN_QUERY, record.query);
        insertValues.put(COLUMN_DATE, new Date().getTime());
        return upsert(sTableName, insertValues, COLUMN_QUERY + "=?", new String[]{record.query});
    }

    public static Cursor getAllForQuery(String query) {
        return MainApplication.getDatabase().query(sTableName, new String[]{COLUMN_QUERY, COLUMN_DATE},
                COLUMN_QUERY + " LIKE '%" + query + "%' COLLATE NOCASE", null, null, null, COLUMN_DATE + " DESC");
    }

    public static Cursor getAllForQuery() {
        return MainApplication.getDatabase().query(sTableName, new String[]{COLUMN_QUERY, COLUMN_DATE},
                null, null, null, null, COLUMN_DATE + " DESC");
    }
}
