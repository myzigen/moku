package com.mhr.mobile.widget.recyclerview;

import com.mikelau.shimmerrecyclerviewx.ShimmerRecyclerViewX;

public class MyShimmerRecyclerView {

  private final ShimmerRecyclerViewX recyclerView;
  private int count = 8;
  private int layout = 0;

  private MyShimmerRecyclerView(ShimmerRecyclerViewX recyclerView) {
    this.recyclerView = recyclerView;
  }

  public static MyShimmerRecyclerView with(ShimmerRecyclerViewX recyclerView) {
    return new MyShimmerRecyclerView(recyclerView);
  }

  public MyShimmerRecyclerView count(int count) {
    this.count = count;
    return this;
  }

  public MyShimmerRecyclerView layout(int layout) {
    this.layout = layout;
    return this;
  }

  public MyShimmerRecyclerView build() {
    if (layout != 0) {
      recyclerView.setDemoChildCount(count);
      recyclerView.setDemoLayoutReference(layout);
    } else {
      throw new IllegalStateException(
          "Shimmer layout must be set using layout(R.layout.shimmer_layout)");
    }
    return this;
  }

  public void start() {
    recyclerView.showShimmerAdapter();
  }

  public void stop() {
    recyclerView.hideShimmerAdapter();
  }
}
