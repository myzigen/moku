package com.mhr.mobile.ui.intro;

import android.content.Context;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.util.QiosPreferences;

public class UserSession {
  private final Context activity;

  public UserSession(Context activity) {
    this.activity = activity;
  }

  public static UserSession with(Context activity) {
    return new UserSession(activity);
  }

  private QiosPreferences getPref() {
    return new QiosPreferences(activity);
  }

  public boolean isLoggedIn() {
    return getPref().isLogin();
  }

  public boolean hasPin() {
    return getPref().getUserSession().getHashPin();
  }

  public ResponseUsers.User getUser() {
    return getPref().getUserSession();
  }

  public String getNama() {
    return getUser().getNama();
  }

  public String getNomor() {
    return getUser().getNomor();
  }

  public String getToken() {
    return getUser().getToken();
  }

  public String getNamaToko() {
    return getUser().getNamaToko();
  }

  public String getAlamatToko() {
    return getUser().getAlamatToko();
  }

  public void logout() {
    getPref().clearSessionLogin();
  }
}
