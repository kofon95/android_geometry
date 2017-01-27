package com.kofon.geometry.drawable;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public interface IDrawable{
    void onDraw(Canvas canvas);
    void init(View container);
}
