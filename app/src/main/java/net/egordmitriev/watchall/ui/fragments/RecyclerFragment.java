package net.egordmitriev.watchall.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.egordmitriev.watchall.helpers.ARecyclerHelper;

/**
 * Created by EgorDm on 5/1/2016.
 */
public abstract class RecyclerFragment<R extends ARecyclerHelper> extends BaseFragment {

    protected R mHelper;

    protected RecyclerFragment() {

    }

    protected abstract R getHelper();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mHelper = getHelper();
        super.onCreate(savedInstanceState);
        mHelper.create(savedInstanceState, getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mHelper.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mHelper.onSaveState(outState);
        super.onSaveInstanceState(outState);
    }
}
