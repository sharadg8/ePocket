package com.sharad.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.sharad.epocket.R;

/**
 * Created by Sharad on 19-Jun-16.
 */

public class FloatingActionMenu extends ViewGroup {
    private static final int ANIMATION_DURATION = 300;
    private static int MARGIN_BETWEEN_FABS;
    private static int PADDING;

    private boolean isExpanded;

    private FloatingActionButton mMainButton;

    public FloatingActionMenu(Context context) {
        this(context, null, 0);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        MARGIN_BETWEEN_FABS = (int) context.getResources().getDimension(R.dimen.fam_margin_between_fab);
        PADDING = (int) context.getResources().getDimension(R.dimen.fam_padding);
        isExpanded = false;

        mMainButton = new FloatingActionButton(context, attrs, defStyleAttr);

        configureMainButton();
    }

    private void configureMainButton() {
        //mMainButton.setBackgroundTintList(backgroundNormal);
        //mMainButton.setRippleColor(backgroundPressed);
        mMainButton.setColorFilter(Color.WHITE);

        updateMainFabImage();

        mMainButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                toggle();
            }
        });

        addView(mMainButton);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int collapsedMainFabY = b - t - mMainButton.getMeasuredHeight() - PADDING;
        int height = PADDING;

        final int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE || child == mMainButton) {
                continue;
            }

            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            final int childX = getMeasuredWidth() - child.getMeasuredWidth() - PADDING;
            final int deltaY = height + childHeight;
            child.layout(childX, height, childX + childWidth, deltaY);
            child.setAlpha(isExpanded ? 1 : 0);

            if (!isExpanded) {
                child.setTranslationY(collapsedMainFabY - height);
            }

            height += childHeight + (i <= 1 ? MARGIN_BETWEEN_FABS : 0);
        }

        // Places the main button as last
        final int mainWidth = mMainButton.getMeasuredWidth();
        final int mainHeight = mMainButton.getMeasuredHeight();
        final int mainX = getMeasuredWidth() - mMainButton.getMeasuredWidth() - PADDING;
        mMainButton.layout(mainX, height, mainX + mainWidth, height + mainHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final int childWidth = child.getMeasuredWidth();
            if (childWidth > width) {
                width = childWidth;
            }

            height += child.getMeasuredHeight();
        }

        // Add padding
        width += 2 * PADDING;
        height += 2 * PADDING + MARGIN_BETWEEN_FABS;

        setMeasuredDimension(width, height);
    }

    @Override
    protected boolean isChildrenDrawingOrderEnabled() {
        return true;
    }

    @Override
    protected int getChildDrawingOrder(final int childCount, final int i) {
        return childCount - (i + 1);
    }

    public void toggle() {
        if (isExpanded) {
            collapse();
        } else {
            expand();
        }

        isExpanded = !isExpanded;

        updateMainFabImage();
    }

    /**
     * Expands this {@link FloatingActionMenu}. Each child will move vertically to its original position above the main FAB.
     */
    private void expand() {
        final AccelerateDecelerateInterpolator accDecInterpolator = new AccelerateDecelerateInterpolator();
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE || child == mMainButton) {
                continue;
            }

            final ValueAnimator animator = ValueAnimator.ofFloat(mMainButton.getTop() - child.getTop(), 0);
            animator.setDuration(ANIMATION_DURATION);
            animator.setInterpolator(accDecInterpolator);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull final ValueAnimator animation) {
                    child.setAlpha(animation.getAnimatedFraction());
                    child.setTranslationY((Float) animation.getAnimatedValue());
                }
            });
            animator.start();
        }
    }

    /**
     * Each child will translate vertically, from their actual position to behind the main FAB.
     */
    private void collapse() {
        final AccelerateDecelerateInterpolator accDecInterpolator = new AccelerateDecelerateInterpolator();
        final int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE || child == mMainButton) {
                continue;
            }

            final float deltaY = mMainButton.getTop() - child.getTop();
            final float fromYDelta = isExpanded ? 0 : -deltaY;
            final float toYDelta = isExpanded ? deltaY : 0;

            final ValueAnimator animator = ValueAnimator.ofFloat(fromYDelta, toYDelta);
            animator.setDuration(ANIMATION_DURATION);
            animator.setInterpolator(accDecInterpolator);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull final ValueAnimator animation) {
                    child.setAlpha(1 - animation.getAnimatedFraction());
                    child.setTranslationY((Float) animation.getAnimatedValue());
                }
            });
            animator.start();
        }
    }

    private void updateMainFabImage() {
        mMainButton.setImageResource(isExpanded ? R.drawable.ic_close_white_24dp : R.drawable.ic_more_horiz_black_24dp);
    }
}
