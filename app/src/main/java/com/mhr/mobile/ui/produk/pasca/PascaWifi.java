package com.mhr.mobile.ui.produk.pasca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mhr.mobile.ui.produk.abs.AbsPascaReload;
import com.mhr.mobile.util.preferences.PrefInputCache;

public class PascaWifi extends AbsPascaReload {
  private String kategori;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    View view = super.onCreateQiosView(layoutInflater, viewGroup, bundle);
    kategori = getAbsIntent("kategori");

    loadCache();
    absContentVisibility(brand != null);
    ui();
    return view;
  }

  private void ui() {
    // new Handler(Looper.getMainLooper()).postDelayed(() -> openSheetProvider(), 500);
  }

  @Override
  protected String absTitleToolbar() {
    return "Tagihan Internet";
  }

  @Override
  protected String absTypeProduk() {
    return kategori;
  }

  @Override
  protected void validate(String nomor) {
    if (nomor.isEmpty()) {
      showExpandError("Nomor belum di isi");
      return;
    }

    if (inquiryData != null) {
      goToActivity();
    } else {
      inquiryPasca(nomor);
    }
  }

  @Override
  protected void absAfterTextChanged(String input) {
    if (input.isEmpty()) {
      binding.btnLihatTagihan.setEnabled(false);
      hideExpandError();
      return;
    }

    if (input.length() >= 5) {
      hideExpandError();
      binding.btnLihatTagihan.setEnabled(true);
      return;
    }

    binding.btnLihatTagihan.setEnabled(false);
  }

  private void loadCache() {
    String json = pref.getString(absTypeProduk());
    if (json != null && !json.isEmpty()) {
      PrefInputCache cache = new Gson().fromJson(json, PrefInputCache.class);

      binding.editText.setText(cache.nomor);
      binding.infoSku.setText(cache.sku); // pastikan infoSku adalah TextView tersembunyi
      binding.txtLabelProvider.setText(cache.brand);
      Glide.with(this).load(cache.logoUrl).into(binding.logoProvider);
      binding.logoProvider.setVisibility(View.VISIBLE);
      binding.btnLihatTagihan.setEnabled(true);

      // simpan kembali ke variabel class
	  this.strSku = cache.sku;
      this.brand = cache.brand;
      this.logoUrl = cache.logoUrl;

      boolean isChecked = pref.getBoolean("reminder_enabled", false);
      binding.checkboxReminder.setChecked(isChecked);

      String savedTanggal = pref.getString("tanggal_internet");

      if (savedTanggal != null && savedTanggal.contains("/")) {
        String[] split = savedTanggal.split("/");
        String hari = split[0];
        binding.infoTglReminder.setText(hari);
      } else {
        binding.infoTglReminder.setText("1"); // Default
      }
    }
  }
}
