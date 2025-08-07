package com.mhr.mobile.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.net.NetworkInfo;

public class NetworkUtil {
  public static boolean isNetworkAvailable(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (cm == null) return false;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
      return nc != null && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    } else {
      // Untuk versi < Android M
      NetworkInfo ni = cm.getActiveNetworkInfo();
      return ni != null && ni.isConnected();
    }
  }
}