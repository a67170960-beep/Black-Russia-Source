package com.dinuscxj.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/** Local replacement for the small server-list progress indicator. */
public class CircleProgressBar extends View {
    private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int progress;
    private int max = 100;

    public CircleProgressBar(Context context) {
        super(context);
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        float stroke = 5f * getResources().getDisplayMetrics().density;
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(stroke);
        backgroundPaint.setColor(Color.argb(35, 255, 255, 255));
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(stroke);
        progressPaint.setStrokeCap(Paint.Cap.SQUARE);
        progressPaint.setColor(Color.BLUE);
    }

    public void setProgressStartColor(int color) {
        progressPaint.setColor(color);
        invalidate();
    }

    public void setProgressEndColor(int color) {
        progressPaint.setColor(color);
        invalidate();
    }

    public void setProgress(int value) {
        progress = Math.max(0, Math.min(value, max));
        invalidate();
    }

    public void setMax(int value) {
        max = Math.max(1, value);
        progress = Math.min(progress, max);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float halfStroke = progressPaint.getStrokeWidth() / 2f;
        RectF bounds = new RectF(halfStroke, halfStroke, getWidth() - halfStroke, getHeight() - halfStroke);
        canvas.drawArc(bounds, 0f, 360f, false, backgroundPaint);
        float sweep = 360f * progress / max;
        canvas.drawArc(bounds, -90f, sweep, false, progressPaint);
    }
}
