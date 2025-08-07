package com.mhr.mobile.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;
import com.mhr.mobile.viewmodel.NetworkViewModel;

public class NetworkMonitoringUtil extends ConnectivityManager.NetworkCallback {
  public static final String TAG = NetworkMonitoringUtil.class.getSimpleName();

  public final ConnectivityManager mConnectivityManager;
  private final NetworkViewModel mNetworkStateManager;

  // Constructor
  public NetworkMonitoringUtil(Context context) {
    mConnectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    mNetworkStateManager = NetworkViewModel.getInstance();
  }
  
  public static NetworkMonitoringUtil connect(Context c){
	  return new NetworkMonitoringUtil(c);
  }

  @Override
  public void onAvailable(Network network) {
    super.onAvailable(network);
    //Log.d(TAG, "onAvailable() called: Connected to network");
    // Check the network capabilities for internet access
    NetworkCapabilities networkCapabilities = mConnectivityManager.getNetworkCapabilities(network);
    if (networkCapabilities != null
        && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
      mNetworkStateManager.setNetworkConnectivityStatus(true); // Koneksi internet tersedia
    }
  }

  @Override
  public void onLost(Network network) {
    super.onLost(network);
    //Log.e(TAG, "onLost() called: Lost network connection");
    mNetworkStateManager.setNetworkConnectivityStatus(false); // Koneksi hilang
  }

  public NetworkMonitoringUtil registerNetworkCallbackEvents() {
    //Log.d(TAG, "registerNetworkCallbackEvents() called");
    // Register for connectivity changes
    mConnectivityManager.registerNetworkCallback(
        new NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build(),
        this);
		return this;
  }

  public NetworkMonitoringUtil checkNetworkState() {
    // Use getActiveNetwork() for devices with Android 10 and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      Network network = mConnectivityManager.getActiveNetwork();
      if (network != null) {
        NetworkCapabilities networkCapabilities =
            mConnectivityManager.getNetworkCapabilities(network);
        if (networkCapabilities != null
            && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
          mNetworkStateManager.setNetworkConnectivityStatus(true); // Koneksi internet tersedia
        } else {
          mNetworkStateManager.setNetworkConnectivityStatus(false); // Tidak ada koneksi internet
        }
      } else {
        mNetworkStateManager.setNetworkConnectivityStatus(false); // Tidak ada koneksi
      }
    }
	return this;
  }
}
