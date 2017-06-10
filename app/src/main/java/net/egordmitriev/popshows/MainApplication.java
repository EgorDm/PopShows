package net.egordmitriev.popshows;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.egordmitriev.popshows.api.AnilistServiceHelper;
import net.egordmitriev.popshows.api.database.DatabaseHelper;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class MainApplication extends Application {

    private static MainApplication sInstance;
    private static DatabaseHelper sDatabaseHelper;

    /* Future plans
    TODO: change database lists layout from current all media in list data to add media in own table and a many-to-many relationship. Like my db
     */

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sDatabaseHelper = new DatabaseHelper(getApplicationContext());
        sDatabaseHelper.createTables();
        AnilistServiceHelper.setup();
    }

    public static MainApplication getInstance() {
        return sInstance;
    }

    public static SQLiteDatabase getDatabase() {
        return sDatabaseHelper.writableDB;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
