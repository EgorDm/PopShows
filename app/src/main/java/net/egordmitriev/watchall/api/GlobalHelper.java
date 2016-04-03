package net.egordmitriev.watchall.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.MainApplication;
import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.api.database.tables.AnimesTable;
import net.egordmitriev.watchall.api.database.tables.MoviesTable;
import net.egordmitriev.watchall.api.database.tables.SearchSuggestTable;
import net.egordmitriev.watchall.api.database.tables.SeriesTable;
import net.egordmitriev.watchall.pojo.BaseModel;
import net.egordmitriev.watchall.utils.APIUtils;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class GlobalHelper {

    private static int sNetworkState;

    public static int updateNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                MainApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        int ret = 0;
        if (activeNetworkInfo != null) {
            ret = (activeNetworkInfo.isConnected() && activeNetworkInfo.getType() != ConnectivityManager.TYPE_MOBILE)
                    ? APIUtils.NETWORK_STATE_WIFI : APIUtils.NETWORK_STATE_MOBILE;
        }
        sNetworkState = ret;
        return ret;
    }

    public static int getNetworkState() {
        return sNetworkState;
    }

    public void clearListCache() {
        MoviesTable.drop();
        SeriesTable.drop();
        AnimesTable.drop();
        Toast.makeText(MainApplication.getAppContext(), R.string.toast_list_cache_cleared, Toast.LENGTH_LONG).show();
    }

    public void clearSearchHistory() {
        SearchSuggestTable.drop();
        Toast.makeText(MainApplication.getAppContext(), R.string.toast_history_cleared, Toast.LENGTH_LONG).show();
    }

    public static void shareObject(BaseModel model) {
        Logger.d("Sharing " + model.id);
        //TODO share it across the world
    }
}
