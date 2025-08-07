package com.mhr.mobile;

import android.app.Application;
import android.content.Context;
import com.mhr.mobile.interfaces.CartUpdateListener;
import com.mhr.mobile.util.NetworkMonitoringUtil;
import com.mhr.mobile.util.QiosPreferences;
import com.onesignal.Continue;
import com.onesignal.OneSignal;

public class MyApp extends Application {
  public static final String TAG = MyApp.class.getSimpleName();
  private static Context appContext;
  private NetworkMonitoringUtil mNetworkMonitoringUtil;
  private static final String APP_ID = "553e32ab-501f-4779-8533-e424508be0a7";
  private static CartUpdateListener listener;
  private static QiosPreferences preferences;
  // Logger logger;
  @Override
  public void onCreate() {
    super.onCreate();
    

    appContext = getApplicationContext();
    preferences = new QiosPreferences(this);
 

    OneSignal.initWithContext(this, APP_ID);
    OneSignal.getNotifications().requestPermission(false, Continue.none());
    OneSignal.getUser().getPushSubscription();

    // Inisialisasi NetworkMonitoringUtil
    mNetworkMonitoringUtil = new NetworkMonitoringUtil(getApplicationContext());
    mNetworkMonitoringUtil.checkNetworkState(); // Cek keadaan awal jaringan
    mNetworkMonitoringUtil.registerNetworkCallbackEvents(); // Daftarkan callback
  }

  public static void setCartUpdate(CartUpdateListener listener) {
    listener = listener;
  }

  public static void notifyCartUpdate(int count) {
    if (listener != null) listener.onCartUpdated(count);
  }

  public static QiosPreferences getPreferences() {
    return preferences;
  }
}
