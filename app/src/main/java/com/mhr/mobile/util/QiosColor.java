package com.mhr.mobile.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mhr.mobile.R;

public class QiosColor {

  public static int getColor(Context context, int color) {
    return ContextCompat.getColor(context, color);
  }

  public static int getActiveColor(Context context) {
    return ContextCompat.getColor(context, R.color.me_color_icon);
  }

  public static int getDisableColor(Context context) {
    return ContextCompat.getColor(context, R.color.me_divider);
  }

  public static int getSuccessColor(Context context, int color) {
    return ContextCompat.getColor(context, color);
  }

  public static int getErrorColor(Context context) {
    return ContextCompat.getColor(context, R.color.status_canceled);
  }

  public static void applyDominantColorGradient(Context ctx, String imageUrl, View targetView) {
    Glide.with(ctx)
        .asBitmap()
        .load(imageUrl)
		//.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
		//.placeholder(R.drawable.ic_no_image)
		.centerCrop()
        .into(
            new CustomTarget<Bitmap>() {
              @Override
              public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                // Extract dominant color using Palette
                Palette.from(resource)
                    .generate(
                        palette -> {
                          int defaultColor = getColor(ctx, android.R.color.darker_gray);
                          int dominantColor = palette.getDominantColor(defaultColor);

                          // Tambahkan alpha: nilai 0–255 (contoh: 180 = sekitar 70% opacity)
                          int dominantColorWithAlpha =
                              ColorUtils.setAlphaComponent(dominantColor, 22);

                          GradientDrawable gradientDrawable =
                              new GradientDrawable(
                                  GradientDrawable.Orientation.TOP_BOTTOM,
                                  new int[] {dominantColorWithAlpha, Color.TRANSPARENT});
                          gradientDrawable.setCornerRadius(0f);

                          targetView.setBackground(gradientDrawable);
                        });
              }

              @Override
              public void onLoadCleared(@Nullable Drawable placeholder) {
                // Optional: handle cleanup
              }
            });
  }

  public static void applyDominantColorToView(Bitmap resource, View view) {
    if (resource != null) {
      Palette.from(resource)
          .generate(
              new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                  if (palette != null) {
                    // Dapatkan warna dominan
                    int dominantColor = palette.getDominantColor(Color.BLACK);

                    // Terapkan warna dominan ke tampilan
                    view.setBackgroundColor(dominantColor);
                  }
                }
              });
    }
  }
}
