package com.mhr.mobile.ui.produk.prepaid.kuota;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.ProviderUtils;
import java.util.ArrayList;
import java.util.List;
import com.mhr.mobile.R;

public class KuotaVoucherInject extends AbsKuotaReload {

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    View view = super.onCreateQiosView(layoutInflater, viewGroup, bundle);
    klikFilter();
    return view;
  }

  @Override
  protected String absText() {
	  return "Nomor Seri";
  }

  @Override
  protected String absPlaceholderText() {
	  return "1234-5678-90XX-X";
  }

  private void klikFilter() {
    // loadCache();
	toolbar.setTitle("Cetak Voucher Kosong");
	binding.endIconContainer.setVisibility(View.GONE);
	binding.inputLayout.setEndIconDrawable(R.drawable.barcode_scan);
    binding.btnNamaPaket.setOnClickListener(this::onFilterClick);
    binding.btnKuota.setOnClickListener(this::onFilterClick);
    binding.btnMasaAktif.setOnClickListener(this::onFilterClick);
    binding.btnTerapkan.setOnClickListener(this::filterProduk);
    binding.btnReset.setOnClickListener(this::filterReset);
  }

  @Override
  protected void validate(ResponsePricelist pricelist) {
    String nomor = binding.etPhoneNumber.getText().toString();
    String brand = getIntent().getStringExtra("brand");
    if (nomor.isEmpty()) {
      showExpandError("Nomor Seri belum di isi");
      setujui(false);
      return;
    }

    if (nomor.length() < 10) {
      showExpandError("Nomor Seri tidak sah");
      setujui(false);
      return;
    }
	/*
    if (!ProviderUtils.detectBrand(nomor, brand)) {
      showExpandError("Eiits Salah! Ini Bukan Nomor " + brand);
      setujui(false);
      return;
    }
	*/

    setujui(true);
    binding.wrapperPembayaran.setVisibility(View.VISIBLE);
    binding.pembayaran.harga.setText(FormatUtils.formatRupiah(pricelist.getHargaJual()));
    infoPembelian(pricelist, nomor);
    hideKeyboard();
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
