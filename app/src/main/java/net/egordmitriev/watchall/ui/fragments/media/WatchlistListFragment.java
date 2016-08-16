package net.egordmitriev.watchall.ui.fragments.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.egordmitriev.loaderview.LoaderView;
import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.helpers.ASyncableMediaRecyclerHelper;
import net.egordmitriev.watchall.pojo.user.ListRequestData;
import net.egordmitriev.watchall.pojo.watchall.WatchlistModel;
import net.egordmitriev.watchall.ui.activities.media.WatchlistDetailActivity;
import net.egordmitriev.watchall.ui.fragments.base.RecyclerFragment;
import net.egordmitriev.watchall.utils.APIUtils;
import net.egordmitriev.watchall.utils.SaveUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by EgorDm on 5/15/2016.
 */
public class WatchlistListFragment extends RecyclerFragment<WatchlistListFragment.WatchlistListHelper> {

    @Override
    protected WatchlistListHelper getHelper() {
        return new WatchlistListHelper(getActivity());
    }

    public class WatchlistListHelper extends ASyncableMediaRecyclerHelper<WatchlistModel, WatchlistModel.Base[]> {
        public WatchlistListHelper(Context context) {
            super(context);
        }

        @Override
        public void onCreate(Bundle savedState, Bundle arguments) {
            super.onCreate(savedState, arguments);
            mRequestData = new ListRequestData(-1, WatchlistModel.TYPE);
        }

        @Override
        protected void setupLoadMore() {

        }

        @Override
        protected void handleData(WatchlistModel.Base[] data) {
            if (mData != null && mAdapter != null) {
                mData.clear();
                mAdapter.clear();
            }
            if (data == null || data.length < 0) {
                dataEnded = true;
                setState(LoaderView.STATE_EXTRA);
                return;
            }

            List<WatchlistModel> result = new ArrayList<>(data.length);
            for (WatchlistModel.Base model : data) {
                result.add(new WatchlistModel(model, null));
            }
            addData(result);
            if (data.length < APIUtils.ANILIST_RESULTS_PERPAGE) {
                dataEnded = true;
                setState(LoaderView.STATE_EXTRA);
            }
        }

        @Override
        public void requestData() {
            super.requestData();
            onRequestInitial();
        }

        @Override
        public void onRequestInitial() {
            super.onRequestInitial();
            // mRequestCallback.success(null);
        }

        @Override
        public void addData(Collection<WatchlistModel> data) {
            super.addData(data);
            mAdapter.setLoaderState(LoaderView.STATE_EXTRA);//TODO: chnage fklin loader
        }

        @Override
        public int getLayout() {
            return R.layout.fragment_card_generated_list;
        }

        @Override
        public RecyclerView.LayoutManager getLayoutManager() {
            return new GridLayoutManager(mContext, 3);
        }

        @Override
        public void onClick(Card card, View view) {
            MediaCard mediaCard = (MediaCard) card;
            Intent intent = new Intent(getActivity(), WatchlistDetailActivity.class);
            intent.putExtra(SaveUtils.SAVED_READ_WRITE, false);
            intent.putExtra(SaveUtils.STATE_SAVED_DATA_LIST, mData.get(mediaCard.clickID - 1));
            startActivity(intent);
        }
    }
}
