package net.egordmitriev.watchall.ui.activities.base;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import net.egordmitriev.watchall.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EgorDm on 4/29/2016.
 */
public class TabsActivity extends BaseActivity {

    protected Adapter mAdapter;
    protected ViewPager mViewPager;

    @SuppressWarnings("ConstantConditions")
    protected void initTabs() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        if (mViewPager != null) {
            mAdapter = new Adapter(getSupportFragmentManager());
            mAdapter = setupTabs(mAdapter);
            mViewPager.setAdapter(mAdapter);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (mViewPager != null) {
            tabLayout.setupWithViewPager(mViewPager);
        }

    }

    protected Adapter setupTabs(Adapter adapter) {
        return adapter;
    }

    public static class Adapter extends FragmentStatePagerAdapter {
        private final List<FragmentTab> mTabs = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(FragmentTab fragment) {
            mTabs.add(fragment);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return mTabs.get(position).fragment;
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).title;
        }

        public static class FragmentTab {
            public final Fragment fragment;
            public final String title;

            public FragmentTab(Fragment fragment, String title) {
                this.fragment = fragment;
                this.title = title;
            }
        }
    }
}