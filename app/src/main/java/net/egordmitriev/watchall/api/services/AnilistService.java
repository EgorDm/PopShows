package net.egordmitriev.watchall.api.services;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by EgorDm on 4/1/2016.
 */
public interface AnilistService {

    @FormUrlEncoded
    @POST("/auth/access_token")
    JsonObject requestAccesToken(@Field("grant_type") String grant_type, @Field("client_id") String client_id, @Field("client_secret") String client_secret);

    @GET("/anime/{id}/page")
    JsonObject getAnime(@Query("access_token") String access_token, @Path("id") int animeId);

    @GET("/browse/anime")
    JsonObject getAnimeDiscover(@Query("access_token") String access_token, @QueryMap Map<String, String> options);

    @GET("/anime/review/{review_id}")
    JsonObject getReview(@Query("access_token") String access_token, @Path("review_id") int reviewId);

    @GET("/anime/{anime_id}/reviews")
    JsonObject getAnimeReviews(@Query("access_token") String access_token, @Path("anime_id") int animeId);

    @GET("/character/{id}")
    JsonObject getCharacter(@Query("access_token") String access_token, @Path("id") int characterId);

    @GET("/staff/{id}")
    JsonObject getStaff(@Query("access_token") String access_token, @Path("id") int staffId);

    @GET("/anime/search/{query}")
    JsonObject searchAnime(@Path("query") String query, @Query("access_token") String access_token, @Query("page") int page);

}
