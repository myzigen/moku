package com.mhr.mobile.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.DialogPopupBinding;

public class PopupDialog extends DialogFragment {
  private DialogPopupBinding binding;
  private String imageUrl;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup v, Bundle savedInstanceState) {
    binding = DialogPopupBinding.inflate(getLayoutInflater());
    // Menghapus header dialog
    if (getDialog() != null && getDialog().getWindow() != null) {
      getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }
    // Inflate layout loading
    View view = binding.getRoot();
    binding.close.setOnClickListener(close -> dismiss());
    Glide.with(requireActivity()).load(imageUrl).into(binding.image);
    return view;
  }

  public void setImagePopup(String url) {
    this.imageUrl = url;
  }

  @Override
  public int getTheme() {
    return R.style.PopupDialogTheme;
  }
}
