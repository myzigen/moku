package com.mhr.mobile.ui.navcontent.home.marketplace;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.MenuMarketplaceBinding;
import com.mhr.mobile.manage.response.MarketplaceResponse;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.FormatUtils;

public class MarketplaceProduk extends InjectionActivity {
  private MenuMarketplaceBinding binding;
  private String produkName;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuMarketplaceBinding.inflate(getLayoutInflater());
    // toolbar.setBackgroundColor(QiosColor.getColor(this, R.color.white));
    terimaData();
    return binding.getRoot();
  }

  @Override
  protected String getTitleToolbar() {
    return produkName;
  }

  private void terimaData() {
    MarketplaceResponse.Data data = getIntent().getParcelableExtra("marketplace");
    produkName = data.getProdukName();
    toolbar.setTitle(getTitleToolbar());
    Glide.with(this)
        .load(data.getImageUrl().get(0))
        .placeholder(R.drawable.ic_no_image)
        .into(binding.image);
    binding.infoHarga.setText(FormatUtils.formatRupiah(data.getHarga()));
	binding.infoProduk.setText(produkName);
	binding.infoDesc.setText(data.getDeskripsi());
  }
}
