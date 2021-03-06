package net.egordmitriev.popshows.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.egordmitriev.popshows.R;

/**
 * Created by EgorDm on 4/29/2016.
 */
public class WrapperView extends LinearLayout {
    private ViewGroup mChildContainer;

    public WrapperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WrapperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {

        final int parentLayoutID;
        final int childContainerID;
        {
            final TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.WrapperView, defStyle, 0);

            parentLayoutID = styledAttributes.getResourceId(R.styleable.WrapperView_parentLayoutID, 0);
            childContainerID = styledAttributes.getResourceId(R.styleable.WrapperView_childContainerID, 0);

            styledAttributes.recycle();
        }

        if (parentLayoutID == 0 || childContainerID == 0) {
            Log.e("Error", "WrappedLayout.WrappedLayout(): Error reading custom attributes from XML. layoutToInflate = " + parentLayoutID + ", childContainerID =" + childContainerID);
        } else {
            final View inflatedLayout = View.inflate(getContext(), parentLayoutID, this);
            mChildContainer = (ViewGroup) inflatedLayout.findViewById(childContainerID);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (mChildContainer == null) {
            super.addView(child, index, params);
        } else {
            mChildContainer.addView(child, index, params);
        }
    }
}
