package com.mhr.mobile.widget.recyclerview;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A custom ItemDecoration for applying spacing between items in RecyclerView. Supports both
 * GridLayoutManager and LinearLayoutManager.
 */
public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

  private final int spanCount; // Number of columns in the grid (ignored for LinearLayoutManager)
  private final int spacing; // Spacing in pixels
  private final boolean includeEdge; // Whether to include spacing at the edges

  /**
   * Constructor for SpacingItemDecoration.
   *
   * @param spanCount Number of columns in the grid (set to 1 for LinearLayoutManager).
   * @param spacing Spacing in pixels between items.
   * @param includeEdge True to include spacing at the edges, false otherwise.
   */
  public SpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
    this.spanCount = spanCount;
    this.spacing = spacing;
    this.includeEdge = includeEdge;
  }

  @Override
  public void getItemOffsets(
      @NonNull Rect outRect,
      @NonNull View view,
      @NonNull RecyclerView parent,
      @NonNull RecyclerView.State state) {
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

    if (layoutManager instanceof LinearLayoutManager
        && !(layoutManager instanceof GridLayoutManager)) {
      // Handle LinearLayoutManager (horizontal or vertical)
      handleLinearLayout(outRect, view, parent, (LinearLayoutManager) layoutManager);
    } else if (layoutManager instanceof GridLayoutManager) {
      // Handle GridLayoutManager
      handleGridLayout(outRect, view, parent);
    }
  }

  private void handleLinearLayout(Rect outRect, View view, RecyclerView parent, LinearLayoutManager layoutManager) {
    int position = parent.getChildAdapterPosition(view); // Posisi item
    int itemCount = parent.getAdapter().getItemCount();  // Total item

    // Tambahkan spasi pada setiap sisi
    outRect.left = spacing;  // Spasi di sisi kiri
    outRect.right = spacing; // Spasi di sisi kanan

    if (includeEdge) {
        // Spasi atas untuk item pertama
        outRect.top = position == 0 ? spacing : spacing / 2;
        // Spasi bawah untuk item terakhir
        outRect.bottom = position == itemCount - 1 ? spacing : spacing / 2;
    } else {
        // Spasi atas hanya di antara item (tanpa spasi pada tepi atas RecyclerView)
        outRect.top = position == 0 ? 0 : spacing / 2;
        // Spasi bawah hanya di antara item (tanpa spasi pada tepi bawah RecyclerView)
        outRect.bottom = position == itemCount - 1 ? 0 : spacing / 2;
    }
}

  private void handleGridLayout(Rect outRect, View view, RecyclerView parent) {
    int position = parent.getChildAdapterPosition(view); // Item position
    int column = position % spanCount; // Column index of the item
    int itemCount = parent.getAdapter().getItemCount(); // Total item count
    int rowCount = (int) Math.ceil((double) itemCount / spanCount); // Total number of rows
    int currentRow = position / spanCount; // Current row index

    if (includeEdge) {
      // Add spacing for the left and right edges
      outRect.left = spacing - column * spacing / spanCount;
      outRect.right = (column + 1) * spacing / spanCount;

      // Add spacing for the top edge of the first row
      outRect.top = currentRow == 0 ? spacing : spacing / 2;
      // Add spacing for the bottom edge of the last row
      outRect.bottom = currentRow == rowCount - 1 ? spacing : spacing / 2;
    } else {
      // Add spacing only between items (excluding edges)
      outRect.left = column * spacing / spanCount;
      outRect.right = spacing - (column + 1) * spacing / spanCount;

      // Add spacing only between rows (excluding edges)
      outRect.top = currentRow == 0 ? 0 : spacing / 2;
      outRect.bottom = currentRow == rowCount - 1 ? 0 : spacing / 2;
    }
  }
}
