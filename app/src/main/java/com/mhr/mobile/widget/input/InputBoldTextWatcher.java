package com.mhr.mobile.widget.input;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class InputBoldTextWatcher implements TextWatcher {
  private final EditText editText;

  public InputBoldTextWatcher(EditText editText) {
    this.editText = editText;
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    if (s.length() == 0) {
      editText.setTypeface(null, Typeface.NORMAL);
    } else {
      editText.setTypeface(null, Typeface.BOLD);
    }
  }

  @Override
  public void afterTextChanged(Editable s) {}
}
