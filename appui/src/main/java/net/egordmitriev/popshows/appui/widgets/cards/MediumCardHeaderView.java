package net.egordmitriev.popshows.appui.widgets.cards;

import android.content.Context;
import android.util.AttributeSet;

import net.egordmitriev.appui.R;

import it.gmariotti.cardslib.library.view.component.CardHeaderView;

/**
 * Created by EgorDm on 4/30/2016.
 */
public class MediumCardHeaderView extends CardHeaderView {

    public MediumCardHeaderView(Context context) {
        super(context);
    }

    public MediumCardHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediumCardHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initView() {
        card_header_layout_resourceID = R.layout.card_media_small_header_outer_layout;
        super.initView();
    }
}