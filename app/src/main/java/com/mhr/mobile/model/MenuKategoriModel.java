package com.mhr.mobile.model;

public class MenuKategoriModel {
  private int drawableResId;
  private int imageRes;
  private String imageUrl;
  private String namaKategori;
  private String type;

  public MenuKategoriModel() {}

  public MenuKategoriModel(String imageUrl, String namaKategori) {
    this.imageUrl = imageUrl;
    this.namaKategori = namaKategori;
  }

  public MenuKategoriModel(int drawableResId, int imageRes, String namaKategori) {
    this.drawableResId = drawableResId;
    this.imageRes = imageRes;
    this.namaKategori = namaKategori;
  }

  // Setter getter
  public int getId() {
    return drawableResId;
  }

  public void setId(int drawableResId) {
    this.drawableResId = drawableResId;
  }

  public int getIconKategori() {
    return imageRes;
  }

  public void setIconKategori(int imageRes) {
    this.imageRes = imageRes;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getNamaKategori() {
    return namaKategori;
  }

  public void setNamaKategori(String namaKategori) {
    this.namaKategori = namaKategori;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
