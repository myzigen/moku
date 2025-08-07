package com.mhr.mobile.ui.navcontent.akun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.SettingPinAppBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.navcontent.akun.AkunNewPin;
import com.mhr.mobile.ui.sheet.SheetPIN;

public class AkunPin extends InjectionActivity {
  private SettingPinAppBinding binding;
  private boolean suppressTransaksiListener = false;
  private boolean suppressLoginListener = false;

  @Override
  protected String getTitleToolbar() {
    return "PIN Keamanan";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SettingPinAppBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    applyPin();
    binding.btnUbahPin.setOnClickListener(
        v -> {
          targetActivity(AkunNewPin.class);
        });
  }

  private void applyPin() {
    // Switch untuk PIN Transaksi
    binding.switchTransaksi.setChecked(pref.getPinPurchase());
    binding.switchTransaksi.setOnCheckedChangeListener(
        (btn, isChecked) -> {
          if (suppressTransaksiListener) return;
          if (!isChecked) {
            // Validasi dulu saat ingin NONAKTIFKAN
            SheetPIN pinPurchase = SheetPIN.newInstance("purchase");
            pinPurchase.setUpdateField("pin_purchase", false);
            pinPurchase.setOnPinValidatedListener(
                () -> {
                  pref.setPinPurchase(false);
                  suppressTransaksiListener = true;
                  binding.switchTransaksi.setChecked(false);
                  suppressTransaksiListener = false;
                });

            pinPurchase.show(getSupportFragmentManager(), "checked-pin-purchase");
            suppressTransaksiListener = true;
            // Kembalikan ke posisi semula dulu
            binding.switchTransaksi.setChecked(true);
            suppressTransaksiListener = false;
          } else {
            // Langsung aktifkan tanpa validasi
            pref.setPinPurchase(true);
          }
        });

    // Switch untuk PIN Masuk Aplikasi
    binding.switchPinMasuk.setChecked(pref.getPin());
    binding.switchPinMasuk.setOnCheckedChangeListener(
        (btnView, isChecked) -> {
          if (suppressLoginListener) return;
          if (!isChecked) {
            // Validasi saat ingin mematikan
            SheetPIN pinLogin = SheetPIN.newInstance("pin");
            pinLogin.setUpdateField("pin_login", false);
            pinLogin.setOnPinValidatedListener(
                () -> {
                  pref.setPin(false);
                  suppressLoginListener = true;
                  binding.switchPinMasuk.setChecked(false);
                  suppressLoginListener = false;
                });
            pinLogin.show(getSupportFragmentManager(), "checked-pin-login");
            suppressLoginListener = true;
            // Kembalikan ke posisi semula dulu
            binding.switchPinMasuk.setChecked(true);
            suppressLoginListener = false;
          } else {
            // Langsung aktifkan
            pref.setPin(true);
          }
        });
  }
}
