package net.egordmitriev.popshows.ui.widgets.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCardHeader;

/**
 * Created by EgorDm on 4/30/2016.
 */
public class MediaCardSplit extends MediaCard {

    private String mTitleSecondary;
    private String mSubtitleSecondary;
    protected String mThumbnailSecondary;

    public MediaCardSplit(Context context, int type, String title, String subtitle, String thumbnail, String titleSec, String subtitleSec, String thumbnailSec) {
        super(context, R.layout.card_media_split_inner_layout, type, title, subtitle, thumbnail);
        mTitleSecondary = titleSec;
        mSubtitleSecondary = subtitleSec;
        mThumbnailSecondary = thumbnailSec;
    }

    @Override
    protected MediaCardHeader getHeader() {
        return null;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        ((TextView)view.findViewById(R.id.card_title_primary)).setText(mCardTitle);
        ((TextView)view.findViewById(R.id.card_title_secondary)).setText(mTitleSecondary);
        ((TextView)view.findViewById(R.id.card_subtitle_primary)).setText(mCardSubtitle);
        ((TextView)view.findViewById(R.id.card_subtitle_secondary)).setText(mSubtitleSecondary);

        if(mThumbnailSecondary != null) {
            Glide.with(getContext())
                    .load(mThumbnailSecondary)
                    .placeholder(R.drawable.noposter_media)
                    .into((ImageView) ((View) parent.getParent()).findViewById(R.id.card_thumbnail_secondary));
        }
    }
}
