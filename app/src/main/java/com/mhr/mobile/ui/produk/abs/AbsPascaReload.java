package com.mhr.mobile.ui.produk.abs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.mhr.mobile.api.request.RequestInquiryPasca;
import com.mhr.mobile.api.response.ResponseInquiryPasca;
import com.mhr.mobile.databinding.FormPascaBinding;
import com.mhr.mobile.ui.dialog.DialogMokuAlert;
import com.mhr.mobile.ui.dialog.LoadingDialogFragment;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.pasca.tagihan.TagihanWifi;
import com.mhr.mobile.ui.produk.sheet.SheetPascabayar;
import com.mhr.mobile.ui.sheet.SheetKalendar;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.preferences.PrefInputCache;

public abstract class AbsPascaReload extends InjectionActivity implements TextWatcher {
  protected FormPascaBinding binding;
  protected Class<?> targetActivity;
  protected String logoUrl, brand, strSku;
  protected LoadingDialogFragment dialog;
  protected TextInputEditText editText;
  protected ResponseInquiryPasca inquiryData;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = FormPascaBinding.inflate(getLayoutInflater());
    dialog = new LoadingDialogFragment();
    editText = binding.editText;
    initEditext(editText);
    editText.addTextChangedListener(this);
    setupCommonListeners();

    return binding.getRoot();
  }

  @Override
  protected String getTitleToolbar() {
    return absTitleToolbar();
  }

  protected void setupCommonListeners() {
    binding.desc.setText("Hai, " + session.getNama() + " Mau bayar tagihan apa nih?");
    binding.btnPilihLayanan.setOnClickListener(v -> openSheetProvider());
    binding.btnLihatTagihan.setEnabled(false);
    binding.btnLihatTagihan.setOnClickListener(
        v -> validate(binding.editText.getText().toString()));
    binding.provider.setOnClickListener(v -> openSheetProvider());

    binding.checkboxReminder.setOnCheckedChangeListener(
        (btnview, isChecked) -> {
          pref.setBoolean("reminder_enabled", isChecked);
        });

    binding.btnKalendar.setOnClickListener(
        v -> {
          SheetKalendar sheetKalendar = new SheetKalendar();
          sheetKalendar.setOnTanggalDipilihListener(
              tanggal -> {
                tampilkanTglReminder(tanggal);
              });
          sheetKalendar.show(getSupportFragmentManager(), "SheetKalendar");
        });
  }

  private void tampilkanTglReminder(String tanggal) {
    if (tanggal != null && !tanggal.isEmpty()) {
      binding.infoTglReminder.setText(tanggal);
    } else {
      binding.infoTglReminder.setText("1");
    }
  }

  protected void openSheetProvider() {
    hideKeyboard();
    SheetPascabayar sheet = new SheetPascabayar();
    sheet.setType(absTypeProduk());
    sheet.setOnSelectProviderListener(
        (sku, ref, iconUrl, providerName) -> {
          absContentVisibility(true);
          binding.txtLabelProvider.setText(providerName);
          binding.logoProvider.setVisibility(View.VISIBLE);
          Glide.with(this).load(iconUrl).into(binding.logoProvider);
          strSku = sku;
          this.logoUrl = iconUrl;
          this.brand = providerName;
        });

    sheet.show(getSupportFragmentManager());
  }

  protected void saveCache(String nomor) {
    PrefInputCache cache = new PrefInputCache();
    cache.nomor = nomor;
    cache.sku = strSku;
    // pastikan infoSku di-set saat pilih provider
    cache.brand = brand;
    cache.logoUrl = logoUrl;

    String json = new Gson().toJson(cache);
    pref.setString(absTypeProduk(), json);
  }

  protected void inquiryPasca(String nomor) {
    RequestInquiryPasca request = new RequestInquiryPasca(this);
    request.setToken(session.getToken());
    request.setCustomerNo(nomor);
    request.setSku(strSku);
    request.setTesting(true);
    request.requestInquiryPasca(
        new RequestInquiryPasca.Callback() {
          @Override
          public void onRequest() {
            showExpandLoading("Pengecekan ID");
          }

          @Override
          public void onDataChanged(ResponseInquiryPasca inquiry) {
            inquiryData = inquiry;

            if (inquiry != null && inquiry.data != null) {
              // Validasi benar-benar berhasil dan customer_name ada
              boolean isSukses =
                  "Sukses".equalsIgnoreCase(inquiry.data.status)
                      && "00".equals(inquiry.data.rc)
                      && inquiry.data.customer_name != null
                      && !inquiry.data.customer_name.trim().isEmpty();

              if (isSukses) {
                new Handler(Looper.getMainLooper())
                    .postDelayed(
                        () -> {
                          showExpandSuccess(inquiry.data.customer_name);
                        },
                        2000);
                saveCache(inquiry.data.customer_no);
              } else {
                inquiryData = null; // Reset data agar tidak bisa lanjut ke goToActivity
                showExpandError("Tagihan tidak ditemukan atau tidak valid.");
              }
            } else {
              showExpandError("Gagal memuat data inquiry.");
            }
          }

          @Override
          public void onFailure(String error) {
            showExpandError(error);
            error(error);
          }
        });
  }

  protected void absContentVisibility(boolean visibility) {
    binding.backdrop.setVisibility(visibility ? View.VISIBLE : View.GONE);
    binding.root.setVisibility(visibility ? View.VISIBLE : View.GONE);
    binding.desc.setVisibility(visibility ? View.GONE : View.VISIBLE);
    binding.btnPilihLayanan.setVisibility(visibility ? View.GONE : View.VISIBLE);
  }

  protected void error(String error) {
    DialogMokuAlert.with(this).TampilkanPesan("Response", error, binding.getRoot());
  }

  protected void absTargetActivity(Class<?> targetActivity) {
    this.targetActivity = targetActivity;
  }

  protected void goToActivity() {
    if (inquiryData == null) return;

    Bundle args = new Bundle();
    args.putParcelable("Data", inquiryData.data);
    Intent i = new Intent(this, TagihanWifi.class);
    i.putExtras(args);
    i.putExtra("brand_icon_url", logoUrl);
    i.putExtra("brand", brand);
    // i.putExtra("inquiry", inquiryData);
    startActivity(i);
  }

  protected String getAbsIntent(String key) {
    Intent intent = getIntent();
    return intent != null ? intent.getStringExtra(key) : null;
  }

  protected void showExpandLoading(String message) {
    AndroidViews.showExpandLoading(message, binding.expandable, binding.txtCekPengguna);
  }

  protected void showExpandSuccess(String message) {
    AndroidViews.showExpandSuccess(message, binding.expandable, binding.txtCekPengguna);
  }

  protected void showExpandError(String message) {
    AndroidViews.ShowExpandError(message, binding.expandable, binding.txtCekPengguna);
  }

  protected void hideExpandError() {
    binding.txtCekPengguna.setText("");
    if (binding.expandable.isExpanded()) {
      binding.expandable.collapse();
    }
  }

  protected String getSafeBrandKey(String kategori, String brand) {
    if (kategori == null || brand == null) return "cache_default";
    return kategori.toLowerCase().replaceAll("\\s+", "_")
        + "_"
        + brand.toLowerCase().replaceAll("[^a-z0-9]+", "_");
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    absAfterTextChanged(s.toString().trim());
  }

  protected abstract void validate(String nomor);

  protected abstract void absAfterTextChanged(String input);

  protected abstract String absTitleToolbar();

  protected abstract String absTypeProduk();
}
