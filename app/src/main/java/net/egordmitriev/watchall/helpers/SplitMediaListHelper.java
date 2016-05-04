package net.egordmitriev.watchall.helpers;

import android.content.Context;

import net.egordmitriev.watchall.R;

/**
 * Created by EgorDm on 5/4/2016.
 */
public class SplitMediaListHelper extends DefaultMediaListHelper {

    public SplitMediaListHelper(Context context) {
        super(context);
    }

    @Override
    public void setupAdapter() {
        super.setupAdapter();
        mAdapter.setRowLayoutId(R.layout.card_media_split_list_layout);
    }
}