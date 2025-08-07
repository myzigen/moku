package com.mhr.mobile.util.preferences;

import com.google.gson.Gson;
import com.mhr.mobile.MyApp;

public class PrefInputCache {
  public String nomor;
  public String brand;
  public String logoUrl;
  public String sku;
  public String customer_name;

  private static final Gson gson = new Gson();

  public static void save(PrefInputCache obj) {
    if (obj == null || obj.brand == null || obj.brand.isEmpty()) return;
    String key = getKeyForBrand(obj.brand);
    String json = gson.toJson(obj);
    MyApp.getPreferences().setString(key, json);
  }

  public static PrefInputCache load(String brand) {
    String key = getKeyForBrand(brand);
    String json = MyApp.getPreferences().getString(key);
    if (json == null || json.isEmpty()) return null;
    return gson.fromJson(json, PrefInputCache.class);
  }

  public static void clear(String brand) {
    String key = getKeyForBrand(brand);
    MyApp.getPreferences().remove(key);
  }

  private static String getKeyForBrand(String brand) {
    return "cache_token_" + brand.toLowerCase().replaceAll("\\s+", "_");
  }
}
