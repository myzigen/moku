package com.mhr.mobile.ui.sheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.BtmSheetKeyboardBinding;

public class SheetKeyboard {

  private final BottomSheetDialog dialog;
  private final BtmSheetKeyboardBinding binding;
  private EditText targetEditText;
  private OnKeyboardDismissListener listener;

  public SheetKeyboard(Context context) {
    dialog = new BottomSheetDialog(context, R.style.KeyboardStyle);
    binding = BtmSheetKeyboardBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(binding.getRoot());

  }

  public void setEditText(EditText editText) {
    this.targetEditText = editText;
    if (editText != null) {
      EditorInfo editorInfo = new EditorInfo();
      InputConnection ic = editText.onCreateInputConnection(editorInfo);
      if (ic != null) {
        binding.keyboard.setInputConnection(ic);
        binding.keyboard.setTargetEditText(editText);
      }
    }
  }

  public void setOnKeyboardDismissListener(OnKeyboardDismissListener listener) {
    this.listener = listener;
  }

  public void show() {
    dialog.show();
  }

  public void dismiss() {
    dialog.dismiss();
  }

  public boolean isShowing() {
    return dialog.isShowing();
  }

  public interface OnKeyboardDismissListener {
    void onDismiss();
  }
}
