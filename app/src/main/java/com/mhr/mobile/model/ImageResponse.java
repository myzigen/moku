package com.mhr.mobile.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class ImageResponse {

  @SerializedName("images")
  private Map<String, Image> images;

  public Map<String, Image> getImages() {
    return images;
  }

  public void setImages(Map<String, Image> images) {
    this.images = images;
  }

  public static class Image {
    @SerializedName("imageUrl")
    private String imageUrl;

    public String getImageUrl() {
      return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }
  }
}
