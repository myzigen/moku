package com.mhr.mobile.model;

public class ListSheet {
  private int logoResId;
  private String logoUrl;
  private String labelName;

  public int getLogoResId() {
    return logoResId;
  }

  public void setLogoResId(int resId) {
    this.logoResId = resId;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String url) {
    this.logoUrl = url;
  }

  public String getLabelName() {
    return labelName;
  }

  public void setLabelName(String label) {
    this.labelName = label;
  }
}
