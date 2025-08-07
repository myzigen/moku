package com.mhr.mobile.ui.produk.sheet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.SheetInfoPriceBinding;
import com.mhr.mobile.interfaces.NavigationCallback;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.ui.produk.ProdukPembayaran;
import com.mhr.mobile.util.FormatUtils;

public class SheetInfoPrice extends InjectionSheetFragment {
  private SheetInfoPriceBinding binding;
  private boolean isExpanded = false;
  private ResponsePricelist pricelist;
  private String nomor, inquiry;
  private int harga;

  public static SheetInfoPrice newInstance(ResponsePricelist response) {
    SheetInfoPrice fragment = new SheetInfoPrice();
    Bundle args = new Bundle();
    args.putParcelable("pricelist", response);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetInfoPriceBinding.inflate(getLayoutInflater());
    View view = binding.getRoot();

    initialize();
    initUi();
    return view;
  }

  private void initialize() {
    pricelist = getArguments().getParcelable("pricelist");
    String strHarga = FormatUtils.formatRupiah(harga);

    if (strHarga != null && !strHarga.isEmpty()) {
      int price = pricelist.getHargaJual();
      String formatPrice = FormatUtils.formatRupiah(price);
      binding.infoHarga.setText(formatPrice);
    } else {
      binding.infoHarga.setText(strHarga);
    }
    binding.infoNomor.setText(nomor);
    binding.infoKategori.setText(pricelist.getCategory());
    Glide.with(requireActivity()).load(pricelist.getBrandIconUrl()).into(binding.img);
    if (inquiry != null && !inquiry.isEmpty()) {
      binding.infoProduk.setText(inquiry);
      binding.infoDetailProduk.setText(pricelist.getProductName());
    } else {
      binding.infoProduk.setText(pricelist.getProductName());
      binding.infoDetailProduk.setText(pricelist.getDesc());
    }
  }

  private void initUi() {
    closeShowing(false);
    binding.btnAktivasi.setText("Aktifkan");
    binding.btnAktivasi.setOnClickListener(v -> goToActivity());
  }

  private void goToActivity() {
    dismiss();
    Intent intent = null;
    intent = new Intent(requireActivity(), ProdukPembayaran.class);
    Bundle args = new Bundle();
    args.putParcelable("pricelist", pricelist);
    intent.putExtras(args);
    intent.putExtra("nomor", nomor);
    intent.putExtra("inquiry", inquiry);

    if (intent != null) {
      if (requireActivity() instanceof NavigationCallback) {
        ((NavigationCallback) requireActivity()).abStartActivity(intent);
      }
    }
  }

  public void setInfoNomor(String nomor) {
    this.nomor = nomor;
  }

  public void setInfoHarga(int harga) {
    this.harga = harga;
  }

  public void setInquiry(String inquiry) {
    this.inquiry = inquiry;
  }
}
