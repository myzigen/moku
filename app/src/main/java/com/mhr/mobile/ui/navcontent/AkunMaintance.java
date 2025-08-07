package com.mhr.mobile.ui.navcontent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.AkunMaintanceBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class AkunMaintance extends InjectionActivity {
  private AkunMaintanceBinding binding;

  @Override
  public View onCreateQiosView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = AkunMaintanceBinding.inflate(inflater, viewGroup, false);
    return binding.getRoot();
  }
}
