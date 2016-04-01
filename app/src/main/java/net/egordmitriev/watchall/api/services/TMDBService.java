package net.egordmitriev.watchall.api.services;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by EgorDm on 4/1/2016.
 */
public interface TMDBService {

    //SEARCH METHODS
    @GET("/search/multi")
    JsonObject searchAll(@Query("query") String query, @Query("page") int page);

    @GET("/search/keyword")
    JsonObject searchKeywords(@Query("query") String query, @Query("page") int page);

    //PERSON METHODS
    @GET("/person/{id}")
    JsonObject getPerson(@Path("id") int personId, @Query("append_to_response") String appendToRes);

    //MOVIE METHODS
    @GET("/movie/{id}")
    JsonObject getMovie(@Path("id") int movieId, @Query("append_to_response") String appendToRes);

    @GET("/discover/movie")
    JsonObject getMovieDiscover(@QueryMap Map<String, String> options);

    @GET("/movie/now_playing")
    JsonObject getMovieNewest(@Query("page") int page);

    @GET("/movie/popular")
    JsonObject getMoviePopular(@Query("page") int page);

    @GET("/movie/upcoming")
    JsonObject getMovieUpcoming(@Query("page") int page);

    @GET("/movie/top_rated")
    JsonObject getMovieToprated(@Query("page") int page);

    //SERIE METHODS
    @GET("/tv/{id}")
    JsonObject getSerie(@Path("id") int serieId, @Query("append_to_response") String appendToRes);

    @GET("/tv/{id}/season/{season_number}")
    JsonObject getSeason(@Path("id") int serieId, @Path("season_number") int seasonNumber);

    @GET("/discover/tv")
    JsonObject getSerieDiscover(@QueryMap Map<String, String> options);

    @GET("/tv/on_the_air")
    JsonObject getSerieOnAir(@Query("page") int page);

    @GET("/tv/airing_today")
    JsonObject getSerieAiringToday(@Query("page") int page);

    @GET("/tv/top_rated")
    JsonObject getSerieToprated(@Query("page") int page);

    @GET("/tv/popular")
    JsonObject getSeriePopular(@Query("page") int page);
}
