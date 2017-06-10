package net.egordmitriev.popshows.helpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.egordmitriev.loaderview.LoaderView;
import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.appui.adapters.ALoaderHeaderCardsAdapter;
import net.egordmitriev.popshows.appui.adapters.LoaderCardsAdapter;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.pojo.DetailedModel;
import net.egordmitriev.popshows.pojo.watchall.WatchlistModel;
import net.egordmitriev.popshows.ui.modelviews.WatchlistView;
import net.egordmitriev.popshows.ui.widgets.cards.WatchlistCard;
import net.egordmitriev.popshows.utils.TypeRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.BaseRecyclerViewAdapter;
import it.gmariotti.cardslib.library.view.base.CardViewWrapper;

/**
 * Created by EgorDm on 5/13/2016.
 */
public class WatchlistDetailHelper extends ASyncableMediaRecyclerHelper<DetailedModel, DetailedModel[]> {

    public WatchlistModel watchlist;

    public WatchlistDetailHelper(Context context) {
        super(context);
    }

    public void refreshHeader() {
        ((WatchlistViewAdapter) mAdapter).refreshHeader();
    }

    @Override
    protected void handleData(DetailedModel[] data) {
        if (data == null || data.length < 0) {
            dataEnded = true;
            setState(LoaderView.STATE_EXTRA);
            return;
        }

        List<DetailedModel> result = Arrays.asList(data);
        addData(result);
        dataEnded = true;
        setState(LoaderView.STATE_EXTRA);
    }

    public void openDetailData() {
        mRequestCallback.success(WatchlistView.parseListContents(watchlist.detail.list_contents));
    }

    @Override
    public void onRequestInitial() {
        super.onRequestInitial();
        watchlist.requestDetail(new DetailedModel.DetailCallback(new TypeRunnable<Boolean>() {
            @Override
            public void run(Boolean args) {
                if (args) {
                    openDetailData();
                } else {
                    mRequestCallback.failure(null);
                }
            }
        }));
    }

    @Override
    public void requestData() {
        clean();
        onRequestInitial();
    }

    public void clean() {
        mData.clear();
        mAdapter.clear();
    }

    @Override
    public LoaderCardsAdapter<MediaCard> createAdapter() {
        return new WatchlistViewAdapter(mContext, new ArrayList<MediaCard>());
    }

    protected class WatchlistViewAdapter extends ALoaderHeaderCardsAdapter<MediaCard> {

        public WatchlistViewAdapter(Context context, List<MediaCard> data) {
            super(context, data);
            enableHeader();
        }

        public WatchlistViewAdapter(Context context, List<MediaCard> data, int loaderViewRes) {
            super(context, data, loaderViewRes);
            //TODO create medium list holder
        }

        public void refreshHeader() {
            onBindHeaderViewHolder(mHeader, 0);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            BaseRecyclerViewAdapter.CardViewHolder cardViewHolder = (BaseRecyclerViewAdapter.CardViewHolder) viewHolder;
            CardViewWrapper mCardView = cardViewHolder.mCardView;
            WatchlistCard mCard = new WatchlistCard(mContext, watchlist);

            if (mCardView != null) {
                //It is important to set recycle value for inner layout elements
                mCardView.setForceReplaceInnerLayout(Card.equalsInnerLayout(mCardView.getCard(), mCard));

                //It is important to set recycle value for performance issue
                mCardView.setRecycle(cardViewHolder.recycled);

                if (mCardView.getCard() != null) {
                    mCardView.refreshCard(mCard);
                } else {
                    mCardView.setCard(mCard);
                }
            }
        }


        @Override
        public RecyclerView.ViewHolder onCreateHeader(ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.card_media_medium_list_layout, parent, false);
            return new BaseRecyclerViewAdapter.CardViewHolder(view);
        }
    }
}
