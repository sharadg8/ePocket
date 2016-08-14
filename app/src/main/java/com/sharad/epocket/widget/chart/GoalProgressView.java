package com.sharad.epocket.widget.chart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Utils;

/**
 * Created by Sharad on 14-Aug-16.
 */

public class GoalProgressView extends FrameLayout {
    private static final int START_ANGLE = 270;

    Background backgroundView;
    Progress progressView;
    Pointer pointerView;

    public GoalProgressView(Context context) {
        this(context, null);
    }

    public GoalProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        backgroundView = new Background(context);
        LayoutParams backgroundParams =  new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        int backgroundMargin = Utils.dp2pix(18);
        backgroundParams.setMargins(backgroundMargin, backgroundMargin, backgroundMargin, backgroundMargin);
        backgroundView.setLayoutParams(backgroundParams);
        backgroundView.setElevation(Utils.dp2pix(12));

        addView(backgroundView);

        progressView = new Progress(context, 18);
        LayoutParams progressParams =  new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        int progressMargin = Utils.dp2pix(30);
        progressParams.setMargins(progressMargin, progressMargin, progressMargin, progressMargin);
        progressView.setLayoutParams(progressParams);
        progressView.setElevation(Utils.dp2pix(12));

        addView(progressView);

        pointerView = new Pointer(context);
        int pointerPadding = Utils.dp2pix(48);
        pointerView.setPadding(pointerPadding, pointerPadding, pointerPadding, pointerPadding);
        pointerView.setElevation(Utils.dp2pix(13));

        addView(pointerView);
    }

    public void setProgress(float progress, float max) {
        this.progressView.setProgress(progress, max);
    }

    public void setPointer(float pointer, float max) {
        this.pointerView.setPointer(pointer, max);
    }

    private class Background extends View {
        Paint paint;
        Rect bound;
        public Background(Context context) {
            super(context);

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setOutlineProvider(new BackgroundOutline(w, h));
            }

            bound = new Rect(0, 0, w, h);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float radius = Math.min(bound.width(), bound.height()) / 2;
            canvas.drawCircle(bound.centerX(), bound.centerY(), radius, paint);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private class BackgroundOutline extends ViewOutlineProvider {

            int width;
            int height;

            BackgroundOutline(int width, int height) {
                this.width = width;
                this.height = height;
            }

            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, width, height);
            }
        }
    }

    private class Progress extends View {
        Paint paint;
        RectF bound;
        int strokeWidth;
        float sweepAngle = 270;

        public Progress(Context context, int strokeDp) {
            super(context);

            strokeWidth = Utils.dp2pix(strokeDp);

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(strokeWidth);
            paint.setColor(ContextCompat.getColor(context, R.color.accent));
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            int halfStroke = strokeWidth/2;
            bound = new RectF(halfStroke, halfStroke, w-halfStroke, h-halfStroke);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Path path = new Path();
            path.arcTo(bound, START_ANGLE, sweepAngle, true);
            canvas.drawPath(path, paint);
        }

        public void setProgress(float progress, float max) {
            sweepAngle = 360 * progress / max;
            sweepAngle = Math.min(sweepAngle, 359.99f);
            invalidate();
        }
    }

    private class Pointer extends View {
        Paint paint;
        Paint runnerPaint;
        Path path;
        RectF circleBound;
        RectF runnerBound;
        float sweepAngle = 15;
        Bitmap runnerOrig;
        Bitmap runner;
        Point runnerPoint;

        public Pointer(Context context) {
            super(context);

            path = new Path();
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(ContextCompat.getColor(context, R.color.primary));

            runnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            runnerPaint.setColor(Color.WHITE);

            Drawable d = ContextCompat.getDrawable(context, R.drawable.ic_directions_run_black_24dp);
            DrawableCompat.setTint(d, Color.WHITE);
            runnerOrig = getBitmap((VectorDrawable)d);
            runnerPoint = new Point();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            circleBound = new RectF(getPaddingLeft(), getPaddingTop(), w-getPaddingRight(), h-getPaddingBottom());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setOutlineProvider(new PointerOutline(circleBound));
            }
            float radius = getPaddingLeft()/3;
            runnerBound = new RectF(circleBound.centerX() - radius, 0, circleBound.centerX()+radius, 2*radius);
            setPointer(15, 100);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawPath(path, paint);
            int x = runnerPoint.x - runner.getWidth()/2;
            int y = runnerPoint.y - runner.getHeight()/2;
            canvas.drawBitmap(runner, x, y, runnerPaint);
        }

        private Bitmap getBitmap(VectorDrawable vectorDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
            return bitmap;
        }

        public void setPointer(float pointer, float max) {
            sweepAngle = 360 * pointer / max;

            createPointerPath();

            Matrix matrix = new Matrix();
            matrix.postRotate(sweepAngle, circleBound.centerX(), circleBound.centerX());
            path.transform(matrix);

            runner = Bitmap.createBitmap(runnerOrig, 0, 0, runnerOrig.getWidth(), runnerOrig.getHeight(), matrix, true);
            float[] pts = new float[2];
            pts[0] = runnerBound.centerX();
            pts[1] = runnerBound.centerY();
            matrix.mapPoints(pts);
            runnerPoint = new Point((int)pts[0], (int)pts[1]);

            invalidate();
        }

        private void createPointerPath() {
            float radius = Math.min(runnerBound.width(), runnerBound.height()) / 2;
            path.reset();
            path.arcTo(circleBound, 276, 348, true);
            path.lineTo(runnerBound.centerX(), runnerBound.centerY());
            path.arcTo(runnerBound, 96, 348, true);
            path.close();

            path.addCircle(runnerBound.centerX(), runnerBound.centerY(), radius, Path.Direction.CW);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private class PointerOutline extends ViewOutlineProvider {
            Rect bound;
            PointerOutline(RectF bound) {
                this.bound = new Rect();
                bound.round(this.bound);
            }

            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(bound);
            }
        }
    }
}
