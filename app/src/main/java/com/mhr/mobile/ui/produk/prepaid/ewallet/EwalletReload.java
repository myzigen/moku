package com.mhr.mobile.ui.produk.prepaid.ewallet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.inquiry.InquiryCustomer;
import com.mhr.mobile.ui.produk.abs.AbsPrepaidReload;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.ProviderUtils;
import com.mhr.mobile.util.preferences.PrefInputCache;
import java.util.ArrayList;
import java.util.List;

public class EwalletReload extends AbsPrepaidReload {
  private boolean nomorSudahDicek = false;
  private String brand, brandIcon;
  private String cachedNomor = null;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    View view = super.onCreateQiosView(layoutInflater, viewGroup, bundle);
    initiaize();
    absFilterVisibility(tidak);
    return view;
  }

  private void initiaize() {
    brand = getIntent().getStringExtra("brand");
    brandIcon = getIntent().getStringExtra("brand_icon");
    loadCache();
  }

  @Override
  protected String absTitleToolbar() {
    return getIntent().getStringExtra("brand");
  }

  @Override
  protected String absText() {
    return "Nomor Hp";
  }

  @Override
  protected String absPlaceholderText() {
    return "081234567890";
  }

  @Override
  protected void absAfterTextChanged(String nomor) {
    if (nomor.isEmpty()) {
      hideExpandError();
      reset();
      binding.wrapperPembayaran.setVisibility(View.GONE);
      return;
    }

    if (nomor.equalsIgnoreCase(cachedNomor) && inquiry != null && !inquiry.isEmpty()) {
      // Jangan reset kalau nomor masih sama dan inquiry sudah ada
      showExpandSuccess(inquiry);
      nomorSudahDicek = true;
    } else if (!nomor.equalsIgnoreCase(cachedNomor)) {
      // Reset hanya jika benar-benar berbeda
      nomorSudahDicek = false;
      inquiry = null;
      binding.pembayaran.btnLanjutBayar.setEnabled(true);
    }

    hideExpandError();
  }

  private void reset() {
    adapter.setInputValid(tidak);
    adapter.resetSelectedPosition();
    binding.wrapperPembayaran.setVisibility(View.GONE);
  }

  @Override
  protected void absValidate(ResponsePricelist pricelist) {
    String validate = editText.getText().toString();

    if (validate.isEmpty()) {
      showExpandError("Nomor " + brand + " Belum Di Isi");
      adapter.setInputValid(false);
      adapter.resetSelectedPosition();
      return;
    }

    if (validate.length() < 11) {
      showExpandError("Nomor tidak sah");
      adapter.setInputValid(false);
      adapter.resetSelectedPosition();
      return;
    }

    hideKeyboard();
    adapter.setInputValid(true);
    absDetectProvider(validate);
    binding.wrapperPembayaran.setVisibility(View.VISIBLE);
    binding.pembayaran.harga.setText(FormatUtils.formatRupiah(pricelist.getHargaJual()));

    // ✅ Jika nomor sama dengan cache, dan inquiry sudah pernah diset (meskipun kosong), jangan
    // panggil ulang API
    if (validate.equalsIgnoreCase(cachedNomor) && inquiry != null) {
      if (!inquiry.isEmpty()) {
        showExpandSuccess(inquiry);
      } else {
        showExpandError("Nomor tidak terdaftar");
      }
      infoPembelian(pricelist, validate);
      return;
    }

    // Kalau belum pernah dicek atau nomor berbeda → reset & panggil API
    inquiry = ""; // ← tandai sudah dicek, walaupun hasilnya kosong
    cachedNomor = validate;
    checkCustomerName(validate, brand, pricelist);
  }

  @Override
  protected void absDetectProvider(String nomor) {
    if (nomor.length() < 4) {
      return;
    }
    String provider = ProviderUtils.detectProvider(nomor);
    if (provider.equals("Unknown")) {
      showExpandError("Nomor tidak terdaftar");
    }
  }

  private void checkCustomerName(String nomor, String brand, ResponsePricelist model) {
    InquiryCustomer.with(this)
        .InquiryEwallet(
            nomor,
            brand,
            new InquiryCustomer.Callback() {
              @Override
              public void onLoading() {
                binding.pembayaran.btnLanjutBayar.setEnabled(false);
                binding.progressExpand.setVisibility(View.VISIBLE);
                showExpandLoading("Pengecekan nomor...");
              }

              @Override
              public void onSuccess(String trName) {
                inquiry = trName;
                new Handler(Looper.getMainLooper())
                    .postDelayed(
                        () -> {
                          binding.pembayaran.btnLanjutBayar.setEnabled(true);
                          binding.progressExpand.setVisibility(View.GONE);
                          showExpandSuccess(trName);
                        },
                        2000);

                infoPembelian(model, nomor);
                saveCache();
              }

              @Override
              public void onError(String error) {
                binding.progressExpand.setVisibility(View.GONE);
                showExpandError("Nomor " + brand + error);
                binding.pembayaran.btnLanjutBayar.setEnabled(false);
              }
            });
  }

  @Override
  protected void absNomorRiwayat(String nomor) {}

  @Override
  protected String absBrandIconUrl() {
    return getIntent().getStringExtra("brand_icon");
  }

  @Override
  protected String absKategoriProduk() {
    return getIntent().getStringExtra("category");
  }

  @Override
  protected void absProdukLoaded(List<ResponsePricelist> produk) {
    List<ResponsePricelist> filtered = new ArrayList<>();
    for (ResponsePricelist item : produk) {
      if (item.getBrand().equalsIgnoreCase(brand)) {
        filtered.add(item);
      }
    }

    adapter.perbaruiData(filtered);
  }

  @Override
  protected void absProdukFailed(String failed) {}

  private void saveCache() {
    PrefInputCache cache = new PrefInputCache();
    cache.nomor = editText.getText().toString();
    cache.customer_name = inquiry;
    String json = new Gson().toJson(cache);
    pref.setString("cache_ewallet" + brand.toLowerCase(), json);
  }

  private void loadCache() {
    String json = pref.getString("cache_ewallet" + brand.toLowerCase());
    if (json != null && !json.isEmpty()) {
      PrefInputCache cache = new Gson().fromJson(json, PrefInputCache.class);

      editText.setText(cache.nomor);
      this.inquiry = cache.customer_name;
      this.cachedNomor = cache.nomor;
    }
  }

  @Override
  protected void onPhoneNumberPicked(String phoneNumber) {
    editText.setText(phoneNumber);
    binding.etPhoneNumber.setSelection(phoneNumber.length());
    absDetectProvider(phoneNumber);
    hideKeyboard();
    reset();
  }
}
