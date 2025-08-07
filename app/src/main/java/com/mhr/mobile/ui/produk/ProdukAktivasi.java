package com.mhr.mobile.ui.produk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.api.request.RequestPricelist;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.InjectionRecyclerviewBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.adapter.ProdukAktivasiAdapter;
import com.mhr.mobile.ui.produk.prepaid.kuota.KuotaReload;
import java.util.ArrayList;
import java.util.List;

public class ProdukAktivasi extends InjectionActivity {
  private InjectionRecyclerviewBinding binding;
  private ProdukAktivasiAdapter adapter;
  private String ARG;

  @Override
  protected String getTitleToolbar() {
    return getIntent().getStringExtra("brand");
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = InjectionRecyclerviewBinding.inflate(getLayoutInflater());
    initialize();
    loadProduk();
    return binding.getRoot();
  }

  private void initialize() {
    ARG = getIntent().getStringExtra("kategori");
  }

  private void loadProduk() {
    binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    adapter = new ProdukAktivasiAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnClickListener(
        item -> {
          Intent i = new Intent(this, KuotaReload.class);
          i.putExtra("brand", item.getBrand());
          i.putExtra("kategori", ARG);
          startActivity(i);
        });
    RequestPricelist request = new RequestPricelist(this);
    request.setType("prabayar");
    request.setKategori(ARG);
    request.startExecute(
        new RequestPricelist.Callback() {
          @Override
          public void onRequest() {
            binding.lottie.setVisibility(View.VISIBLE);
          }

          @Override
          public void onDataChange(List<ResponsePricelist> pricelist) {
            List<ResponsePricelist> filtered = new ArrayList<>();
            for (ResponsePricelist item : pricelist) {
              if (item.getBrand().equalsIgnoreCase(getIntent().getStringExtra("brand"))) {
                filtered.add(item);
              }
            }
            adapter.updateAdapter(filtered);
            binding.lottie.setVisibility(View.GONE);
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
  }
}
