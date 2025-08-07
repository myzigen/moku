package com.mhr.mobile.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

public class GestureControlViewPager extends ViewPager {
    public boolean isSwipeGestureEnabled = true;

    public GestureControlViewPager(Context context) {
        super(context);
    }

    public GestureControlViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // returning false will not propagate the swipe event
        return isSwipeGestureEnabled ? super.onTouchEvent(ev) : false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSwipeGestureEnabled ? super.onInterceptTouchEvent(ev) : false;
    }
}

