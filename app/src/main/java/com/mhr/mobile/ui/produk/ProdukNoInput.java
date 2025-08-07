package com.mhr.mobile.ui.produk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.InjectionRecyclerviewBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.adapter.ProdukAdapter;
import com.mhr.mobile.ui.produk.sheet.SheetInfoPrice;
import com.mhr.mobile.util.FormatUtils;
import java.util.ArrayList;

public class ProdukNoInput extends InjectionActivity {
  private InjectionRecyclerviewBinding binding;
  private ProdukAdapter adapter;

  @Override
  protected String getTitleToolbar() {
    return getIntent().getStringExtra("brand");
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = InjectionRecyclerviewBinding.inflate(getLayoutInflater());
    loadProduk();
    return binding.getRoot();
  }

  private void loadProduk() {
    binding.recyclerview.setLayoutManager(getGridLayoutManager(2));
    adapter = new ProdukAdapter(new ArrayList<>());
    adapter.setOnDataClickListener(
        item -> {
          String nomor = session.getNomor();
          String normalNomor = FormatUtils.denormalizeNomor(nomor);
          SheetInfoPrice sheet = SheetInfoPrice.newInstance(item);
          sheet.setInfoNomor(normalNomor);
          sheet.show(getSupportFragmentManager());
        });
    ProdukRequest.with(this)
        .Kategori(getIntent().getStringExtra("kategori"))
        .Adapter(adapter)
        .ViewShimmer(binding.recyclerview)
        .RequestProdukBrand(getIntent().getStringExtra("brand"));
  }
}
