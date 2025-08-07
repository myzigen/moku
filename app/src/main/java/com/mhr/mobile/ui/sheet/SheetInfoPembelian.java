package com.mhr.mobile.ui.sheet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mhr.mobile.R;
import com.mhr.mobile.api.request.cart.CartRequest;
import com.mhr.mobile.api.response.cart.CartResponse;
import com.mhr.mobile.databinding.SheetDetailPembelianBinding;
import com.mhr.mobile.ui.dialog.LoadingDialogFragment;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.ui.produk.ProdukPembayaran;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.WindowPreferencesManager;

public class SheetInfoPembelian extends InjectionSheetFragment {
  private SheetDetailPembelianBinding binding;
  private LoadingDialogFragment dialog;
  private String nomorPelanggan;
  private String nameProduk, brandIcon;
  private String buyerSkuCode;
  private int harga;
  private String ARG_PLN = "";

  public static SheetInfoPembelian InquiryPLN(String inquiryUserName) {
    SheetInfoPembelian fragment = new SheetInfoPembelian();
    Bundle args = new Bundle();
    args.putString("inquiry_pln", inquiryUserName);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected String getSheetTitle() {
    return "Informasi Pembelian";
  }

  @Override
  public int getTheme() {
    return R.style.FullscreenBottomSheetDialogFragment;
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetDetailPembelianBinding.inflate(inflater, viewGroup, false);
    View view = binding.getRoot();
    new WindowPreferencesManager(requireContext()).applyEdgeToEdgePreference(getDialog().getWindow());
    AndroidViews.applyInsets(view);
    initUi();
    return view;
  }

  private void initUi() {
    if (getArguments() != null) {
      ARG_PLN = getArguments().getString("inquiry_pln", "");
    }
    dialog = new LoadingDialogFragment();
    binding.ubah.setOnClickListener(v -> addToCart());

    binding.btnPembayaran.setOnClickListener(
        v -> {
          dialog.show(getParentFragmentManager(), "pilih pembayaran");
          // dialog.showSuccess(() -> pilihPembayaran(v));
          dialog.dismiss();
          pilihPembayaran(v);
        });
    infoUiPembelian();

    binding.switchPenjual.setOnCheckedChangeListener(
        (btn, isChecked) -> {
          if (isChecked) {
            binding.expandable.expand();
          } else {
            binding.expandable.collapse();
          }
        });
  }

  private void infoUiPembelian() {
    binding.hp.setText(nomorPelanggan);
    binding.harga.setText(FormatUtils.formatRupiah(harga));
    binding.totalPrice.setText(FormatUtils.formatRupiah(harga));
    if (ARG_PLN.isEmpty()) {
      binding.brand.setText(nameProduk);
    } else {
      binding.brand.setText(ARG_PLN);
      binding.tvTitleBrand.setText("ID Pelanggan");
    }
  }

  private void pilihPembayaran(View v) {
    dismiss();
    Intent intent = new Intent(requireActivity(), ProdukPembayaran.class);
    intent.putExtra("infoNomor", nomorPelanggan);
    intent.putExtra("infoInquiryToken", ARG_PLN);
    intent.putExtra("infoProduk", nameProduk);
    intent.putExtra("infoHarga", harga);
    intent.putExtra("infoBuyerSkuCode", buyerSkuCode);
    startActivity(intent);
  }

  private void addToCart() {
	  /*
    CartRequest request = new CartRequest(requireActivity());
    request.setNamaProduk(nameProduk);
    request.setCodeProduk(buyerSkuCode);
    request.setNomorTujuan(nomorPelanggan);
    request.setBrandIcon(brandIcon);
    request.setHarga(harga);
    request.requestAddToCart(
        new CartRequest.Callback() {
          @Override
          public void onAddToCart(CartResponse response) {
            AndroidViews.showToast(response.status, requireContext());
          }
        });
		*/
  }

  public void infoNomorPelanggan(String nomorPelanggan) {
    this.nomorPelanggan = nomorPelanggan;
  }

  public void infoProduk(String nameProduk) {
    this.nameProduk = nameProduk;
  }

  public void infoHarga(int harga) {
    this.harga = harga;
  }

  public void infoBuyerSkuCode(String buyerSkuCode) {
    this.buyerSkuCode = buyerSkuCode;
  }

  public void setBrandIcon(String brandIcon) {
    this.brandIcon = brandIcon;
  }
  
  @Override
  public void onStart() {
    super.onStart();
    View view = getView();
    if (view != null) {
      View parent = (View) view.getParent();
      BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(parent);
      behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      behavior.setSkipCollapsed(true);
      behavior.setDraggable(true);
    }
  }
}
