package com.mhr.mobile.ui.sheet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.SheetErrorLayoutBinding;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.WindowPreferencesManager;

public class SheetErrorLayout extends InjectionSheetFragment {
  private SheetErrorLayoutBinding binding;
  private WindowPreferencesManager windowManager;
  private Class<?> targetActivity;
  private String infoTitle, infoText, infoBtn;

  @Override
  protected String getSheetTitle() {
    return infoTitle;
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetErrorLayoutBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();
    initUi();
    return view;
  }

  private void initUi() {
    binding.infoText.setText(infoText);
    binding.btn.setText(infoBtn);
    binding.btn.setOnClickListener(
        v -> {
          if (targetActivity != null) {
            startActivity(new Intent(requireActivity(), targetActivity));
            dismiss();
          }
        });
  }

  public void setTitle(String title) {
    this.infoTitle = title;
  }

  public void setText(String text) {
    this.infoText = text;
  }

  public void setTextBtn(String btn) {
    this.infoBtn = btn;
  }

  public void setToActivity(Class<?> targetActivity) {
    this.targetActivity = targetActivity;
  }
}
