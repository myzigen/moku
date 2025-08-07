package com.mhr.mobile.ui.produk;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import com.mhr.mobile.api.request.RequestPricelist;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.InjectionRecyclerviewBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.adapter.ProdukTypeAdapter;
import com.mhr.mobile.ui.produk.helper.HelperFilterProduk;
import com.mhr.mobile.ui.produk.prepaid.PrepaidForm;
import com.mhr.mobile.ui.produk.prepaid.ewallet.EwalletReload;
import com.mhr.mobile.ui.produk.prepaid.game.GameReload;
import com.mhr.mobile.ui.produk.prepaid.kuota.KuotaPerdanaMassal;
import com.mhr.mobile.ui.produk.prepaid.kuota.KuotaVoucherFisik;
import com.mhr.mobile.ui.produk.prepaid.kuota.KuotaVoucherInject;
import com.mhr.mobile.ui.produk.prepaid.kuota.KuotaVoucherInjectMassal;
import com.mhr.mobile.ui.produk.prepaid.pulsa.PulsaSingaporeReload;
import com.mhr.mobile.ui.produk.prepaid.streaming.StreamingReload;
import java.util.ArrayList;
import java.util.List;

public class ProdukType extends InjectionActivity {
  private InjectionRecyclerviewBinding binding;
  private ProdukTypeAdapter adapter;
  private List<ResponsePricelist> mData = new ArrayList<>();
  private String kategori, filterMode;
  private String currentTab = "satuan"; // default

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = InjectionRecyclerviewBinding.inflate(getLayoutInflater());

    kategori = getAbsIntent("type_produk");
    filterMode = getAbsIntent("filter_mode");
    initUi();

    return binding.getRoot();
  }

  @Override
  protected String getTitleToolbar() {
    return getIntent().getStringExtra("brand");
  }

  private void initUi() {
    initRecyclerView();
    if ("Aktivasi Perdana".equalsIgnoreCase(kategori)) binding.tab.setVisibility(View.VISIBLE);
    if ("Aktivasi Voucher".equalsIgnoreCase(kategori)) binding.tab.setVisibility(View.VISIBLE);
    binding.tab.addTab(binding.tab.newTab().setText("Satuan"));
    binding.tab.addTab(binding.tab.newTab().setText("Massal"));
    binding.tab.addOnTabSelectedListener(
        new TabLayout.OnTabSelectedListener() {
          @Override
          public void onTabSelected(TabLayout.Tab tab) {
            String tabText = tab.getText().toString().toLowerCase();
            currentTab = tabText; // bisa "satuan" atau "massal"
          }

          @Override
          public void onTabUnselected(TabLayout.Tab tab) {}

          @Override
          public void onTabReselected(TabLayout.Tab tab) {}
        });
  }

  private void initRecyclerView() {
    binding.recyclerview.addItemDecoration(getSpacingItemDecoration(3, 30, true));
    binding.recyclerview.setLayoutManager(getGridLayoutManager(3));
    adapter = new ProdukTypeAdapter(mData);
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnClickListener(
        model -> {
          Intent intent = null;
          if ("E-Money".equalsIgnoreCase(kategori)) {
            intent = new Intent(this, EwalletReload.class);
            intent.putExtra("brand", model.getBrand());
            intent.putExtra("brand_icon", model.getBrandIconUrl());
            intent.putExtra("category", model.getCategory());
          } else if ("Games".equalsIgnoreCase(kategori)) {
            intent = new Intent(this, GameReload.class);
            setExtraOnly(intent, model);
          } else if ("Voucher".equalsIgnoreCase(kategori)) {
            // Ambil filter_mode dari intent
            if ("voucher_belanja".equalsIgnoreCase(filterMode)) {
              intent = new Intent(this, ProdukNoInput.class);
            } else if ("voucher_kuota".equalsIgnoreCase(filterMode)) {
              intent = new Intent(this, KuotaVoucherFisik.class);
            }
            setExtraOnly(intent, model);
          } else if ("Masa Aktif".equalsIgnoreCase(kategori)) {
            setExtra(model);
          } else if ("Streaming".equalsIgnoreCase(kategori)) {
            intent = new Intent(this, StreamingReload.class);
            setExtraOnly(intent, model);
          } else if ("paket sms & telpon".equalsIgnoreCase(kategori)) {
            setExtra(model);
          } else if ("Aktivasi Perdana".equalsIgnoreCase(kategori)) {
            if (currentTab.equals("satuan")) {
              intent = new Intent(this, KuotaVoucherInject.class);
            } else if (currentTab.equals("massal")) {
              intent = new Intent(this, KuotaPerdanaMassal.class);
            }
            setExtraOnly(intent, model);
          } else if ("Aktivasi Voucher".equalsIgnoreCase(kategori)) {
            if (currentTab.equals("satuan")) {
              intent = new Intent(this, KuotaVoucherInject.class);
            } else if (currentTab.equals("massal")) {
              intent = new Intent(this, KuotaVoucherInjectMassal.class);
            }
            setExtraOnly(intent, model);
          } else if ("Pulsa".equalsIgnoreCase(kategori)) {
            intent = new Intent(this, PulsaSingaporeReload.class);
            setExtraOnly(intent, model);
          }
          if (intent != null) abStartActivity(intent);
        });
    dataIsReady();
  }

  private void dataIsReady() {
    if (adapter != null && adapter.getItemCount() > 0) {
      return;
    }
    loadProduk();
  }

  private void loadProduk() {
    RequestPricelist request = new RequestPricelist(this);
    request.setType("prabayar");
    request.setKategori(kategori);
    request.startExecute(
        new RequestPricelist.Callback() {
          @Override
          public void onRequest() {
            binding.lottie.setVisibility(View.VISIBLE);
          }

          @Override
          public void onDataChange(List<ResponsePricelist> pricelist) {
            // Ambil filter_mode dari intent
            String filterMode = getIntent().getStringExtra("filter_mode");

            List<ResponsePricelist> filtered;
            if ("Voucher".equalsIgnoreCase(kategori)) {
              if ("voucher_belanja".equalsIgnoreCase(filterMode)) {
                filtered = HelperFilterProduk.filterVoucherBelanja(pricelist);
              } else if ("voucher_kuota".equalsIgnoreCase(filterMode)) {
                filtered = HelperFilterProduk.filterVoucherKuota(pricelist);
              } else {
                filtered = pricelist; // default: tidak difilter
              }
            } else {
              filtered = pricelist;
            }

            adapter.updateProduk(filtered);
            binding.lottie.setVisibility(View.GONE);
            showInfo();
          }

          @Override
          public void onFailure(String errorMessage) {
            binding.lottie.setVisibility(View.GONE);
          }
        });
  }

  @Override
  public void onDataReload() {
    dataIsReady();
  }

  private void setExtraOnly(Intent intent, ResponsePricelist model) {
    intent.putExtra("brand", model.getBrand());
    intent.putExtra("brand_icon", model.getBrandIconUrl());
    intent.putExtra("kategori", model.getCategory());
  }

  private void setExtra(ResponsePricelist model) {
    Intent intent = null;
    intent = new Intent(this, PrepaidForm.class);
    setExtraOnly(intent, model);
    if (intent != null) abStartActivity(intent);
  }

  private void showInfo() {
    if ("Aktivasi Perdana".equalsIgnoreCase(kategori)) {
      infoCatatan("Pilih provider kartu perdana yang ingin kamu isi");
      infoPenjelasan("Apa itu Inject kartu Perdana?");
    }

    if ("Aktivasi Voucher".equalsIgnoreCase(kategori)) {
      infoCatatan("Pilih Provider Voucher kosong yang ingin kamu isi");
      infoPenjelasan("Cara Inject Voucher Kosong");
    }
  }

  private void infoCatatan(String catatan) {
    binding.expandable.expand();
    binding.infoCatatan.setText(catatan);
  }

  private void infoPenjelasan(String penjelasan) {
    binding.infoPenjelasan.setPaintFlags(
        binding.infoPenjelasan.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    binding.infoPenjelasan.setText(penjelasan);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    binding = null;
  }
}
