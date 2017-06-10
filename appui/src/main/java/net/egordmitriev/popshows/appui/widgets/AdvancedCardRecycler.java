package net.egordmitriev.popshows.appui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by EgorDm on 4/30/2016.
 */
public class AdvancedCardRecycler extends CardsRecycler {

    protected
    @FloatRange
    float column_width;

    private boolean mGridLayout;
    private Runnable mLoadMoreCallback;

    public AdvancedCardRecycler(Context context) {
        super(context);
    }

    public AdvancedCardRecycler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvancedCardRecycler(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyle) {
        super.init(context, attrs, defStyle);
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.columnWidth
            };
            TypedArray array = context.obtainStyledAttributes(
                    attrs, attrsArray);
            //TODO Add correct one?
            column_width = array.getDimensionPixelSize(0, -1);
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (mGridLayout) {
            if (column_width > 0) {
                int spanCount = (int) Math.max(1, getMeasuredWidth() / column_width);
                ((GridLayoutManager) getLayoutManager()).setSpanCount(spanCount);
            }
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        if (layout instanceof GridLayoutManager) {
            mGridLayout = true;
            this.addItemDecoration(new SpacesItemDecoration(0));
        } else if (layout instanceof LinearLayoutManager) {
            mGridLayout = false;
        }
    }

    public boolean loadingMore = false;
    public void setLoadMoreCallback(Runnable loadNextCallback) {
        mLoadMoreCallback = loadNextCallback;
        OnScrollListener listener = new OnScrollListener() {
            int visibleThreshold = 5;
            int firstVisibleItem, visibleItemCount, totalItemCount, previousTotal;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mGridLayout) {
                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = getLayoutManager().getItemCount();
                    firstVisibleItem = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                }

                if (loadingMore) {
                    if (totalItemCount > previousTotal) {
                        loadingMore = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loadingMore && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    if (mLoadMoreCallback != null) {
                        mLoadMoreCallback.run();
                    }
                    loadingMore = true;
                }
            }
        };
        addOnScrollListener(listener);
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = space;
        }
    }
}