package com.mhr.mobile.model;

import com.google.gson.annotations.SerializedName;

public class ItemSliderHome {

  @SerializedName("url_gambar_slide")
  private String image;

  public ItemSliderHome(String imahe) {
    this.image = imahe;
  }

  public String getSliderImage() {
    return image;
  }
}
