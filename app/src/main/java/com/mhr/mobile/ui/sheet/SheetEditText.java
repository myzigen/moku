package com.mhr.mobile.ui.sheet;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.SheetEditextBinding;
import com.mhr.mobile.util.QiosColor;

public class SheetEditText extends BottomSheetDialogFragment implements TextWatcher {
  private SheetEditextBinding binding;
  public static final String DEFAULT_PLACEHOLDER = "Belum Diatur";
  public static final String ARG_TYPE = "type";
  public static final String ARG_VALUE = "value";

  public interface onFieldEditedListener {
    void onFieldEdited(String type, String value);
  }

  public static SheetEditText newInstance(String type, String value) {
    SheetEditText fragment = new SheetEditText();
    // Nilai Default Placeholder
    if (value == null || value.trim().equalsIgnoreCase(DEFAULT_PLACEHOLDER)) {
      value = "";
    }
    Bundle args = new Bundle();
    args.putString(ARG_TYPE, type);
    args.putString(ARG_VALUE, value);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetEditextBinding.inflate(getLayoutInflater());
    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    binding.editText.requestFocus();
    binding.editText.addTextChangedListener(this);

    String type = getArguments().getString(ARG_TYPE, "");
    String currentValue = getArguments().getString(ARG_VALUE, "");

    if (!currentValue.trim().isEmpty()) {
      binding.editText.setText(currentValue);
      binding.editText.setSelection(currentValue.length());
    }

    binding.editText.setSelection(binding.editText.getText().length());
    binding.input.setPlaceholderText(getHintByField(type));
    binding.btn.setEnabled(false);
    binding.btn.setTextColor(QiosColor.getColor(requireActivity(), R.color.color_gray_inactive));
    binding.btn.setOnClickListener(v -> apply(type));

    return binding.getRoot();
  }

  private void apply(String type) {
    String result = binding.editText.getText().toString();
    if (getActivity() instanceof onFieldEditedListener) {
      ((onFieldEditedListener) getActivity()).onFieldEdited(type, result);
    }
    dismiss();
  }

  private String getHintByField(String field) {
    switch (field) {
      case "nama":
        return "Masukan nama";
      case "nomor":
        return "081234567890";
      case "nama_toko":
        return "Masukan Nama Toko";
      case "alamat_toko":
        return "Masukan Alamat Toko";
      default:
        return "Edit";
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    String input = s.toString().trim();

    if (input.isEmpty()) {
      validate(false);
    } else if (input.length() >= 3) {
      validate(true);
    } else {
      validate(false);
    }
  }

  private void validate(boolean val) {
    binding.btn.setEnabled(val);
    if (!val) {
      binding.btn.setTextColor(QiosColor.getColor(requireActivity(), R.color.color_gray_inactive));
    } else {
      binding.btn.setTextColor(QiosColor.getColor(requireActivity(), R.color.me_color_text));
    }
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    BottomSheetDialog dialog = new BottomSheetDialog(requireContext());

    dialog.setOnShowListener(
        dialogInterface -> {
          BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
          FrameLayout bottomSheet =
              bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

          if (bottomSheet != null) {
            bottomSheet.setBackgroundResource(R.drawable.shape_corners_top);
            BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(true);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
          }

          // Set warna navigation bar jadi putih
          dialog.getWindow().setNavigationBarColor(Color.WHITE);

          // Opsional: set ikon nav bar jadi gelap (biar tombol back & home kelihatan di background
          // putih)
          dialog
              .getWindow()
              .getDecorView()
              .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        });

    return dialog;
  }
}
