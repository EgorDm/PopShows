package net.egordmitriev.watchall.api.database.tables;

import android.content.ContentValues;

import net.egordmitriev.watchall.pojo.watchall.WatchlistModel;

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
        if(sCreated) return false;
        createTable(sTableName, QUERY_COLUMNS);
        return true;
    }

    public static boolean upsert(WatchlistModel watchlist, int identifier) {
        Date currentDate = new Date();
        ContentValues insertValues = new ContentValues();
        if(identifier == -1) {
            identifier = ((int) lastInsertID(sTableName)) + 1;
        }

        return true;
    }

}
