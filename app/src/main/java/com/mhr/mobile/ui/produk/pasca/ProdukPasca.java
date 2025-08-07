package com.mhr.mobile.ui.produk.pasca;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.api.request.RequestInquiryPasca;
import com.mhr.mobile.api.response.ResponseInquiryPasca;
import com.mhr.mobile.databinding.MenuPascabayarBinding;
import com.mhr.mobile.ui.dialog.DialogMokuAlert;
import com.mhr.mobile.ui.dialog.LoadingDialogFragment;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.adapter.ProdukAdapter;
import com.mhr.mobile.ui.produk.pasca.tagihan.TagihanWifi;
import com.mhr.mobile.ui.produk.sheet.SheetPascabayar;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.QiosPreferences;

public class ProdukPasca extends InjectionActivity {
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
  private LoadingDialogFragment dialog;
  private String typeProduk;
  private String sku, ref, logoUrl, brand;

  @Override
  protected String getTitleToolbar() {
    return getIntent().getStringExtra("brand");
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuPascabayarBinding.inflate(getLayoutInflater());

    pref = new QiosPreferences(this);
    dialog = new LoadingDialogFragment();
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
      sheetPasca(ARG_PLN_PASCA);
    }

    if (ARG_HP_PASCA.equalsIgnoreCase(typeProduk)) {
      binding.provider.setOnClickListener(v -> sheetPasca(ARG_HP_PASCA));
    }

    binding.btnLihatTagihan.setOnClickListener(this::validate);

    return binding.getRoot();
  }

  private void validate(View v) {
    String nomor = binding.editText.getText().toString();
    String infoSku = binding.infoSku.getText().toString();
    if (nomor.isEmpty()) {
      AndroidViews.showToast("Nomor kosong", this);
    } else {
      inquiryPasca(nomor, infoSku);
    }
  }

  private void inquiryPasca(String nomor, String infoSku) {
    RequestInquiryPasca request = new RequestInquiryPasca(this);
    request.setToken(session.getToken());
    request.setSku(infoSku);
    request.setCustomerNo(nomor);
    request.setTesting(true);
    request.requestInquiryPasca(
        new RequestInquiryPasca.Callback() {
          @Override
          public void onRequest() {
            dialog.show(getSupportFragmentManager(), "inquiry");
          }

          @Override
          public void onDataChanged(ResponseInquiryPasca inquiry) {
            goToActivity(inquiry);
            dialog.dismiss();
          }

          @Override
          public void onFailure(String error) {
            DialogMokuAlert.with(ProdukPasca.this)
                .TampilkanPesan("Response", error, binding.getRoot());
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
          this.logoUrl = iconUrl;
          this.brand = providerName;
          binding.txtLabelProvider.setText(providerName);
          binding.logoProvider.setVisibility(View.VISIBLE);
          binding.btnLihatTagihan.setEnabled(true);
          Glide.with(this).load(iconUrl).into(binding.logoProvider);
        });

    pascabayar.show(getSupportFragmentManager(), "TAG");
  }

  private void goToActivity(ResponseInquiryPasca inquiry) {
    Bundle args = new Bundle();
    args.putParcelable("Data", inquiry.data);
    Intent i = new Intent(this, TagihanWifi.class);
    i.putExtras(args);
    i.putExtra("brand_icon_url", logoUrl);
    i.putExtra("brand", brand);
    startActivity(i);
  }
}
