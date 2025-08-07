package com.mhr.mobile.api.listener;

import java.util.List;

public interface MokuCallback<T> {
  public void onDataLoading();

  public void onDataValue(T data);

  public void onDataError(String error);
}