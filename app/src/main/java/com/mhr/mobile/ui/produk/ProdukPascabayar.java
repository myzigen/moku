package com.mhr.mobile.ui.produk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.api.request.RequestTransaksi;
import com.mhr.mobile.api.response.ResponseTransaksiPasca;
import com.mhr.mobile.databinding.MenuPascabayarBinding;
import com.mhr.mobile.ui.dialog.DialogMokuAlert;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.adapter.ProdukAdapter;
import com.mhr.mobile.ui.produk.sheet.SheetPascabayar;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.QiosPreferences;

public class ProdukPascabayar extends InjectionActivity {
  public static String ARG_INTERNET = "Internet Pascabayar";
  public static String ARG_TV = "TV PASCABAYAR";
  public static String ARG_HP_PASCA = "HP PASCABAYAR";
  public static String ARG_PLN_NON = "PLN NONTAGLIS";
  public static String ARG_PLN_PASCA = "PLN PASCABAYAR";
  public static String ARG_PBB = "PBB";
  public static String ARG_MULTIFINANCE = "MULTIFINANCE";
  public static String ARG_PDAM = "PDAM";
  public static String ARG_GAS = "Gas Negara";
  public static String ARG_BPJS = "BPJS KESEHATAN";
  private MenuPascabayarBinding binding;
  private ProdukAdapter adapter;
  private QiosPreferences pref;
  private String typeProduk;
  private String sku, ref;

  @Override
  protected String getTitleToolbar() {
    return getIntent().getStringExtra("brand");
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuPascabayarBinding.inflate(getLayoutInflater());

    pref = new QiosPreferences(this);
    typeProduk = getIntent().getStringExtra("type_produk");

    if (ARG_INTERNET.equalsIgnoreCase(typeProduk)) {
      binding.provider.setOnClickListener(v -> sheetPasca(ARG_INTERNET));
    }

    if (ARG_TV.equalsIgnoreCase(typeProduk)) {
      binding.provider.setOnClickListener(v -> sheetPasca(ARG_TV));
    }

    if (ARG_PBB.equalsIgnoreCase(typeProduk)) {
      binding.provider.setOnClickListener(v -> sheetPasca(ARG_PBB));
    }

    if (ARG_MULTIFINANCE.equalsIgnoreCase(typeProduk)) {
      binding.provider.setOnClickListener(v -> sheetPasca(ARG_MULTIFINANCE));
    }

    if (ARG_PDAM.equalsIgnoreCase(typeProduk)) {
      binding.provider.setOnClickListener(v -> sheetPasca(ARG_PDAM));
    }

    if (ARG_GAS.equalsIgnoreCase(typeProduk)) {
      binding.provider.setOnClickListener(v -> sheetPasca(ARG_GAS));
    }

    if (ARG_BPJS.equalsIgnoreCase(typeProduk)) {
      binding.provider.setOnClickListener(v -> sheetPasca(ARG_BPJS));
    }

    if (ARG_PLN_NON.equalsIgnoreCase(typeProduk)) {
      binding.provider.setOnClickListener(v -> sheetPasca(ARG_PLN_NON));
    }

    if (ARG_PLN_PASCA.equalsIgnoreCase(typeProduk)) {
      binding.provider.setOnClickListener(v -> sheetPasca(ARG_PLN_PASCA));
    }

    if (ARG_HP_PASCA.equalsIgnoreCase(typeProduk)) {
      binding.provider.setOnClickListener(v -> sheetPasca(ARG_HP_PASCA));
    }

    binding.btnLihatTagihan.setOnClickListener(this::validate);

    return binding.getRoot();
  }

  private void validate(View v) {
    String nomor = binding.editText.getText().toString();
    if (nomor.isEmpty()) {
      AndroidViews.showToast("Nomor kosong", this);
    } else {
      lihatTagihan(nomor);
    }
  }

  private void lihatTagihan(String nomor) {
    RequestTransaksi request = new RequestTransaksi(this);
    request.setToken(session.getToken());
    request.setSku("internet");
    request.setCustomerNo(nomor);
    request.setRefId(ref);
    request.setPrice(80000);
    request.requestTransaksiPasca(
        new RequestTransaksi.CallbackPasca() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChanged(ResponseTransaksiPasca response) {
            if (response.getData() != null && response.getData().getRefId() != null) {
              DialogMokuAlert.with(ProdukPascabayar.this)
                  .TampilkanPesan("Gagal", response.getData().getFrom());
            } else {
              // Menangani kasus spesifik dari server
              String message = response.getMessage();
              if (response.getData() != null && "server".equals(response.getData().getFrom())) {
                switch (response.getData().getType()) {
                  case "saldo":
                    int kurang =
                        response.getData().getExpected() - response.getData().getAvailable();
                    message =
                        "Saldo kamu kurang Rp "
                            + kurang
                            + "\nHarga: "
                            + response.getData().getExpected()
                            + "\nSaldo tersedia: "
                            + response.getData().getAvailable();
                    break;
                  case "token_tidak_valid":
                    message = "Sesi login kamu habis. Silakan login ulang.";
                    break;
                  case "input_kurang":
                    message = "Data input kurang lengkap.";
                    break;
                    // Tambahkan tipe lain jika
                }
                DialogMokuAlert.with(ProdukPascabayar.this).TampilkanPesan("Gagal", message);
              }
            }
          }

          @Override
          public void onFailure(String error) {
            AndroidViews.showToast(error, ProdukPascabayar.this);
          }
        });
  }

  private void sheetPasca(String type) {
    binding.editText.clearFocus();

    SheetPascabayar pascabayar = new SheetPascabayar();
    pascabayar.setType(type);
    pascabayar.setOnSelectProviderListener(
        (sku, ref, iconUrl, providerName) -> {
          this.sku = sku;
          this.ref = ref;
          binding.txtLabelProvider.setText(providerName);
          binding.logoProvider.setVisibility(View.VISIBLE);
          binding.btnLihatTagihan.setEnabled(true);
          Glide.with(this).load(iconUrl).into(binding.logoProvider);
        });

    pascabayar.show(getSupportFragmentManager(), "TAG");
  }
}
