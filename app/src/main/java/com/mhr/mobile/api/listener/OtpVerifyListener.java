package com.mhr.mobile.api.listener;

import com.mhr.mobile.api.response.ResponseOtp;

public interface OtpVerifyListener {
  public void onRequest();

  public void onReceive(ResponseOtp usersOtp);

  public void onFailure(String error);
}
