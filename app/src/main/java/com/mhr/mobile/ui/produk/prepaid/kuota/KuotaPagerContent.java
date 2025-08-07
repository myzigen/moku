package com.mhr.mobile.ui.produk.prepaid.kuota;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import com.mhr.mobile.api.request.RequestPricelist;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.InjectionRecyclerviewBinding;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.produk.adapter.ProdukTypeAdapter;
import com.mhr.mobile.ui.produk.prepaid.PrepaidForm;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KuotaPagerContent extends InjectionFragment {
  private InjectionRecyclerviewBinding binding;
  private ProdukTypeAdapter adapter;
  private String ARGKategori;

  public static KuotaPagerContent newInstance(String kategori) {
    KuotaPagerContent fragment = new KuotaPagerContent();
    Bundle args = new Bundle();
    args.putString("kategori", kategori);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateQiosFragment(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
    binding = InjectionRecyclerviewBinding.inflate(getLayoutInflater());
    initialize();
    initUi();
    return binding.getRoot();
  }

  private void initialize() {
    ARGKategori = getArguments().getString("kategori");
  }

  private void initUi() {
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(3, 30, true));
    binding.recyclerview.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
    adapter = new ProdukTypeAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);

    loadProvider();
    adapter.setOnClickListener(
        model -> {
          Intent intent = null;
          if (ARGKategori.equalsIgnoreCase("Data")) {
            intent = new Intent(requireActivity(), KuotaReload.class);
          } else if (ARGKategori.equalsIgnoreCase("Voucher")) {
            intent = new Intent(requireActivity(), KuotaVoucherFisik.class);
          }

          intent.putExtra("brand_icon", model.getBrandIconUrl());
          intent.putExtra("brand", model.getBrand());
          intent.putExtra("kategori", ARGKategori);

          if (intent != null) startActivity(intent);
        });
  }

  private void loadProvider() {
    RequestPricelist request = new RequestPricelist(requireActivity());
    request.setType("prabayar");
    request.setKategori(ARGKategori);
    request.startExecute(
        new RequestPricelist.Callback() {
          @Override
          public void onRequest() {
            binding.lottie.setVisibility(View.VISIBLE);
          }

          @Override
          public void onDataChange(List<ResponsePricelist> pricelist) {
            List<ResponsePricelist> filterItem = new ArrayList<>();
            List<String> filter =
                Arrays.asList("TELKOMSEL", "INDOSAT", "AXIS", "BY.U", "TRI", "XL", "SMARTFREN");
            for (ResponsePricelist item : pricelist) {
              if ("Voucher".equalsIgnoreCase(ARGKategori) && filter.contains(item.getBrand())) {
                filterItem.add(item);
              } else if ("Data".equalsIgnoreCase(ARGKategori)) {
                filterItem.add(item);
              }
            }
            adapter.updateProduk(filterItem);
            binding.lottie.setVisibility(View.GONE);
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
  }
}
