package com.mhr.mobile.ui.produk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.MenuPulsaBinding;
import com.mhr.mobile.ui.inject.InjectionFragment;

public class ProdukPLNToken extends InjectionFragment {
  private MenuPulsaBinding binding;

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = MenuPulsaBinding.inflate(getLayoutInflater());

    return binding.getRoot();
  }
}
