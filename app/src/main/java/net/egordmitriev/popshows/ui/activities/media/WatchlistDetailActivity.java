package net.egordmitriev.popshows.ui.activities.media;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.api.WatchAllServiceHelper;
import net.egordmitriev.popshows.api.database.tables.WatchlistsTable;
import net.egordmitriev.popshows.helpers.AMediaCardRecyclerHelper;
import net.egordmitriev.popshows.helpers.WatchlistDetailHelper;
import net.egordmitriev.popshows.pojo.DetailedModel;
import net.egordmitriev.popshows.pojo.watchall.WatchlistModel;
import net.egordmitriev.popshows.ui.activities.WatchlistPageActivity;
import net.egordmitriev.popshows.ui.activities.base.BaseActivity;
import net.egordmitriev.popshows.ui.dialogs.WatchlistAddToDialog;
import net.egordmitriev.popshows.ui.dialogs.WatchlistEditDialog;
import net.egordmitriev.popshows.utils.APIUtils;
import net.egordmitriev.popshows.utils.SaveUtils;
import net.egordmitriev.popshows.utils.TypeRunnable;

/**
 * Created by EgorDm on 5/13/2016.
 */
public class WatchlistDetailActivity extends BaseActivity implements AMediaCardRecyclerHelper.IMediaItemMenuListener<DetailedModel> {

    private WatchlistDetailHelper mHelper;
    private boolean mReadWrite;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mHelper.onSaveState(outState);
        super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initToolbar();
        mReadWrite = getIntent().getExtras().getBoolean(SaveUtils.SAVED_READ_WRITE, false);
        mHelper = new WatchlistDetailHelper(this);
        mHelper.watchlist = getIntent().getExtras().getParcelable(SaveUtils.STATE_SAVED_DATA_LIST);
        if (mReadWrite) {
            mHelper.setOverflowMenu(R.menu.watchlist_actions_item_readwrite, this);
        } else {
            mHelper.setOverflowMenu(R.menu.watchlist_actions_item_readonly, this);
        }
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
                if (!mReadWrite) return;
                WatchlistAddToDialog dialogMove = WatchlistAddToDialog.newInstance(item, mHelper.watchlist.id, new Runnable() {
                    @Override
                    public void run() {
                        mHelper.watchlist.detail = null;
                        mHelper.requestData();
                    }
                });
                dialogMove.show(getSupportFragmentManager(), "Move to list");
                break;
            case R.id.media_addtofav:
                Logger.d("Favourite");
                if (WatchlistsTable.addMedia(APIUtils.sGlobalParser.toJsonTree(item.base).getAsJsonObject(), WatchAllServiceHelper.getFavouritesID())) {
                    Toast.makeText(this, String.format(getString(R.string.toast_added_to_list), "Favourites"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.toast_unknown_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.media_deletefromlist:
                if (!mReadWrite) return;
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
            case R.id.menu_edit:
                mHelper.watchlist.requestDetail(new DetailedModel.DetailCallback(new TypeRunnable<Boolean>() {
                    @Override
                    public void run(Boolean args) {
                        openCreationDialog(mHelper.watchlist);
                    }
                }));
                break;
            case R.id.menu_delete:
                deleteWatchlist();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteWatchlist() {
        WatchlistPageActivity.deleted = true;
        finish();
    }

    private void openCreationDialog(WatchlistModel edit) {
        WatchlistEditDialog dialog = WatchlistEditDialog.newInstance(edit);
        dialog.show(getSupportFragmentManager(), "WatchlistCreationDialog");
        dialog.setCallbackRunnable(new Runnable() {
            @Override
            public void run() {
                mHelper.refreshHeader();
            }
        });
    }

    @Override
    protected int getMenu() {
        if (mReadWrite) {
            return R.menu.watchlist_detail;
        } else {
            return -1;
        }
    }
}
