package net.egordmitriev.watchall.ui.activities.media;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.pojo.anilist.AnimeModel;
import net.egordmitriev.watchall.ui.activities.base.AMediaPageActivity;
import net.egordmitriev.watchall.ui.fragments.media.AnimeListFragment;
import net.egordmitriev.watchall.utils.MediaUtils;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class AnimePageActivity extends AMediaPageActivity {

    private static final int[] ACTIONS = {
            MediaUtils.ACTION_TOPAIRING,
            MediaUtils.ACTION_JUSTADDED,
            MediaUtils.ACTION_POPULAR,
            MediaUtils.ACTION_TOPRATED,
            MediaUtils.ACTION_UPCOMING};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawer();
    }

    @Override
    protected void createTabs(Adapter adapter) {
        String[] titles = getResources().getStringArray(R.array.anime_tabs);
        createTabs(adapter, titles, ACTIONS, AnimeModel.TYPE, null);
    }

    @Override
    protected void createCategoryTab(Adapter adapter) {
        adapter.addFragment(createCategoryTab(AnimeModel.TYPE));
        super.createCategoryTab(adapter);
    }

    @Override
    protected Fragment createFragment() {
        return new AnimeListFragment();
    }

    @Override
    protected int getStarterTab() {
        return 1;
    }

    @Override
    public int getOwnID() {
        return PAGE_ANIME;
    }

    @Override
    protected int getMediaSearchType() {
        return AnimeModel.TYPE;
    }
}
