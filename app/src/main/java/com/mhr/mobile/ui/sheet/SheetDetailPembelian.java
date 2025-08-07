package com.mhr.mobile.ui.sheet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.api.request.RequestTransaksi;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.api.response.ResponseTransaksi;
import com.mhr.mobile.databinding.SheetDetailPembelianBinding;
import com.mhr.mobile.ui.dialog.DialogMokuAlert;
import com.mhr.mobile.ui.dialog.LoadingDialogFragment;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.ui.intro.UserSession;
import com.mhr.mobile.ui.navcontent.history.TransaksiSelesaiEvent;

import com.mhr.mobile.ui.navcontent.home.dashboard.DashboardTopupEvent;
import com.mhr.mobile.ui.status.StatusTransaksi;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosPreferences;
import org.greenrobot.eventbus.EventBus;

public class SheetDetailPembelian extends InjectionSheetFragment {
  private SheetDetailPembelianBinding binding;
  private LoadingDialogFragment dialog;
  private QiosPreferences preferences;
  private String nomor;
  private String customerName;
  private String produkCode;
  private String produkName;
  private double harga;
  private String typeApi;
  private String brand;
  private int iconResId;
  private String imageUrl;
  private String kategori;
  private UserSession session;
  // Digiflazz
  public static SheetDetailPembelian newInstance(ResponsePricelist digiflazz) {
    SheetDetailPembelian fragment = new SheetDetailPembelian();
    fragment.setProdukCode(digiflazz.getBuyerSkuCode());
    fragment.setHarga(digiflazz.getPriceAfterDiskon());
    return fragment;
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetDetailPembelianBinding.inflate(inflater, viewGroup, false);
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    initialize();
    terimaData();
  }

  private void initialize() {
    preferences = new QiosPreferences(requireActivity());
    session = UserSession.with(requireActivity());
    dialog = new LoadingDialogFragment();
    binding.ubah.setOnClickListener(v -> dismiss());
  }

  private void terimaData() {
    binding.hp.setText(nomor);
    binding.brand.setText(produkName);
    binding.tvTitleBrand.setText(customerName);
    binding.harga.setText(FormatUtils.formatRupiah(harga));
    binding.totalPrice.setText(FormatUtils.formatRupiah(harga));
    binding.btnPembayaran.setOnClickListener(this::startPembayaranDigiflazz);
  }

  private void startPembayaranDigiflazz(View v) {
    RequestTransaksi request = new RequestTransaksi(requireActivity());
    request.setToken(session.getToken());
    request.setSku(produkCode);
    request.setCustomerNo(nomor);
    request.setPrice(10000);
    request.requestTransaksi(
        new RequestTransaksi.Callback() {
          @Override
          public void onRequest() {
            dialog.show(getParentFragmentManager(), "loadingDigiflazz");
          }

          @Override
          public void onDataChanged(ResponseTransaksi response) {
            if (response.getData() != null && response.getData().getRefId() != null) {
              dialog.showSuccess(() -> detailTransaksi(response));
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
                dialog.dismiss();
                DialogMokuAlert.with(requireActivity()).TampilkanPesan("Gagal", message);
              }
            }
          }

          @Override
          public void onFailure(String error) {
            dialog.dismiss();
            DialogMokuAlert.with(requireActivity()).TampilkanPesan("Gagal", error);
          }
        });
  }

  private void detailTransaksi(ResponseTransaksi response) {
    EventBus.getDefault().postSticky(new DashboardTopupEvent());
    EventBus.getDefault().postSticky(new TransaksiSelesaiEvent());

    dismiss(); // atau tutup SheetDetailPembelian
    Intent intent = new Intent(requireActivity(), StatusTransaksi.class);
    intent.putExtra("ref_id", response.getData().getRefId());
    intent.putExtra("brand_icon_url", imageUrl);
    intent.putExtra("no_hp", nomor);

    if (intent != null) startActivity(intent);
    requireActivity().finish();
  }

  public void setNomor(String nomor) {
    this.nomor = nomor;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public void setProdukCode(String produkCode) {
    this.produkCode = produkCode;
  }

  public void setHarga(double harga) {
    this.harga = harga;
  }

  public void setTypeApi(String typeApi) {
    this.typeApi = typeApi;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setIcon(int iconResId) {
    this.iconResId = iconResId;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setProdukName(String produkName) {
    this.produkName = produkName;
  }

  public void setKategori(String kategori) {
    this.kategori = kategori;
  }
}
