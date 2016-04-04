package net.egordmitriev.watchall.api.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.egordmitriev.watchall.api.database.tables.AnimesTable;
import net.egordmitriev.watchall.api.database.tables.MoviesTable;
import net.egordmitriev.watchall.api.database.tables.SearchSuggestTable;
import net.egordmitriev.watchall.api.database.tables.SeriesTable;
import net.egordmitriev.watchall.api.database.tables.WatchlistsTable;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "watchall_database";
    private static final int DATABASE_VERSION = 1;

    public SQLiteDatabase writeableDB;
    private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        writeableDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        WatchlistsTable.createTable();
        SearchSuggestTable.createTable();
        MoviesTable.createTable();
        SeriesTable.createTable();
        AnimesTable.createTable();
        //TODO: check tables created
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
