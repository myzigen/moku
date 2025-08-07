package com.mhr.mobile.ui.produk.prepaid.pln;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.inquiry.InquiryCustomer;
import com.mhr.mobile.ui.produk.abs.AbsPrepaidReload;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.preferences.PrefInputCache;
import java.util.List;

public class PLNTokenReload extends AbsPrepaidReload {
  private String iconUrl = "https://api.qiospro.my.id/assets/logo/pln.png";
  private boolean cacheMeter = false;

  private String cachedNomor = null;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    View view = super.onCreateQiosView(layoutInflater, viewGroup, bundle);
    
    binding.infoNoProduk.setVisibility(View.GONE);
    absFilterVisibility(tidak);
	loadCache();
    return view;
  }

  @Override
  protected String absTitleToolbar() {
    return "Token Listrik";
  }

  @Override
  protected String absText() {
    return "No Meter";
  }

  @Override
  protected String absPlaceholderText() {
    return "No meter/ID Pelanggan";
  }

  @Override
  protected void absAfterTextChanged(String nomor) {
    if (nomor.isEmpty()) {
      hideExpandError();
      reset();
      return;
    }

    if (nomor.equalsIgnoreCase(cachedNomor) && inquiry != null && !inquiry.isEmpty()) {
      // Jangan reset kalau nomor masih sama dan inquiry sudah ada
      showExpandSuccess(inquiry);
      cacheMeter = true;
    } else if (!nomor.equalsIgnoreCase(cachedNomor)) {
      // Reset hanya jika benar-benar berbeda
      cacheMeter = false;
      inquiry = null;
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
    String validate = binding.etPhoneNumber.getText().toString();
    if (validate.isEmpty()) {
      showExpandError("ID Pelanggan Belum Di Isi");
      adapter.setInputValid(false);
      adapter.resetSelectedPosition();
      return;
    }
    if (validate.length() < 11) {
      showExpandError("ID Pelanggan Tidak Ditemukan");
      adapter.setInputValid(false);
      adapter.resetSelectedPosition();
      return;
    }

    hideKeyboard();
    adapter.setInputValid(true);
    binding.wrapperPembayaran.setVisibility(View.VISIBLE);
    binding.pembayaran.harga.setText(FormatUtils.formatRupiah(pricelist.getHargaJual()));

    if (validate.equalsIgnoreCase(cachedNomor) && inquiry != null) {
      if (!inquiry.isEmpty()) {
        showExpandSuccess(inquiry);
      } else {
        showExpandError("Nometer tidak ditemukan");
      }
      infoPembelian(pricelist, validate);
      return;
    }

    inquiry = "";
    cachedNomor = validate;
    checkCustomerName(validate, pricelist);
  }

  @Override
  protected void absDetectProvider(String nomor) {}

  @Override
  protected void absNomorRiwayat(String nomor) {}

  @Override
  protected String absBrandIconUrl() {
    return iconUrl;
  }

  @Override
  protected String absKategoriProduk() {
    return "pln";
  }

  @Override
  protected void absProdukLoaded(List<ResponsePricelist> produk) {
    adapter.perbaruiData(produk);
  }

  @Override
  protected void absProdukFailed(String failed) {}

  private void checkCustomerName(String nometer, ResponsePricelist model) {
    InquiryCustomer.with(this)
        .InquiryPLNToken(
            nometer,
            new InquiryCustomer.Callback() {
              @Override
              public void onLoading() {
				showExpandLoading("Pengecekan Pengguna...");
              }

              @Override
              public void onSuccess(String trName) {
                showExpandSuccess(trName);
                inquiry = trName;
                infoPembelian(model, nometer);
				saveCache();
              }

              @Override
              public void onError(String error) {}
            });
  }

  private void saveCache() {
    PrefInputCache cache = new PrefInputCache();
    cache.nomor = editText.getText().toString();
    cache.customer_name = inquiry;
    String json = new Gson().toJson(cache);
    pref.setString("cache_token_listrik" + absKategoriProduk().toLowerCase(), json);
  }

  private void loadCache() {
    String json = pref.getString("cache_token_listrik" + absKategoriProduk().toLowerCase());
    if (json != null && !json.isEmpty()) {
      PrefInputCache cache = new Gson().fromJson(json, PrefInputCache.class);

      editText.setText(cache.nomor);
      this.inquiry = cache.customer_name;
      this.cachedNomor = cache.nomor;
    }
  }
}
