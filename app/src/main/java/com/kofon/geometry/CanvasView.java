package com.kofon.geometry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by kofon on 24.01.17.
 */
public class CanvasView extends View {
    private final IDrawable mDrawable;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrawable = isInEditMode() ? null : ((IDrawable) context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDrawable.touchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()) return;
        mDrawable.onDraw(canvas);
    }
}
