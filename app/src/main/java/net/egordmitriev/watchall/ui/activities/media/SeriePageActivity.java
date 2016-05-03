package net.egordmitriev.watchall.ui.activities.media;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.pojo.tmdb.SerieModel;
import net.egordmitriev.watchall.ui.activities.base.AMediaPageActivity;
import net.egordmitriev.watchall.ui.fragments.media.SerieListFragment;
import net.egordmitriev.watchall.utils.MediaUtils;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class SeriePageActivity extends AMediaPageActivity {

    private static final int[] ACTIONS = {
            MediaUtils.ACTION_POPULAR,
            MediaUtils.ACTION_AIRINGTODAY,
            MediaUtils.ACTION_ONTHEAIR,
            MediaUtils.ACTION_TOPRATED};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawer();
    }

    @Override
    protected void createTabs(Adapter adapter) {
        String[] titles = getResources().getStringArray(R.array.serie_tabs);
        createTabs(adapter, titles, ACTIONS, SerieModel.TYPE, null);
    }

    @Override
    protected void createCategoryTab(Adapter adapter) {
        adapter.addFragment(createCategoryTab(SerieModel.TYPE));
        super.createCategoryTab(adapter);
    }

    @Override
    protected Fragment createFragment() {
        return new SerieListFragment();
    }

    @Override
    protected int getStarterTab() {
        return 1;
    }

    @Override
    public int getOwnID() {
        return PAGE_SERIES;
    }
}
