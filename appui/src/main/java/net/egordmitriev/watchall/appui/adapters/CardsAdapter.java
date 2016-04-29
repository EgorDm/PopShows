package net.egordmitriev.watchall.appui.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.egordmitriev.appui.R;
import net.egordmitriev.watchall.appui.interfaces.ViewAdapterImplementation;
import net.egordmitriev.watchall.appui.widgets.CardsRecycler;
import net.egordmitriev.watchall.appui.widgets.cards.BaseCardMedia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.BaseRecyclerViewAdapter;
import it.gmariotti.cardslib.library.view.base.CardViewWrapper;

/**
 * Created by EgorDm on 4/29/2016.
 */

/**
 * Created by EgorDm on 12/11/2015.
 */
public class CardsAdapter<T extends BaseCardMedia> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ViewAdapterImplementation<T> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FOOTER = 1;

    protected List<Integer> list = new ArrayList<>();

    protected
    @LayoutRes
    int mRowLayoutId = R.layout.card_media_small_list_layout;

    protected Context mContext;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.ViewHolder mFooter;
    protected boolean mUsingFooter;

    protected List<T> mDataList;

    public CardsAdapter(Context context, List<T> data) {
        super();
        mContext = context;
        mDataList = data;
    }

    @Override
    public int getItemCount() {
        int count = (mDataList != null) ? mDataList.size() : 0;
        return (mUsingFooter) ? count + 1 : count;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position == getItemCount() - 1) && mUsingFooter) {
            return TYPE_FOOTER;
        }
        return getItemViewTypeNormal(position);
    }

    protected int getItemViewTypeNormal(int position) {
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            if (mFooter == null) {
                mFooter = onCreateFooter(parent);
            }
            return mFooter;
        }
        return onCreateNormalViewHolder(parent, viewType);
    }

    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(mRowLayoutId, parent, false);
        return new BaseRecyclerViewAdapter.CardViewHolder(view);
    }

    public RecyclerView.ViewHolder onCreateFooter(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) != TYPE_FOOTER) {
            onBindNormalViewHolder(viewHolder, position);
        }
    }

    public void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof BaseRecyclerViewAdapter.CardViewHolder) {
            BaseRecyclerViewAdapter.CardViewHolder cardViewHolder = (BaseRecyclerViewAdapter.CardViewHolder) viewHolder;
            CardViewWrapper mCardView = cardViewHolder.mCardView;
            T mCard = getItem(position);

            if (mCardView != null) {
                //It is important to set recycle value for inner layout elements
                mCardView.setForceReplaceInnerLayout(Card.equalsInnerLayout(mCardView.getCard(), mCard));

                //It is important to set recycle value for performance issue
                mCardView.setRecycle(cardViewHolder.recycled);

                //Save original swipeable to prevent cardSwipeListener (listView requires another cardSwipeListener)
                boolean originalSwipeable = mCard.isSwipeable();
                mCard.setSwipeable(false);

                mCardView.setCard(mCard);
                mCard.setSwipeable(originalSwipeable);

                //If card has an expandable button override animation
                if ((mCard.getCardHeader() != null && mCard.getCardHeader().isButtonExpandVisible()) || mCard.getViewToClickToExpand() != null) {

                    setupExpandCollapseListAnimation(mCardView);
                }
            }
        }
    }

    /**
     * Overrides the default collapse/expand animation in a List
     *
     * @param cardView {@link it.gmariotti.cardslib.library.view.base.CardViewWrapper}
     */
    protected void setupExpandCollapseListAnimation(CardViewWrapper cardView) {
        if (cardView == null) return;
        if (mRecyclerView instanceof CardsRecycler) {
            cardView.setOnExpandListAnimatorListener((CardsRecycler) mRecyclerView);
        }
    }

    public void enableFooter() {
        mUsingFooter = true;
    }

    @Override
    public void set(final Collection<? extends T> collection) {
        mDataList = new ArrayList<>(collection);
        notifyDataSetChanged();
    }

    @Override
    public boolean add(T item) {
        boolean result = mDataList.add(item);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public void add(int i, T item) {
        mDataList.add(i, item);
        notifyItemInserted(i);
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        boolean result = mDataList.addAll(collection);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public boolean remove(final T item) {
        boolean result = mDataList.remove(item);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public T remove(final int position) {
        T result = mDataList.remove(position);
        notifyItemRemoved(position);
        return result;
    }

    @Override
    public boolean contains(final T item) {
        return mDataList.contains(item);
    }

    @Override
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public T getItem(int position) {
        return mDataList.get(position);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void setRowLayoutId(int rowLayoutId) {
        mRowLayoutId = rowLayoutId;
    }
}
