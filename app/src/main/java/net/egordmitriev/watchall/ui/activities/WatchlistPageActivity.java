package net.egordmitriev.watchall.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.api.WatchAllServiceHelper;
import net.egordmitriev.watchall.api.database.tables.WatchlistsTable;
import net.egordmitriev.watchall.helpers.AMediaCardRecyclerHelper;
import net.egordmitriev.watchall.helpers.WatchlistPageHelper;
import net.egordmitriev.watchall.pojo.DetailedModel;
import net.egordmitriev.watchall.pojo.watchall.WatchlistModel;
import net.egordmitriev.watchall.ui.activities.base.HomeActivity;
import net.egordmitriev.watchall.ui.dialogs.WatchlistEditDialog;
import net.egordmitriev.watchall.utils.TypeRunnable;

/**
 * Created by EgorDm on 5/12/2016.
 */
public class WatchlistPageActivity extends HomeActivity implements AMediaCardRecyclerHelper.IMediaItemMenuListener<WatchlistModel> {

    private WatchlistPageHelper mHelper;
    public static boolean deleted;

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
        mHelper = new WatchlistPageHelper(this);
        mHelper.setOverflowMenu(R.menu.watchlist_actions_readwrite, this);
        mHelper.onCreate(savedInstanceState, getIntent().getExtras());
        View view = mHelper.onCreateView(getLayoutInflater(), (ViewGroup) findViewById(R.id.content), savedInstanceState);
        ((ViewGroup) findViewById(R.id.content)).addView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (deleted) {
            deleted = false;
            deleteWatchlist(mHelper.lastViewed);
        }
        mHelper.refresh();
    }

    @Override
    protected int getMenu() {
        return R.menu.watchlists_page;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                openCreationDialog(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getOwnID() {
        return PAGE_LISTS;
    }

    @Override
    public void onMenuItemClick(MenuItem menuItem, final WatchlistModel item) {
        switch (menuItem.getItemId()) {
            case R.id.watchlist_edit:
                editWatchlist(item);
                break;
            case R.id.watchlist_delete:
                deleteWatchlist(item);
                break;
        }
    }

    private void editWatchlist(final WatchlistModel item) {
        item.requestDetail(new DetailedModel.DetailCallback(new TypeRunnable<Boolean>() {
            @Override
            public void run(Boolean args) {
                openCreationDialog(item);
            }
        }));
    }

    private void openCreationDialog(WatchlistModel edit) {
        WatchlistEditDialog dialog = WatchlistEditDialog.newInstance(edit);
        dialog.show(getSupportFragmentManager(), "WatchlistCreationDialog");
        dialog.setCallbackRunnable(new Runnable() {
            @Override
            public void run() {
                mHelper.refresh();
            }
        });
    }

    private void deleteWatchlist(final WatchlistModel item) {
        item.requestDetail(new DetailedModel.DetailCallback(new TypeRunnable<Boolean>() {
            @Override
            public void run(Boolean args) {
                if (WatchAllServiceHelper.deleteWatchlist(item.id)) {
                    mHelper.refresh();
                    WatchlistPageActivity.this.ShowSnackbar(R.string.snackbar_deleted_list, R.string.snackbar_undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            item.setID(-1);
                            if (WatchlistsTable.upsert(item, item.id)) {
                                mHelper.refresh();
                                Toast.makeText(WatchlistPageActivity.this, R.string.toast_restored, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(WatchlistPageActivity.this, R.string.toast_unknown_error, Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }
}
