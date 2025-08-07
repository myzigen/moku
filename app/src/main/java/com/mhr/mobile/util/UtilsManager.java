package com.mhr.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.mhr.mobile.R;
import java.util.UUID;

public class UtilsManager {
  private static boolean isClicked = false;
  private static final long DELAY = 800;

  public static void getStatusBarColor(Window window, Context context) {
    window.setStatusBarColor(ContextCompat.getColor(context, R.color.me_primary));
	window.setNavigationBarColor(ContextCompat.getColor(context, R.color.me_primary));
  }

  public static void setStatusBarTranslucent(@NonNull Window window) {
    window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
  }

  public static void setAllowDrawUnderStatusBar(@NonNull Window window) {
    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
  }

  public static String getDeviceId() {
    return UUID.randomUUID().toString();
  }

  public static String getDeviceName() {
    return Build.MANUFACTURER + " " + Build.MODEL; // Contoh: "Samsung Galaxy A52"
  }

  public static String getVersionApp(Context context) {
    try {
      PackageInfo appInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return appInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return "Versi Tidak Di Ketahui";
    }
  }

  public static void hideKeyboard(Activity activity) {
    View view = activity.getCurrentFocus();
    if (view != null) {
      InputMethodManager imm =
          (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
      if (imm != null) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }
    }
  }

  public static void hideKeyboard(Context context, View view) {
    InputMethodManager imm =
        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  public static void getDoneEnter(EditText editText) {
    editText.setOnKeyListener(
        (v, keyCode, event) -> {
          if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            // Tangkap aksi Enter dan lakukan sesuatu (jika perlu)
            return true; // Return true untuk mencegah aksi default
          }
          return false;
        });
  }

  public static boolean allowClick() {
    if (isClicked) return false;
    new Handler(Looper.getMainLooper()).postDelayed(() -> isClicked = false, DELAY);
    return true;
  }

  public static void vibrateDevice(Context context) {
    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    if (vibrator != null && vibrator.hasVibrator()) {
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
      } else {
        vibrator.vibrate(150);
      }
    }
  }
}
