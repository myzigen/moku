package com.mhr.mobile.ui.content;

/*
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.R;
import com.mhr.mobile.api.request.RequestTransaksi;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.api.response.ResponseTransaksi;
import com.mhr.mobile.databinding.ContentDetailPembelianBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.helper.HelperTransaction;
import com.mhr.mobile.ui.sheet.SheetErrorLayout;
import com.mhr.mobile.ui.sheet.SheetPIN;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import com.mhr.mobile.util.preferences.PrefInputCache;
import com.mhr.mobile.widget.button.OnActiveListener;
*/

public class ContentDetailPembelian /*extends InjectionActivity */{
	/*
  private ContentDetailPembelianBinding binding;
  private ResponsePricelist data;
  private String inquiry;

  @Override
  protected String getTitleToolbar() {
    return "Rincian";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentDetailPembelianBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    terimaData();
    applyPromo();
	binding.btnBayar.post(() -> binding.btnBayar.startHintAnimation());
    binding.btnBayar.setOnActiveListener(
        new OnActiveListener() {
          @Override
          public void onActive() {
            if (pref.getPinPurchase()) {
              SheetPIN sheet = SheetPIN.newInstance("purchase");
              sheet.setOnPinValidatedListener(
                  () -> {
                    startTransaksiIak();
                  });
              sheet.show(getSupportFragmentManager());
            } else {
              startTransaksiIak();
            }
          }
        });

    binding.switchPenjual.setOnCheckedChangeListener(
        (btn, isChecked) -> {
          if (isChecked) {
            initEditext(binding.editext);
            binding.expandable.expand();
            binding.switchText.setText("Aktif");
            binding.switchText.setTextColor(QiosColor.getColor(this, R.color.status_approved));
          } else {
            binding.expandable.collapse();
            binding.switchText.setText("Non-Aktif");
            binding.switchText.setTextColor(QiosColor.getColor(this, R.color.status_canceled));
            hideKeyboard();
          }
        });

    binding.editext.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {}

          @Override
          public void afterTextChanged(Editable s) {
            try {
              // Ambil input harga jual user
              String input = s.toString().replace(".", "").replace(",", "");
              if (input.isEmpty()) {
                binding.infoMargins.setText("Rp0");
                return;
              }

              int hargaJualUser = Integer.parseInt(input);
              int hargaModal = data.getHargaJual();

              int margin = hargaJualUser - hargaModal;
              if (margin < 0) margin = 0;

              binding.infoMargins.setText(FormatUtils.formatRupiah(margin));
            } catch (NumberFormatException e) {
              binding.infoMargins.setText("Rp 0");
            }
          }
        });
  }

  private void terimaData() {
    data = getIntent().getParcelableExtra("pricelist");
    inquiry = getIntent().getStringExtra("inquiry");
    String strHarga = FormatUtils.formatRupiah(data.getHargaJual());
    Glide.with(this).load(data.getBrandIconUrl()).into(binding.img);
    binding.tvHp.setText(getIntent().getStringExtra("nomor"));
    binding.tvProdukPrice.setText(strHarga);
    binding.tvTotalPrice.setText(strHarga);
    binding.infoHargaModal.setText(strHarga);

    if (inquiry != null && !inquiry.isEmpty()) {
      binding.tvProdukName.setText(inquiry);
      binding.tvProdukDetail.setText(data.getProductName());
    } else {
      binding.tvProdukName.setText(data.getProductName());
      binding.tvProdukDetail.setText(data.getCategory());
    }
  }

  private void saveCache(String nomor) {
    PrefInputCache cache = new PrefInputCache();
    cache.nomor = nomor;
    cache.customer_name = inquiry;
    cache.brand = data.getBrand(); // pastikan ada .getBrand()
    cache.logoUrl = data.getBrandIconUrl();
    cache.sku = data.getBuyerSkuCode();

    PrefInputCache.save(cache);
  }

  private void startTransaksiIak() {
    String nomor = getIntent().getStringExtra("nomor");
    SheetErrorLayout errorLayout = new SheetErrorLayout();
    SheetPIN pin = SheetPIN.newInstance("purchase");
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

  private void applyPromo() {
    
  }

  @Override
  public void onBackPressed() {
      super.onBackPressed();
  }
  */
}
