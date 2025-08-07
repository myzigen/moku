package com.mhr.mobile.widget.tooltip;

import java.util.Locale;

class UiUtils {

  public static boolean isRtl() {
    return isRtl(Locale.getDefault());
  }

  private static boolean isRtl(Locale locale) {
    final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
    return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT
        || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
  }
}
