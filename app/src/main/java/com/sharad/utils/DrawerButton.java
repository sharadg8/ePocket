package com.sharad.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharad.epocket.R;

/**
 * Created by Sharad on 16-Jun-16.
 */

public class DrawerButton extends ViewGroup {
    private View mIconHolderView;
    private View mLineView;
    private TextView mTitleText;
    private ImageView mIconView;

    public DrawerButton(Context context){
        super(context);
        init(context, null);
    }

    public DrawerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DrawerButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setFocusable(true);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.navigation_button, this, true);

        mIconHolderView = findViewById(R.id.nav_background);
        mLineView = findViewById(R.id.nav_line);
        mIconView = (ImageView) findViewById(R.id.nav_icon);
        mTitleText = (TextView) findViewById(R.id.nav_title);

        setBackground(getContext().getDrawable(R.drawable.ripple_drawable));
        setContentDescription(mTitleText.getText().toString());
    }

    public void setText(String title) {
        mTitleText.setText(title);
    }

    public void setImageResource(int icon) {
        mIconView.setImageResource(icon);
    }

    public void setColor(int color) {
        Drawable d = mIconHolderView.getBackground();
        ((ShapeDrawable)d.mutate()).getPaint().setColor(ContextCompat.getColor(getContext(), color));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = Math.max(width, height);
        setMeasuredDimension(size, size);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int unit = getHeight() / 10;
        int centerX = unit * 5;
        int circleRadius = unit * 3;
        mIconHolderView.layout(centerX-circleRadius, unit, centerX+circleRadius, unit+circleRadius*2);

        int iconSize = mIconView.getDrawable().getIntrinsicWidth();
        int iconTop = ((int)(unit * 3.4) - iconSize/2);
        mIconView.layout(centerX-iconSize/2, iconTop, centerX+iconSize/2, iconTop+iconSize);

        int lineSize = (int)(mIconView.getDrawable().getIntrinsicWidth() * 1.4);
        int lineTop = (int)(unit * 5.4);
        mLineView.layout(centerX-lineSize/2, lineTop, centerX+lineSize/2, lineTop+6);
        mLineView.setAlpha(0.4f);

        int left = (getWidth() - mTitleText.getMeasuredWidth())/2;
        int top = ((int)(unit * 8.2) - mTitleText.getMeasuredHeight()/2);
        mTitleText.layout(left, top, left + mTitleText.getMeasuredWidth(), top+mTitleText.getMeasuredHeight());
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }
}
