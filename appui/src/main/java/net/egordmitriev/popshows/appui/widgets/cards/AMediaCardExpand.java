package net.egordmitriev.popshows.appui.widgets.cards;

import android.content.Context;

import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * Created by EgorDm on 4/30/2016.
 */
public abstract class AMediaCardExpand extends MediaCardSmall {

    public AMediaCardExpand(Context context, int type, String title, String subtitle, float rating, String special, String thumbnail) {
        super(context, type, title, subtitle, thumbnail, rating, special);
    }

    @Override
    protected void init() {
        MediaCardHeader header = getHeader();
        header.setButtonExpandVisible(true);
        addCardHeader(header);
        addCardThumbnail(new CardMediaThumbnail(mContext));
        addCardExpand(getMediaCardExpand());
    }

    protected abstract CardExpand getMediaCardExpand();
}
