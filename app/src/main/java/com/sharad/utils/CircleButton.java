package com.sharad.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Checkable;
import android.widget.ImageView;

import com.sharad.epocket.R;

public class CircleButton extends ImageView implements Checkable {

	public static final int CIRCLE_BUTTON_TYPE_COLOR = 0;
	public static final int CIRCLE_BUTTON_TYPE_ICON = 1;
	private int mButtonType = CIRCLE_BUTTON_TYPE_COLOR;
	private int colorFilterNormal;
	private int colorFilterSelected;

	private static final int PRESSED_COLOR_LIGHTUP = 255 / 25;
	private static final int PRESSED_RING_ALPHA = 75;
	private static final int DEFAULT_PRESSED_RING_WIDTH_DIP = 4;
	private static final int ANIMATION_TIME_ID = android.R.integer.config_shortAnimTime;

	private int centerY;
	private int centerX;
	private int outerRadius;
	private int pressedRingRadius;

	private Paint circlePaint;
	private Paint focusPaint;

	private float animationProgress;

	private int pressedRingWidth;
	private int defaultColor = Color.BLACK;
	private int pressedColor;
	private ObjectAnimator pressedAnimator;
    private boolean mChecked = false;
    private int srcResource = 0;
    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

	public CircleButton(Context context) {
		super(context);
		init(context, null);
	}

	public CircleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public CircleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
		if(mChecked != checked) {
			mChecked = checked;
			if (mChecked) {
				setImageResource(srcResource);
			} else {
				setImageDrawable(null);
			}
			refreshDrawableState();
		}
    }

    public void toggle() {
        setChecked(!mChecked);
    }

	public void setButtonType(int type) {
		mButtonType = type;
	}

	@Override
	public void setImageResource(int id) {
		srcResource = id;
	}

	public int getImageResource() {
		return this.srcResource;
	}

	public final void setColorFilter(int color, int colorSelected) {
		colorFilterNormal = color;
		colorFilterSelected = colorSelected;
		setColorFilter(color);
	}

    @Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);

		if (circlePaint != null) {
			circlePaint.setColor(pressed ? pressedColor : defaultColor);
		}

		if (pressed) {
			showPressedRing();
		} else {
			hidePressedRing();
			//toggle();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(mButtonType == CIRCLE_BUTTON_TYPE_COLOR) {
			canvas.drawCircle(centerX, centerY, pressedRingRadius + animationProgress, focusPaint);
		}
		if(mChecked || (mButtonType == CIRCLE_BUTTON_TYPE_COLOR)) {
			canvas.drawCircle(centerX, centerY, outerRadius - pressedRingWidth, circlePaint);
		}
		if(mChecked || (mButtonType == CIRCLE_BUTTON_TYPE_ICON)) {
			Drawable d = getResources().getDrawable(srcResource);
			if((mButtonType == CIRCLE_BUTTON_TYPE_ICON)) {
				int filter = mChecked ? colorFilterSelected : colorFilterNormal;
				d.setColorFilter(filter, PorterDuff.Mode.SRC_ATOP);
			}
			d.setBounds(centerX - 24, centerY - 24, centerX + 24, centerY + 24);
			d.draw(canvas);
		}
		super.onDraw(canvas);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		centerX = w / 2;
		centerY = h / 2;
		outerRadius = Math.min(w, h) / 2;
		pressedRingRadius = outerRadius - pressedRingWidth - pressedRingWidth / 2;
	}

	public float getAnimationProgress() {
		return animationProgress;
	}

	public void setAnimationProgress(float animationProgress) {
		this.animationProgress = animationProgress;
		this.invalidate();
	}

	public int getColor() {
		return this.defaultColor;
	}
	public void setColor(int color) {
		this.defaultColor = color;
		this.pressedColor = getHighlightColor(color, PRESSED_COLOR_LIGHTUP);

		circlePaint.setColor(defaultColor);
		focusPaint.setColor(defaultColor);
		focusPaint.setAlpha(PRESSED_RING_ALPHA);

		this.invalidate();
	}

	private void hidePressedRing() {
		pressedAnimator.setFloatValues(pressedRingWidth, 0f);
		pressedAnimator.start();
	}

	private void showPressedRing() {
		pressedAnimator.setFloatValues(animationProgress, pressedRingWidth);
		pressedAnimator.start();
	}

	private void init(Context context, AttributeSet attrs) {
		this.setFocusable(true);
		this.setScaleType(ScaleType.CENTER_INSIDE);
		setClickable(true);

		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setStyle(Paint.Style.FILL);

		focusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		focusPaint.setStyle(Paint.Style.STROKE);

		pressedRingWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PRESSED_RING_WIDTH_DIP, getResources()
				.getDisplayMetrics());

		int color = Color.BLACK;
		if (attrs != null) {
			final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleButton);
			color = a.getColor(R.styleable.CircleButton_cb_color, color);
			pressedRingWidth = (int) a.getDimension(R.styleable.CircleButton_cb_pressedRingWidth, pressedRingWidth);
			a.recycle();
		}

		setColor(color);

		focusPaint.setStrokeWidth(pressedRingWidth);
		final int pressedAnimationTime = getResources().getInteger(ANIMATION_TIME_ID);
		pressedAnimator = ObjectAnimator.ofFloat(this, "animationProgress", 0f, 0f);
		pressedAnimator.setDuration(pressedAnimationTime);

        setImageDrawable(null);
    }

    private int getHighlightColor(int color, int amount) {
        return Color.argb(Math.min(255, Color.alpha(color)), Math.min(255, Color.red(color) + amount),
                Math.min(255, Color.green(color) + amount), Math.min(255, Color.blue(color) + amount));
    }


}