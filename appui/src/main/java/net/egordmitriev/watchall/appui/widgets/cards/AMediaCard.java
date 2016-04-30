package net.egordmitriev.watchall.appui.widgets.cards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import net.egordmitriev.appui.R;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by EgorDm on 4/29/2016.
 */
public abstract class AMediaCard extends Card {

    public int clickID;
    public int type;

    public AMediaCard(Context context, int innerLayout, int type) {
        super(context, innerLayout);
        this.type = type;
        init();
    }

    protected void init() {
        addCardHeader(getHeader());
        addCardThumbnail(new CardMediaThumbnail(mContext));
    }

    protected abstract AMediaCardHeader getHeader();

    protected abstract String getThumbnail();

    public void setMenu(int menuID, CardHeader.OnClickCardHeaderPopupMenuListener listener) {
        if (hasHeader()) {
            getCardHeader().setPopupMenu(menuID, listener);
        }
    }

    public CardMediaThumbnail getMediaThumbnail() {
        return (CardMediaThumbnail) getCardThumbnail();
    }

    public class CardMediaThumbnail extends CardThumbnail {
        private ImageView mImageView;

        public CardMediaThumbnail(Context context) {
            super(context);
            setExternalUsage(true);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View imageView) {
            mImageView = (ImageView) imageView;
            Glide.with(getContext())
                    .load(getThumbnail())
                    .placeholder(R.drawable.noposter_media)
                    .into(mImageView);
        }

        public Bitmap getThumbnailDrawable() {
            if (mImageView.getDrawable() instanceof GlideBitmapDrawable) {
                return ((GlideBitmapDrawable) mImageView.getDrawable()).getBitmap();
            }
            if (mImageView.getDrawable() instanceof BitmapDrawable) {
                return null;
            }
            return ((GlideBitmapDrawable) ((TransitionDrawable) mImageView.getDrawable()).getDrawable(1)).getBitmap();
        }

        public ImageView getImageView() {
            return mImageView;
        }
    }
}
