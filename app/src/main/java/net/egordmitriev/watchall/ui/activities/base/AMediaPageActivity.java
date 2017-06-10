package net.egordmitriev.watchall.ui.activities.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.orhanobut.logger.Logger;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.pojo.data.Category;
import net.egordmitriev.watchall.pojo.tmdb.MovieModel;
import net.egordmitriev.watchall.pojo.user.ListRequestData;
import net.egordmitriev.watchall.ui.activities.SearchActivity;
import net.egordmitriev.watchall.ui.fragments.media.CategoryListFragment;
import net.egordmitriev.watchall.utils.SaveUtils;

/**
 * Created by EgorDm on 5/1/2016.
 */
public abstract class AMediaPageActivity extends HomeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initToolbar();
        initTabs();
        mViewPager.setCurrentItem(getStarterTab(), false);
        //mTabLayout.getTabAt(getStarterTab()).select();
        //mTabLayout.setScrollPosition(2,0f,true);
        Logger.d("Select"+getStarterTab());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Intent intent = new Intent(this, SearchActivity.class);
                SearchActivity.sSearchType = getMediaSearchType();
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Adapter setupTabs(Adapter adapter) {
        createCategoryTab(adapter);
        createTabs(adapter);

        return super.setupTabs(adapter);
    }

    protected Adapter.FragmentTab createCategoryTab(int type) {
        CategoryListFragment fragment = new CategoryListFragment();
        fragment.categoryType = type;
        return new Adapter.FragmentTab(fragment, "Categories");
    }

    protected void createCategoryTab(Adapter adapter) {
    }

    ;

    protected void createTabs(Adapter adapter, String[] titles, int[] actions, int type, Category category) {
        for (int i = 0; i < titles.length; i++) {
            ListRequestData data;
            if (category != null) {
                data = new ListRequestData(actions[i], type, category);
            } else {
                data = new ListRequestData(actions[i], type);
            }
            Fragment fragment = createFragment();
            Bundle args = new Bundle();
            args.putParcelable(SaveUtils.SAVED_DISPLAY_DATA, data);
            fragment.setArguments(args);
            adapter.addFragment(new Adapter.FragmentTab(fragment, titles[i]));
        }
    }

    protected abstract void createTabs(Adapter adapter);

    protected abstract Fragment createFragment();

    protected abstract int getStarterTab();

    protected int getLayout() {
        return R.layout.activity_drawer_tabs;
    }

    protected int getMediaSearchType() {
        return MovieModel.TYPE;
    }
}
