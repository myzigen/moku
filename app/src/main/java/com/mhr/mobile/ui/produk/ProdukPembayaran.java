package com.mhr.mobile.ui.produk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.api.request.RequestTransaksi;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.api.response.ResponseTransaksi;
import com.mhr.mobile.databinding.FormPembayaranBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.helper.HelperTransaction;
import com.mhr.mobile.ui.sheet.SheetErrorLayout;
import com.mhr.mobile.ui.sheet.SheetPIN;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.widget.button.OnActiveListener;

public class ProdukPembayaran extends InjectionActivity {
  private FormPembayaranBinding binding;
  private ResponsePricelist data;
  private String inquiry;

  @Override
  protected String getTitleToolbar() {
    return "Rincian";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = FormPembayaranBinding.inflate(layoutInflater, viewGroup, false);
    infoProduk();
    ui();
    return binding.getRoot();
  }

  private void ui() {
    binding.btnBayar.post(() -> binding.btnBayar.startHintAnimation());
    binding.btnBayar.setOnActiveListener(
        new OnActiveListener() {
          @Override
          public void onActive() {
            if (pref.getPinPurchase()) {
              SheetPIN sheet = SheetPIN.newInstance("purchase");
              sheet.setOnPinValidatedListener(
                  () -> {
                    startTransaksi();
                  });
              sheet.show(getSupportFragmentManager());
            } else {
              startTransaksi();
            }
          }
        });
  }

  private void infoProduk() {
    data = getIntent().getParcelableExtra("pricelist");
    inquiry = getAbsIntent("inquiry");
    String kategori = data.getCategory();
    String brand = data.getBrand();

    Glide.with(this).load(data.getBrandIconUrl()).into(binding.img);
    binding.infoNomor.setText(getAbsIntent("nomor"));
    binding.infoDesc.setText(data.getProductName());
    binding.infoHarga.setText(FormatUtils.formatRupiah(data.getHargaJual()));
    binding.infoSaldo.setText(pref.getSaldo());
    String displayKategori;
    String displayBrand;

    switch (brand) {
      case "PLN":
        displayBrand = "Token Listrik";
        break;
      default:
        displayBrand = brand;
        break;
    }

    switch (kategori) {
      case "Data":
        displayKategori = "Paket Data";
        break;
      case "E-Money":
        displayKategori = "E-Wallet";
        break;
      default:
        displayKategori = kategori;
        break;
    }

    binding.infoKategori.setText(displayKategori);
    binding.infoBrand.setText(displayBrand);
    binding.infoProduk.setText(displayKategori);

    if (inquiry != null && !inquiry.isEmpty()) {
      binding.wrapInquiry.setVisibility(View.VISIBLE);
      binding.infoInquiry.setVisibility(View.VISIBLE);
      binding.infoInquiry.setText(inquiry);
    } else {
      binding.wrapInquiry.setVisibility(View.GONE);
      binding.infoInquiry.setVisibility(View.GONE);
    }

    // Ambil saldo dari pref dan ubah ke angka
    String saldoStr = pref.getSaldo().replaceAll("[^\\d]", ""); // Hapus "Rp", titik, spasi
    int saldo = 0;
    try {
      saldo = Integer.parseInt(saldoStr);
    } catch (NumberFormatException e) {
      saldo = 0; // fallback jika error parsing
    }

    // Bandingkan dengan harga jual
    int harga = data.getHargaJual();

    if (saldo < harga) {
      binding.titleSaldo.setVisibility(View.VISIBLE);
      binding.titleSaldo.setText("Saldo tidak cukup");
      // binding.btnBayar.setEnabled(false); // opsional: nonaktifkan tombol
    } else {
      binding.titleSaldo.setVisibility(View.GONE);
      // binding.btnBayar.setEnabled(true);
    }
  }

  private void startTransaksi() {
    String nomor = getIntent().getStringExtra("nomor");
    SheetErrorLayout errorLayout = new SheetErrorLayout();

    HelperTransaction helper = HelperTransaction.with(this);
    RequestTransaksi request = new RequestTransaksi(this);
    request.setToken(session.getToken());
    request.setSku(data.getBuyerSkuCode());
    request.setNamaPembeli(inquiry);
    request.setCustomerNo(nomor);
    request.setPrice(data.getHargaJual());
    request.setTesting(true);
    request.requestTransaksi(
        new RequestTransaksi.Callback() {
          @Override
          public void onRequest() {
            helper.DialogShow();
          }

          @Override
          public void onDataChanged(ResponseTransaksi response) {
            helper.GetResponse(response);
          }

          @Override
          public void onFailure(String error) {
            helper.DialogDismiss();
            errorLayout.setText(error);
            errorLayout.setTextBtn("Muat Ulang");
            errorLayout.show(getSupportFragmentManager(), "ContentDetailPembelian");
          }
        });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    binding = null;
  }

  @Override
  public void onResume() {
    super.onResume();
    if (isTaskRoot()) {
      back();
    }
  }
}
