package com.mhr.mobile.widget.input;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import com.google.android.material.textfield.TextInputEditText;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class RupiahEditText extends TextInputEditText {
  private boolean isFormatting = false;

  public RupiahEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    setInputType(InputType.TYPE_CLASS_NUMBER);

    addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No-op
          }

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No-op
          }

          @Override
          public void afterTextChanged(Editable s) {
            if (isFormatting) return;

            isFormatting = true;
            String originalString = s.toString();

            if (originalString.isEmpty()) {
              isFormatting = false;
              return;
            }

            String cleanString = originalString.replaceAll("[,.]", "");
            double parsed = 0.0;
            try {
              parsed = Double.parseDouble(cleanString);
            } catch (NumberFormatException e) {
              parsed = 0.0;
            }

            String formattedString = getFormattedNumber(parsed);

            setText(formattedString);
            setSelection(formattedString.length());

            isFormatting = false;
          }
        });
  }

  private String getFormattedNumber(double number) {
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY);
    symbols.setGroupingSeparator('.');
    symbols.setDecimalSeparator(',');
    DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
    return decimalFormat.format(number);
  }
}
