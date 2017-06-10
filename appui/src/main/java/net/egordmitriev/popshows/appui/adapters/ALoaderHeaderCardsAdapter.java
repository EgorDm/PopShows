package net.egordmitriev.popshows.appui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;

import java.util.List;

/**
 * Created by EgorDm on 5/13/2016.
 */
public abstract class ALoaderHeaderCardsAdapter<T extends MediaCard> extends LoaderCardsAdapter<T> {

    public static final int TYPE_HEADER = 2;

    protected RecyclerView.ViewHolder mHeader;
    protected boolean mUsingHeader;

    public ALoaderHeaderCardsAdapter(Context context, List<T> data) {
        super(context, data);
    }

    public ALoaderHeaderCardsAdapter(Context context, List<T> data, int loaderViewRes) {
        super(context, data, loaderViewRes);
    }

    @Override
    public int getItemCount() {
        return (mUsingHeader) ? super.getItemCount() + 1 : super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mUsingHeader && position == 0) {
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            if (mHeader == null) {
                mHeader = onCreateHeader(parent);
            }
            return mHeader;
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            onBindHeaderViewHolder(viewHolder, position);
            return;
        }
        super.onBindViewHolder(viewHolder, position);
    }

    @Override
    public T getItem(int position) {
        return super.getItem((mUsingHeader) ? position - 1 : position);
    }

    public abstract void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    public RecyclerView.ViewHolder onCreateHeader(ViewGroup parent) {
        return null;
    }

    public void enableHeader() {
        mUsingHeader = true;
    }
}
