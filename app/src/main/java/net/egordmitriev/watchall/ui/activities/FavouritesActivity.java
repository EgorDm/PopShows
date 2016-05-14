package net.egordmitriev.watchall.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.api.WatchAllServiceHelper;
import net.egordmitriev.watchall.api.database.tables.WatchlistsTable;
import net.egordmitriev.watchall.appui.adapters.LoaderCardsAdapter;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.helpers.AMediaCardRecyclerHelper;
import net.egordmitriev.watchall.helpers.WatchlistDetailHelper;
import net.egordmitriev.watchall.pojo.DetailedModel;
import net.egordmitriev.watchall.ui.activities.base.HomeActivity;
import net.egordmitriev.watchall.ui.dialogs.WatchlistAddToDialog;

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
