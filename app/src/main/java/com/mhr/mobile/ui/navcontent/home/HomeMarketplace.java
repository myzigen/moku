package com.mhr.mobile.ui.navcontent.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.MarketplaceAdapter;
import com.mhr.mobile.databinding.InjectionRecyclerviewBinding;
import com.mhr.mobile.manage.request.MarketplaceRequest;
import com.mhr.mobile.manage.response.MarketplaceResponse;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.navcontent.home.marketplace.MarketplaceProduk;
import java.util.ArrayList;
import java.util.List;

public class HomeMarketplace extends InjectionFragment {
  private InjectionRecyclerviewBinding binding;
  private MarketplaceAdapter adapter;

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = InjectionRecyclerviewBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    applyRecycler();
    dataIsReady();
  }

  private void applyRecycler() {
    binding.recyclerview.setDemoLayoutReference(R.layout.shimmer_marketplace);
    binding.recyclerview.addItemDecoration(getSpacingDecoration(2, 20, true));
    binding.recyclerview.setLayoutManager(getGridLayoutManager(2));
    adapter = new MarketplaceAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnProdukClickListener(
        produk -> {
          Intent intent = new Intent(requireActivity(), MarketplaceProduk.class);
          Bundle args = new Bundle();
          args.putParcelable("marketplace", produk);
          intent.putExtras(args);
          getMainActivity().abStartActivity(intent);
        });
  }

  private void dataIsReady() {
    if (adapter != null && adapter.getItemCount() > 0) {
      return;
    }
    applyMarket();
  }

  private void applyMarket() {
    MarketplaceRequest request = new MarketplaceRequest(requireActivity());
    request.setApiKey();
    request.startRequestProduk(
        new MarketplaceRequest.RequestProduk() {
          @Override
          public void onStartRequest() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onResponse(List<MarketplaceResponse.Data> data) {
            adapter.updateData(data);
            binding.recyclerview.hideShimmerAdapter();
          }

          @Override
          public void onFailure(String error) {
            //binding.recyclerview.hideShimmerAdapter();
            // AndroidViews.showSnackbar(binding.getRoot(), error);
          }
        });
  }

  @Override
  public void onDataReload() {
    dataIsReady();
  }
}
