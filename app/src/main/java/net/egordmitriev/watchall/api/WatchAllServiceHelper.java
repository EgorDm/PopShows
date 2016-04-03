package net.egordmitriev.watchall.api;

import net.egordmitriev.watchall.api.base.ServiceHelperBase;
import net.egordmitriev.watchall.api.services.WatchAllService;
import net.egordmitriev.watchall.utils.APIUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by EgorDm on 4/1/2016.
 */
public class WatchAllServiceHelper extends ServiceHelperBase {

    private static final String API_BASE_URL = "http://watchit.egordmitriev.net/api/";
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(APIUtils.getDebugInterceptor());
    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(APIUtils.sGlobalParser));


    public static final WatchAllService sService = createService(WatchAllService.class, builder, httpClient);

    public WatchAllServiceHelper() {
    }
}
