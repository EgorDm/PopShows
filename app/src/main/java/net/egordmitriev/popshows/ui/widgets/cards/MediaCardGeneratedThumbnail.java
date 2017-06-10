package net.egordmitriev.popshows.ui.widgets.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCardLarge;

import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by EgorDm on 4/30/2016.
 */
public class MediaCardGeneratedThumbnail extends MediaCardLarge {
    protected int mThumbnailGenerated;

    public MediaCardGeneratedThumbnail(Context context, int type, String title, String subtitle, String thumbnail, int generatedColor) {
        this(context, type, title, subtitle, thumbnail, generatedColor, null);
    }

    public MediaCardGeneratedThumbnail(Context context, int type, String title, String subtitle, String thumbnail, int generatedColor, String special) {
        super(context, type, title, subtitle, special, thumbnail);
        mThumbnailGenerated = generatedColor;
    }

    @Override
    protected void init() {
        addCardHeader(getHeader());
        addCardThumbnail(new CardMediaGeneratedThumbnail(mContext));
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
                ((TextView) parent.findViewById(R.id.card_icon_title)).setText(mCardTitle);
                imageView.setBackgroundColor(mThumbnailGenerated);
            }
        }
    }
}
