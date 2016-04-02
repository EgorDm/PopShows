package net.egordmitriev.watchall;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import net.egordmitriev.watchall.api.database.DatabaseHelper;

/**
 * Created by EgorDm on 4/2/2016.
 */
public class MainApplication extends Application {

    private static MainApplication sInstance;
    private static DatabaseHelper sDatabaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        sDatabaseHelper = new DatabaseHelper(getApplicationContext());
    }

    public static MainApplication getInstance() {
        return sInstance;
    }

    public static SQLiteDatabase getDatabase() {
        return sDatabaseHelper.writeableDB;
    }
}
