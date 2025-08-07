package com.mhr.mobile.ui.navcontent.home.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.adapter.TripayAdapter;
import com.mhr.mobile.api.request.RequestTripay;
import com.mhr.mobile.api.response.ResponseTripay;
import com.mhr.mobile.databinding.InjectionRecyclerviewBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import java.util.ArrayList;
import java.util.List;

public class DashboardDaftarBank extends InjectionActivity {
  private InjectionRecyclerviewBinding binding;
  private TripayAdapter adapter;

  @Override
  public String getTitleToolbar() {
    return "Metode Pembayaran";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = InjectionRecyclerviewBinding.inflate(getLayoutInflater());

    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    loadDaftarBank();
    initRecyclerView();
  }

  private void initRecyclerView() {
    binding.recyclerview.addItemDecoration(getSpacingItemDecoration(1, 30, true));
    binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    adapter = new TripayAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnBankClickListener(
        (a, b, x) -> {
          // AndroidViews.showToast(b,this);
        });
  }

  private void loadDaftarBank() {
    RequestTripay requestTripay = new RequestTripay(this);
    requestTripay.Header("Bearer DEV-UW5IiQsQzByxRmFiL9NchYo5alAy2lg9rPHjhggH");
    requestTripay.requestPaymentChannel(
        new RequestTripay.CallbackChannel() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(ResponseTripay tripay) {
            List<ResponseTripay.PaymentMethod> data = tripay.getData();
            adapter.updateAdapter(data);
            for (ResponseTripay.PaymentMethod method : tripay.getData()) {}
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
  }
}
