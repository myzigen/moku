package com.mhr.mobile.ui.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.ContentNotificationBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class ContentNotification extends InjectionActivity {
  private ContentNotificationBinding binding;

  @Override
  protected String getTitleToolbar() {
    return "Notifikasi";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentNotificationBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }
}
