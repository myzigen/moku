package com.mhr.mobile.ui.inject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.mhr.mobile.util.AndroidViews;

public abstract class InjectionTheme extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
  }

  protected void showToast(String message) {
    AndroidViews.showToast(message, this);
  }

  protected void showSnackbar(String message) {
    AndroidViews.showSnackbar(this, message);
  }
}
