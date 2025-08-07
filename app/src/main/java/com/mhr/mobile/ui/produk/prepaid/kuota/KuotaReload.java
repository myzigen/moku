package com.mhr.mobile.ui.produk.prepaid.kuota;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.ui.produk.adapter.AdapterHelper;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.ProviderUtils;
import com.mhr.mobile.util.preferences.PrefInputCache;
import java.util.ArrayList;
import java.util.List;

public class KuotaReload extends AbsKuotaReload {

  private String cachedNomor = null;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    View view = super.onCreateQiosView(layoutInflater, viewGroup, bundle);

    initUi();

    return view;
  }

  @Override
  protected String absText() {
    return "Nomor Hp";
  }

  @Override
  protected String absPlaceholderText() {
    return "081234567890";
  }

  private void hitungJumlahProduk() {
    AdapterHelper.hitungJumlahProdukByKuota(adapter, binding.jumlahProduk);
  }

  @Override
  protected void validate(ResponsePricelist item) {
    String nomor = binding.etPhoneNumber.getText().toString();
    String brand = getIntent().getStringExtra("brand");
    if (nomor.isEmpty()) {
      showExpandError("Nomor tujuan belum di isi");
      setujui(false);
      return;
    }

    if (nomor.length() < 10) {
      showExpandError("Nomor tujuan tidak sah");
      setujui(false);
      return;
    }

    if (!ProviderUtils.detectBrand(nomor, brand)) {
      showExpandError("Eiits Salah! Ini Bukan Nomor " + brand);
      setujui(false);
      return;
    }

    setujui(true);
    binding.wrapperPembayaran.setVisibility(View.VISIBLE);
    binding.pembayaran.harga.setText(FormatUtils.formatRupiah(item.getHargaJual()));
    infoPembelian(item, nomor);
    hideKeyboard();
  }

  private void initUi() {
    loadCache();
    binding.btnNamaPaket.setOnClickListener(this::onFilterClick);
    binding.btnKuota.setOnClickListener(this::onFilterClick);
    binding.btnMasaAktif.setOnClickListener(this::onFilterClick);
    binding.btnTerapkan.setOnClickListener(this::filterProduk);
    binding.btnReset.setOnClickListener(this::filterReset);
  }

  private void saveCache() {
    PrefInputCache cache = new PrefInputCache();
    cache.nomor = binding.etPhoneNumber.getText().toString();
    // cache.customer_name = inquiry;
    String json = new Gson().toJson(cache);
    pref.setString("cache_kuota" + getIntent().getStringExtra("brand").toLowerCase(), json);
  }

  private void loadCache() {
    PrefInputCache cache = PrefInputCache.load(getIntent().getStringExtra("brand"));
    if (cache != null && cache.nomor != null) {
      binding.etPhoneNumber.setText(cache.nomor);
    }
    /*
       String json = pref.getString("cache_kuota" + getIntent().getStringExtra("brand").toLowerCase());
       if (json != null && !json.isEmpty()) {
         PrefInputCache cache = new Gson().fromJson(json, PrefInputCache.class);

         binding.etPhoneNumber.setText(cache.nomor);
       }
    */
  }

  @Override
  protected String absBrandIconUrl() {
    return getAbsIntent("brand_icon");
  }

  @Override
  protected String absKategoriProduk() {
    return getAbsIntent("kategori");
  }

  @Override
  protected void absProdukLoaded(List<ResponsePricelist> produk) {
    List<ResponsePricelist> filtered = new ArrayList<>();
    for (ResponsePricelist item : produk) {
      if (item.getBrand().equalsIgnoreCase(getAbsIntent("brand"))) {
        filtered.add(item);
      }
    }

    adapter.setOriginalData(filtered);
  }

  @Override
  protected void absProdukFailed(String failed) {}
}
