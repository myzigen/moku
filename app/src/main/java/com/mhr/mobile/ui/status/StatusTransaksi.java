package com.mhr.mobile.ui.status;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.mhr.mobile.api.request.cart.CartRequest;
import com.mhr.mobile.api.response.cart.CartResponse;
import com.mhr.mobile.databinding.ContentPaymentStatusBinding;
import com.mhr.mobile.ui.content.ContentKeranjang;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.adapter.AdapterHelper;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.preferences.PrefStatusTransaksi;
import com.mhr.mobile.viewmodel.StatusTransaksiViewModel;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class StatusTransaksi extends InjectionActivity {
  private ContentPaymentStatusBinding binding;
  private StatusTransaksiViewModel vm;
  private PrefStatusTransaksi pref;
  private String message = "Transaksi tidak ditemukan";

  @Override
  protected String getTitleToolbar() {
    return "Status Transaksi";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentPaymentStatusBinding.inflate(layoutInflater, viewGroup, false);
    vm = new ViewModelProvider(this).get(StatusTransaksiViewModel.class);
    pref = new PrefStatusTransaksi(this);
    checkTransaksi();
    binding.hp.setOnClickListener(this::copy);
    return binding.getRoot();
  }

  private void checkTransaksi() {
    String refId = getIntent().getStringExtra("ref_id");
    int jumlah = getIntent().getIntExtra("jumlah", 0);
    // binding.refId.setText(refId);
    binding.trId.setVisibility(View.GONE);
    binding.harga.setText(FormatUtils.formatRupiah(jumlah));

    JSONObject cached = pref.getStatus(refId);
    if (cached != null) {
      tampilkanData(cached);
      return;
    }

    OkHttpClient client = new OkHttpClient();
    String url = "https://api.qiospro.my.id/api/transaksi_status.php?ref_id=" + refId;
    Request request = new Request.Builder().url(url).get().build();

    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> AndroidViews.showToast("Gagal koneksi", StatusTransaksi.this));
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                  String responseBody = response.body().string();

                  try {
                    JSONObject json = new JSONObject(responseBody);

                    if (json.optBoolean("status")) {
                      JSONObject data = json.optJSONObject("data");
                      pref.saveStatus(data);
                      runOnUiThread(() -> tampilkanData(data));
                    } else {
                      runOnUiThread(() -> AndroidViews.showToast(message, StatusTransaksi.this));
                    }

                  } catch (JSONException e) {
                    e.printStackTrace();
                  }

                } else {
                  runOnUiThread(() -> AndroidViews.showToast("Server Gagal", StatusTransaksi.this));
                }
              }
            });
  }

  private void copy(View v) {
    String copyNomor = binding.hp.getText().toString();
    AndroidViews.copyToClipboard(this, copyNomor, "Berhasil Di Salin", v);
  }

  private void tampilkanData(JSONObject data) {
    String status = data.optString("status");
    String brand = data.optString("brand");
    int harga = data.optInt("amount");
    String tanggal = data.optString("tanggal");
    String sn = data.optString("sn");
    String message = data.optString("message");
    String icon = data.optString("brand_icon_url");
    String sku = data.optString("sku");
    String nomor = data.optString("customer_no");
    String refId = data.optString("ref_id");
    binding.harga.setText(FormatUtils.formatRupiah(harga));
    binding.tgl.setText(tanggal);
    binding.status.setText(status);
    AdapterHelper.setStatusColor(binding.status, status, this);
    binding.brand.setText(brand);
    binding.hp.setText(nomor);
    binding.refId.setText(refId);
    Glide.with(binding.logoProvider).load(icon).into(binding.logoProvider);

    binding.btnTmbhFavorite.setOnClickListener(
        v -> {
          addToFavorite(brand, sku, nomor, icon, harga);
        });
    if (status.equalsIgnoreCase("sukses")) {
      binding.btnTmbhFavorite.setVisibility(View.VISIBLE);
      targetActivity(StatusLayoutCallback.class);
    }

    vm.setViewModel(nomor, refId);
  }
  
  

  private void addToFavorite(String name, String sku, String no, String icon, int price) {
    CartRequest request = new CartRequest(this);
    request.setNamaProduk(name);
    request.setCodeProduk(sku);
    request.setNomorTujuan(no);
    request.setBrandIcon(icon);
    request.setHarga(price);
    request.requestAddToCart(
        new CartRequest.Callback() {
          @Override
          public void onAddToCart(CartResponse response) {
            targetActivity(ContentKeranjang.class);
          }
        });
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }
}
