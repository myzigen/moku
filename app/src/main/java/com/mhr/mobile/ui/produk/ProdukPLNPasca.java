package com.mhr.mobile.ui.produk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.PagerPlnContentBinding;
import com.mhr.mobile.ui.inject.InjectionFragment;

public class ProdukPLNPasca extends InjectionFragment {
  private PagerPlnContentBinding binding;

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = PagerPlnContentBinding.inflate(i, v, false);
    return binding.getRoot();
  }
}
