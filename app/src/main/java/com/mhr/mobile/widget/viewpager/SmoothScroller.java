package com.mhr.mobile.widget.viewpager;

import android.content.Context;
import android.widget.Scroller;
import android.view.animation.DecelerateInterpolator;

class SmoothScroller extends Scroller {
    private int durationScrollMillis = 1;

    public SmoothScroller(Context context, int durationScroll) {
        super(context, new DecelerateInterpolator());
        this.durationScrollMillis = durationScroll;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, durationScrollMillis);
    }
}

