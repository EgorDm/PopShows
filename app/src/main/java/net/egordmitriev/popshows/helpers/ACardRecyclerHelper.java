package net.egordmitriev.popshows.helpers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.egordmitriev.loaderview.LoaderView;
import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.appui.adapters.CardsAdapter;
import net.egordmitriev.popshows.appui.widgets.AdvancedCardRecycler;
import net.egordmitriev.popshows.pojo.CardedModel;
import net.egordmitriev.popshows.utils.SaveUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by EgorDm on 5/1/2016.
 */
public abstract class ACardRecyclerHelper<T extends CardedModel, A extends CardsAdapter> extends ARecyclerHelper<AdvancedCardRecycler, A> {

    protected ArrayList<T> mData;
    protected LoaderView mLoaderView;

    public ACardRecyclerHelper(Context context) {
        super(context);
    }

    @Override
    public void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        outState.putParcelableArrayList(SaveUtils.STATE_SAVED_DATA_LIST, mData);
    }

    @Override
    public void onCreate(Bundle savedState, Bundle arguments) {
        if (savedState != null) {
            mData = savedState.getParcelableArrayList(SaveUtils.STATE_SAVED_DATA_LIST);
        }
        if (mData == null && arguments != null) {
            mData = arguments.getParcelableArrayList(SaveUtils.STATE_SAVED_DATA_LIST);
        }
        super.onCreate(savedState, arguments);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        mLoaderView = (LoaderView) layout.findViewById(R.id.loaderview_list);
        setupRecycler();
        if (mData == null || mData.size() < 1) {
            mData = new ArrayList<>();
            if (canRequestInitial()) {
                onRequestInitial();
            }
        } else {
            List<T> tempData = mData;
            mData = new ArrayList<>();
            addData(tempData);
        }
        return layout;
    }

    protected boolean canRequestInitial() {
        return true;
    }

    @Override
    public void setupAdapter() {
        super.setupAdapter();
        mAdapter.setRecyclerView(mRecycler);
        mRecycler.setCardsAdapter(mAdapter);
        setState(LoaderView.STATE_LOADING);
    }

    public void addData(final Collection<T> data) {
        setState(LoaderView.STATE_IDLE);
    }

    public void setState(int state) {
        if (mData == null || mData.size() < 1) {
            if (mLoaderView != null) mLoaderView.setState(state);
        } else {
            if (mLoaderView != null) mLoaderView.setState(LoaderView.STATE_IDLE);
        }
    }

    public abstract void onRequestInitial();
}
