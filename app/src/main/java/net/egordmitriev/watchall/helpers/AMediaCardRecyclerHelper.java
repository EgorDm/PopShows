package net.egordmitriev.watchall.helpers;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import net.egordmitriev.watchall.appui.adapters.CardsAdapter;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.pojo.DetailedModel;
import net.egordmitriev.watchall.pojo.user.ListRequestData;
import net.egordmitriev.watchall.ui.modelviews.base.AModelView;
import net.egordmitriev.watchall.utils.SaveUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

/**
 * Created by EgorDm on 5/1/2016.
 */
public abstract class AMediaCardRecyclerHelper<T extends DetailedModel, A extends CardsAdapter<MediaCard>> extends ACardRecyclerHelper<T, A> implements Card.OnCardClickListener {

    public interface IMediaItemMenuListener<U extends DetailedModel> {
        void onMenuItemClick(MenuItem menuItem, U item);
    }

    public boolean dataEnded;
    public boolean usingCounter;
    protected ListRequestData mRequestData;
    protected boolean mGridView = false;
    protected int mOverflowMenu = -1;
    protected IMediaItemMenuListener<T> mMenuListener;

    public AMediaCardRecyclerHelper(Context context) {
        super(context);
    }

    @Override
    public void onSaveState(Bundle outState) {
        outState.putBoolean(SaveUtils.STATE_SAVED_DATA_ENDED, dataEnded);
        super.onSaveState(outState);
    }

    @Override
    public void onCreate(Bundle savedState, Bundle arguments) {
        super.onCreate(savedState, arguments);
        if(savedState != null) {
            dataEnded = savedState.getBoolean(SaveUtils.STATE_SAVED_DATA_ENDED, false);
        }
        if(arguments != null) {
            mRequestData = arguments.getParcelable(SaveUtils.SAVED_DISPLAY_DATA);
        }
    }

    @Override
    public void addData(final Collection<T> data) {
        super.addData(data);
        List<MediaCard> tempList = generateCards(data);
        for(Card card : tempList) {
            card.setOnClickListener(this);
        }

        mData.addAll(data);
        mAdapter.addAll(tempList);
    }

    private List<MediaCard> generateCards(final Collection<T> data) {
        ArrayList<MediaCard> ret = new ArrayList<>(data.size());
        int i = mData.size();
        for (final T item : data) {
            i++;
            MediaCard mediaCard = item.onCreateCard(mContext, (usingCounter) ? i+". ": null, !mGridView);
            mediaCard.clickID = i;
            if(mOverflowMenu != -1) {
                mediaCard.setMenu(mOverflowMenu, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                    @Override
                    public void onMenuItemClick(BaseCard card, MenuItem menuItem) {
                        mMenuListener.onMenuItemClick(menuItem, item);
                    }
                });
            }
            ret.add(mediaCard);
        }
        return ret;
    }

    public void setOverflowMenu(int overflowMenu, IMediaItemMenuListener<T> listener) {
        mOverflowMenu = overflowMenu;
        mMenuListener = listener;
    }

    @Override
    public void onClick(Card card, View view) {
        MediaCard mediaCard = (MediaCard) card;
        DetailedModel model =  mData.get(mediaCard.clickID-1);

        if(model != null) {
            AModelView.transitionPoster = ((MediaCard.CardMediaThumbnail)mediaCard.getCardThumbnail()).getThumbnailDrawable();
            //TODO: open detail
            /*DetailActivity.openDetail(mContext,
                    (DetailedItemModel<net.egordmitriev.watchit.models.base.BaseModel, net.egordmitriev.watchit.models.base.BaseModel, net.egordmitriev.watchit.views.modelviews.base.BaseMediaView>) model);*/
        }
    }
}
