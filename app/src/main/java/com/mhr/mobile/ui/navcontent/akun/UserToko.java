package com.mhr.mobile.ui.navcontent.akun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.UserTokoBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class UserToko extends InjectionActivity {
  private UserTokoBinding binding;

  @Override
  public View onCreateQiosView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserTokoBinding.inflate(inflater, viewGroup, false);
    return binding.getRoot();
  }
}
