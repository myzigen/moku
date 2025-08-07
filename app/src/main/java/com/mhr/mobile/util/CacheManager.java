package com.mhr.mobile.util;
import android.content.SharedPreferences;
import android.content.Context;

public class CacheManager {
  private static final String PREFS_NAME = "produk_cache";
  private static final String KEY_DATA = "cached_data";
  private static final String KEY_TIME = "cached_time";

  public static void saveCache(Context context, String data) {
    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    prefs.edit().putString(KEY_DATA, data).putLong(KEY_TIME, System.currentTimeMillis()).apply();
  }

  public static String getCache(Context context) {
    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    return prefs.getString(KEY_DATA, null);
  }

  public static boolean isCacheValid(Context context, long validDurationMillis) {
    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    long savedTime = prefs.getLong(KEY_TIME, 0);
    return (System.currentTimeMillis() - savedTime) < validDurationMillis;
  }

  public static void clearCache(Context context) {
    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    prefs.edit().clear().apply();
  }
}
