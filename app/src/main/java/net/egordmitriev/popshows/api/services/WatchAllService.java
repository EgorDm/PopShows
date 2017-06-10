package net.egordmitriev.popshows.api.services;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by EgorDm on 4/1/2016.
 */
public interface WatchAllService {

    @FormUrlEncoded
    @POST("users/token")
    Call<JsonObject> getToken(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("users/register")
    Call<JsonObject> register(@Field("fullname") String username, @Field("password") String password, @Field("email") String email);

    @GET("users/profile")
    Call<JsonObject> getProfile();

    @POST("users/edit/{id}")
    Call<JsonObject> editProfile(@Path("id") int userID, @Body JsonObject body);

    @Multipart
    @POST("users/edit/{id}")
    Call<JsonObject> editAvatar(@Path("id") int userId, @Part("avatar") MultipartBody.Part file);

    @Multipart
    @POST("users/edit/{id}")
    Call<JsonObject> editBackdrop(@Path("id") int userId, @Part("backdrop") MultipartBody.Part file);

    @GET("users/view/{id}")
    Call<JsonObject> getUser(@Path("id") int userID);


    @GET("activities/index")
    Call<JsonObject> getActivities(@QueryMap Map<String, String> options);

    @GET("reviews/index")
    Call<JsonObject> getReviews(@QueryMap Map<String, String> options);

    @POST("reviews/add")
    Call<JsonObject> addReview(@Body JsonObject review);

    @POST("reviews/edit/{id}")
    Call<JsonObject> editReview(@Path("id") int reviewID, @Body JsonObject review);

    @POST("reviews/delete/{id}")
    Call<JsonObject> deleteReview(@Path("id") int reviewID);

    @POST("watchlists/synchronize")
    Call<JsonObject> syncWatchlist(@Body JsonObject[] watchlists);

    @GET("watchlists/index")
    Call<JsonObject> getWatchlists(@Query("user") int userID, @Query("title") String query);

    @GET("watchlists/view/{id}")
    Call<JsonObject> viewWatchlist(@Path("id") int listID);

    @POST("watchlists/add")
    Call<JsonObject> addWatchlist(@Body JsonObject watchlists);

    @POST("watchlists/edit/{id}")
    Call<JsonObject> editWatchlist(@Path("id") int listID, @Body JsonObject watchlists);

    @POST("watchlists/delete/{id}")
    Call<JsonObject> deleteWatchlist(@Path("id") int listID);

}
