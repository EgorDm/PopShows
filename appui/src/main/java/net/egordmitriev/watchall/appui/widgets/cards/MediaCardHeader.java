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
public class MediaCardHeader extends CardHeader {

    protected String mCardTitle;
    protected String mCardSubtitle;

    public MediaCardHeader(Context context, String title, String subtitle) {
        this(context, R.layout.card_media_small_header_inner_layout, title, subtitle);
    }

    public MediaCardHeader(Context context, int innerLayout, String title, String subtitle) {
        super(context, innerLayout);
        mCardTitle = title;
        mCardSubtitle = subtitle;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        TextView titleView = (TextView) view.findViewById(R.id.card_title);
        titleView.setText(getCardTitle());

        TextView subtitleView = (TextView) view.findViewById(R.id.card_subtitle);
        subtitleView.setText(getCardSubtitle());
    }

    public String getCardSubtitle() {
        return mCardSubtitle;
    }

    public void setCardSubtitle(String cardSubtitle) {
        mCardSubtitle = cardSubtitle;
    }

    public String getCardTitle() {
        return mCardTitle;
    }

    public void setCardTitle(String cardTitle) {
        mCardTitle = cardTitle;
    }
}