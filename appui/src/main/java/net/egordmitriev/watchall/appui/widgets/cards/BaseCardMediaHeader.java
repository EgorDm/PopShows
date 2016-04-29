package net.egordmitriev.watchall.appui.widgets.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.egordmitriev.appui.R;

import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by EgorDm on 4/29/2016.
 */
public abstract class BaseCardMediaHeader extends CardHeader {

    public BaseCardMediaHeader(Context context) {
        super(context, R.layout.card_media_small_header_inner_layout);
    }

    public BaseCardMediaHeader(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView titleView = (TextView) view.findViewById(R.id.card_title);
        titleView.setText(getCardTitle());

        TextView subtitleView = (TextView) view.findViewById(R.id.card_subtitle);
        subtitleView.setText(getCardSubtitle());
    }

    protected abstract String getCardTitle();
    protected abstract String getCardSubtitle();
}