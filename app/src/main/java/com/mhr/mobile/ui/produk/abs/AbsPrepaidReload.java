package com.mhr.mobile.ui.produk.abs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.mhr.mobile.R;
import com.mhr.mobile.api.request.RequestHistory;
import com.mhr.mobile.api.request.RequestPricelist;
import com.mhr.mobile.api.response.ResponseHistory;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.FormPrepaidBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.adapter.ProdukAdapter;
import com.mhr.mobile.ui.produk.sheet.SheetInfoPrice;
import com.mhr.mobile.util.AndroidViews;
import com.mikelau.shimmerrecyclerviewx.ShimmerRecyclerViewX;
import java.util.ArrayList;
import java.util.List;

public abstract class AbsPrepaidReload extends InjectionActivity implements TextWatcher {
  protected FormPrepaidBinding binding;
  protected List<ResponsePricelist> allProduk = new ArrayList<>();
  protected ProdukAdapter adapter;
  protected ShimmerRecyclerViewX recyclerview;
  protected TextInputEditText editText;
  protected AppBarLayout appBarLayout;
  private TextView titleNomor;
  private boolean isGridOrLinear = false;
  protected boolean ya = true;
  protected boolean tidak = false;
  private boolean produkLoaded = false;
  protected String inquiry;
  private int shimmerCount = 8;

  @Override
  protected String getTitleToolbar() {
    return absTitleToolbar();
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = FormPrepaidBinding.inflate(getLayoutInflater());
    
    absApplyUi();

    return binding.getRoot();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean("produkLoaded", produkLoaded);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    produkLoaded = savedInstanceState.getBoolean("produkLoaded", false);
  }

  protected void absFilterVisibility(boolean visibility) {
    binding.root.setVisibility(visibility ? View.VISIBLE : View.GONE);
  }

  protected void absApplyUi() {
    titleNomor = binding.title;
    editText = binding.etPhoneNumber;
    recyclerview = binding.recyclerview;
    appBarLayout = binding.appBar;
    View logoProvider = binding.logoProvider;

    appBarLayout.addOnOffsetChangedListener(
        new AppBarLayout.OnOffsetChangedListener() {
          @Override
          public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            float totalScrollRange = appBarLayout.getTotalScrollRange();
            float offsetFactor = 1f - (Math.abs(verticalOffset) / totalScrollRange); // 1 → 0

            float titleY = -toolbar.getHeight() * (1f - offsetFactor);
            titleNomor.setTranslationY(titleY);
            titleNomor.setAlpha(offsetFactor);

            float logoY = -toolbar.getHeight() * (1f - offsetFactor);
            logoProvider.setTranslationY(logoY);
            logoProvider.setAlpha(offsetFactor);

            // Geser backdrop naik (semakin menyusut)
            float y = -toolbar.getHeight() * (1f - offsetFactor);
            binding.backdrop.setTranslationY(y);
          }
        });

    editText.addTextChangedListener(this);
    editText.setEnabled(false);
    binding.clearText.setEnabled(false);
    binding.btnPickContact.setEnabled(false);
    titleNomor.setText(absText());
    binding.inputLayout.setPlaceholderText(absPlaceholderText());
    binding.btnPickContact.setOnClickListener(v -> launchContactPicker());
    binding.clearText.setOnClickListener(this::delete);
    if (absBrandIconUrl() != null && !absBrandIconUrl().isEmpty()) {
      Glide.with(this).load(absBrandIconUrl()).into(binding.logoProvider);
    } else {
      binding.logoProvider.setImageResource(R.drawable.ic_chip);
    }
    initEditext(editText);
    absRecycler();
    riwayatLastNumber(absKategoriProduk());
  }

  private void delete(View v) {
    editText.setText("");
    binding.clearText.setVisibility(View.GONE);
  }

  protected void infoPembelian(ResponsePricelist model, String nomor) {
    binding.pembayaran.btnLanjutBayar.setOnClickListener(
        v -> {
          isClicked();
          SheetInfoPrice sheet = SheetInfoPrice.newInstance(model);
          sheet.setInfoNomor(nomor);
          sheet.setInquiry(inquiry);
          sheet.show(getSupportFragmentManager(), "Pulsa");
        });
  }

  private void absRecycler() {
    recyclerview.addItemDecoration(getSpacingItemDecoration(2, 50, true));
    isGridOrLinear();
    adapter = new ProdukAdapter(new ArrayList<>());
    recyclerview.setAdapter(adapter);
    adapter.setOnDataClickListener(item -> absValidate(item));
  }

  private void isGridOrLinear() {
    if (isGridOrLinear) {
      recyclerview.setLayoutManager(getLinearLayoutManager());
      recyclerview.setDemoLayoutManager(ShimmerRecyclerViewX.LayoutMangerType.LINEAR_VERTICAL);
    } else {
      recyclerview.setLayoutManager(getGridLayoutManager(2));
      recyclerview.setDemoLayoutManager(ShimmerRecyclerViewX.LayoutMangerType.GRID);
      recyclerview.setDemoLayoutReference(R.layout.shimmer_pricelist);
    }
  }

  protected void isLayoutManager(boolean or) {
    this.isGridOrLinear = or;
  }

  private void absLoadProduk() {
    RequestPricelist request = new RequestPricelist(this);
    request.setType(getString(R.string.prabayar));
    request.setKategori(absKategoriProduk());
    request.startExecute(
        new RequestPricelist.Callback() {
          @Override
          public void onRequest() {
            binding.infoNoProduk.setVisibility(View.GONE);
            binding.recyclerview.setDemoChildCount(shimmerCount);
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onDataChange(List<ResponsePricelist> pricelist) {
            allProduk = pricelist;
            absProdukLoaded(pricelist);
            editText.setEnabled(true);
            binding.clearText.setEnabled(true);
            binding.btnPickContact.setEnabled(true);
            binding.recyclerview.hideShimmerAdapter();
            produkLoaded = true;
          }

          @Override
          public void onFailure(String errorMessage) {
            absProdukFailed(errorMessage);
            // binding.recyclerview.hideShimmerAdapter();
          }
        });
  }

  protected void riwayatLastNumber(String jenis) {
    RequestHistory request = new RequestHistory(this);
    request.setToken(session.getToken());
    request.setKategori("prabayar");
    request.setJenis(jenis);
    request.requestLastNumber(
        new RequestHistory.CallbackHistory() {
          @Override
          public void sendRequest() {}

          @Override
          public void onHistory(ResponseHistory data) {
            if (data != null && data.getData() != null && data.getData().getNomor() != null) {
              absNomorRiwayat(data.getData().getNomor());
              // AndroidViews.showSnackbar(AbsPrepaidReload.this, "Riwayat tersedia.");
            } else {
              // Optional: log atau tampilkan pesan jika tidak ada data
              // AndroidViews.showSnackbar(AbsPrepaidReload.this, "Riwayat tidak tersedia.");
            }
          }

          @Override
          public void onFailure(String error) {
            absProdukFailed(error);
          }
        });
  }

  public void showExpandLoading(String load) {
    AndroidViews.showExpandLoading(load, binding.expandable, binding.txtPesanKePengguna);
  }

  public void showExpandSuccess(String pesan) {
    AndroidViews.showExpandSuccess(pesan, binding.expandable, binding.txtPesanKePengguna);
  }

  protected void showExpandError(String message) {
    AndroidViews.ShowExpandError(message, binding.expandable, binding.txtPesanKePengguna);
  }

  protected void hideExpandError() {
    binding.txtPesanKePengguna.setText("");
    if (binding.expandable.isExpanded()) {
      binding.expandable.collapse();
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {
    binding.clearText.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
  }

  @Override
  public void afterTextChanged(Editable s) {
    absAfterTextChanged(s.toString().trim());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    editText.removeTextChangedListener(this);
    binding = null;
  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (isTaskRoot()) {
      back();
    }

    if (!produkLoaded) {
      AndroidViews.runAfterEnterTransition(getWindow(), this::absLoadProduk);
    }
  }

  protected abstract String absTitleToolbar();

  protected abstract String absText();

  protected abstract String absPlaceholderText();

  protected abstract void absAfterTextChanged(String nomor);

  protected abstract void absValidate(ResponsePricelist pricelist);

  protected abstract void absDetectProvider(String nomor);

  protected abstract void absNomorRiwayat(String nomor);

  protected abstract String absBrandIconUrl();

  protected abstract String absKategoriProduk();

  protected abstract void absProdukLoaded(List<ResponsePricelist> produk);

  protected abstract void absProdukFailed(String failed);
}
