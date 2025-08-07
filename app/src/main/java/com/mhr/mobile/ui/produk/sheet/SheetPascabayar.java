package com.mhr.mobile.ui.produk.sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mhr.mobile.api.request.RequestPricelist;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.SheetPascabayarBinding;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.ui.produk.adapter.ProdukPascaAdapter;
import java.util.ArrayList;
import java.util.List;

public class SheetPascabayar extends InjectionSheetFragment {
  private SheetPascabayarBinding binding;
  private ProdukPascaAdapter adapter;
  private String type;
  private SelectProviderListener listener;
  private List<ResponsePricelist> mData = new ArrayList<>();

  public interface SelectProviderListener {
    void onSelect(String sku, String ref, String iconUrl, String provider);
  }

  public void setOnSelectProviderListener(SelectProviderListener listener) {
    this.listener = listener;
  }

  @Override
  protected String getSheetTitle() {
    return "Pilih Penyedia";
  }

  public static SheetPascabayar newInstance(String type) {
    SheetPascabayar fragment = new SheetPascabayar();
    Bundle args = new Bundle();
    args.putString("type", type);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetPascabayarBinding.inflate(getLayoutInflater());

    if (getArguments() != null) {
      type = getArguments().getString("type");
    }

    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    uiRecycler();
    initProduk();
    closeShowing(false);
  }

  private void uiRecycler() {
    binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireActivity()));
    adapter = new ProdukPascaAdapter(mData);
    binding.recyclerview.setAdapter(adapter);
    int count = adapter.getItemCount();
    binding.recyclerview.setDemoChildCount(count);
    adapter.setOnItemClickListener(
        produk -> {
          dismiss();
          String sku = produk.getBuyerSkuCode();
          if (listener != null) {
            listener.onSelect(sku, "535", produk.getBrandIconUrl(), produk.getProductName());
          }
        });
  }

  private void initProduk() {
    RequestPricelist requestPricelist = new RequestPricelist(requireActivity());
    requestPricelist.setType("pascabayar");
    requestPricelist.setKategori(type);
    requestPricelist.startExecute(
        new RequestPricelist.Callback() {
          @Override
          public void onRequest() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onDataChange(List<ResponsePricelist> pricelist) {
            binding.recyclerview.hideShimmerAdapter();
            mData = pricelist;
            adapter.setNotifyProduk(pricelist);
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
  }

  public void setType(String type) {
    this.type = type;
  }
}
