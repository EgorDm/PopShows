package net.egordmitriev.popshows.helpers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by EgorDm on 5/1/2016.
 */
public abstract class ARecyclerHelper<R extends RecyclerView, A extends RecyclerView.Adapter> {

    protected R mRecycler;
    protected A mAdapter;
    protected Context mContext;

    public ARecyclerHelper(Context context) {
        mContext = context;
    }

    public void onSaveState(Bundle outState) {
    }

    public void onCreate(Bundle savedState, Bundle arguments) {

    }

    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(getLayout(), container, false);
        mRecycler = (R) layout.findViewById(net.egordmitriev.popshows.R.id.recycler);
        return layout;
    }

    public void setupRecycler() {
        mRecycler.setLayoutManager(getLayoutManager());
        mRecycler.setHasFixedSize(false);
        mRecycler.setNestedScrollingEnabled(true);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(200);
        animator.setRemoveDuration(200);
        mRecycler.setItemAnimator(animator);
        setupAdapter();
    }

    public abstract A createAdapter();

    public void setupAdapter() {
        mAdapter = createAdapter();
    }

    protected abstract int getLayout();

    protected abstract RecyclerView.LayoutManager getLayoutManager();
}
