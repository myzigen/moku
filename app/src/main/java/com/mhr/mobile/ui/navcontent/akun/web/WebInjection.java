package com.mhr.mobile.ui.navcontent.akun.web;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.browser.customtabs.CustomTabsIntent;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class WebInjection extends InjectionActivity {
  public static String ARG_URL = "url";

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    String webUrl = getIntent().getStringExtra(ARG_URL);
    if (webUrl != null && !webUrl.isEmpty()) {
      CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
	  builder.enableUrlBarHiding();
	  builder.setShowTitle(false);
      CustomTabsIntent customTabsIntent = builder.build();
      customTabsIntent.launchUrl(this, Uri.parse(webUrl));
    }
    // Karena Chrome Custom Tabs tidak memerlukan layout, langsung akhiri activity
    finish();
    return new View(this);
  }
}
