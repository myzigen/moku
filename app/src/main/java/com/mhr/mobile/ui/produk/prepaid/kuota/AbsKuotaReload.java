package com.mhr.mobile.ui.produk.prepaid.kuota;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.DataInternetAdapter;
import com.mhr.mobile.api.request.RequestPricelist;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.FormPrepaidBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.adapter.AdapterHelper;
import com.mhr.mobile.ui.produk.adapter.GenericFilterAdapter;
import com.mhr.mobile.ui.produk.helper.HelperFilterKuota;
import com.mhr.mobile.ui.produk.sheet.SheetInfoPrice;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.ProviderUtils;
import com.mhr.mobile.util.QiosColor;
import com.mhr.mobile.util.StrUtils;
import com.mikelau.shimmerrecyclerviewx.ShimmerRecyclerViewX;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbsKuotaReload extends InjectionActivity implements TextWatcher {
  protected FormPrepaidBinding binding;
  protected DataInternetAdapter adapter;
  protected GenericFilterAdapter adapterPaket, adapterKuota, adapterMasaAktif;
  private List<ResponsePricelist> mData = new ArrayList<>();
  private View lastClickedButton = null;
  private View lastArrow = null;
  private String tempNamaPaket = "";
  private String tempKuotaRange = "";
  private String tempMasaAktif = "";
  private boolean isReset = false;
  private String cachedNomor = null;
  //
  private boolean isCollapsed = false;
  private boolean isAnimating = false;
  private int totalScrollY = 0;

  @Override
  public String getTitleToolbar() {
    return getAbsIntent("brand");
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = FormPrepaidBinding.inflate(getLayoutInflater());

    AppBarLayout appBarLayout = binding.appBar;
    View logoProvider = binding.logoProvider;
    TextView title = binding.title;
   
    appBarLayout.addOnOffsetChangedListener(
        new AppBarLayout.OnOffsetChangedListener() {
          @Override
          public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            float totalScrollRange = appBarLayout.getTotalScrollRange();
            float offsetFactor = 1f - (Math.abs(verticalOffset) / totalScrollRange); // 1 → 0

            float titleY = -toolbar.getHeight() * (1f - offsetFactor);
            title.setTranslationY(titleY);
            title.setAlpha(offsetFactor);

            float logoY = -toolbar.getHeight() * (1f - offsetFactor);
            logoProvider.setTranslationY(logoY);
            logoProvider.setAlpha(offsetFactor);

            // Geser backdrop naik (semakin menyusut)
            float y = -toolbar.getHeight() * (1f - offsetFactor);
            binding.backdrop.setTranslationY(y);
          }
        });

    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    binding.title.setText(absText());
    binding.inputLayout.setPlaceholderText(absPlaceholderText());
    binding.etPhoneNumber.addTextChangedListener(this);
    binding.btnPickContact.setOnClickListener(v -> launchContactPicker());
    initEditext(binding.etPhoneNumber);
    if (absBrandIconUrl() != null && !absBrandIconUrl().isEmpty()) {
      Glide.with(this).load(absBrandIconUrl()).into(binding.logoProvider);
    } else {
      binding.logoProvider.setImageResource(R.drawable.ic_chip);
    }

    binding.layer.setOnTouchListener(
        (v, event) -> {
          binding.layer.setVisibility(View.GONE);
          binding.expandFilter.collapse();
          if (binding.etPhoneNumber.hasFocus()) {
            binding.etPhoneNumber.requestFocus();
            showKeyboard();
          }
          if (lastArrow != null) {
            animateArrow(lastArrow, 180f, 0f);
            lastArrow = null;
            lastClickedButton = null;
            tandaiText();
          }
          return false;
        });

    binding.clearText.setOnClickListener(
        v -> {
          binding.etPhoneNumber.setText("");
          binding.clearText.setVisibility(View.GONE);
        });

    binding.etPhoneNumber.setOnTouchListener(
        (v, event) -> {
          if (event.getAction() == MotionEvent.ACTION_DOWN) {
            showKeyboard();
            if (ifExpandFilter()) return true;
          }

          return false; // biarkan normal jika expandFilter tidak terbuka
        });
    initRecycler();
    loadProduk();
  }

  private void initRecycler() {
    adapter = new DataInternetAdapter(new ArrayList<>());
	binding.recyclerview.setDemoLayoutManager(ShimmerRecyclerViewX.LayoutMangerType.LINEAR_VERTICAL);
    binding.recyclerview.setDemoLayoutReference(R.layout.shimmer_pricelist);
    binding.recyclerview.addItemDecoration(getSpacingItemDecoration(1, 50, true));
    binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnDataClickListener(produk -> validate(produk));
    binding.recyclerview.setOnTouchListener(
        (v, event) -> {
          hideKeyboard();
          return false;
        });
    // @ FilterAdapter
    binding.recyclerFilter.addItemDecoration(getSpacingItemDecoration(2, 50, true));
    binding.recyclerFilter.setLayoutManager(getGridLayoutManager(2));
  }

  protected String getAbsIntent(String key) {
    Intent intent = getIntent();
    return intent != null ? intent.getStringExtra(key) : null;
  }

  private void loadProduk() {
    RequestPricelist requestPricelist = new RequestPricelist(this);
    requestPricelist.setType("prabayar");
    requestPricelist.setKategori(absKategoriProduk());
    requestPricelist.startExecute(
        new RequestPricelist.Callback() {
          @Override
          public void onRequest() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onDataChange(List<ResponsePricelist> pricelist) {
            mData = pricelist;

            hitungJumlahProduk();
            absProdukLoaded(pricelist);
            binding.recyclerview.hideShimmerAdapter();
          }

          @Override
          public void onFailure(String errorMessage) {
            absProdukFailed(errorMessage);
          }
        });
  }

  private void hitungJumlahProduk() {
    AdapterHelper.hitungJumlahProdukByKuota(adapter, binding.jumlahProduk);
  }

  protected void onFilterClick(View clicked) {
    hideKeyboard();
    View currentArrow = null;
    if (clicked.getId() == R.id.btnNamaPaket) {
      currentArrow = binding.toggleNamaPaket;
    } else if (clicked.getId() == R.id.btnKuota) {
      currentArrow = binding.toggleKuota;
    } else if (clicked.getId() == R.id.btnMasaAktif) {
      currentArrow = binding.toggleMasaAktif;
    }

    // Jika tombol yang sama diklik lagi → tutup
    if (binding.expandFilter.isExpanded() && clicked == lastClickedButton) {
      binding.expandFilter.collapse();
      binding.layer.setVisibility(View.GONE);
      if (binding.etPhoneNumber.hasFocus()) {
        binding.etPhoneNumber.requestFocus();
        showKeyboard();
      }
      if (currentArrow != null) animateArrow(currentArrow, 180f, 0f);
      lastClickedButton = null;
      lastArrow = null;
      tandaiText();
      return;
    }

    // ❗ Tutup panah sebelumnya (jika beda tombol)
    if (lastArrow != null && lastArrow != currentArrow) {
      animateArrow(lastArrow, 180f, 0f);
    }

    // ❗ Buka panah yang sekarang
    if (currentArrow != null) {
      animateArrow(currentArrow, 0f, 180f);
    }

    lastClickedButton = clicked;
    lastArrow = currentArrow;
    tandaiText();

    // Filter list data
    List<String> listNamaPaket = new ArrayList<>();
    List<String> listKuota = new ArrayList<>();
    List<String> listMasaAktif = new ArrayList<>();

    if (clicked.getId() == R.id.btnNamaPaket) {
      for (ResponsePricelist item : mData) {
        if (item.getBrand().equalsIgnoreCase(getAbsIntent("brand"))) {
          listNamaPaket.add(item.getType());
        }
      }
      listNamaPaket = new ArrayList<>(new LinkedHashSet<>(listNamaPaket));
      if (adapterPaket == null) {
        adapterPaket = new GenericFilterAdapter(listNamaPaket);
        adapterPaket.setOnClickListener(
            item -> {
              if (item == null || item.trim().isEmpty()) {
                tempNamaPaket = "";
                resetTandaiButton();
              } else {
                tempNamaPaket = item;
              }
            });
      } else {
        adapterPaket.updateData(listNamaPaket);
      }

      binding.recyclerFilter.setAdapter(adapterPaket);

    } else if (clicked.getId() == R.id.btnKuota) {
      listKuota.add("< 1GB");
      listKuota.add("1 - 5GB");
      listKuota.add("6 - 10GB");
      listKuota.add("11 - 20GB");
      listKuota.add("> 20GB");
      if (adapterKuota == null) {
        adapterKuota = new GenericFilterAdapter(listKuota);
        adapterKuota.setOnClickListener(
            item -> {
              if (item == null || item.trim().isEmpty()) {
                tempKuotaRange = "";
                resetTandaiButton();
              } else {
                tempKuotaRange = item;
              }
            });
      } else {
        adapterKuota.updateData(listKuota);
      }
      binding.recyclerFilter.setAdapter(adapterKuota);

    } else if (clicked.getId() == R.id.btnMasaAktif) {
      for (ResponsePricelist item : mData) {
        if (item.getBrand().equalsIgnoreCase(getAbsIntent("brand"))) {
          listMasaAktif.add(item.getMasaAktif());
        }
      }
      listMasaAktif = new ArrayList<>(new LinkedHashSet<>(listMasaAktif));
      Collections.sort(listMasaAktif, Comparator.comparingInt(HelperFilterKuota::urutkanHari));
      if (adapterMasaAktif == null) {
        adapterMasaAktif = new GenericFilterAdapter(listMasaAktif);
        adapterMasaAktif.setOnClickListener(
            item -> {
              if (item == null || item.trim().isEmpty()) {
                tempMasaAktif = "";
                resetTandaiButton();
              } else {
                tempMasaAktif = item;
              }
            });
      } else {
        adapterMasaAktif.updateData(listMasaAktif);
      }
      binding.recyclerFilter.setAdapter(adapterMasaAktif);
    }

    if (!binding.expandFilter.isExpanded()) {
      binding.expandFilter.expand();
      binding.layer.setVisibility(View.VISIBLE);
    }
  }

  protected void filterProduk(View v) {
    // Jika baru saja direset, tidak perlu menerapkan filter
    if (isReset) {
      isReset = false; // reset flag agar tidak mengganggu klik berikutnya
      AndroidViews.showToast("Filter telah direset", this);
      binding.expandFilter.collapse();
      binding.layer.setVisibility(View.GONE);
      resetTandaiButton();
      if (lastArrow != null) animateArrow(lastArrow, 180f, 0f);
      return;
    }

    List<ResponsePricelist> listOriginal = new ArrayList<>(adapter.getOriginalData());
    List<ResponsePricelist> listFilter = new ArrayList<>(listOriginal);

    StrUtils.FILTER_PAKET = tempNamaPaket;
    StrUtils.FILTER_KUOTA = tempKuotaRange;
    StrUtils.FILTER_MASA_AKTIF = tempMasaAktif;

    if (HelperFilterKuota.noFilterSelected()) {
      AndroidViews.showToast("Tidak ada filter yang dipilih", this);
      binding.expandFilter.collapse();
      binding.layer.setVisibility(View.GONE);
      resetTandaiButton();
      adapter.updateData(new ArrayList<>(adapter.getOriginalData()));
      if (lastArrow != null) animateArrow(lastArrow, 180f, 0f);
      return;
    }

    if (!StrUtils.FILTER_PAKET.isEmpty()) {
      listFilter =
          listFilter.stream()
              .filter(produk -> StrUtils.FILTER_PAKET.equalsIgnoreCase(produk.getType()))
              .collect(Collectors.toList());
    }

    if (!StrUtils.FILTER_KUOTA.isEmpty()) {
      listFilter =
          listFilter.stream()
              .filter(produk -> HelperFilterKuota.filterByKuota(produk, StrUtils.FILTER_KUOTA))
              .collect(Collectors.toList());
    }

    if (!StrUtils.FILTER_MASA_AKTIF.isEmpty()) {
      listFilter =
          listFilter.stream()
              .filter(produk -> StrUtils.FILTER_MASA_AKTIF.equalsIgnoreCase(produk.getMasaAktif()))
              .collect(Collectors.toList());
    }

    if (listFilter.isEmpty()) {
      AndroidViews.showToast("Tidak ada hasil", this);
      adapter.updateData(new ArrayList<>(adapter.getOriginalData()));
    } else {
      adapter.updateData(listFilter);
    }
    if (!HelperFilterKuota.noFilterSelected()) tandaiButton();
    adapter.resetSelectedPosition();
    binding.recyclerview.scrollToPosition(0);
    hitungJumlahProduk();
    binding.expandFilter.collapse();
    binding.layer.setVisibility(View.GONE);
    if (lastArrow != null) animateArrow(lastArrow, 180f, 0f);
  }

  protected void filterReset(View v) {
    isReset = true;
    StrUtils.FILTER_PAKET = "";
    StrUtils.FILTER_KUOTA = "";
    StrUtils.FILTER_MASA_AKTIF = "";
    tempNamaPaket = "";
    tempKuotaRange = "";
    tempMasaAktif = "";

    if (adapterPaket != null) {
      adapterPaket.resetSelectedPosition();
    }
    if (adapterKuota != null) {
      adapterKuota.resetSelectedPosition();
    }
    if (adapterMasaAktif != null) {
      adapterMasaAktif.resetSelectedPosition();
    }
    binding.expandFilter.collapse();
    binding.layer.setVisibility(View.GONE);
    if (lastArrow != null) animateArrow(lastArrow, 180f, 0f);
    binding.recyclerview.scrollToPosition(0);
    adapter.resetSelectedPosition();
    adapter.updateData(new ArrayList<>(adapter.getOriginalData()));
    hitungJumlahProduk();
    resetTandaiButton();
  }

  private void tandaiText() {
    // Reset semua ke normal
    binding.namaPaket.setTypeface(null, Typeface.NORMAL);
    binding.namaKuota.setTypeface(null, Typeface.NORMAL);
    binding.namaMasaAktif.setTypeface(null, Typeface.NORMAL);

    // Bold yang diklik
    if (lastClickedButton == binding.btnNamaPaket) {
      binding.namaPaket.setTypeface(null, Typeface.BOLD);
    } else if (lastClickedButton == binding.btnKuota) {
      binding.namaKuota.setTypeface(null, Typeface.BOLD);
    } else if (lastClickedButton == binding.btnMasaAktif) {
      binding.namaMasaAktif.setTypeface(null, Typeface.BOLD);
    }
  }

  private void tandaiButton() {
    if (!tempNamaPaket.isEmpty()) {
      binding.namaPaket.setTextColor(QiosColor.getColor(this, R.color.me_blue));
      binding.namaPaket.setTypeface(null, Typeface.BOLD);
    }

    if (!tempKuotaRange.isEmpty()) {
      binding.namaKuota.setTextColor(QiosColor.getColor(this, R.color.me_blue));
      binding.namaKuota.setTypeface(null, Typeface.BOLD);
    }
    if (!tempMasaAktif.isEmpty()) {
      binding.namaMasaAktif.setTextColor(QiosColor.getColor(this, R.color.me_blue));
      binding.namaMasaAktif.setTypeface(null, Typeface.BOLD);
    }
  }

  private void resetTandaiButton() {
    binding.namaPaket.setTextColor(QiosColor.getColor(this, R.color.me_toolbar_title));
    binding.namaKuota.setTextColor(QiosColor.getColor(this, R.color.me_toolbar_title));
    binding.namaMasaAktif.setTextColor(QiosColor.getColor(this, R.color.me_toolbar_title));
    binding.namaPaket.setTypeface(null, Typeface.NORMAL);
    binding.namaKuota.setTypeface(null, Typeface.NORMAL);
    binding.namaMasaAktif.setTypeface(null, Typeface.NORMAL);
  }

  private void animateArrow(View arrow, float start, float end) {
    ObjectAnimator animator = ObjectAnimator.ofFloat(arrow, "rotation", start, end);
    animator.setDuration(100);
    animator.start();
  }

  protected void infoPembelian(ResponsePricelist model, String nomor) {
    binding.pembayaran.btnLanjutBayar.setOnClickListener(
        v -> {
          SheetInfoPrice sheet = SheetInfoPrice.newInstance(model);
          sheet.setInfoNomor(nomor);
          sheet.show(getSupportFragmentManager(), "Kuota");
          // saveCache();
          if (!ifExpandFilter()) return;
        });
  }

  @Override
  protected void onPhoneNumberPicked(String phoneNumber) {
    binding.etPhoneNumber.setText(phoneNumber);
    binding.etPhoneNumber.setSelection(phoneNumber.length());
    hideKeyboard();
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    binding.clearText.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
  }

  @Override
  public void afterTextChanged(Editable s) {
    String nomor = s.toString().trim();
    String brand = getIntent().getStringExtra("brand");
    if (nomor.isEmpty()) {
      hideExpandError();
      setujui(false);
      return;
    }

    if (nomor.length() >= 4) {
      boolean cocok = ProviderUtils.detectBrand(nomor, brand);
      if (!cocok) {
        showExpandError("Eiits Salah! Ini Bukan Nomor " + brand);
        setujui(false);
      } else {
        hideExpandError();
        setujui(true);
      }
    } else {
      setujui(false);
    }
  }

  protected void setujui(boolean setuju) {
    adapter.setInputValid(setuju);
  }

  protected void showExpandError(String message) {
    AndroidViews.ShowExpandError(message, binding.expandable, binding.txtPesanKePengguna);
  }

  protected void hideExpandError() {
    reset();
    binding.txtPesanKePengguna.setText("");
    if (binding.expandable.isExpanded()) {
      binding.expandable.collapse();
    }
  }

  protected void reset() {
    binding.wrapperPembayaran.setVisibility(View.GONE);
    adapter.resetSelectedPosition();
  }

  @Override
  public void onBackPressed() {
    if (!ifExpandFilter()) super.onBackPressed();
  }

  protected boolean ifExpandFilter() {
    if (binding.expandFilter.isExpanded()) {
      binding.expandFilter.collapse();
      binding.layer.setVisibility(View.GONE);
      if (lastArrow != null) {
        animateArrow(lastArrow, 180f, 0f);
        lastArrow = null;
        lastClickedButton = null;
        tandaiText();
      }
      if (binding.etPhoneNumber.hasFocus()) {
        binding.etPhoneNumber.requestFocus();
        showKeyboard();
      }
      return true;
    }
    return false;
  }

  @Override
  public void onDestroy() {
    if (!ifExpandFilter()) super.onDestroy();
  }

  // Superclass
  protected abstract void validate(ResponsePricelist pricelist);

  protected abstract String absText();

  protected abstract String absPlaceholderText();

  protected abstract String absBrandIconUrl();

  protected abstract String absKategoriProduk();

  protected abstract void absProdukLoaded(List<ResponsePricelist> produk);

  protected abstract void absProdukFailed(String failed);
}
