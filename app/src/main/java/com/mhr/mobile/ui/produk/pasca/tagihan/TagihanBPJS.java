package com.mhr.mobile.ui.produk.pasca.tagihan;

import android.os.Bundle;
import android.view.View;
import com.mhr.mobile.api.response.Desc;
import com.mhr.mobile.util.FormatUtils;
import java.util.List;
import com.bumptech.glide.Glide;
import com.mhr.mobile.api.response.ResponseInquiryPasca;
import com.mhr.mobile.databinding.TagihanWifiBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.mhr.mobile.ui.inject.InjectionActivity;

public class TagihanBPJS extends InjectionActivity {
	private TagihanWifiBinding binding;
  
  @Override
  protected String getTitleToolbar() {
	  return "Bpjs Kesehatan";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = TagihanWifiBinding.inflate(getLayoutInflater());
	initVisibility();
    infoTagihan();
    return binding.getRoot();
  }
  
  private void initVisibility(){
	 binding.title1.setText("Alamat");
  }

  private void infoTagihan() {
    String logoUrl = getIntent().getStringExtra("brand_icon_url");
    ResponseInquiryPasca.Data data = getIntent().getParcelableExtra("Data");
    Glide.with(this).load(logoUrl).into(binding.logo);
    binding.infoNoPelanggan.setText(data.customer_no);
    binding.infoNamaPelanggan.setText(data.customer_name);
    binding.infoSellingPrice.setText(FormatUtils.formatRupiah(data.selling_price));
    binding.infoTitle1.setText(data.desc.alamat);
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

      binding.infoPeriode.setText(tagihan.periode);
      //binding.infoPrice.setText(FormatUtils.formatRupiah(totalTagihan));
      //binding.infoAdmin.setText(FormatUtils.formatRupiah(totalAdmin));
      //binding.infoDenda.setText(FormatUtils.formatRupiah(tagihan.denda));
    }
  }
}