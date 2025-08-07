package com.mhr.mobile.api.listener;

import com.mhr.mobile.api.response.ResponseUsers;

public interface CheckNomorListener {
  public void onRequest();

  public void onCheckNomor(ResponseUsers users);

  public void onFailure(String error);
}
