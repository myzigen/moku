package com.mhr.mobile.ui.inject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.InjectionSheetContainerBinding;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.WindowPreferencesManager;

public abstract class InjectionSheetFragment extends BottomSheetDialogFragment {
  private InjectionSheetContainerBinding binding;
  private ViewGroup container;
  private WindowPreferencesManager windowManager;
  private boolean closeShowing;
  protected TextView textView;

  @Override
  public int getTheme() {
    return R.style.FullscreenBottomSheetDialogFragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    windowManager = new WindowPreferencesManager(requireContext());
    windowManager.applyEdgeToEdgePreference(getDialog().getWindow());
    binding = InjectionSheetContainerBinding.inflate(inflater, viewGroup, false);
    textView = binding.title;
    setDefaultSheetTitle(textView);
    container = binding.getRoot();
    container.setBackgroundResource(R.drawable.shape_corners_top);
    View contentView = onCreateSheetView(inflater, viewGroup, bundle);
    binding.container.addView(contentView);
    binding.close.setOnClickListener(v -> dismiss());

    AndroidViews.applyInsets(container);
    binding.close.setVisibility(closeShowing ? View.VISIBLE : View.GONE);
    return container;
  }

  public void show(FragmentManager fm) {
    show(fm, "TAG");
  }

  protected void closeShowing(boolean show) {
    this.closeShowing = show;
  }

  protected String getSheetTitle() {
    return "";
  }

  private void setDefaultSheetTitle(TextView textView) {
    if (!TextUtils.isEmpty(getSheetTitle())) {
      binding.containerTitle.setVisibility(View.VISIBLE);
      binding.close.setVisibility(View.VISIBLE);
      binding.drag.setVisibility(View.GONE);
      textView.setText(getSheetTitle());
    } else {
      binding.containerTitle.setVisibility(View.GONE);
      binding.close.setVisibility(View.GONE);
      binding.drag.setVisibility(View.VISIBLE);
    }
  }

  protected abstract View onCreateSheetView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, Bundle bundle);

  // protected void onPhoneNumberPicked(String phoneNumber);

  @Override
  public void onStart() {
    super.onStart();
    View view = getView();
    if (view != null) {
      View parent = (View) view.getParent();
      BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(parent);
      behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      behavior.setSkipCollapsed(true);
      behavior.setDraggable(true);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
    dismissAllowingStateLoss();
  }
}
