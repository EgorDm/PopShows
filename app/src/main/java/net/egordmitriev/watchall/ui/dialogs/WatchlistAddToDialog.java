package net.egordmitriev.watchall.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.api.WatchAllServiceHelper;
import net.egordmitriev.watchall.api.database.tables.WatchlistsTable;
import net.egordmitriev.watchall.pojo.DetailedModel;
import net.egordmitriev.watchall.pojo.watchall.WatchlistModel;
import net.egordmitriev.watchall.utils.APIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EgorDm on 5/13/2016.
 */
public class WatchlistAddToDialog extends DialogFragment {

    private ItemAdapter mAdapter;
    public static DetailedModel sItem;
    public static int sFromList;
    public boolean moveMedia;
    public Runnable moveRefresh;

    public static WatchlistAddToDialog newInstance(DetailedModel item) {
        sItem = item;
        return new WatchlistAddToDialog();
    }

    public static WatchlistAddToDialog newInstance(DetailedModel item, int fromlist, Runnable moveRefresh) {
        WatchlistAddToDialog ret = newInstance(item);
        ret.moveMedia = true;
        ret.moveRefresh = moveRefresh;
        sFromList = fromlist;
        return ret;
    }

    public WatchlistAddToDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.PaddinglessDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ListView dialogView = (ListView) inflater.inflate(R.layout.dialogue_addto , null);
        dialogView.setDivider(null);
        mAdapter = new ItemAdapter(getActivity(), new ArrayList<WatchlistModel.Base>());
        dialogView.setAdapter(mAdapter);
        mAdapter.addAll(WatchAllServiceHelper.getMyWatchlists());

        builder.setTitle((!moveMedia) ? R.string.title_addto_list : R.string.title_moveto_list);
        builder.setView(dialogView);

        Dialog ret = builder.create();
        ret.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return ret;
    }

    public View createWatchlistItem(final WatchlistModel.Base model, ViewGroup parent) {
        View ret = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_addto_watchlist, parent, false);
        ((TextView) ret.findViewById(R.id.title)).setText(model.title);
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WatchlistsTable.addMedia(APIUtils.sGlobalParser.toJsonTree(sItem.base).getAsJsonObject(), model.id)) {
                    if (moveMedia) {
                        WatchlistsTable.removeMedia(sItem.base.id, sFromList);
                        if (moveRefresh != null) moveRefresh.run();
                    }
                    Toast.makeText(getActivity(), String.format(getString(R.string.toast_added_to_list), model.title), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), R.string.toast_unknown_error, Toast.LENGTH_SHORT).show();
                }
                getDialog().dismiss();
            }
        });
        return ret;
    }

    public View createWatchlistHeader(ViewGroup parent) {
        View ret = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_addto_watchlist, parent, false);
        ((TextView) ret.findViewById(R.id.title)).setText(R.string.title_create_new_list);
        ((ImageView) ret.findViewById(R.id.title_icon)).setImageResource(R.drawable.ic_add);
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                WatchlistEditDialog dialog = WatchlistEditDialog.newInstance(null, APIUtils.sGlobalParser.toJsonTree(sItem.base).getAsJsonObject());
                dialog.show(getFragmentManager(), "WatchlistCreationDialog");
            }
        });
        return ret;
    }

    public class ItemAdapter extends ArrayAdapter<WatchlistModel.Base> {

        public ItemAdapter(Context context, List<WatchlistModel.Base> items) {
            super(context, 0, items);
        }

        @Override
        public int getCount() {
            return super.getCount() + 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position != 0) {
                return createWatchlistItem(getItem(position - 1), parent);
            } else {
                return createWatchlistHeader(parent);
            }
        }

    }
}
