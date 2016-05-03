package net.egordmitriev.watchall.ui.activities.base;

import android.content.Intent;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.ui.activities.SettingsActivity;
import net.egordmitriev.watchall.ui.activities.media.AnimePageActivity;
import net.egordmitriev.watchall.ui.activities.media.MoviePageActivity;
import net.egordmitriev.watchall.ui.activities.media.SeriePageActivity;

/**
 * Created by EgorDm on 4/29/2016.
 */
public class HomeActivity extends TabsActivity {
    protected static final int PAGE_MOVIES = R.id.nav_movies;
    protected static final int PAGE_SERIES = R.id.nav_series;
    protected static final int PAGE_ANIME = R.id.nav_anime;
    protected static final int PAGE_LISTS = R.id.nav_lists;
    protected static final int PAGE_FAVOURITES = R.id.nav_favourites;
    protected static final int PAGE_FRIENDS = R.id.nav_friends;
    protected static final int PAGE_PROFILE = R.id.nav_profile;
    protected static final int PAGE_SETTINGS = R.id.nav_settings;
    protected static final int PAGE_SEARCH = 9;

    @Override
    public void openNavTab(final int itemId) {
        Intent intent = null;
        switch (itemId) {
            case PAGE_MOVIES:
                intent = new Intent(this, MoviePageActivity.class);
                break;
            case PAGE_SERIES:
                intent = new Intent(this, SeriePageActivity.class);
                break;
            case PAGE_ANIME:
                intent = new Intent(this, AnimePageActivity.class);
                break;
            case PAGE_LISTS:
                //intent = new Intent(this, WatchlistPageActivity.class);
                break;
            case PAGE_FAVOURITES:
                //intent = new Intent(this, FavouritesActivity.class);
                break;
            case PAGE_FRIENDS:
                //intent = new Intent(this, TestMainActivity.class);
                break;
            case PAGE_PROFILE:
               // intent = new Intent(this, UserProfileActivity.class);
                break;
            case PAGE_SETTINGS:
                intent = new Intent(this, SettingsActivity.class);
                break;
        }

        if(intent != null) {
            createBackStack(intent);
        }
    }
}