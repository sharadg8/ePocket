package com.sharad.epocket.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

/**
 * Created by Sharad on 23-Jun-16.
 */

public class ZigZagCardView extends FrameLayout {
    private Paint paint;
    private Path path;
    private Paint shadowPaint;
    private final int DESIRED_DUTY_CYCLE = 12;
    private float zigzagWidth;
    private float zigzagHeight;

    public ZigZagCardView(Context context) {
        this(context, null, 0);
    }

    public ZigZagCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZigZagCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setColor(Color.BLACK);

        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int count = w / DESIRED_DUTY_CYCLE;
        zigzagHeight = zigzagWidth = w / (count + 0.5f);
        count += 3;
        path.reset();
        path.moveTo(0, zigzagHeight);
        boolean isZig = true;
        float x = -zigzagWidth;
        float y = 0;
        for(int i=0; i<count; i++) {
            y = isZig ? 0 : zigzagHeight;
            y += getPaddingTop();
            path.lineTo(x, y);
            x += zigzagWidth;
            isZig = !isZig;
        }
        isZig = !isZig;
        for(int i=0; i<count; i++) {
            y = isZig ? h : h - zigzagHeight;
            y -= getPaddingBottom();
            path.lineTo(x, y);
            x -= zigzagWidth;
            isZig = !isZig;
        }

        LinearGradient lg = new LinearGradient(0, 0, 0, h,
                new int[]{Color.TRANSPARENT, 0x60000000, Color.TRANSPARENT},
                new float[]{0, 0.5f, 1}, Shader.TileMode.REPEAT);

        shadowPaint.setShader(lg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new CustomOutline(w, h));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class CustomOutline extends ViewOutlineProvider {
        int width;
        int height;

        CustomOutline(int width, int height) {
            this.width = width;
            this.height = height - (int)zigzagHeight;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRect(0, (int)zigzagHeight, width, height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(canvas.getClipBounds(), shadowPaint);
        canvas.drawPath(path, paint);
    }
}
