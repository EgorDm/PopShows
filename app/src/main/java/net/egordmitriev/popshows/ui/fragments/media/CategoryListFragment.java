package net.egordmitriev.popshows.ui.fragments.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.appui.listeners.RecyclerClickListener;
import net.egordmitriev.popshows.helpers.ARecyclerHelper;
import net.egordmitriev.popshows.pojo.data.Category;
import net.egordmitriev.popshows.pojo.tmdb.MovieModel;
import net.egordmitriev.popshows.ui.activities.media.CategoryPageActivity;
import net.egordmitriev.popshows.ui.fragments.base.RecyclerFragment;
import net.egordmitriev.popshows.utils.MediaUtils;
import net.egordmitriev.popshows.utils.SaveUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by EgorDm on 5/1/2016.
 */
public class CategoryListFragment extends RecyclerFragment<ARecyclerHelper<RecyclerView, CategoryListFragment.CategoryAdapter>> implements RecyclerClickListener.OnItemClickListener {

    public int categoryType = MovieModel.TYPE;
    private ArrayList<Category> mCategories;

    public CategoryListFragment() {
        super();
    }

    @Override
    protected ARecyclerHelper<RecyclerView, CategoryAdapter> getHelper() {
        return new ARecyclerHelper<RecyclerView, CategoryAdapter>(getActivity()) {

            @Override
            public void onSaveState(Bundle outState) {
                if(mAdapter != null) {
                    outState.putParcelableArrayList(SaveUtils.STATE_SAVED_DATA_LIST, mCategories);
                }
                super.onSaveState(outState);
            }

            @Override
            public void onCreate(Bundle savedState, Bundle arguments) {
                if(savedState != null) {
                    mCategories = savedState.getParcelableArrayList(SaveUtils.STATE_SAVED_DATA_LIST);
                }
                super.onCreate(savedState, arguments);
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View layout = super.onCreateView(inflater, container, savedInstanceState);
                setupRecycler();
                return layout;
            }

            @Override
            public CategoryAdapter createAdapter() {
                if(mCategories == null) {
                    mCategories = MediaUtils.getCategoryList(mContext, categoryType);
                }
                return new CategoryAdapter(mCategories);
            }

            @Override
            public void setupAdapter() {
                super.setupAdapter();
                mRecycler.setAdapter(mAdapter);
                mRecycler.addOnItemTouchListener(new RecyclerClickListener(mContext, mRecycler, CategoryListFragment.this));
            }

            @Override
            protected int getLayout() {
                return R.layout.fragment_base_recycler;
            }

            @Override
            public RecyclerView.LayoutManager getLayoutManager() {
                return new LinearLayoutManager(mContext);
            }
        };
    }

    @Override
    public void onItemClick(View view, int position) {
        openCategory(getActivity(), mCategories.get(position));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    public static void openCategory(Context context, Category category) {
        Intent intent = new Intent(context, CategoryPageActivity.class);
        intent.putExtra(SaveUtils.SAVED_DISPLAY_DATA, category);
        context.startActivity(intent);
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

        protected List<Category> mData;

        public CategoryAdapter(List<Category> data) {
            mData = data;
        }

        public void setAll(@NonNull final Collection<Category> collection) {
            mData = new ArrayList<>(collection);
            notifyDataSetChanged();
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(getActivity()).inflate(R.layout.view_category_list_item, parent, false);
            return new CategoryViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {
            holder.initView(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class CategoryViewHolder extends RecyclerView.ViewHolder {
            protected TextView mTitleView;
            protected Category mData;


            public CategoryViewHolder(View itemView) {
                super(itemView);
            }

            public void initView(Category data) {
                mData = data;
                mTitleView = (TextView) itemView.findViewById(R.id.media_category_title);
                if(mTitleView != null) mTitleView.setText(mData.title);
            }
        }
    }
}
