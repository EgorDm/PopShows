package net.egordmitriev.watchall.utils;

import android.content.Context;

import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.pojo.anilist.AnimeModel;
import net.egordmitriev.watchall.pojo.data.Category;
import net.egordmitriev.watchall.pojo.tmdb.MovieModel;
import net.egordmitriev.watchall.pojo.tmdb.SerieModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class MediaUtils {

    public static final int ACTION_POPULAR = 0;
    public static final int ACTION_NEWEST = 1;
    public static final int ACTION_TOPRATED = 2;
    public static final int ACTION_UPCOMING = 3;
    public static final int ACTION_ONTHEAIR = 4;
    public static final int ACTION_AIRINGTODAY = 5;
    public static final int ACTION_TOPAIRING = 6;
    public static final int ACTION_JUSTADDED = 7;

    public static final String JOB_PRODUCER = "Producer";
    public static final String JOB_DIRECTOR = "Director";
    public static final String JOB_EDITOR = "Editor";

    public static String getGenreName(Context context, int genreId) {
        switch (genreId) {
            case Category.GENRE_MOVIE_ACTION:
                return context.getResources().getString(R.string.genre_movie_action);
            case Category.GENRE_MOVIE_ADVENTURE:
                return context.getResources().getString(R.string.genre_movie_adventure);
            case Category.GENRE_MOVIE_ANIMATION:
                return context.getResources().getString(R.string.genre_movie_animation);
            case Category.GENRE_MOVIE_COMEDY:
                return context.getResources().getString(R.string.genre_movie_comedy);
            case Category.GENRE_MOVIE_CRIME:
                return context.getResources().getString(R.string.genre_movie_crime);
            case Category.GENRE_MOVIE_DOCUMENTARY:
                return context.getResources().getString(R.string.genre_movie_documentary);
            case Category.GENRE_MOVIE_DRAMA:
                return context.getResources().getString(R.string.genre_movie_drama);
            case Category.GENRE_MOVIE_FAMILY:
                return context.getResources().getString(R.string.genre_movie_family);
            case Category.GENRE_MOVIE_FANTASY:
                return context.getResources().getString(R.string.genre_movie_fantasy);
            case Category.GENRE_MOVIE_FOREIGN:
                return context.getResources().getString(R.string.genre_movie_foreign);
            case Category.GENRE_MOVIE_HISTORY:
                return context.getResources().getString(R.string.genre_movie_history);
            case Category.GENRE_MOVIE_HORROR:
                return context.getResources().getString(R.string.genre_movie_horror);
            case Category.GENRE_MOVIE_MUSIC:
                return context.getResources().getString(R.string.genre_movie_music);
            case Category.GENRE_MOVIE_MYSTERY:
                return context.getResources().getString(R.string.genre_movie_mystery);
            case Category.GENRE_MOVIE_ROMANCE:
                return context.getResources().getString(R.string.genre_movie_romance);
            case Category.GENRE_MOVIE_SCIFI:
                return context.getResources().getString(R.string.genre_movie_scifi);
            case Category.GENRE_MOVIE_TVMOVIE:
                return context.getResources().getString(R.string.genre_movie_tvmovie);
            case Category.GENRE_MOVIE_THRILLER:
                return context.getResources().getString(R.string.genre_movie_thriller);
            case Category.GENRE_MOVIE_WAR:
                return context.getResources().getString(R.string.genre_movie_war);
            case Category.GENRE_MOVIE_WESTERN:
                return context.getResources().getString(R.string.genre_movie_western);
            case Category.GENRE_SERIE_ACTIONADVENTURE:
                return context.getResources().getString(R.string.genre_serie_actionadventure);
            case Category.GENRE_SERIE_EDUCATION:
                return context.getResources().getString(R.string.genre_serie_education);
            case Category.GENRE_SERIE_KIDS:
                return context.getResources().getString(R.string.genre_serie_kids);
            case Category.GENRE_SERIE_NEWS:
                return context.getResources().getString(R.string.genre_serie_news);
            case Category.GENRE_SERIE_REALITY:
                return context.getResources().getString(R.string.genre_serie_reality);
            case Category.GENRE_SERIE_SCIFIFANTASY:
                return context.getResources().getString(R.string.genre_serie_scififantasy);
            case Category.GENRE_SERIE_SOAP:
                return context.getResources().getString(R.string.genre_serie_soap);
            case Category.GENRE_SERIE_TALK:
                return context.getResources().getString(R.string.genre_serie_talk);
            case Category.GENRE_SERIE_WARPOLITICS:
                return context.getResources().getString(R.string.genre_serie_warpolitics);
        }
        return "";
    }

    public static int getGenreIconId(int genreId) {
        switch (genreId) {
            case Category.GENRE_MOVIE_ACTION:
                return R.drawable.ic_genre_action;
            case Category.GENRE_MOVIE_ADVENTURE:
                return R.drawable.ic_genre_adventure;
            case Category.GENRE_MOVIE_ANIMATION:
                return R.drawable.ic_genre_animation;
            case Category.GENRE_MOVIE_COMEDY:
                return R.drawable.ic_genre_comedy;
            case Category.GENRE_MOVIE_CRIME:
                return R.drawable.ic_genre_crime;
            case Category.GENRE_MOVIE_DOCUMENTARY:
                return R.drawable.ic_genre_documentary;
            case Category.GENRE_MOVIE_DRAMA:
                return R.drawable.ic_genre_drama;
            case Category.GENRE_MOVIE_FAMILY:
                return R.drawable.ic_genre_family;
            case Category.GENRE_MOVIE_FANTASY:
                return R.drawable.ic_genre_fantasy;
            case Category.GENRE_MOVIE_FOREIGN:
                return R.drawable.ic_genre_foreign;
            case Category.GENRE_MOVIE_HISTORY:
                return R.drawable.ic_genre_historical;
            case Category.GENRE_MOVIE_HORROR:
                return R.drawable.ic_genre_horror;
            case Category.GENRE_MOVIE_MUSIC:
                return R.drawable.ic_genre_musical;
            case Category.GENRE_MOVIE_MYSTERY:
                return R.drawable.ic_genre_mystery;
            case Category.GENRE_MOVIE_ROMANCE:
                return R.drawable.ic_genre_romance;
            case Category.GENRE_MOVIE_SCIFI:
                return R.drawable.ic_genre_scifi;
            case Category.GENRE_MOVIE_TVMOVIE:
                return R.drawable.ic_genre_tvmovie;
            case Category.GENRE_MOVIE_THRILLER:
                return R.drawable.ic_genre_thriller;
            case Category.GENRE_MOVIE_WAR:
                return R.drawable.ic_genre_war;
            case Category.GENRE_MOVIE_WESTERN:
                return R.drawable.ic_genre_western;
            case Category.GENRE_SERIE_ACTIONADVENTURE:
                return R.drawable.ic_genre_action;
            case Category.GENRE_SERIE_EDUCATION:
                return R.drawable.ic_genre_education;
            case Category.GENRE_SERIE_KIDS:
                return R.drawable.ic_genre_kids;
            case Category.GENRE_SERIE_NEWS:
                return R.drawable.ic_genre_news;
            case Category.GENRE_SERIE_REALITY:
                return R.drawable.ic_genre_reality;
            case Category.GENRE_SERIE_SCIFIFANTASY:
                return R.drawable.ic_genre_scifi;
            case Category.GENRE_SERIE_SOAP:
                return R.drawable.ic_genre_soap;
            case Category.GENRE_SERIE_TALK:
                return R.drawable.ic_genre_talk;
            case Category.GENRE_SERIE_WARPOLITICS:
                return R.drawable.ic_genre_war;
        }
        return R.drawable.ic_genre_action;
    }

    public static ArrayList<Category> getCategoryList(Context context, int mediaType) {
        String[] categoryNames;
        int[] categoryIds = new int[0];
        ArrayList<Category> catList = new ArrayList<>();

        if (mediaType == MovieModel.TYPE) {
            categoryNames = getMovieGenreNameList(context);
            categoryIds = getMovieGenreIds();
        } else if (mediaType == SerieModel.TYPE) {
            categoryNames = getSerieGenreNameList(context);
            categoryIds = getSerieGenreIds();
        } else if (mediaType == AnimeModel.TYPE) {
            categoryNames = getAnimeGenreNameList(context);
        } else {
            Logger.d("Incorrect model type. No categories found!");
            return catList;
        }

        for (int i = 0; i < categoryNames.length; i++) {
            int categoryId = -1;
            if (categoryIds.length > i) {
                categoryId = categoryIds[i];
            }
            catList.add(new Category(categoryNames[i], categoryId, mediaType));
        }

        return catList;
    }

    public static Category[] getPredefinedCategoryList(Context context, int mediaType, int[] pCategoryIds) {
        Category[] catList = new Category[pCategoryIds.length];
        int index = 0;
        for (int categoryId : pCategoryIds) {
            catList[index] = new Category(getGenreName(context, categoryId), categoryId, mediaType);
            index++;
        }
        return catList;
    }

    public static Category[] getPredefinedCategoryList(int mediaType, String[] pCategoryNames) {
        Category[] catList = new Category[pCategoryNames.length];
        int index = 0;
        for (String categoryName : pCategoryNames) {
            catList[index] = new Category(categoryName, -1, mediaType);
            index++;
        }
        return catList;
    }

    public static String[] getMovieGenreNameList(Context context) {
        return context.getResources().getStringArray(R.array.genre_movie_list);
    }

    public static int[] getMovieGenreIds() {
        return new int[]{
                Category.GENRE_MOVIE_ACTION,
                Category.GENRE_MOVIE_ADVENTURE,
                Category.GENRE_MOVIE_ANIMATION,
                Category.GENRE_MOVIE_COMEDY,
                Category.GENRE_MOVIE_CRIME,
                Category.GENRE_MOVIE_DOCUMENTARY,
                Category.GENRE_MOVIE_DRAMA,
                Category.GENRE_MOVIE_FAMILY,
                Category.GENRE_MOVIE_FANTASY,
                Category.GENRE_MOVIE_FOREIGN,
                Category.GENRE_MOVIE_HISTORY,
                Category.GENRE_MOVIE_HORROR,
                Category.GENRE_MOVIE_MUSIC,
                Category.GENRE_MOVIE_MYSTERY,
                Category.GENRE_MOVIE_ROMANCE,
                Category.GENRE_MOVIE_SCIFI,
                Category.GENRE_MOVIE_TVMOVIE,
                Category.GENRE_MOVIE_THRILLER,
                Category.GENRE_MOVIE_WAR,
                Category.GENRE_MOVIE_WESTERN,
        };
    }

    public static String[] getSerieGenreNameList(Context context) {
        return context.getResources().getStringArray(R.array.genre_serie_list);
    }

    public static int[] getSerieGenreIds() {
        return new int[]{
                Category.GENRE_SERIE_ACTIONADVENTURE,
                Category.GENRE_MOVIE_ANIMATION,
                Category.GENRE_MOVIE_COMEDY,
                Category.GENRE_MOVIE_DOCUMENTARY,
                Category.GENRE_MOVIE_DRAMA,
                Category.GENRE_SERIE_EDUCATION,
                Category.GENRE_MOVIE_FAMILY,
                Category.GENRE_SERIE_KIDS,
                Category.GENRE_MOVIE_MYSTERY,
                Category.GENRE_SERIE_NEWS,
                Category.GENRE_SERIE_REALITY,
                Category.GENRE_SERIE_SCIFIFANTASY,
                Category.GENRE_SERIE_SOAP,
                Category.GENRE_SERIE_TALK,
                Category.GENRE_SERIE_WARPOLITICS,
                Category.GENRE_MOVIE_WESTERN
        };
    }

    public static String[] getAnimeGenreNameList(Context context) {
        return context.getResources().getStringArray(R.array.genre_anime_list);
    }

    public static String convertGenreString(Context context, int[] genres) {
        if (genres == null || genres.length < 1) return "";
        StringBuilder ret = new StringBuilder(getGenreName(context, genres[0]));
        int maxLength = (genres.length < 3) ? genres.length : 3;
        for (int i = 1; i < maxLength; i++) {
            String genreName = getGenreName(context, genres[i]);
            if (i == maxLength - 1 && maxLength > 1) {
                ret.append(" and ");
            } else {
                ret.append(", ");
            }
            ret.append(genreName);
        }

        return ret.toString();
    }

    public static float convertMediaVote(float vote) {
        return vote / 2f;
    }

    public static float convertAnimeVote(float vote) {
        return vote / 20f;
    }

    public static final Map<String, Integer> ANIME_RELATION_ORDER = new HashMap<String, Integer>() {
        {
            put("sequel", 0);
            put("prequel", 2);
            put("alternative", 4);
            put("side story", 6);
            put("character", 8);
            put("summary", 10);
            put("other", 12);
        }
    };
}
