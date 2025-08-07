package com.mhr.mobile.ui.produk.prepaid.pulsa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.ui.produk.abs.AbsPrepaidReload;
import com.mhr.mobile.util.FormatUtils;
import java.util.ArrayList;
import java.util.List;

public class PulsaSingaporeReload extends AbsPrepaidReload {
  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    View view = super.onCreateQiosView(layoutInflater, viewGroup, bundle);
    absFilterVisibility(tidak);
    return view;
  }

  @Override
  protected String absTitleToolbar() {
    return getAbsIntent("brand");
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
  protected void absAfterTextChanged(String nomor) {}

  @Override
  protected void absValidate(ResponsePricelist pricelist) {
    String validate = editText.getText().toString().trim();
    if (validate.isEmpty()) {
      showExpandError("Nomor Belum Di Isi");
      return;
    }

    if (validate.length() < 10) {
      showExpandError("Nomor Tidak Sah");
      return;
    }

    hideKeyboard();
	adapter.setInputValid(true);
	binding.wrapperPembayaran.setVisibility(View.VISIBLE);
    binding.pembayaran.harga.setText(FormatUtils.formatRupiah(pricelist.getHargaJual()));
	infoPembelian(pricelist, validate);
  }

  @Override
  protected void absDetectProvider(String nomor) {}

  @Override
  protected void absNomorRiwayat(String nomor) {}

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

    adapter.perbaruiData(filtered);
  }

  @Override
  protected void absProdukFailed(String failed) {}
}
