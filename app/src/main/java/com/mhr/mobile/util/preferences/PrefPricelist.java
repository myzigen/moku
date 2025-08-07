package com.mhr.mobile.util.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mhr.mobile.api.response.ResponsePricelist;
import java.lang.reflect.Type;
import java.util.List;

public class PrefPricelist {
  private static final String PREF_NAME = "pricelist_pref";
  private static final String KEY_PRICELIST = "cached_pricelist";

  public static void save(Context context, List<ResponsePricelist> data) {
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    Gson gson = new Gson();
    String json = gson.toJson(data);
    editor.putString(KEY_PRICELIST, json);
    editor.apply();
  }

  public static List<ResponsePricelist> get(Context context) {
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    String json = prefs.getString(KEY_PRICELIST, null);
    if (json != null) {
      Gson gson = new Gson();
      Type type = new TypeToken<List<ResponsePricelist>>() {}.getType();
      return gson.fromJson(json, type);
    }
    return null;
  }

  public static boolean isAvailable(Context context) {
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    return prefs.contains(KEY_PRICELIST);
  }

  public static void clear(Context context) {
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    prefs.edit().remove(KEY_PRICELIST).apply();
  }
}