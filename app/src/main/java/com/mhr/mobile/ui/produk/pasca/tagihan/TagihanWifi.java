package com.mhr.mobile.ui.produk.pasca.tagihan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.api.request.RequestTransaksi;
import com.mhr.mobile.api.response.Desc;
import com.mhr.mobile.api.response.ResponseInquiryPasca;
import com.mhr.mobile.api.response.ResponseTransaksiPasca;
import com.mhr.mobile.databinding.TagihanWifiBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.helper.HelperTransaction;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosPreferences;
import com.mhr.mobile.widget.button.OnActiveListener;
import java.util.List;

public class TagihanWifi extends InjectionActivity {
  private TagihanWifiBinding binding;
  private int totalTagihan;
  private String inquiryName;

  @Override
  protected String getTitleToolbar() {
    return "Rincian";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = TagihanWifiBinding.inflate(getLayoutInflater());
    hideView();
    infoTagihan();
    return binding.getRoot();
  }

  private void hideView() {
    binding.title1.setVisibility(View.GONE);
    binding.infoTitle1.setVisibility(View.GONE);
    binding.titleDenda.setVisibility(View.GONE);
    binding.infoDenda.setVisibility(View.GONE);
  }

  private void infoTagihan() {
    String logoUrl = getIntent().getStringExtra("brand_icon_url");
    ResponseInquiryPasca.Data data = getIntent().getParcelableExtra("Data");

    if (data == null || data.desc == null || data.desc.detail == null) return;
    inquiryName = data.customer_name;
    binding.infoNoPelanggan.setText(data.customer_no);
    binding.infoNamaPelanggan.setText(inquiryName);

    Glide.with(this).load(logoUrl).into(binding.logo);
    binding.infoSellingPrice.setText(FormatUtils.formatRupiah(data.selling_price));

    int totalTagihan = 0;
    int totalAdmin = 0;

    List<Desc.DetailUniversal> detailList = data.desc.detail;

    for (Desc.DetailUniversal tagihan : detailList) {
      try {
        int nilai = Integer.parseInt(tagihan.nilai_tagihan);
        int admin = Integer.parseInt(tagihan.admin);
        totalTagihan += nilai;
        totalAdmin += admin;
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    }

    // Ambil periode pertama dan terakhir dari API langsung
    String semuaPeriode = "-";
    if (!detailList.isEmpty()) {
      String awal = detailList.get(0).periode;
      String akhir = detailList.get(detailList.size() - 1).periode;

      semuaPeriode = awal.equals(akhir) ? awal : awal + " / " + akhir;
    }
    this.totalTagihan = totalTagihan;
    binding.infoPeriode.setText(semuaPeriode);
    binding.infoPrice.setText(FormatUtils.formatRupiah(totalTagihan));
    binding.infoAdmin.setText(FormatUtils.formatRupiah(totalAdmin));
    binding.getRoot().post(() -> binding.btnBayar.startHintAnimation());
    binding.btnBayar.setOnActiveListener(
        new OnActiveListener() {
          @Override
          public void onActive() {
            bayarTagihan(data);
          }
        });
  }

  private void bayarTagihan(ResponseInquiryPasca.Data pasca) {
    QiosPreferences preferences = new QiosPreferences(this);
    HelperTransaction helperTransaction = HelperTransaction.with(this);

    RequestTransaksi request = new RequestTransaksi(this);
    request.setToken(session.getToken());
    request.setSku(pasca.buyer_sku_code);
    request.setNamaPembeli(inquiryName);
    request.setCustomerNo(pasca.customer_no);
    request.setRefId(pasca.ref_id);
    request.setPrice(totalTagihan);
    request.setTesting(true);
    request.requestTransaksiPasca(
        new RequestTransaksi.CallbackPasca() {
          @Override
          public void onRequest() {
            helperTransaction.DialogShow();
          }

          @Override
          public void onDataChanged(ResponseTransaksiPasca response) {
            helperTransaction.GetResponsePasca(response);
          }

          @Override
          public void onFailure(String error) {
            helperTransaction.DialogDismiss();
          }
        });
  }

  private void goToActivity() {}
}
