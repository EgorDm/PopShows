package net.egordmitriev.watchall.ui.activities.base;

import android.content.Intent;
import android.os.Handler;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.adapters.WatchAllAuthenticator;
import net.egordmitriev.watchall.api.WatchAllServiceHelper;
import net.egordmitriev.watchall.api.base.APIError;
import net.egordmitriev.watchall.pojo.watchall.UserModel;
import net.egordmitriev.watchall.ui.widgets.CircleImageView;
import net.egordmitriev.watchall.utils.APIUtils;
import net.egordmitriev.watchall.utils.DataCallback;

/**
 * Created by EgorDm on 4/28/2016.
 */
@SuppressWarnings("ConstantConditions")
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;
    private MenuItem mPreviousMenuItem;
    private Handler mHandler;
    private View mHeaderView;

    protected static final int MAIN_CONTENT_FADEOUT_DURATION = 250;
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    protected void initToolbar() {
        //mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar);
        //TODO: create a view with toolbar :D

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDefaultDisplayHomeAsUpEnabled(true);
        }
    }

    protected void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        if (navigationMenuView != null) {
            navigationMenuView.setVerticalScrollBarEnabled(false);
        }

        if (getOwnID() != -1) {
            mPreviousMenuItem = navigationView.getMenu().findItem(getOwnID());
            mPreviousMenuItem.setChecked(true);
        }

        if (mToolbar != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navdrawer_open, R.string.navdrawer_close);
            mDrawerLayout.setDrawerListener(toggle);
            toggle.syncState();
        }
        mHandler = new Handler();
        mHeaderView = navigationView.getHeaderView(0);
        setupHeaderAccount();
    }

    protected void updateHeaderAccount() {
        if ((WatchAllAuthenticator.getAccount() != null && mHeaderView.findViewById(R.id.drawer_account_add) != null)
                || (WatchAllAuthenticator.getAccount() == null && mHeaderView.findViewById(R.id.drawer_account_add) == null)) {
            ((ViewGroup) mHeaderView).removeAllViews();
            setupHeaderAccount();
        } else if (WatchAllAuthenticator.getAccount() != null) {
            WatchAllServiceHelper.getMyProfile(new DataCallback<UserModel>() {
                @Override
                public void success(UserModel data) {
                    ((TextView) mHeaderView.findViewById(R.id.drawer_username)).setText(data.fullname);
                    ((TextView) mHeaderView.findViewById(R.id.drawer_email)).setText(data.email);
                    CircleImageView avatar = (CircleImageView) mHeaderView.findViewById(R.id.drawer_profile_avatar_image);
                    avatar.setBackgroundTint(data.profile_color);
                    if (data.avatar == null) {
                        ((TextView) mHeaderView.findViewById(R.id.drawer_profile_avatar_title)).setText(data.fullname);
                    } else {
                        Glide.with(BaseActivity.this).load(APIUtils.getAvatarUrl(data.id)).into(avatar);
                    }
                }

                @Override
                public void failure(APIError error) {

                }
            }, false);
        }
    }

    private void setupHeaderAccount() {
        if (WatchAllAuthenticator.getAccount() != null) {
            final View header = getLayoutInflater().inflate(R.layout.nav_profile_view, (ViewGroup) mHeaderView, true);
            WatchAllServiceHelper.getMyProfile(new DataCallback<UserModel>() {
                @Override
                public void success(UserModel data) {
                    ((TextView) mHeaderView.findViewById(R.id.drawer_username)).setText(data.fullname);
                    ((TextView) mHeaderView.findViewById(R.id.drawer_email)).setText(data.email);
                    CircleImageView avatar = (CircleImageView) mHeaderView.findViewById(R.id.drawer_profile_avatar_image);
                    avatar.setBackgroundTint(data.profile_color);
                    if (data.avatar == null) {
                        ((TextView) mHeaderView.findViewById(R.id.drawer_profile_avatar_title)).setText(data.fullname);
                    } else {
                        Glide.with(BaseActivity.this).load(APIUtils.getAvatarUrl(data.id)).into(avatar);
                    }
                }

                @Override
                public void failure(APIError error) {

                }
            }, false);
        } else {
            final View header = getLayoutInflater().inflate(R.layout.nav_profile_add, (ViewGroup) mHeaderView, true);
            View accountAddView = header.findViewById(R.id.drawer_account_add);
            accountAddView.setClickable(true);
            accountAddView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WatchAllAuthenticator.setupAccount(BaseActivity.this, new DataCallback<Boolean>() {
                        @Override
                        public void success(Boolean data) {

                        }

                        @Override
                        public void failure(APIError error) {

                        }
                    });
                }
            });
        }
    }

    public int getOwnID() {
        return -1;
    }

    public void ShowSnackbar(int textRes, int actionRes, View.OnClickListener listener) {
        Snackbar
                .make(findViewById(R.id.main_content), textRes, Snackbar.LENGTH_LONG)
                .setAction(actionRes, listener)
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        if (item == mPreviousMenuItem) {
            mDrawerLayout.closeDrawers();
            return true;
        }

        changeItemChecked(item);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openNavTab(item.getItemId());
            }
        }, NAVDRAWER_LAUNCH_DELAY);

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
        }

        mDrawerLayout.closeDrawers();
        return true;
    }

    protected void changeItemChecked(final MenuItem item) {
        item.setChecked(true);
        if (mPreviousMenuItem != null && mPreviousMenuItem != item) {
            mPreviousMenuItem.setChecked(false);
        }
        mPreviousMenuItem = item;
    }

    @Override
    protected void onResume() {
        super.onResume();
        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null && mainContent.getAlpha() == 0) {
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
        }
        if (getOwnID() != -1) {
            changeItemChecked(((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(getOwnID()));
        }
        if (mHeaderView != null) {
            updateHeaderAccount();
        }
    }

    public void openNavTab(final int itemId) {
    }

    public void createBackStack(Intent intent) {
        startActivity(intent);
        overridePendingTransition(0, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        if (getMenu() != -1) {
            mToolbar.inflateMenu(getMenu());
        }
        return true;
    }

    private volatile boolean isOnDestroyCalled = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOnDestroyCalled = true;
    }

    public boolean isOnDestroyCalled() {
        return this.isOnDestroyCalled;
    }

    protected int getMenu() {
        //TODO: add a menu with search btn
        return -1;
    }
}
