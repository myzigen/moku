package com.mhr.mobile.interfaces;

public interface ConnectivityListener {
  void onNetworkConnected(boolean isConnected);

  void onDataReload();
}
