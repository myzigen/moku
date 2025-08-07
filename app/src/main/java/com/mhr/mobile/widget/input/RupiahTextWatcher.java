package com.mhr.mobile.widget.input;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class RupiahTextWatcher implements TextWatcher {
  private final EditText editText;
  private final DecimalFormat formatter;
  private boolean isEditing = false;
  private static final String PREFIX = "Rp ";

  public RupiahTextWatcher(EditText editText) {
    this.editText = editText;
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ROOT);
    symbols.setGroupingSeparator('.');
    symbols.setDecimalSeparator(',');
    this.formatter = new DecimalFormat("###,###,###", symbols);
    editText.setText(PREFIX); // Set awal "Rp "
    editText.setSelection(PREFIX.length()); // Pastikan kursor ada di akhir "Rp "
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    if (isEditing) return;
    isEditing = true;

    String input = s.toString();

    // Pastikan teks selalu diawali dengan "Rp "
    if (!input.startsWith(PREFIX)) {
      editText.setText(PREFIX + "0");
      editText.setSelection(PREFIX.length());
      isEditing = false;
      return;
    }

    // Ambil angka setelah "Rp "
    String angka = input.replace(PREFIX, "").replaceAll("[^\\d]", "");

    if (!angka.isEmpty()) {
      String formatted = formatter.format(Long.parseLong(angka));
      editText.setText(PREFIX + formatted);
      editText.setSelection((PREFIX + formatted).length()); // Posisikan kursor di akhir angka
    } else {
      editText.setText(PREFIX);
      editText.setSelection(PREFIX.length());
    }

    isEditing = false;
  }
}
