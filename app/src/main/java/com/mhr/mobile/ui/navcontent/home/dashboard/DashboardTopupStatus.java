package com.mhr.mobile.ui.navcontent.home.dashboard;

import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.ContentPaymentStatusBinding;
import com.mhr.mobile.model.RiwayatTransaksi;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class DashboardTopupStatus extends InjectionActivity {
  private ContentPaymentStatusBinding binding;
  private RiwayatTransaksi riwayatTransaksi;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentPaymentStatusBinding.inflate(layoutInflater, viewGroup, false);
    riwayatTransaksi = getIntent().getParcelableExtra("kirim");
    // terimaData();
    // initUi();
    return binding.getRoot();
  }
}
