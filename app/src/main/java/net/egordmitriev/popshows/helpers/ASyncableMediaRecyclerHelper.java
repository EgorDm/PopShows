package net.egordmitriev.popshows.helpers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import net.egordmitriev.loaderview.LoaderView;
import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.api.base.APIError;
import net.egordmitriev.popshows.appui.adapters.LoaderCardsAdapter;
import net.egordmitriev.popshows.appui.widgets.cards.MediaCard;
import net.egordmitriev.popshows.pojo.CardedModel;
import net.egordmitriev.popshows.utils.DataCallback;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by EgorDm on 5/1/2016.
 */
public abstract class ASyncableMediaRecyclerHelper<T extends CardedModel, U>
        extends AMediaCardRecyclerHelper<T, LoaderCardsAdapter<MediaCard>>
        implements LoaderView.LoaderViewCallback, Runnable {

    protected int mCurrentPage = 1;
    protected boolean mLockedRequest;
    protected final DataCallback<U> mRequestCallback;

    public ASyncableMediaRecyclerHelper(Context context) {
        super(context);
        mRequestCallback = new DataCallback<U>() {
            @Override
            public void success(U data) {
                mLockedRequest = false;
                handleData(data);
            }

            @Override
            public void failure(APIError error) {
                setState(LoaderView.STATE_ERROR);
                mLockedRequest = false;
                if (error != null) Logger.e(error.getMessage());
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        mLoaderView.setListener(this);
        setupLoadMore();
        return layout;
    }

    protected void setupLoadMore() {
        mRecycler.setLoadMoreCallback(this);
    }

    public void requestData() {
        setState(LoaderView.STATE_LOADING);
        mLockedRequest = true;
        mRequestData.page = mCurrentPage;
    }

    @Override
    public void onRequestInitial() {
        setState(LoaderView.STATE_LOADING);
        mLockedRequest = true;
    }

    @Override
    public void onRetryClick() {
        if (!mLockedRequest && !dataEnded) {
            requestData();
        }
    }

    //Loading more
    @Override
    public void run() {
        mCurrentPage++;
        if (!dataEnded && !mLockedRequest) {
            requestData();
        }
    }

    protected abstract void handleData(U data);

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_card_list_small;
    }

    @Override
    public void addData(Collection<T> data) {
        super.addData(data);
        mAdapter.setLoaderState(LoaderView.STATE_LOADING);
    }

    @Override
    public void setState(int state) {
        super.setState(state);
        if (mData == null || mData.size() < 1) {
            mAdapter.setLoaderState(LoaderView.STATE_IDLE);
        } else {
            mAdapter.setLoaderState(state);
        }
    }

    @Override
    public LoaderCardsAdapter<MediaCard> createAdapter() {
        return new LoaderCardsAdapter<>(mContext, new ArrayList<MediaCard>());
    }
}
