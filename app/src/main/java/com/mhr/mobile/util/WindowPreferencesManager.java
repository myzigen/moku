package com.mhr.mobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.Window;
import android.view.WindowInsets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import com.google.android.material.internal.EdgeToEdgeUtils;

public class WindowPreferencesManager {

  private static final String PREFERENCES_NAME = "window_preferences";
  private static final String KEY_EDGE_TO_EDGE_ENABLED = "edge_to_edge_enabled";

  private final Context context;
  private final OnApplyWindowInsetsListener listener;

  public WindowPreferencesManager(Context context) {
    this.context = context;
    this.listener =
        (v, insets) -> {
          int leftInset = insets.getStableInsetLeft();
          int rightInset = insets.getStableInsetRight();
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            leftInset = insets.getInsets(WindowInsets.Type.systemBars()).left;
            rightInset = insets.getInsets(WindowInsets.Type.systemBars()).right;
          }

          v.setPadding(leftInset, 0, rightInset, 0);
          return insets;
        };
  }

  @SuppressWarnings("ApplySharedPref")
  public void toggleEdgeToEdgeEnabled() {
    getSharedPreferences()
        .edit()
        .putBoolean(KEY_EDGE_TO_EDGE_ENABLED, !isEdgeToEdgeEnabled())
        .commit();
  }

  public boolean isEdgeToEdgeEnabled() {
    return getSharedPreferences()
        .getBoolean(KEY_EDGE_TO_EDGE_ENABLED, Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q);
  }

  @SuppressWarnings("RestrictTo")
  public void applyEdgeToEdgePreference(Window window) {
    EdgeToEdgeUtils.applyEdgeToEdge(window, isEdgeToEdgeEnabled());
    ViewCompat.setOnApplyWindowInsetsListener(
        window.getDecorView(), isEdgeToEdgeEnabled() ? listener : null);
  }

  private SharedPreferences getSharedPreferences() {
    return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
  }
  
  
}
