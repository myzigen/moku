package com.mhr.mobile.viewmodel;

import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NetworkViewModel {
  public static final String TAG = NetworkViewModel.class.getSimpleName();

  private static NetworkViewModel INSTANCE;
  private static final MutableLiveData<Boolean> activeNetworkStatusMLD = new MutableLiveData<>();

  private NetworkViewModel() {}

  public static synchronized NetworkViewModel getInstance() {
    if (INSTANCE == null) {
      // Log.d(TAG, "getInstance() called: Creating new instance");
      INSTANCE = new NetworkViewModel();
    }
    return INSTANCE;
  }

  /** Updates the active network status live-data */
  public void setNetworkConnectivityStatus(boolean connectivityStatus) {
    // Log.d(
    //    TAG,
    //    "setNetworkConnectivityStatus() called with: connectivityStatus = ["
    //        + connectivityStatus
    //        + "]");

    if (Looper.myLooper() == Looper.getMainLooper()) {
      activeNetworkStatusMLD.setValue(connectivityStatus);
    } else {
      activeNetworkStatusMLD.postValue(connectivityStatus);
    }
  }

  /** Returns the current network status */
  public LiveData<Boolean> getNetworkConnectivityStatus() {
    // Log.d(TAG, "getNetworkConnectivityStatus() called");
    return activeNetworkStatusMLD;
  }
}
