package com.mhr.mobile.widget.viewpager;

// NonSwipeableViewPager.java
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class NonSwipeViewPager {

  public static void setUserInputEnabled(ViewPager2 viewPager, boolean enabled) {
    RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);
    recyclerView.setOnTouchListener(enabled ? null : (v, event) -> true);
  }
}
