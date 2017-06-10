package net.egordmitriev.popshows.api.services;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by EgorDm on 4/1/2016.
 */
public interface TMDBService {

    //SEARCH METHODS
    @GET("search/multi")
    Call<JsonObject> searchAll(@Query("query") String query, @Query("page") int page);

    @GET("search/keyword")
    Call<JsonObject> searchKeywords(@Query("query") String query, @Query("page") int page);

    //PERSON METHODS
    @GET("person/{id}")
    Call<JsonObject> getPerson(@Path("id") int personId, @Query("append_to_response") String appendToRes);

    //MOVIE METHODS
    @GET("movie/{id}")
    Call<JsonObject> getMovie(@Path("id") int movieId, @Query("append_to_response") String appendToRes);

    @GET("discover/movie")
    Call<JsonObject> getMovieDiscover(@QueryMap Map<String, String> options);

    @GET("movie/now_playing")
    Call<JsonObject> getMovieNewest(@Query("page") int page);

    @GET("movie/popular")
    Call<JsonObject> getMoviePopular(@Query("page") int page);

    @GET("movie/upcoming")
    Call<JsonObject> getMovieUpcoming(@Query("page") int page);

    @GET("movie/top_rated")
    Call<JsonObject> getMovieToprated(@Query("page") int page);

    //SERIE METHODS
    @GET("tv/{id}")
    Call<JsonObject> getSerie(@Path("id") int serieId, @Query("append_to_response") String appendToRes);

    @GET("tv/{id}/season/{season_number}")
    Call<JsonObject> getSeason(@Path("id") int serieId, @Path("season_number") int seasonNumber);

    @GET("discover/tv")
    Call<JsonObject> getSerieDiscover(@QueryMap Map<String, String> options);

    @GET("tv/on_the_air")
    Call<JsonObject> getSerieOnAir(@Query("page") int page);

    @GET("tv/airing_today")
    Call<JsonObject> getSerieAiringToday(@Query("page") int page);

    @GET("tv/top_rated")
    Call<JsonObject> getSerieToprated(@Query("page") int page);

    @GET("tv/popular")
    Call<JsonObject> getSeriePopular(@Query("page") int page);
}
