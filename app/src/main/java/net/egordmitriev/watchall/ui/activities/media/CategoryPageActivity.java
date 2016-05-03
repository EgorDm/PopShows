package net.egordmitriev.watchall.ui.activities.media;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.pojo.anilist.AnimeModel;
import net.egordmitriev.watchall.pojo.data.Category;
import net.egordmitriev.watchall.pojo.tmdb.MovieModel;
import net.egordmitriev.watchall.pojo.tmdb.SerieModel;
import net.egordmitriev.watchall.ui.activities.base.AMediaPageActivity;
import net.egordmitriev.watchall.ui.fragments.media.AnimeListFragment;
import net.egordmitriev.watchall.ui.fragments.media.MovieListFragment;
import net.egordmitriev.watchall.ui.fragments.media.SerieListFragment;
import net.egordmitriev.watchall.utils.MediaUtils;
import net.egordmitriev.watchall.utils.SaveUtils;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class CategoryPageActivity extends AMediaPageActivity {

    private static final int[] ACTIONS_MOVIE = {
            MediaUtils.ACTION_POPULAR,
            MediaUtils.ACTION_TOPRATED,
            MediaUtils.ACTION_NEWEST};

    //TODO: Airing this week (airdate.lte)... http://docs.themoviedb.apiary.io/#reference/discover/discovertv
    private static final int[] ACTIONS_SERIE = {
            MediaUtils.ACTION_POPULAR,
            MediaUtils.ACTION_TOPRATED};

    private static final int[] ACTIONS_ANIME = {
            MediaUtils.ACTION_POPULAR,
            MediaUtils.ACTION_TOPRATED,
            MediaUtils.ACTION_TOPAIRING,
            MediaUtils.ACTION_JUSTADDED,
            MediaUtils.ACTION_UPCOMING};

    private Category mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCategory = getIntent().getParcelableExtra(SaveUtils.SAVED_DISPLAY_DATA);
        setTitle(getPageTitle());
        super.onCreate(savedInstanceState);
    }

    private String getPageTitle() {
        StringBuilder ret = new StringBuilder();
        switch (mCategory.type) {
            case MovieModel.TYPE:
                ret.append("Movies: ");
                break;
            case SerieModel.TYPE:
                ret.append("Series: ");
                break;
            default:
                ret.append("Anime: ");
                break;
        }
        ret.append(mCategory.title);
        return ret.toString();
    }

    @Override
    protected void createTabs(Adapter adapter) {
        String[] titles;
        int[] actions;
        switch (mCategory.type) {
            case MovieModel.TYPE:
                titles = getResources().getStringArray(R.array.category_movie_tabs);
                actions = ACTIONS_MOVIE;
                break;
            case SerieModel.TYPE:
                titles = getResources().getStringArray(R.array.category_serie_tabs);
                actions = ACTIONS_SERIE;
                break;
            default:
                titles = getResources().getStringArray(R.array.category_anime_tabs);
                actions = ACTIONS_ANIME;
                break;
        }
        createTabs(adapter, titles, actions, mCategory.type, mCategory);
    }

    @Override
    protected Fragment createFragment() {
        switch (mCategory.type) {
            case MovieModel.TYPE:
                return new MovieListFragment();
            case SerieModel.TYPE:
                return new SerieListFragment();
            default:
                return new AnimeListFragment();
        }
    }

    @Override
    protected int getStarterTab() {
        return 0;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_tabs;
    }

    @Override
    protected int getMediaSearchType() {
        return (mCategory.type == AnimeModel.TYPE) ? AnimeModel.TYPE : MovieModel.TYPE;
    }
}
