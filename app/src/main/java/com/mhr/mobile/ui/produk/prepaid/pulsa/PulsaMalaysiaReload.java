package com.mhr.mobile.ui.produk.prepaid.pulsa;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.ui.produk.abs.AbsPrepaidReload;
import java.util.List;

public class PulsaMalaysiaReload extends AbsPrepaidReload {

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    View view = super.onCreateQiosView(layoutInflater, viewGroup, bundle);
	return view;
  }

  @Override
  protected String absTitleToolbar() {
    return null;
  }

  @Override
  protected String absText() {
    return null;
  }

  @Override
  protected String absPlaceholderText() {
    return null;
  }

  @Override
  protected void absAfterTextChanged(String nomor) {}

  @Override
  protected void absValidate(ResponsePricelist pricelist) {}

  @Override
  protected void absDetectProvider(String nomor) {}

  @Override
  protected void absNomorRiwayat(String nomor) {}

  @Override
  protected String absBrandIconUrl() {
    return null;
  }

  @Override
  protected String absKategoriProduk() {
    return null;
  }

  @Override
  protected void absProdukLoaded(List<ResponsePricelist> produk) {}

  @Override
  protected void absProdukFailed(String failed) {}
}
