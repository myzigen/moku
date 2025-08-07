package com.mhr.mobile.model;

public class Akun {
  private int iconResId;
  private String labelName;

  public Akun(int iconResId, String labelName) {
    this.iconResId = iconResId;
    this.labelName = labelName;
  }

  public int getIconResId() {
    return iconResId;
  }

  public String getLabelName() {
    return labelName;
  }
}
