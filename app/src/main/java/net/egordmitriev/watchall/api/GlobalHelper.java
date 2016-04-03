package net.egordmitriev.watchall.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.egordmitriev.watchall.MainApplication;
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
}
