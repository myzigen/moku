package com.mhr.mobile.widget.recyclerview;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingItemHorizontal extends RecyclerView.ItemDecoration {

  private final int horizontalSpacing;

  public SpacingItemHorizontal(int horizontalSpacing) {
    this.horizontalSpacing = horizontalSpacing;
  }

  @Override
  public void getItemOffsets(
      @NonNull Rect outRect,
      @NonNull View view,
      @NonNull RecyclerView parent,
      @NonNull RecyclerView.State state) {
    outRect.left = horizontalSpacing;
    outRect.right = horizontalSpacing;
    // outRect.top dan outRect.bottom tetap 0 karena hanya ingin spacing kiri dan kanan
  }
}
