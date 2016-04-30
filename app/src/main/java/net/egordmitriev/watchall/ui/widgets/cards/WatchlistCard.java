package net.egordmitriev.watchall.ui.widgets.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCard;
import net.egordmitriev.watchall.appui.widgets.cards.MediaCardHeader;
import net.egordmitriev.watchall.pojo.watchall.WatchlistModel;

import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by EgorDm on 4/30/2016.
 */
public class WatchlistCard extends MediaCard {

    protected WatchlistModel mWatchlist;

    public WatchlistCard(Context context, WatchlistModel watchlist) {
        super(context, R.layout.card_media_medium_inner_layout, watchlist.type, watchlist.base.title, "TODO: subtitle", watchlist.base.custom_icon);
        mWatchlist = watchlist;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
        if (mWatchlist.detail != null) {
            ((TextView) view.findViewById(R.id.card_special)).setText(mWatchlist.detail.description);
        }
    }

    @Override
    protected void init() {
        addCardHeader(getHeader());
        addCardThumbnail(new CardMediaGeneratedThumbnail(mContext));
    }

    @Override
    protected MediaCardHeader getHeader() {
        return new MediaCardHeader(mContext, R.layout.card_media_small_header_inner_layout, mCardTitle, mCardSubtitle);
    }

    private class CardMediaGeneratedThumbnail extends CardThumbnail {

        public CardMediaGeneratedThumbnail(Context context) {
            super(context);
            setExternalUsage(true);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View imageView) {
            if (mThumbnail != null && !mThumbnail.isEmpty()) {
                parent.findViewById(R.id.card_icon_title).setVisibility(View.GONE);
                Glide.with(getContext())
                        .load(getThumbnail())
                        .placeholder(R.drawable.noposter_media)
                        .into((ImageView) imageView);
            } else {
                ((TextView) parent.findViewById(R.id.card_icon_title)).setText(mWatchlist.base.title);
                imageView.setBackgroundColor(mWatchlist.base.color);
            }
        }
    }
}
