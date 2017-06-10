package net.egordmitriev.popshows.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.api.WatchAllServiceHelper;
import net.egordmitriev.popshows.api.database.tables.WatchlistsTable;
import net.egordmitriev.popshows.appui.adapters.LoaderCardsAdapter;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.helpers.AMediaCardRecyclerHelper;
import net.egordmitriev.popshows.helpers.WatchlistDetailHelper;
import net.egordmitriev.popshows.pojo.DetailedModel;
import net.egordmitriev.popshows.ui.activities.base.HomeActivity;
import net.egordmitriev.popshows.ui.dialogs.WatchlistAddToDialog;

import java.util.ArrayList;

/**
 * Created by EgorDm on 5/14/2016.
 */
public class FavouritesActivity extends HomeActivity implements AMediaCardRecyclerHelper.IMediaItemMenuListener<DetailedModel> {

    private FavouritesDetailHelper mHelper;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mHelper.onSaveState(outState);
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_drawer);
        initToolbar();
        initDrawer();
        mHelper = new FavouritesDetailHelper(this);
        mHelper.setOverflowMenu(R.menu.watchlist_actions_item_favourites, this);
        mHelper.onCreate(savedInstanceState, getIntent().getExtras());
        View view = mHelper.onCreateView(getLayoutInflater(), (ViewGroup) findViewById(R.id.content), savedInstanceState);
        ((ViewGroup) findViewById(R.id.content)).addView(view);
    }

    @Override
    public void onMenuItemClick(MenuItem menuItem, DetailedModel item) {
        switch (menuItem.getItemId()) {
            case R.id.media_addtolist:
                WatchlistAddToDialog dialogAdd = WatchlistAddToDialog.newInstance(item);
                dialogAdd.show(getSupportFragmentManager(), "Add to list");
                break;
            case R.id.media_movetolist:
                WatchlistAddToDialog dialogMove = WatchlistAddToDialog.newInstance(item, mHelper.watchlist.id, new Runnable() {
                    @Override
                    public void run() {
                        mHelper.watchlist.detail = null;
                        mHelper.requestData();
                    }
                });
                dialogMove.show(getSupportFragmentManager(), "Move to list");
                break;
            case R.id.media_deletefromlist:
                if (WatchlistsTable.removeMedia(item.base.id, mHelper.watchlist.id)) {
                    mHelper.watchlist.detail = null;
                    mHelper.requestData();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getMenu() {
        return -1;
    }

    private class FavouritesDetailHelper extends WatchlistDetailHelper {

        public FavouritesDetailHelper(Context context) {
            super(context);
        }

        @Override
        public void onRequestInitial() {
            watchlist = WatchAllServiceHelper.getFavourites();
            openDetailData();
        }

        @Override
        public LoaderCardsAdapter<MediaCard> createAdapter() {
            return new LoaderCardsAdapter<>(mContext, new ArrayList<MediaCard>());
        }
    }
}
