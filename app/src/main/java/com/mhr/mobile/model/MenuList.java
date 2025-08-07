package com.mhr.mobile.model;

public class MenuList {
  String imageUrl;
  String brand;
  String code;

  public MenuList() {}

  public MenuList(String brand) {
    this.brand = brand;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getBrand() {
    return brand;
  }

  public String getCode() {
    return code;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
