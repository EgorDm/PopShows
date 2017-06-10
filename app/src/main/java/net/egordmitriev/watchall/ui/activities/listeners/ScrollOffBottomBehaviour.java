package net.egordmitriev.watchall.ui.activities.listeners;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by EgorDm on 10-Jun-2017.
 */

public class ScrollOffBottomBehaviour extends CoordinatorLayout.Behavior<FloatingActionMenu> {

    public ScrollOffBottomBehaviour(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, FloatingActionMenu child, int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionMenu child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;

    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionMenu child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        int totalScroll = (dyConsumed + dyUnconsumed);
        if (totalScroll > 0) {
            if(child.isOpened()) {
                child.hideMenu(true);
            }
            child.hideMenuButton(true);
        } else {
            child.showMenuButton(true);
        }
    }
}