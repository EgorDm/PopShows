package net.egordmitriev.watchall.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by EgorDm on 4/1/2016.
 */
public class ServiceHelperBase {

    public static <S> S createService(Class<S> serviceClass, Retrofit.Builder builder, OkHttpClient.Builder httpClient) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }


}
