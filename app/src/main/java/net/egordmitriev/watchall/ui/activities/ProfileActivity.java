package net.egordmitriev.watchall.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import net.egordmitriev.loaderview.LoaderView;
import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.api.WatchAllServiceHelper;
import net.egordmitriev.watchall.api.base.APIError;
import net.egordmitriev.watchall.api.database.tables.WatchlistsTable;
import net.egordmitriev.watchall.pojo.watchall.ReviewModel;
import net.egordmitriev.watchall.pojo.watchall.UserModel;
import net.egordmitriev.watchall.pojo.watchall.WatchlistModel;
import net.egordmitriev.watchall.ui.activities.base.TabsActivity;
import net.egordmitriev.watchall.ui.fragments.ProfileAboutFragment;
import net.egordmitriev.watchall.ui.fragments.media.DefaultMediaListFragment;
import net.egordmitriev.watchall.ui.fragments.media.WatchlistListFragment;
import net.egordmitriev.watchall.utils.APIUtils;
import net.egordmitriev.watchall.utils.DataCallback;
import net.egordmitriev.watchall.utils.SaveUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by EgorDm on 5/15/2016.
 */
public class ProfileActivity extends TabsActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private ImageView mProfileImage;
    private int mMaxScrollSize;
    private LoaderView mLoaderView;

    private int mUserID;
    private UserModel mUserModel;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SaveUtils.SAVED_DISPLAY_DATA, mUserModel);
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getExtras() != null) {
            mUserID = getIntent().getExtras().getInt(SaveUtils.SAVED_DISPLAY_DATA_META, -1);
        } else {
           // mUserID = -1;
            mUserID = 1;
        }
        if (savedInstanceState != null) {
            mUserModel = savedInstanceState.getParcelable(SaveUtils.SAVED_DISPLAY_DATA);
        }
        setContentView(R.layout.activity_profile);
        initToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mProfileImage = (ImageView) findViewById(R.id.profile_avatar_image);

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();

        mLoaderView = (LoaderView) findViewById(R.id.loaderview);
        mLoaderView.setState(LoaderView.STATE_LOADING);
        mLoaderView.setListener(new LoaderView.LoaderViewCallback() {
            @Override
            public void onRetryClick() {
                requestData();
            }
        });
        requestData();
    }

    protected void requestData() {
        if (mUserModel == null) {
            DataCallback<UserModel> callback = new DataCallback<UserModel>() {
                @Override
                public void success(UserModel data) {
                    mUserModel = data;
                    setupProfile();
                    mLoaderView.setVisibility(View.GONE);
                }

                @Override
                public void failure(APIError error) {
                    mLoaderView.setState(LoaderView.STATE_ERROR);
                }
            };
            if(mUserID == -1) {
                WatchAllServiceHelper.getMyProfile(callback, false);

            } else {
                //TODO: server chaneg to only give public watchlists
                WatchAllServiceHelper.getUser(callback, mUserID);
            }
        } else {
            setupProfile();
        }
    }

    @SuppressWarnings("ConstantConditions")
    protected void setupProfile() {
        findViewById(R.id.main_content).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.username)).setText(mUserModel.fullname);
        ((TextView)findViewById(R.id.subtitle)).setText(mUserModel.tagline);
        if(mUserModel.avatar != null) {
            Glide.with(this).load(APIUtils.getAvatarUrl(mUserModel.id))
                    .into(mProfileImage);
        } else {
            Glide.with(this).load(R.drawable.noposter_media)
                    .into(mProfileImage);
        }
        Logger.d(APIUtils.getAvatarUrl(mUserModel.id));
        initTabs();
    }

    @Override
    protected Adapter setupTabs(Adapter adapter) {
        adapter.addFragment(new Adapter.FragmentTab(new ProfileAboutFragment(), "About"));
        WatchlistListFragment watchlistListFragment = new WatchlistListFragment();

        Bundle watchlistBundle = new Bundle();
        ArrayList<WatchlistModel> data = new ArrayList<>();

        if(mUserModel.watchlists != null) {
            for (WatchlistModel.Base model : mUserModel.watchlists) {
                data.add(new WatchlistModel(model, null));
            }
        } else if(mUserID == -1) {
            WatchlistsTable.getAllBase(null, null);//TODO: load local public lists!
        }
        watchlistBundle.putParcelableArrayList(SaveUtils.STATE_SAVED_DATA_LIST, data);
        watchlistListFragment.setArguments(watchlistBundle);
        adapter.addFragment(new Adapter.FragmentTab(watchlistListFragment, "Lists"));//TODO show lists delete loading anim

        DefaultMediaListFragment reviewFragment = new DefaultMediaListFragment();
        Bundle args = new Bundle();
        ArrayList<ReviewModel> models = new ArrayList<>();
        models.add(new ReviewModel(null, new Date(), "Was a very fun movie",
                "I liked it very much but it still kind of strange how the doctor pulled off a stunt like this. I could not beleive my eyes",
                new UserModel(12, "Egor Dmitriev", "lolol", null, -30), 3.5f));
        args.putParcelableArrayList(SaveUtils.STATE_SAVED_DATA_LIST, models);
        reviewFragment.setArguments(args);
        adapter.addFragment(new Adapter.FragmentTab(reviewFragment, "Reviews"));
        return super.setupTabs(adapter);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            mProfileImage.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }
}
