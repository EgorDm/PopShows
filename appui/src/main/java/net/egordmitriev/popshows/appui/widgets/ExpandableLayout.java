package net.egordmitriev.popshows.appui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.egordmitriev.appui.R;

/**
 * Created by EgorDm on 4/29/2016.
 */
public class ExpandableLayout extends RelativeLayout {
    private boolean isAnimationRunning = false;
    private boolean isOpened = false;
    private int duration;
    private FrameLayout contentLayout;
    private FrameLayout headerLayout;
    private Animation animation;
    private Drawable mExpandDrawable;
    private Drawable mCollapseDrawable;
    private ImageView mButton;

    public ExpandableLayout(Context context)
    {
        super(context);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        final View rootView = View.inflate(context, R.layout.widget_view_expandable, this);
        headerLayout = (FrameLayout) rootView.findViewById(R.id.view_expandable_headerlayout);
        mButton = (ImageView) rootView.findViewById(R.id.expandable_button);
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableLayout);
        final int headerID = typedArray.getResourceId(R.styleable.ExpandableLayout_el_headerLayout, -1);
        final int contentID = typedArray.getResourceId(R.styleable.ExpandableLayout_el_contentLayout, -1);
        contentLayout = (FrameLayout) rootView.findViewById(R.id.view_expandable_contentLayout);
        View rippleClickLayout = rootView.findViewById(R.id.expand_clickable);

        mExpandDrawable = ContextCompat.getDrawable(context, R.drawable.ic_expand);
        mCollapseDrawable = ContextCompat.getDrawable(context, R.drawable.ic_collapase);

        if (headerID == -1 || contentID == -1)
            throw new IllegalArgumentException("HeaderLayout and ContentLayout cannot be null!");

        if (isInEditMode())
            return;

        duration = typedArray.getInt(R.styleable.ExpandableLayout_el_duration, getContext().getResources().getInteger(android.R.integer.config_shortAnimTime));
        final View headerView = View.inflate(context, headerID, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        headerLayout.addView(headerView);
        final View contentView = View.inflate(context, contentID, null);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        contentLayout.addView(contentView);
        contentLayout.setVisibility(GONE);
        mButton.setVisibility(VISIBLE);
        mButton.setImageDrawable(isOpened ? mCollapseDrawable : mExpandDrawable);
        rippleClickLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!isAnimationRunning)
                {
                    if (contentLayout.getVisibility() == VISIBLE)
                        collapse(contentLayout);
                    else
                        expand(contentLayout);

                    isAnimationRunning = true;
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            isAnimationRunning = false;
                        }
                    }, duration);
                }
            }
        });

        typedArray.recycle();
    }

    private void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 0;
        v.setVisibility(VISIBLE);

        mButton.setImageDrawable(mCollapseDrawable);
        animation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t)
            {
                if (interpolatedTime == 1)
                    isOpened = true;
                v.getLayoutParams().height = (interpolatedTime == 1) ? LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }


            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(duration);
        v.startAnimation(animation);
    }

    private void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        mButton.setImageDrawable(mExpandDrawable);
        animation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1)
                {
                    v.setVisibility(View.GONE);
                    isOpened = false;
                }
                else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration(duration);
        v.startAnimation(animation);
    }

    public boolean isOpened()
    {
        return isOpened;
    }

    public void show() {
        if (!isAnimationRunning)
        {
            expand(contentLayout);
            isAnimationRunning = true;
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    isAnimationRunning = false;
                }
            }, duration);
        }
    }

    public FrameLayout getHeaderLayout()
    {
        return headerLayout;
    }

    public FrameLayout getContentLayout()
    {
        return contentLayout;
    }

    public void hide() {
        if (!isAnimationRunning)
        {
            collapse(contentLayout);
            isAnimationRunning = true;
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    isAnimationRunning = false;
                }
            }, duration);
        }
    }

    @Override
    public void setLayoutAnimationListener(Animation.AnimationListener animationListener) {
        animation.setAnimationListener(animationListener);
    }
}