package net.egordmitriev.popshows.helpers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.appui.adapters.CardsAdapter;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.pojo.DetailedModel;
import net.egordmitriev.popshows.utils.SaveUtils;

import java.util.ArrayList;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class DefaultMediaListHelper extends AMediaCardRecyclerHelper<DetailedModel, CardsAdapter<MediaCard>> {

    public DefaultMediaListHelper(Context context) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedState, Bundle arguments) {
        super.onCreate(savedState, arguments);
        if (arguments != null) {
            mGridView = arguments.getBoolean(SaveUtils.SAVED_DISPLAY_DATA_META);
        }
    }

    @Override
    public void onRequestInitial() {

    }

    @Override
    public CardsAdapter<MediaCard> createAdapter() {
        return new CardsAdapter<>(mContext, new ArrayList<MediaCard>());
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
        if (mGridView) {
            return new GridLayoutManager(mContext, 3);
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
