package com.mhr.mobile.widget;

import android.graphics.Color;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.R;
import com.mhr.mobile.util.QiosColor;

public class WidgetLayout {
  private NestedScrollView scrollView;
  private RecyclerView recyclerView;

  public WidgetLayout(NestedScrollView scrollView) {
    this.scrollView = scrollView;
  }

  public WidgetLayout(RecyclerView recycler) {
    this.recyclerView = recycler;
  }

  public static WidgetLayout with(NestedScrollView scrollView) {
    return new WidgetLayout(scrollView);
  }

  public static WidgetLayout with(RecyclerView scrollView) {
    return new WidgetLayout(scrollView);
  }

  public WidgetLayout scrollParallax(View slow, View toolbar, View bgKategori) {
    scrollView.setOnScrollChangeListener(
        (NestedScrollView.OnScrollChangeListener)
            (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
              float parallaxSpeed = 0.5f;
              slow.animate().translationY(scrollY * parallaxSpeed).setDuration(0).start();

              int maxScroll = 300;
              float scrollRatio = Math.min(1f, (float) scrollY / maxScroll);

              int fromColor = QiosColor.getColor(slow.getContext(), R.color.me_toolbar_back);
              int toColor = QiosColor.getColor(slow.getContext(), R.color.me_toolbar_front);
              int blended = blendColors(fromColor, toColor, scrollRatio);

              toolbar.setBackgroundColor(blended);
            });
    return this;
  }

  public WidgetLayout scrollParallaxRecycler(View backdrop, View toolbar) {
    recyclerView.addOnScrollListener(
        new RecyclerView.OnScrollListener() {
          private boolean isBackdropVisible = true;

          @Override
          public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            // Deteksi arah scroll
            if (dy > 10 && isBackdropVisible) {
              // Scroll ke bawah → sembunyikan backdrop
              backdrop.animate().translationY(-backdrop.getHeight()).setDuration(200).start();
              isBackdropVisible = false;
            } else if (dy < -10 && !isBackdropVisible) {
              // Scroll ke atas → tampilkan backdrop
              backdrop.animate().translationY(0).setDuration(200).start();
              isBackdropVisible = true;
            }

            // Efek perubahan warna toolbar saat scroll (opsional)
            int scrollY = getRecyclerViewScrollY(recyclerView);
            int maxScroll = 300;
            float scrollRatio = Math.min(1f, (float) scrollY / maxScroll);

            int fromColor = Color.parseColor("#F1F3F7");
            int toColor = Color.WHITE;
            int blended = blendColors(fromColor, toColor, scrollRatio);

            toolbar.setBackgroundColor(blended);
          }
        });

    return this;
  }

  private int getRecyclerViewScrollY(RecyclerView recyclerView) {
    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
      LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
      View topView = recyclerView.getChildAt(0);
      if (topView != null) {
        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        int top = topView.getTop();
        int itemHeight = topView.getHeight();
        return firstPosition * itemHeight - top;
      }
    }
    return 0;
  }

  public int blendColors(int from, int to, float ratio) {
    final float inverseRatio = 1f - ratio;
    float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
    float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
    float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;
    return Color.rgb((int) r, (int) g, (int) b);
  }
}
