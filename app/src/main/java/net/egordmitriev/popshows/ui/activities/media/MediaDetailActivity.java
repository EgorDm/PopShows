package net.egordmitriev.popshows.ui.activities.media;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.orhanobut.logger.Logger;

import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.api.WatchAllServiceHelper;
import net.egordmitriev.popshows.api.database.tables.WatchlistsTable;
import net.egordmitriev.popshows.pojo.DetailedModel;
import net.egordmitriev.popshows.pojo.anilist.AnimeModel;
import net.egordmitriev.popshows.pojo.anilist.CharacterModel;
import net.egordmitriev.popshows.pojo.anilist.StaffModel;
import net.egordmitriev.popshows.pojo.tmdb.MovieModel;
import net.egordmitriev.popshows.ui.activities.SearchActivity;
import net.egordmitriev.popshows.ui.activities.base.BaseActivity;
import net.egordmitriev.popshows.ui.dialogs.WatchlistAddToDialog;
import net.egordmitriev.popshows.ui.fragments.base.BaseFragment;
import net.egordmitriev.popshows.ui.fragments.media.detail.DetailAboutFragment;
import net.egordmitriev.popshows.ui.fragments.media.detail.DetailMainFragment;
import net.egordmitriev.popshows.utils.APIUtils;
import net.egordmitriev.popshows.utils.SaveUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by EgorDm on 5/3/2016.
 */
public class MediaDetailActivity extends BaseActivity {

    public static void open(Context activity, DetailedModel model) {
        Intent intent = new Intent(activity, MediaDetailActivity.class);
        intent.putExtra(SaveUtils.SAVED_DISPLAY_DATA, model);
        activity.startActivity(intent);
    }

    public static final int PAGE_MAIN = 0;
    public static final int PAGE_ABOUT = 1;
    public static final int PAGE_CAST = 2;
    public static final int PAGE_SIMILAR = 3;
    public static final int PAGE_SEASONS = 4;
    public static final int PAGE_STAFF = 5;
    public static final int PAGE_CHARACTERS = 6;
    public static final int PAGE_MOVIES = 7;
    public static final int PAGE_SERIES = 8;

    private DetailedModel mModel;
    private ViewPager mViewPager;
    private DetailPagerAdapter mDetailPagerAdapter;
    private HashMap<Integer, Integer> mPages;
    private List<Fragment> mFragments;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mModel = getIntent().getParcelableExtra(SaveUtils.SAVED_DISPLAY_DATA);
        super.onCreate(savedInstanceState);
        setTitle(mModel.getTitle());

        int layoutRes = mModel.getModelView().getActivityLayout();
        if(layoutRes == R.layout.activity_media_detail_small) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark));
            }
        }

        setContentView(layoutRes);
        mModel.getModelView().setupActivity(mModel, (ViewGroup) findViewById(R.id.main_content));

        initToolbar();

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if(collapsingToolbar != null) {
            collapsingToolbar.setTitleEnabled(false);
        }


        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mDetailPagerAdapter = new DetailPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mDetailPagerAdapter);

        final AppBarLayout appBar = (AppBarLayout) findViewById(R.id.appbar);
        if(layoutRes != R.layout.activity_media_detail_small) {
            final FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.fab);
            fabMenu.setClosedOnTouchOutside(true);
            final FragmentManager manager = getSupportFragmentManager();
            final FloatingActionButton fav = (FloatingActionButton) findViewById(R.id.media_addtofav);
            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fabMenu.hideMenu(true);
                    if (WatchlistsTable.addMedia(APIUtils.sGlobalParser.toJsonTree(mModel.base).getAsJsonObject(), WatchAllServiceHelper.getFavouritesID())) {
                        Toast.makeText(getBaseContext(), String.format(getBaseContext().getString(R.string.toast_added_to_list), "Favourites"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), R.string.toast_unknown_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            final FloatingActionButton addToList = (FloatingActionButton) findViewById(R.id.media_addtolist);
            addToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fabMenu.hideMenu(true);
                    WatchlistAddToDialog dialog = WatchlistAddToDialog.newInstance(mModel);
                    dialog.show(manager, "Add to list");
                }
            });
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position != PAGE_MAIN) {
                    appBar.setExpanded(false, true);
                } else {

                    if(getMainPage().getScrollY() == 0) {
                        appBar.setExpanded(true, true);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Intent intent = new Intent(this, SearchActivity.class);
                if(mModel instanceof AnimeModel || mModel instanceof CharacterModel ||
                        mModel instanceof StaffModel) {
                    SearchActivity.sSearchType = AnimeModel.TYPE;
                } else {
                    SearchActivity.sSearchType = MovieModel.TYPE;
                }
                startActivity(intent);
                Logger.d("Search clicked");
                /**/
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public DetailedModel getModel() {
        return mModel;
    }

    public void openPage(int page) {
        mViewPager.setCurrentItem(page, true);
    }

    public int addPageFragment(BaseFragment fragment, int type) {
        if(mPages.containsKey(type)) { return mPages.get(type); }
        int ret = mFragments.size();
        mFragments.add(fragment);
        mPages.put(type, ret);
        if(mDetailPagerAdapter != null) {
            mDetailPagerAdapter.addedPageFragment();
        }
        return ret;
    }

    private DetailMainFragment getMainPage() {
        return (DetailMainFragment) mFragments.get(0);
    }

    private class DetailPagerAdapter extends FragmentPagerAdapter {

        public DetailPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>();
            mPages = new HashMap<>();

            DetailMainFragment mainFragment = new DetailMainFragment();
            mFragments.add(mainFragment);

            DetailAboutFragment aboutFragment = new DetailAboutFragment();
            mFragments.add(aboutFragment);
        }

        public void addedPageFragment() {
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
