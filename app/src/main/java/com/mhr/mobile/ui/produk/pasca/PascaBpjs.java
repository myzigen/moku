package com.mhr.mobile.ui.produk.pasca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.ui.produk.abs.AbsPascaReload;

public class PascaBpjs extends AbsPascaReload {

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    View view = super.onCreateQiosView(layoutInflater, viewGroup, bundle);
    absContentVisibility(true);
    binding.provider.setClickable(false);
	binding.arrow.setVisibility(View.GONE);
    binding.txtLabelProvider.setText("Bpjs Kesehatan");
	binding.inputLayout.setPlaceholderText("No.VA Keluarga");
    Glide.with(this)
        .load("https://api.qiospro.my.id/assets/logo/ic-bpjs.png")
        .into(binding.logoProvider);
    return view;
  }

  @Override
  protected void validate(String nomor) {}

  @Override
  protected void absAfterTextChanged(String input) {}

  @Override
  protected String absTitleToolbar() {
    return "Bpjs";
  }

  @Override
  protected String absTypeProduk() {
    return getAbsIntent("kategori");
  }
}
