package com.mhr.mobile.ui.navcontent.kelola.pelanggan;

public class FieldPelangganModel {
  public String key;
  public String label;
  public String value;
  public String hint;

  public FieldPelangganModel(String key, String label, String value, String hint) {
    this.key = key;
    this.label = label;
    this.value = value;
    this.hint = hint;
  }

  public boolean isEmpty() {
    return value == null || value.trim().isEmpty() || value.equalsIgnoreCase(hint);
  }

  public String getDisplayValue() {
    return isEmpty() ? hint : value;
  }
}