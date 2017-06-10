package net.egordmitriev.popshows.ui.activities.media;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orhanobut.logger.Logger;

import net.egordmitriev.loaderview.LoaderView;
import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.adapters.SearchAdapter;
import net.egordmitriev.popshows.api.AnilistServiceHelper;
import net.egordmitriev.popshows.api.TMDBServiceHelper;
import net.egordmitriev.popshows.api.WatchAllServiceHelper;
import net.egordmitriev.popshows.api.base.APIError;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.helpers.ASyncableMediaRecyclerHelper;
import net.egordmitriev.popshows.pojo.BaseModel;
import net.egordmitriev.popshows.pojo.DetailedModel;
import net.egordmitriev.popshows.pojo.anilist.AnimeModel;
import net.egordmitriev.popshows.pojo.tmdb.MovieModel;
import net.egordmitriev.popshows.pojo.tmdb.PersonModel;
import net.egordmitriev.popshows.pojo.tmdb.SerieModel;
import net.egordmitriev.popshows.ui.activities.SearchActivity;
import net.egordmitriev.popshows.ui.activities.base.BaseActivity;
import net.egordmitriev.popshows.utils.APIUtils;
import net.egordmitriev.popshows.utils.DataCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EgorDm on 5/7/2016.
 */
public class MediaSearchActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, AdapterView.OnItemClickListener {

    private SearchListHelper mHelper;
    private SearchView mSearchView;
    private ListView mSearchResults;
    private SearchAdapter mResultsAdapter;
    private View mScrim;
    private final Animator.AnimatorListener mScrimListenerOpen = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            mScrim.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };
    private final Animator.AnimatorListener mScrimListenerClose = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mScrim.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mHelper.onSaveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initToolbar();
        mHelper = new SearchListHelper(this);

        mHelper.setOverflowMenu(R.menu.media_menu_readonly, WatchAllServiceHelper.getMediaMenuListener(getSupportFragmentManager(), this));
        mHelper.onCreate(savedInstanceState, getIntent().getExtras());
        View view = mHelper.onCreateView(getLayoutInflater(), (ViewGroup) findViewById(R.id.mainview_container), savedInstanceState);
        ((ViewGroup) findViewById(R.id.mainview_container)).addView(view);

        mSearchResults = (ListView) findViewById(R.id.search_results);
        mResultsAdapter = new SearchAdapter(this);
        mSearchResults.setAdapter(mResultsAdapter);
        mSearchResults.setVisibility(View.VISIBLE);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mResultsAdapter.search(null);
        setupSearchView();
        mSearchView.clearFocus();

        mSearchResults.setOnItemClickListener(this);
        mScrim = findViewById(R.id.scrim);
        mScrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.clearFocus();
            }
        });
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconified(false);
        // Set the query hint.
        mSearchView.setQueryHint(getString(R.string.search_hint_movies));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchView.clearFocus();
                searchOpen(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchFor(s);
                return true;
            }
        });
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mSearchResults.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
                if (hasFocus) {
                    doEnterAnim();
                } else {
                    doExitAnim();
                }
            }
        });

        String query = getIntent().getStringExtra(SearchManager.QUERY);
        query = query == null ? "" : query;
        if (!TextUtils.isEmpty(query)) {
            mSearchView.setQuery(query, false);
        }
    }

    public void searchOpen(String query) {
        mHelper.performSearch(query, SearchActivity.sSearchType);
    }

    private void searchFor(String query) {
        mResultsAdapter.search(query);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSearchView.setQuery(mResultsAdapter.getAtPos(position).query, true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    private class SearchListHelper extends ASyncableMediaRecyclerHelper<DetailedModel, BaseModel[]> implements DataCallback<AnimeModel.Base[]> {

        private String mQuery;

        public void performSearch(String pQuery, int pMediatype) {
            mResultsAdapter.submitSearch(pQuery);
            if (pQuery != null && !pQuery.equals(mQuery)) {
                mQuery = pQuery;
                if (mAdapter != null) {
                    mAdapter.set(new ArrayList<MediaCard>(0));
                    mData = new ArrayList<>(0);
                    mCurrentPage = 1;
                    dataEnded = false;
                    mLockedRequest = false;
                    requestData();
                }
                MediaSearchActivity.this.setTitle("Results for: " + mQuery);
            }
        }

        @Override
        public void onSaveState(Bundle outState) {
            outState.putString(SearchManager.QUERY, mQuery);
            super.onSaveState(outState);
        }

        public SearchListHelper(Context context) {
            super(context);
        }

        @Override
        public void onCreate(Bundle savedState, Bundle arguments) {
            if (mQuery == null && savedState != null) {
                mQuery = savedState.getString(SearchManager.QUERY, null);
            }
            if (mQuery == null && arguments != null) {
                mQuery = arguments.getString(SearchManager.QUERY, null);
            }
            if (mQuery != null) {
                MediaSearchActivity.this.setTitle("Results for: " + mQuery);
            }
            super.onCreate(savedState, arguments);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void onRequestInitial() {
            super.onRequestInitial();
            if (mQuery != null) {
                mLockedRequest = true;
                if (SearchActivity.sSearchType == AnimeModel.TYPE) {
                    AnilistServiceHelper.search(mQuery, mCurrentPage, this);
                } else {
                    TMDBServiceHelper.search(mQuery, mCurrentPage, mRequestCallback);
                }
            }
        }

        @Override
        public void requestData() {
            setState(LoaderView.STATE_LOADING);
            mLockedRequest = true;
            if (mQuery != null) {
                mLockedRequest = true;
                if (SearchActivity.sSearchType == AnimeModel.TYPE) {
                    AnilistServiceHelper.search(mQuery, mCurrentPage, this);
                } else {
                    TMDBServiceHelper.search(mQuery, mCurrentPage, mRequestCallback);
                }
            }
        }

        @Override
        protected void handleData(BaseModel[] data) {
            if (data == null || data.length == 0) {
                dataEnded = true;
                setState(LoaderView.STATE_EXTRA);
                return;
            }

            List<DetailedModel> results = new ArrayList<>(data.length);
            for (BaseModel model : data) {
                //if(!model.adult) {
                switch (model.type) {
                    case MovieModel.TYPE:
                        results.add(new MovieModel((MovieModel.Base) model, null));
                        break;
                    case SerieModel.TYPE:
                        results.add(new SerieModel((SerieModel.Base) model, null));
                        break;
                    case PersonModel.TYPE:
                        results.add(new PersonModel((PersonModel.Base) model, null));
                        break;
                    default:
                        break;
                }
                //}
            }
            addData(results);
            if (data.length < APIUtils.TMDB_RESULTS_PERPAGE) {
                dataEnded = true;
                setState(LoaderView.STATE_EXTRA);
            } else {
                mRecycler.loadingMore = false;
            }
        }

        protected void handleAnimeData(AnimeModel.Base[] data) {
            if (data == null || data.length == 0) {
                dataEnded = true;
                setState(LoaderView.STATE_EXTRA);
                return;
            }

            List<DetailedModel> results = new ArrayList<>(data.length);
            for (AnimeModel.Base model : data) {
                if(!model.adult) {
                    results.add(new AnimeModel(model, null));
                }
            }
            addData(results);
            if (data.length < APIUtils.ANILIST_RESULTS_PERPAGE) {
                dataEnded = true;
                setState(LoaderView.STATE_EXTRA);
            } else {
                mRecycler.loadingMore = false;
            }
        }

        @Override
        public void success(AnimeModel.Base[] data) {
            mLockedRequest = false;
            handleAnimeData(data);
        }

        @Override
        public void failure(APIError error) {
            setState(LoaderView.STATE_ERROR);
            mLockedRequest = false;
            if (error != null) Logger.e(error.getMessage());
        }
    }

    /**
     * On Lollipop+ perform a circular reveal animation (an expanding circular mask) when showing
     * the search panel.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void doEnterAnim() {
        // Fade in a background scrim as this is a floating window. We could have used a
        // translucent window background but this approach allows us to turn off window animation &
        // overlap the fade with the reveal animation – making it feel snappier.
        final View scrim = findViewById(R.id.scrim);
        scrim.animate()
                .alpha(1f)
                .setDuration(500L)
                .setInterpolator(
                        AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in))
                .setListener(mScrimListenerOpen)
                .start();
    }

    /**
     * On Lollipop+ perform a circular animation (a contracting circular mask) when hiding the
     * search panel.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void doExitAnim() {
        // We also animate out the translucent background at the same time.
        final View scrim = findViewById(R.id.scrim);
        scrim.animate()
                .alpha(0f)
                .setDuration(200L)
                .setInterpolator(
                        AnimationUtils.loadInterpolator(MediaSearchActivity.this,
                                android.R.interpolator.fast_out_slow_in))
                .setListener(mScrimListenerClose)
                .start();
    }
}
