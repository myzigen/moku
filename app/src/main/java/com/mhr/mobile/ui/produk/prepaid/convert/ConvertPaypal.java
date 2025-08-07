package com.mhr.mobile.ui.produk.prepaid.convert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.FormConvertBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class ConvertPaypal extends InjectionActivity {
  private FormConvertBinding binding;

  @Override
  protected String getTitleToolbar() {
    return "Convert Paypal";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = FormConvertBinding.inflate(layoutInflater, viewGroup, false);
    initEditext(binding.etNominal);
    return binding.getRoot();
  }
}
