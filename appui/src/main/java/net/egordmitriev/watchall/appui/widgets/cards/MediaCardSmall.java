package net.egordmitriev.watchall.appui.widgets.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import net.egordmitriev.appui.R;

/**
 * Created by EgorDm on 4/30/2016.
 */
public class MediaCardSmall extends MediaCard {

    protected float mRating;
    protected String mSpecial;

    public MediaCardSmall(Context context, int type, String title, String subtitle, float rating, String special, String thumbnail) {
        super(context, R.layout.card_media_small_inner_layout, type, title, subtitle, thumbnail);
        mRating = rating;
        mSpecial = special;
        mThumbnail = thumbnail;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (getRating() != -1f) {
            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.card_ratingbar);
            ratingBar.setRating(getRating());
        }
        if (getSpecial() != null) {
            ((TextView) view.findViewById(R.id.card_special)).setText(getSpecial());
        }
    }

    protected float getRating() {
        return mRating;
    }

    protected String getSpecial() {
        return mSpecial;
    }
}
