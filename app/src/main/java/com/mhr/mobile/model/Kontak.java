package com.mhr.mobile.model;

public class Kontak {
  private String name;
  private String phoneNumber;
  private String photoUri;

  // Constructor, Getter, and Setter
  public Kontak(String name, String phoneNumber, String photoUri) {
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.photoUri = photoUri;
  }

  public String getName() {
    return name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getPhotoUri() {
    return photoUri;
  }
}
