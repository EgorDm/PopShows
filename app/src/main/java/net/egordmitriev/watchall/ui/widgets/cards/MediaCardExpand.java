package net.egordmitriev.watchall.ui.widgets.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.appui.widgets.cards.AMediaCardExpand;

import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * Created by EgorDm on 4/30/2016.
 */
public class MediaCardExpand extends AMediaCardExpand {

    protected String mExpand;

    public MediaCardExpand(Context context, int type, String title, String subtitle, float rating, String thumbnail, String special, String expand) {
        super(context, type, title, subtitle, rating, special, thumbnail);
        mExpand = expand;
    }

    @Override
    protected CardExpand getMediaCardExpand() {
        return new CardMediaSmallExpand(mContext);
    }

    private class CardMediaSmallExpand extends CardExpand {
        public CardMediaSmallExpand(Context context) {
            super(context, R.layout.card_media_expand_inner_layout);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            ((TextView) view.findViewById(R.id.card_expand_text)).setText(mExpand);
        }
    }
}
