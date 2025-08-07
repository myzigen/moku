package com.mhr.mobile.ui.tagihan;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.mhr.mobile.databinding.DetailTagihanInternetBinding;
import com.mhr.mobile.inquiry.response.InquiryResponse;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.viewmodel.InquiryViewModel;

public class DetailTagihanInternet extends InjectionActivity {

  private DetailTagihanInternetBinding binding;
  //private MenuWifi menuWifi;
  private ObjectAnimator animator;
  private InquiryViewModel inquiryViewModel;

  @Override
  protected String getTitleToolbar() {
    return "Checkout";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = DetailTagihanInternetBinding.inflate(layoutInflater, viewGroup, false);
    inquiryViewModel = new ViewModelProvider(this).get(InquiryViewModel.class);

    dataTagihan();

    return binding.getRoot();
  }

  private void dataTagihan() {
    InquiryResponse.Data data = getIntent().getExtras().getParcelable("data");
    // InquiryResponse.Data data = InquiryHelper.getInstance().getData();
    Log.d("terima data", "he" + data);
    if (data != null) {
      binding.namaPelanggan.setText(data.getTrName());
      binding.noPelanggan.setText(data.getHp());

      binding.tagihanPelanggan.setText(FormatUtils.formatRupiah(data.getNominal()));
      binding.biayaAdmin.setText(FormatUtils.formatRupiah(data.getAdmin()));

      double komisi = data.getPrice() - data.getSellingPrice();
      double kurangiHargaDariKomisi = data.getSellingPrice() - data.getPrice();
      binding.komisi.setText(FormatUtils.formatRupiah(komisi));
      binding.subTotal.setText(FormatUtils.formatRupiah(data.getPrice()));
      binding.totalBayarKurangiKomisi.setText(FormatUtils.formatRupiah(kurangiHargaDariKomisi));
      binding.jumLahBayar.setText(FormatUtils.formatRupiah(data.getSellingPrice()));
      Glide.with(this).load(getIntent().getStringExtra("imageUrl")).into(binding.logoProvider);
    }
  }
}
