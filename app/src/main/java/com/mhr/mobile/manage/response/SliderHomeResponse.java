package com.mhr.mobile.manage.response;

import com.google.gson.annotations.SerializedName;
import com.mhr.mobile.model.ItemSliderHome;
import java.util.List;

public class SliderHomeResponse {
  @SerializedName("data")
  private List<ItemSliderHome> data;

  // Getter dan Setter
  public List<ItemSliderHome> getData() {
    return data;
  }

  public void setData(List<ItemSliderHome> data) {
    this.data = data;
  }
}
