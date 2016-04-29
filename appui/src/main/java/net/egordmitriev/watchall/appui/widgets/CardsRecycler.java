package net.egordmitriev.watchall.appui.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import net.egordmitriev.appui.R;
import net.egordmitriev.watchall.appui.adapters.CardsAdapter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.BaseRecyclerViewAdapter;
import it.gmariotti.cardslib.library.view.base.CardViewWrapper;

/**
 * Created by EgorDm on 4/30/2016.
 */
public class CardsRecycler extends RecyclerView implements CardViewWrapper.OnExpandListAnimatorListener {

    /**
     * Card Adapter
     */
    protected CardsAdapter mAdapter;

    //--------------------------------------------------------------------------
    // Custom Attrs
    //--------------------------------------------------------------------------

    /**
     * Default layout to apply to card
     */
    protected
    @LayoutRes
    int list_card_layout_resourceID = R.layout.card_media_small_list_layout;

    /**
     * Layouts to apply to card
     */
    protected
    @LayoutRes
    int[] list_card_layout_resourceIDs;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------
    public CardsRecycler(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CardsRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CardsRecycler(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    //--------------------------------------------------------------------------
    // Init
    //--------------------------------------------------------------------------

    /**
     * Initialize
     *
     * @param attrs
     * @param defStyle
     */
    protected void init(Context context, AttributeSet attrs, int defStyle) {
        //Init attrs
        initAttrs(context, attrs, defStyle);
    }

    /**
     * Init custom attrs.
     *
     * @param attrs
     * @param defStyle
     */
    protected void initAttrs(Context context, AttributeSet attrs, int defStyle) {

        list_card_layout_resourceID = R.layout.list_card_layout;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.card_options, defStyle, defStyle);

        try {
            list_card_layout_resourceID = a.getResourceId(R.styleable.card_options_list_card_layout_resourceID, this.list_card_layout_resourceID);

            int arrayIds = a.getResourceId(R.styleable.card_options_list_card_layout_resourceIDs, 0);
            if (arrayIds > 0) {
                TypedArray layouts = context.getResources().obtainTypedArray(arrayIds);
                if (layouts != null) {
                    list_card_layout_resourceIDs = new int[layouts.length()];
                    for (int i = 0; i < layouts.length(); i++) {
                        list_card_layout_resourceIDs[i] = layouts.getResourceId(i, R.layout.list_card_layout);
                    }
                    layouts.recycle();
                }
            }

        } finally {
            a.recycle();
        }
    }

    //--------------------------------------------------------------------------
    // Adapter
    //--------------------------------------------------------------------------

    /**
     * Set {@link it.gmariotti.cardslib.library.recyclerview.internal.BaseRecyclerViewAdapter} and layout used by items in RecyclerView
     *
     * @param adapter {@link it.gmariotti.cardslib.library.recyclerview.internal.BaseRecyclerViewAdapter}
     */
    public void setCardsAdapter(CardsAdapter adapter) {
        super.setAdapter(adapter);

        //Set Layout used by items
        adapter.setRowLayoutId(list_card_layout_resourceID);
        mAdapter = adapter;

        setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof BaseRecyclerViewAdapter.CardViewHolder) {
                    ((BaseRecyclerViewAdapter.CardViewHolder) viewHolder).recycled = true;
                }
            }
        });
    }

    //--------------------------------------------------------------------------
    // Expand and Collapse animator
    //--------------------------------------------------------------------------

    @Override
    public void onExpandStart(CardViewWrapper viewCard, View expandingLayout) {
        ExpandCollapseHelper.animateExpanding(expandingLayout, viewCard, this);
    }

    @Override
    public void onCollapseStart(CardViewWrapper viewCard, View expandingLayout) {
        ExpandCollapseHelper.animateCollapsing(expandingLayout, viewCard, this);
    }

    /**
     * Helper to animate collapse and expand animation
     */
    private static class ExpandCollapseHelper {

        /**
         * This method expandes the view that was clicked.
         *
         * @param expandingLayout layout to expand
         * @param cardView        cardView
         * @param recyclerView    recyclerView
         */
        public static void animateCollapsing(final View expandingLayout, final CardViewWrapper cardView, final RecyclerView recyclerView) {
            int origHeight = expandingLayout.getHeight();

            ValueAnimator animator = createHeightAnimator(expandingLayout, origHeight, 0);
            animator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(final Animator animator) {
                    expandingLayout.setVisibility(View.GONE);

                    cardView.setExpanded(false);//card.setExpanded(true);

                    notifyAdapter(recyclerView, recyclerView.getLayoutManager().getPosition((View) ((View) cardView).getParent()));

                    Card card = cardView.getCard();
                    if (card.getOnCollapseAnimatorEndListener() != null)
                        card.getOnCollapseAnimatorEndListener().onCollapseEnd(card);
                }
            });
            animator.start();
        }

        /**
         * This method collapse the view that was clicked.
         *
         * @param expandingLayout layout to collapse
         * @param cardView        cardView
         * @param recyclerView    recyclerView
         */
        public static void animateExpanding(final View expandingLayout, final CardViewWrapper cardView, final RecyclerView recyclerView) {
            /* Update the layout so the extra content becomes visible.*/
            expandingLayout.setVisibility(View.VISIBLE);

            View parent = (View) expandingLayout.getParent();
            final int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.AT_MOST);
            final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            expandingLayout.measure(widthSpec, heightSpec);

            ValueAnimator animator = createHeightAnimator(expandingLayout, 0, expandingLayout.getMeasuredHeight());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                final int listViewHeight = recyclerView.getHeight();
                final int listViewBottomPadding = recyclerView.getPaddingBottom();

                final View v = findDirectChild(expandingLayout, recyclerView);

                @Override
                public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                    if (recyclerView.getLayoutManager().canScrollVertically()) {
                        final int bottom = v.getBottom();
                        if (bottom > listViewHeight) {
                            final int top = v.getTop();
                            if (top > 0) {
                                //recyclerView.scrollBy(0,Math.min(bottom - listViewHeight + listViewBottomPadding, top));
                                recyclerView.smoothScrollBy(0, Math.min(bottom - listViewHeight + listViewBottomPadding + 4, top));

                            }
                        }
                    }
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    cardView.setExpanded(true);//card.setExpanded(true);
                    notifyAdapter(recyclerView, recyclerView.getLayoutManager().getPosition((View) ((View) cardView).getParent()));

                    Card card = cardView.getCard();
                    if (card.getOnExpandAnimatorEndListener() != null)
                        card.getOnExpandAnimatorEndListener().onExpandEnd(card);

                }
            });
            animator.start();
        }

        private static View findDirectChild(final View view, final RecyclerView recyclerView) {
            View result = view;
            View parent = (View) result.getParent();
            while (parent != recyclerView) {
                result = parent;
                parent = (View) result.getParent();
            }
            return result;
        }

        public static ValueAnimator createHeightAnimator(final View view, final int start, final int end) {
            ValueAnimator animator = ValueAnimator.ofInt(start, end);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                    int value = (Integer) valueAnimator.getAnimatedValue();

                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.height = value;
                    view.setLayoutParams(layoutParams);
                }
            });
            return animator;
        }

        /**
         * This method notifies the adapter after setting expand value inside cards
         *
         * @param recyclerView
         */
        public static void notifyAdapter(RecyclerView recyclerView, int position) {

            if (recyclerView instanceof CardsRecycler) {

                CardsRecycler cardRecyclerView = (CardsRecycler) recyclerView;
                if (cardRecyclerView.mAdapter != null) {
                    cardRecyclerView.mAdapter.notifyItemChanged(position);
                }
            }
        }
    }
}
