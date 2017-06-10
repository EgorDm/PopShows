package net.egordmitriev.popshows.helpers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.adapters.SectionedCardsAdapter;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.pojo.CardedModel;
import net.egordmitriev.popshows.utils.SaveUtils;

import java.util.ArrayList;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class SectionedMediaRecyclerHelper extends AMediaCardRecyclerHelper<CardedModel, SectionedCardsAdapter> {

    public SectionedMediaRecyclerHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedState, Bundle arguments) {
        super.onCreate(savedState, arguments);
        if(arguments != null) {
            mGridView = arguments.getBoolean(SaveUtils.SAVED_DISPLAY_DATA);
        }
    }

    @Override
    public void onRequestInitial() {

    }


    @Override
    public SectionedCardsAdapter createAdapter() {
        return new SectionedCardsAdapter(mContext, new ArrayList<MediaCard>());
    }

    @Override
    public int getLayout() {
        if (mGridView) {
            return R.layout.fragment_card_list_large;
        } else {
            return R.layout.fragment_card_list_small;
        }
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        if(mGridView) {
            final GridLayoutManager lm = new GridLayoutManager(mContext, 3);
            lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mAdapter.isSection(position) ? lm.getSpanCount() : 1;
                }
            });
            return lm;
        } else {
            return new LinearLayoutManager(mContext);
        }
    }

    @Override
    public void setupRecycler() {
        super.setupRecycler();
        mRecycler.setNestedScrollingEnabled(false);
    }
}
