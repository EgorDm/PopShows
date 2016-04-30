package net.egordmitriev.watchall.appui.widgets.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.egordmitriev.appui.R;

/**
 * Created by EgorDm on 4/30/2016.
 */
public abstract class MediaCardLarge extends MediaCard {

    protected String mSpecial;

    public MediaCardLarge(Context context, int type, String title, String subtitle, String special, String thumbnail) {
        super(context, R.layout.card_media_large_inner_layout, type, title, subtitle, thumbnail);
        mSpecial = special;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
        if(getSpecial() != null) {
            ((TextView)view.findViewById(R.id.card_special)).setText(getSpecial());
        }
    }

    protected String getSpecial() {
        return mSpecial;
    }
}