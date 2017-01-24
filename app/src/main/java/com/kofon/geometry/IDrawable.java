package com.kofon.geometry;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface IDrawable{
    void onDraw(Canvas canvas);
    boolean touchEvent(MotionEvent event);
}
