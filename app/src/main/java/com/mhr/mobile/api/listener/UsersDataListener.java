package com.mhr.mobile.api.listener;

import com.mhr.mobile.api.response.ResponseUsers;

public interface UsersDataListener {
  public void onRequest();

  public void onReceive(ResponseUsers users);

  public void onFailure(String error);
}
