package net.egordmitriev.watchall.api.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.egordmitriev.watchall.pojo.anilist.ClientCredentials;

import java.util.Map;

import retrofit2.Call;
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
    @POST("/api/auth/access_token")
    Call<ClientCredentials> requestAccessToken(@Field("grant_type") String grant_type, @Field("client_id") String client_id, @Field("client_secret") String client_secret);

    @GET("/api/anime/{id}/page")
    Call<JsonObject> getAnime(@Path("id") int animeId);

    @GET("/api/browse/anime")
    Call<JsonArray> getAnimeDiscover(@QueryMap Map<String, String> options);

    @GET("/api/anime/review/{review_id}")
    Call<JsonObject> getReview(@Path("review_id") int reviewId);

    @GET("/api/anime/{anime_id}/reviews")
    Call<JsonObject> getAnimeReviews(@Path("anime_id") int animeId);

    @GET("/api/character/{id}")
    Call<JsonObject> getCharacter(@Path("id") int characterId);

    @GET("/api/staff/{id}")
    Call<JsonObject> getStaff(@Path("id") int staffId);

    @GET("/api/anime/search/{query}")
    Call<JsonArray> searchAnime(@Path("query") String query, @Query("page") int page);

}
