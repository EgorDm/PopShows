package net.egordmitriev.watchall.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.api.PreferencesHelper;
import net.egordmitriev.watchall.pojo.anilist.AnimeModel;
import net.egordmitriev.watchall.pojo.tmdb.MovieModel;
import net.egordmitriev.watchall.pojo.tmdb.SerieModel;
import net.egordmitriev.watchall.ui.activities.base.HomeActivity;
import net.egordmitriev.watchall.ui.activities.media.AnimePageActivity;
import net.egordmitriev.watchall.ui.activities.media.MoviePageActivity;
import net.egordmitriev.watchall.ui.activities.media.SeriePageActivity;
import net.egordmitriev.watchall.utils.Ads;

public class MainActivity extends HomeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ads.Initialize(this);

        String result = PreferencesHelper.getInstance().getString(R.string.pref_discovery_homepage);
        int homepage = (result != null) ? Integer.parseInt(result) : 1;
        Intent intent;
        switch (homepage) {
            case MovieModel.TYPE:
                intent = new Intent(this, MoviePageActivity.class);
                break;
            case SerieModel.TYPE:
                intent = new Intent(this, SeriePageActivity.class);
                break;
            case AnimeModel.TYPE:
                intent = new Intent(this, AnimePageActivity.class);
                break;
            default:
                intent = new Intent(this, MoviePageActivity.class);
                break;
        }
        startActivity(intent);

    }
}
