package com.sharad.epocket.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
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
    private View mBackground;

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

        mBackground = new View(context, attrs, defStyleAttr);
        Drawable d = ContextCompat.getDrawable(context, R.drawable.circle);
        //((GradientDrawable)d).setColor(ContextCompat.getColor(context, R.color.accent));
        mBackground.setBackground(d);
        mBackground.setVisibility(GONE);
        addView(mBackground);

        mMainButton = new FloatingActionButton(context, attrs, defStyleAttr);
        addView(mMainButton);
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
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // Places the main button as last
        int mainWidth = mMainButton.getMeasuredWidth();
        int mainHeight = mMainButton.getMeasuredHeight();
        int mainX = getMeasuredWidth() - mMainButton.getMeasuredWidth() - PADDING;
        int mainY = getMeasuredHeight() - mMainButton.getMeasuredHeight() - PADDING;
        mMainButton.layout(mainX, mainY, mainX + mainWidth, mainY + mainHeight);

        int bgCenterX = mainX + mainWidth/2;
        int bgCenterY = mainY + mainHeight/2;

        if (isExpanded && (mBackground.getVisibility() != GONE)) {
            int radius = mainWidth + MARGIN_BETWEEN_FABS + mainWidth / 2;
            mBackground.layout(bgCenterX - radius, bgCenterY - radius, bgCenterX + radius, bgCenterY + radius);
        }

        final int childCount = getChildCount();
        int angle = 180;
        for (int i = childCount - 1; i >= 2; i--) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE || child == mMainButton) {
                continue;
            }

            float newX = (float)(bgCenterX + (mainWidth + MARGIN_BETWEEN_FABS) * Math.cos(Math.toRadians(angle)));
            float newY = (float)(bgCenterY + (mainWidth + MARGIN_BETWEEN_FABS) * Math.sin(Math.toRadians(angle)));

            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            final int childX = (int)newX - child.getMeasuredWidth()/2;
            final int childY = (int)newY - child.getMeasuredHeight()/2;
            child.layout(childX, childY, childX + childWidth, childY + childHeight);
            child.setAlpha(isExpanded ? 1 : 0);

            if (!isExpanded) {
                child.setTranslationX(mainX - childX);
                child.setTranslationY(mainY - childY);
            }
            angle += 45;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 2 * mMainButton.getMeasuredWidth();
        int height = 2 * mMainButton.getMeasuredHeight();
        width += PADDING + MARGIN_BETWEEN_FABS;
        height += PADDING + MARGIN_BETWEEN_FABS;

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

            final float deltaY = mMainButton.getTop() - child.getTop();
            final float fromYDelta = isExpanded ? 0 : deltaY;
            final float toYDelta = isExpanded ? -deltaY : 0;
            final float deltaX = mMainButton.getLeft() - child.getLeft();
            final float fromXDelta = isExpanded ? 0 : deltaX;
            final float toXDelta = isExpanded ? -deltaX : 0;

            AnimatorSet animSetXY = new AnimatorSet();
            ObjectAnimator y = ObjectAnimator.ofFloat(child, "translationY", fromYDelta, toYDelta);
            ObjectAnimator x = ObjectAnimator.ofFloat(child, "translationX", fromXDelta, toXDelta);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(child, "alpha", 0, 1);

            animSetXY.playTogether(x, y, alpha);
            animSetXY.setInterpolator(accDecInterpolator);
            animSetXY.setDuration(ANIMATION_DURATION);
            animSetXY.start();
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
            final float deltaX = mMainButton.getLeft() - child.getLeft();
            final float fromXDelta = isExpanded ? 0 : -deltaX;
            final float toXDelta = isExpanded ? deltaX : 0;

            AnimatorSet animSetXY = new AnimatorSet();
            ObjectAnimator y = ObjectAnimator.ofFloat(child, "translationY", fromYDelta, toYDelta);
            ObjectAnimator x = ObjectAnimator.ofFloat(child, "translationX", fromXDelta, toXDelta);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(child, "alpha", 1, 0);

            animSetXY.playTogether(x, y, alpha);
            animSetXY.setInterpolator(accDecInterpolator);
            animSetXY.setDuration(ANIMATION_DURATION);
            animSetXY.start();
        }
    }

    private void updateMainFabImage() {
        mMainButton.setImageResource(isExpanded ? R.drawable.ic_close_black_24dp : R.drawable.ic_more_horiz_black_24dp);
    }
}
