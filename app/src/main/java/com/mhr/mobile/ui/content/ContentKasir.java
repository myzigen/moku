package com.mhr.mobile.ui.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.ContentKasirBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class ContentKasir extends InjectionActivity {
  private ContentKasirBinding binding;

  @Override
  protected String getTitleToolbar() {
    return "Kasir";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentKasirBinding.inflate(getLayoutInflater());
    // initUi();
    return binding.getRoot();
  }

  /*
  private void initUi() {
    NestedScrollView nestedScroll = binding.nestedScroll;
    View wrapInput = binding.wrapInput;
    View stickyInput = binding.stickyInput;

    final int[] initialWrapInputLocation = new int[2];

    // Tunggu sampai layout selesai untuk ambil posisi awal `wrap_input`
    wrapInput
        .getViewTreeObserver()
        .addOnGlobalLayoutListener(
            () -> {
              wrapInput.getLocationOnScreen(initialWrapInputLocation);
            });

    nestedScroll.setOnScrollChangeListener(
        new View.OnScrollChangeListener() {
          @Override
          public void onScrollChange(
              View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            int[] currentLocation = new int[2];
            wrapInput.getLocationOnScreen(currentLocation);
            int currentY = currentLocation[1];

            if (currentY <= initialWrapInputLocation[1]) {
              stickyInput.setVisibility(View.VISIBLE);
              wrapInput.setVisibility(View.INVISIBLE);
            } else {
              stickyInput.setVisibility(View.GONE);
              wrapInput.setVisibility(View.VISIBLE);
            }
          }
        });
  }
  */
}
