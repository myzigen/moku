package com.mhr.mobile.api.listener;

public interface OtpRequestListener {
  void onRequest();

  void onReceive();

  void onError(String error);
}
