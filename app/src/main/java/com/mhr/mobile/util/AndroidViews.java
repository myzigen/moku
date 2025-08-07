package com.mhr.mobile.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.transition.Transition;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mhr.mobile.R;
import net.cachapa.expandablelayout.ExpandableLayout;

public class AndroidViews {
  private static CountDownTimer timer;

  public interface CountdownListener {
    void onTick(long minutes, long seconds);

    void onFinish();
  }

  public static void showExpandLoading(String load, ExpandableLayout expand, TextView text) {
    expand.setBackgroundResource(R.drawable.shape_corners_bottom_loading);
    expand.expand();
    text.setTextColor(Color.parseColor("#000000"));
    text.setText(load);
  }

  public static void showExpandSuccess(String success, ExpandableLayout expandable, TextView text) {
    expandable.setBackgroundResource(R.drawable.shape_corners_bottom_succes);
    expandable.expand();
    text.setTextColor(Color.parseColor("#00AA5B"));
    text.setText(success);
  }

  public static void ShowExpandError(
      String error, ExpandableLayout expandableLayout, TextView textView) {
    expandableLayout.setBackgroundResource(R.drawable.shape_corners_bottom_error);
    expandableLayout.expand();
    textView.setTextColor(Color.parseColor("#D62F57"));
    textView.setText(error);
  }

  public static void showToast(String message, Context context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
  }

  public static void showSnackbar(Activity context, String message) {
    View rootView = context.findViewById(android.R.id.content);
    Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
  }

  public static void showSnackbarCentered(Activity activity, String message) {
    View rootView = activity.findViewById(android.R.id.content);

    Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);

    View snackbarView = snackbar.getView();
    FrameLayout.LayoutParams params =
        new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    params.gravity = Gravity.CENTER;
    snackbarView.setLayoutParams(params);
    int padding = (int) (50 * activity.getResources().getDisplayMetrics().density);
    snackbarView.setPadding(padding, padding, padding, padding);

    // Pusatkan teks
    TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    textView.setGravity(Gravity.CENTER);
    textView.setMaxLines(5); // supaya tidak terpotong jika teks panjang

    snackbar.show();
  }

  public static void showSnackbarBelow(View targetView, String message) {
    View rootView = targetView.getRootView();

    Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);

    // Ambil view snackbar
    View snackbarView = snackbar.getView();

    // Hitung posisi targetView di layar
    int[] location = new int[2];
    targetView.getLocationOnScreen(location);
    int targetBottomY = location[1] + targetView.getHeight();

    // Ambil tinggi status bar untuk offset
    int statusBarHeight = 0;
    int resourceId =
        targetView.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      statusBarHeight = targetView.getResources().getDimensionPixelSize(resourceId);
    }

    // Konfigurasi layout params
    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
    params.gravity = Gravity.TOP;
    params.topMargin = targetBottomY - statusBarHeight + 8; // +8 biar ada jarak
    snackbarView.setLayoutParams(params);

    // Tambahkan styling
    snackbarView.setPadding(32, 16, 32, 16);
    TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

    snackbar.show();
  }

  public static void copyToClipboard(Activity context, String text, String message, View v) {
    ClipboardManager clipboardManager =
        (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newPlainText("copy", text);
    clipboardManager.setPrimaryClip(clip);
	showToast(message,context);
    //	showSnackbarBelow(v, message);
  }

  public static void applyInsets(View content) {
    ViewUtils.doOnApplyWindowInsets(
        content,
        (v, insets, initialPadding) -> {
          content.setPaddingRelative(
              initialPadding.start,
              initialPadding.top,
              initialPadding.end,
              initialPadding.bottom
                  + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
          return insets;
        });
  }

  public static int dpToPx(float dp, Context context) {
    return dpToPx(dp, context.getResources());
  }

  public static int dpToPx(float dp, Resources resources) {
    float px =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    return (int) px;
  }

  /**
   * Mengembalikan SpannableString dengan teks yang di-highlight.
   *
   * @param originalText Teks asli
   * @param queryText Teks pencarian
   * @param highlightColor Warna highlight (contoh: Color.RED)
   * @return SpannableString dengan teks yang di-highlight
   */
  public static SpannableString getHighlightedText(
      String originalText, String queryText, int highlightColor) {
    SpannableString spannableString = new SpannableString(originalText);

    if (queryText == null || queryText.isEmpty()) {
      return spannableString; // Jika tidak ada query, kembalikan teks asli
    }

    String lowerOriginal = originalText.toLowerCase();
    String lowerQuery = queryText.toLowerCase();

    int startIndex = lowerOriginal.indexOf(lowerQuery);
    while (startIndex >= 0) {
      int endIndex = startIndex + lowerQuery.length();
      spannableString.setSpan(
          new ForegroundColorSpan(highlightColor),
          startIndex,
          endIndex,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      startIndex = lowerOriginal.indexOf(lowerQuery, endIndex); // Cari instance berikutnya
    }

    return spannableString;
  }

  public static void startTimer(long duration, CountdownListener listener) {
    timer =
        new CountDownTimer(duration, 1000) {
          @Override
          public void onTick(long millisUntilFinished) {
            long minutes = millisUntilFinished / (60 * 1000);
            long seconds = (millisUntilFinished / 1000) % 60;
            listener.onTick(minutes, seconds);
          }

          @Override
          public void onFinish() {
            listener.onFinish();
          }
        }.start();
  }

  public static void cancelCountdown() {
    if (timer != null) {
      timer.cancel();
    }
  }

  public static void tabTooltip(TabLayout tabLayout) {
    for (int i = 0; i < tabLayout.getTabCount(); i++) {
      TabLayout.Tab tab = tabLayout.getTabAt(i);
      if (tab != null) {
        tab.view.setOnLongClickListener(
            new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View v) {
                return true;
              }
            });
      }
    }
  }

  public interface OnTransitionEndCallback {
    void onEnd();
  }

  /**
   * Menjalankan kode setelah enter transition selesai. Jika tidak ada transition, langsung
   * dijalankan.
   *
   * @param window Window dari activity
   * @param callback Kode yang ingin dijalankan setelah transisi
   */
  public static void runAfterEnterTransition(Window window, OnTransitionEndCallback callback) {
    Transition transition = window.getEnterTransition();
    if (transition != null) {
      transition.addListener(
          new Transition.TransitionListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
              transition.removeListener(this);
              callback.onEnd();
            }

            @Override
            public void onTransitionStart(Transition transition) {}

            @Override
            public void onTransitionCancel(Transition transition) {}

            @Override
            public void onTransitionPause(Transition transition) {}

            @Override
            public void onTransitionResume(Transition transition) {}
          });
    } else {
      callback.onEnd();
    }
  }

  public static void animateShake(View view) {
    Animation shake = new TranslateAnimation(0, 16, 0, 0);
    shake.setDuration(100);
    shake.setRepeatMode(Animation.REVERSE);
    shake.setRepeatCount(3);
    view.startAnimation(shake);
  }

  public static void startCountdownTimer(TextView textView, long serverLockTime) {
    long currentTime = System.currentTimeMillis() / 1000;
    long sisaDetik = serverLockTime - currentTime;

    if (sisaDetik <= 0) {
      // binding.keyboard.setEnabled(true);
      // binding.etPin.setEnabled(true);
      // textView.setVisibility(View.GONE);
      return;
    }

    long millis = sisaDetik * 1000;

    // Disable input
    // binding.keyboard.setEnabled(false);
    // binding.etPin.setEnabled(false);
    // textView.setVisibility(View.VISIBLE);

    // Jalankan timer
    timer =
        new CountDownTimer(millis, 1000) {
          @Override
          public void onTick(long millisUntilFinished) {
            long menit = (millisUntilFinished / 1000) / 60;
            long detik = (millisUntilFinished / 1000) % 60;
            String sisa = String.format("Tunggu %02d:%02d", menit, detik);
            textView.setText(sisa);
          }

          @Override
          public void onFinish() {
            textView.setText("Silakan masukkan PIN kembali");
            // binding.keyboard.setEnabled(true);
            // binding.etPin.setEnabled(true);
            textView.postDelayed(() -> textView.setVisibility(View.GONE), 2000);
          }
        }.start();
  }
  
  public static Drawable getDrawable(Context context, int bg){
	  Drawable drawable = ContextCompat.getDrawable(context,bg);
	  if (drawable != null){
		  drawable = drawable.mutate();
	  }
	  return drawable;
  }
}
