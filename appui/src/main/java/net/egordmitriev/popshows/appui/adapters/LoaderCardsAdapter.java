package net.egordmitriev.popshows.appui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.egordmitriev.appui.R;
import net.egordmitriev.loaderview.LoaderView;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;

import java.util.List;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class LoaderCardsAdapter<T extends MediaCard> extends CardsAdapter<T> {

    public LoaderView loaderView;
    protected int mLoaderInitialState;
    protected int mLoaderViewRes = R.layout.card_media_list_loader;

    public LoaderCardsAdapter(Context context, List<T> data) {
        super(context, data);
        enableFooter();
    }

    public LoaderCardsAdapter(Context context, List<T> data, int loaderViewRes) {
        super(context, data);
        mLoaderViewRes = loaderViewRes;
        enableFooter();
    }

    protected int getLoaderViewRes() {
        return mLoaderViewRes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooter(ViewGroup parent) {
        loaderView = (LoaderView) LayoutInflater.from(mContext).inflate(getLoaderViewRes(), parent, false);
        loaderView.setState(mLoaderInitialState);
        return new LoaderHolder(loaderView);
    }

    public void setLoaderState(int state) {
        if (loaderView == null) {
            mLoaderInitialState = state;
        } else {
            loaderView.setState(state);
        }
    }

    public class LoaderHolder extends RecyclerView.ViewHolder {

        public final LoaderView loaderView;

        public LoaderHolder(LoaderView itemView) {
            super(itemView);
            loaderView = itemView;
        }
    }
}
