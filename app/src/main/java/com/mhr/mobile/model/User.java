package com.mhr.mobile.model;

import com.google.firebase.firestore.PropertyName;

public class User {
  @PropertyName("userId")
  private String userId;

  @PropertyName("userName")
  private String userName;

  @PropertyName("userEmail")
  private String userEmail;

  private String userPassword;
  private String userDeviceId;
  private String userDeviceName;
  private String userNomor;
  private double latitude;
  private double longitude;

  @PropertyName("userSaldo")
  private int userSaldo;

  public User() {}

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getUserPassword() {
    return userPassword;
  }

  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }

  public int getUserSaldo() {
    return userSaldo;
  }

  public void setUserSaldo(int userSaldo) {
    this.userSaldo = userSaldo;
  }

  public String getUserDeviceId() {
    return userDeviceId;
  }

  public void setUserDeviceId(String userDeviceId) {
    this.userDeviceId = userDeviceId;
  }

  public String getUserDeviceName() {
    return userDeviceName;
  }

  public void setUserDeviceName(String userDeviceName) {
    this.userDeviceName = userDeviceName;
  }

  public String getUserNomor() {
    return userNomor;
  }

  public void setUserNomor(String userNomor) {
    this.userNomor = userNomor;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}
