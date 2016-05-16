package net.egordmitriev.watchall.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.widget.ImageView;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.pojo.watchall.ReviewModel;
import net.egordmitriev.watchall.pojo.watchall.UserModel;
import net.egordmitriev.watchall.ui.activities.base.TabsActivity;
import net.egordmitriev.watchall.ui.fragments.ProfileAboutFragment;
import net.egordmitriev.watchall.ui.fragments.media.DefaultMediaListFragment;
import net.egordmitriev.watchall.ui.fragments.media.WatchlistListFragment;
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

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initToolbar();
        initTabs();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mProfileImage = (ImageView) findViewById(R.id.profile_avatar_image);

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
    }

    @Override
    protected Adapter setupTabs(Adapter adapter) {
        adapter.addFragment(new Adapter.FragmentTab(new ProfileAboutFragment(), "About"));
        adapter.addFragment(new Adapter.FragmentTab(new WatchlistListFragment(), "Lists"));

        DefaultMediaListFragment reviewFragment = new DefaultMediaListFragment();
        Bundle args = new Bundle();
        ArrayList<ReviewModel> models = new ArrayList<>();
        models.add( new ReviewModel(null, new Date(), "Was a very fun movie",
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
