package com.mhr.mobile.ui.produk.prepaid.kuota;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.FormPrepaidMassalBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.sheet.SheetPrepaidType;

public class KuotaPerdanaMassal extends InjectionActivity {
  private FormPrepaidMassalBinding binding;
  private String kategori, brand;

  @Override
  protected String getTitleToolbar() {
    return "Inject Perdana";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = FormPrepaidMassalBinding.inflate(getLayoutInflater());
    initialize();
    ui();
    return binding.getRoot();
  }
/*
  @Override
  protected String absText() {
    return "Nomor Hp";
  }

  @Override
  protected String absPlaceholderText() {
    return "081234567890";
  }
  
  */

  private void ui() {
    binding.etPilihOperator.setShowSoftInputOnFocus(false);
    binding.etPilihOperator.setOnClickListener(this::pilihOperator);
  }

  private void initialize() {
    kategori = getAbsIntent("kategori");
    brand = getAbsIntent("brand");
  }

  private void pilihOperator(View v) {
    SheetPrepaidType sheet = SheetPrepaidType.newInstance(kategori, brand);
    sheet.show(getSupportFragmentManager());
  }
}
