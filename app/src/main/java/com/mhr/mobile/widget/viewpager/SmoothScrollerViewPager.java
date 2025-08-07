package com.mhr.mobile.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;
import java.lang.ref.WeakReference;

public class SmoothScrollerViewPager extends GestureControlViewPager {

  private float initialXValue = 0f;
  public int direction = SwipeDirection_ALL;
  private SmoothScroller ownScroller = null;

  public SmoothScrollerViewPager(Context context) {
    super(context);
    init();
  }

  public SmoothScrollerViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    setScrollDuration(0);
    isSwipeGestureEnabled = true;
    direction = SwipeDirection_ALL;
  }

  public void setScrollDuration(int millis) {
    try {
      Class<?> viewpager = ViewPager.class;
      java.lang.reflect.Field scroller = viewpager.getDeclaredField("mScroller");
      scroller.setAccessible(true);
      WeakReference<Context> wr = new WeakReference<>(getContext());
      ownScroller = new SmoothScroller(wr.get(), millis);
      scroller.set(this, ownScroller);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    return isSwipeAllowed(ev) ? super.onTouchEvent(ev) : false;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return isSwipeAllowed(ev) ? super.onInterceptTouchEvent(ev) : false;
  }

  private boolean isSwipeAllowed(MotionEvent event) {
    if (direction == SwipeDirection_ALL) return true;
    if (direction == SwipeDirection_NONE) return false;
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      initialXValue = event.getX();
      return true;
    }
    if (event.getAction() == MotionEvent.ACTION_MOVE) {
      try {
        float diffX = event.getX() - initialXValue;
        if (diffX > 0 && direction == SwipeDirection_RIGHT) {
          return false; // swipe from left to right detected
        } else if (diffX < 0 && direction == SwipeDirection_LEFT) {
          return false; // swipe from right to left detected
        }
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
    return true;
  }

  public static final int SwipeDirection_RIGHT = 1;
  public static final int SwipeDirection_LEFT = 2;
  public static final int SwipeDirection_ALL = 0;
  public static final int SwipeDirection_NONE = -1;
  public static final int SCROLL_MODE_DEFAULT = 250;
  public static final int SCROLL_MODE_MEDIUM = 750;
  public static final int SCROLL_MODE_SLOW = 1000;
  public static final int SCROLL_MODE_ULTRA_SLOW = 2000;
}
