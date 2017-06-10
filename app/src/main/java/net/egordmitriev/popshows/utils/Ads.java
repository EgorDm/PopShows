package net.egordmitriev.popshows.utils;

import android.content.Context;

/**
 * Created by EgorDm on 11-Jun-2017.
 */

public class Ads {

    private static final String AD_ID = "ca-app-pub-4143151420048054~2448373728";
    public static void Initialize(Context context) {

        com.google.android.gms.ads.MobileAds.initialize(context, AD_ID);
        //MobileAds.initialize(this, "YOUR_ADMOB_APP_ID");
    }
}
