package com.mhr.mobile.api.listener;

import java.util.List;

public interface MokuListCallback<T> {
  public void onEvent();

  public void onDataValue(List<T> data);

  public void onError(String error);
}
