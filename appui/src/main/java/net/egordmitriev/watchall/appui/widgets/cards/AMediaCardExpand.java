package net.egordmitriev.watchall.appui.widgets.cards;

import android.content.Context;

import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * Created by EgorDm on 4/30/2016.
 */
public abstract class AMediaCardExpand extends AMediaCardSmall {

    public AMediaCardExpand(Context context, int type) {
        super(context, type);
    }

    @Override
    protected void init() {
        AMediaCardHeader header = getHeader();
        header.setButtonExpandVisible(true);
        addCardHeader(header);
        addCardThumbnail(new CardMediaThumbnail(mContext));
        addCardExpand(getMediaCardExpand());
    }

    protected abstract CardExpand getMediaCardExpand();
}
