package com.mhr.mobile.ui.produk.prepaid;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.FormPrepaidBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.ProdukRequest;
import com.mhr.mobile.ui.produk.adapter.ProdukAdapter;
import com.mhr.mobile.ui.produk.sheet.SheetInfoPrice;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.ProviderUtils;
import java.util.ArrayList;

public class PrepaidForm extends InjectionActivity implements TextWatcher {
  private FormPrepaidBinding binding;
  private ProdukAdapter adapter;
  private String ARG_KATEGORI;

  @Override
  protected String getTitleToolbar() {
    return getIntent().getStringExtra("brand");
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = FormPrepaidBinding.inflate(getLayoutInflater());
    initUi();
	binding.root.setVisibility(View.GONE);
    return binding.getRoot();
  }

  private void initUi() {
    initEditext(binding.etPhoneNumber);
    // binding.containerNoProduk.setVisibility(View.GONE);
    // binding.etPhoneNumber.addTextChangedListener(new
    // InputBoldTextWatcher(binding.etPhoneNumber));
    ARG_KATEGORI = getIntent().getStringExtra("kategori");
    binding.etPhoneNumber.addTextChangedListener(this);
    if ("Games".equalsIgnoreCase(ARG_KATEGORI)) {
      binding.title.setText("ID Game");
      binding.inputLayout.setPlaceholderText("1234567890");
    } else {
      binding.title.setText("Nomor Hp");
      binding.inputLayout.setPlaceholderText("081234567890");
    }

    binding.btnPickContact.setOnClickListener(v -> launchContactPicker());
    binding.clearText.setOnClickListener(
        v -> {
          binding.etPhoneNumber.setText("");
          binding.clearText.setVisibility(View.GONE);
        });
    Glide.with(this).load(getIntent().getStringExtra("brand_icon")).into(binding.logoProvider);
    loadProduk();
  }

  private void loadProduk() {
    String filterMode = getIntent().getStringExtra("filter_mode");

    if ("Aktivasi Perdana".equalsIgnoreCase(ARG_KATEGORI)) {
      binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    } else if ("voucher_kuota".equalsIgnoreCase(filterMode)) {
      binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    } else if ("Aktivasi Voucher".equalsIgnoreCase(ARG_KATEGORI)) {
      binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    } else if ("voucher_belanja".equalsIgnoreCase(filterMode)) {
      binding.recyclerview.setLayoutManager(getGridLayoutManager(2));
    } else {
      binding.recyclerview.setLayoutManager(getGridLayoutManager(2));
    }

    adapter = new ProdukAdapter(new ArrayList<>());
    adapter.setOnDataClickListener(item -> validate(item));

    ProdukRequest.with(this)
        .Kategori(ARG_KATEGORI)
        .Adapter(adapter)
        .ViewShimmer(binding.recyclerview)
        .RequestProdukBrand(getIntent().getStringExtra("brand"));
  }

  private void validate(ResponsePricelist item) {
    String brand = getIntent().getStringExtra("brand");
    String validasi = binding.etPhoneNumber.getText().toString().trim();
    if (validasi.isEmpty()) {
      showExpandError("Nomor tujuan belum di isi");
      adapter.setInputValid(false);
      return;
    }

    if (validasi.length() < 10) {
      showExpandError("Nomor tujuan tidak sah");
      adapter.setInputValid(false);
      return;
    }

    if (!ProviderUtils.detectBrand(validasi, brand)) {
      showExpandError("Eiits Salah! Ini Bukan Nomor " + brand);
      adapter.setInputValid(false);
      return;
    }

    // Validasi Disetujui
    binding.pembayaran.harga.setText(FormatUtils.formatRupiah(item.getHargaJual()));
    binding.wrapperPembayaran.setVisibility(View.VISIBLE);
    hideKeyboard();
    adapter.setInputValid(true);

    binding.pembayaran.btnLanjutBayar.setOnClickListener(
        v -> {
          SheetInfoPrice p = SheetInfoPrice.newInstance(item);
          p.setInfoNomor(validasi);
          p.show(getSupportFragmentManager(), "form");
        });
  }

  private void showExpandError(String message) {
    AndroidViews.ShowExpandError(message, binding.expandable, binding.txtPesanKePengguna);
  }

  private void hidePesanError() {
    adapter.resetSelectedPosition();
    binding.txtPesanKePengguna.setText("");
    if (binding.expandable.isExpanded()) {
      binding.expandable.collapse();
    }
  }

  @Override
  protected void onPhoneNumberPicked(String phoneNumber) {
    binding.etPhoneNumber.setText(phoneNumber);
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    binding.clearText.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
  }

  @Override
  public void afterTextChanged(Editable s) {
    String validate = s.toString().trim();
    String brand = getIntent().getStringExtra("brand");

    if (validate.isEmpty()) {
      hidePesanError();
      adapter.setInputValid(false);
      return;
    }

    // Validasi hanya dilakukan jika cukup panjang
    if (validate.length() >= 4) {
      boolean cocok = ProviderUtils.detectBrand(validate, brand);

      if (!cocok) {
        showExpandError("Eiits Salah! Ini Bukan Nomor " + brand);
        adapter.setInputValid(false);
      } else {
        hidePesanError();
        adapter.setInputValid(true);
      }
    } else {
      // Jika kurang dari 4 digit, anggap belum valid
      adapter.setInputValid(false);
    }
  }
}
