package com.mhr.mobile.ui.status;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.R;
import com.mhr.mobile.api.request.RequestTransaksi;
import com.mhr.mobile.api.request.cart.CartRequest;
import com.mhr.mobile.api.response.ResponseTransaksi;
import com.mhr.mobile.api.response.cart.CartResponse;
import com.mhr.mobile.databinding.LayoutStatusBinding;
import com.mhr.mobile.ui.content.ContentKeranjang;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.produk.prepaid.pulsa.PulsaReload;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import com.mhr.mobile.util.preferences.PrefStatusTransaksi;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class StatusLayoutCallback extends InjectionActivity {
  private LayoutStatusBinding binding;
  private PrefStatusTransaksi pref;
  private String message = "Transaksi tidak ditemukan";
  private boolean statusLoaded = false;
  private boolean firstBack = true;
  private String ARG_REFID;

  @Override
  public View onCreateQiosView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = LayoutStatusBinding.inflate(inflater, viewGroup, false);
    toolbar.setTitle(getTitleToolbar());
    toolbar.setTitleCentered(true);
    Drawable navIconTint = toolbar.getNavigationIcon();
    navIconTint.setTint(QiosColor.getColor(this, R.color.white));
    toolbar.setTitleTextColor(QiosColor.getColor(this, R.color.white));
    pref = new PrefStatusTransaksi(this);

    // binding.btnBeli.setOnClickListener(v -> target(namaProduk));

    ARG_REFID = getAbsIntent("ref_id");

    return binding.getRoot();
  }

  @Override
  public void onResume() {
    super.onResume();

    if (isTaskRoot()) {
      back();
    }

    if (!statusLoaded) {
      checkTransaksi();
    }
  }

  private void displayStatus(String status) {
    if ("sukses".equalsIgnoreCase(status)) {
      toolbar.setTitle("Transaksi Sukses");
      appbar.setBackgroundColor(Color.parseColor("#00A68E"));
      binding.wave.setColorFilter(Color.parseColor("#00A68E"), PorterDuff.Mode.SRC_IN);
      binding.plus.setColorFilter(Color.parseColor("#00927D"), PorterDuff.Mode.SRC_IN);
      binding.lottie.setAnimation(R.raw.price_success);
      // binding.yey.setText("Yeyy, " + new String(Character.toChars(0x1F929)));
      binding.infoHarga.setTextColor(Color.parseColor("#00A68E"));
      binding.infoStatus.setText(getStatusLabel(status));
      binding.infoStatus.setTextColor(Color.parseColor("#00A68E"));
      binding.viewCall.setVisibility(View.VISIBLE);
    } else if ("gagal".equalsIgnoreCase(status)) {
      int colorCancel = QiosColor.getColor(this, R.color.status_canceled);
      toolbar.setTitle("Transaksi Gagal");
      appbar.setBackgroundColor(QiosColor.getColor(this, R.color.status_canceled));
      binding.wave.setColorFilter(colorCancel, PorterDuff.Mode.SRC_IN);
      binding.plus.setVisibility(View.GONE);
      binding.lottie.setAnimation(R.raw.price_failed);
      binding.infoHarga.setTextColor(colorCancel);
      binding.infoStatus.setText(getStatusLabel(status));
      binding.infoStatus.setTextColor(colorCancel);
    } else if ("pending".equalsIgnoreCase(status)) {
      int colorPending = QiosColor.getColor(this, R.color.status_pending);
      toolbar.setTitle("Transaksi Diproses");
      appbar.setBackgroundColor(colorPending);
      binding.wave.setColorFilter(colorPending, PorterDuff.Mode.SRC_IN);
      binding.plus.setVisibility(View.GONE);
      binding.lottie.setAnimation(R.raw.price_pending);
      binding.infoHarga.setTextColor(colorPending);
      binding.infoStatus.setText(getStatusLabel(status));
      binding.infoStatus.setTextColor(colorPending);
      // Bantuan
      binding.viewCall.setVisibility(View.VISIBLE);
      binding.btnBeli.setStrokeColor(ColorStateList.valueOf(colorPending));
      binding.btnBeli.setText("Pusan Bantuan");
      binding.btnBeli.setTextColor(colorPending);
      binding.card1.setVisibility(View.GONE);
      binding.btnFavorite.setVisibility(View.GONE);
    }
  }

  private void initData(JSONObject data) {
    String status = data.optString("status");
    String produkName = data.optString("pembelian");
    String brand = data.optString("brand");
    int harga = data.optInt("amount");
    String tanggal = data.optString("tanggal");
    String sn = data.optString("sn");
    String message = data.optString("message");
    String icon = data.optString("brand_icon_url");
    String sku = data.optString("sku");
    String namaPembeli = data.optString("nama_pembeli");
    String nomor = data.optString("customer_no");
    String refId = data.optString("ref_id");

    if (status.equals("PENDING")) {
		cekStatusPending(refId);
	}

    displayStatus(status);
    // Glide.with(this).load(icon).into(binding.infoImgProvider);
    /*
    if ("Data".equalsIgnoreCase(brand)) {
         brand = "Paket Data";
         binding.infoProduk.setText(brand);
       } else {
         binding.infoProduk.setText(brand);
       }
    */

    if (namaPembeli.isEmpty()) {
      binding.titleInquiry.setVisibility(View.GONE);
      binding.infoInquiry.setVisibility(View.GONE);
    } else {
      binding.infoInquiry.setText(namaPembeli);
      binding.titleInquiry.setVisibility(View.VISIBLE);
      binding.infoInquiry.setVisibility(View.VISIBLE);
    }

    if (!sn.isEmpty()) {
      binding.infoSn.setText(sn);
      binding.infoSn.setVisibility(View.VISIBLE);
      binding.titleSn.setVisibility(View.VISIBLE);
    } else {
      binding.titleSn.setVisibility(View.GONE);
      binding.infoSn.setVisibility(View.GONE);
    }

    binding.infoProduk.setText(produkName);
    binding.infoProvider.setText(brand);
    binding.infoNomor.setText(nomor);
    binding.infoId.setText("#" + refId);
    binding.infoTanggal.setText(tanggal);
    binding.infoProvider.setText(brand);
    binding.btnUnduh.setOnClickListener(
        v -> {
          Intent i = new Intent(this, PreviewBuktiBayar.class);
          i.putExtra("produk", produkName);
          i.putExtra("brand", brand);
          i.putExtra("ref_id", refId);
          i.putExtra("tanggal", tanggal);
          i.putExtra("nomor", nomor);
          i.putExtra("sn", sn);
          i.putExtra("harga", harga);
          abStartActivity(i);
        });
    binding.btnFavorite.setOnClickListener(
        v -> {
          addToFavorite(brand, sku, nomor, icon, harga);
        });

    statusLoaded = true;
  }

  public String getStatusLabel(String status) {
    if (status == null) return "Status Kosong";
    switch (status.toLowerCase()) {
      case "sukses":
        return "Berhasil";
      case "gagal":
        return "Gagal";
      case "pending":
        return "Di Proses";
      default:
        return "Status Tidak Diketahui";
    }
  }

  private void target(String brand) {
    if ("Pulsa".equalsIgnoreCase(brand)) {
      targetActivity(PulsaReload.class);
    }
  }

  private void checkTransaksi() {
    int harga = getIntent().getIntExtra("harga", 0);
    binding.infoHarga.setText(FormatUtils.formatRupiah(harga));

    JSONObject cached = pref.getStatus(ARG_REFID);
    if (cached != null) {
      initData(cached);
      return;
    }

    OkHttpClient client = new OkHttpClient();
    String url = "https://api.qiospro.my.id/api/transaksi_status.php?ref_id=" + ARG_REFID;
    Request request = new Request.Builder().url(url).get().build();

    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> showToast("Gagal koneksi"));
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
                      runOnUiThread(() -> initData(data));
                    } else {
                      runOnUiThread(() -> showToast(message));
                    }

                  } catch (JSONException e) {
                    e.printStackTrace();
                  }

                } else {
                  runOnUiThread(() -> showToast("Server Gagal"));
                }
              }
            });
  }

  private void cekStatusPending(String refId) {
    RequestTransaksi requestStatus = new RequestTransaksi(this);
    requestStatus.setRefId(refId);
    requestStatus.requestCekStatus(
        new RequestTransaksi.Callback() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChanged(ResponseTransaksi response) {
            showSnackbar("Panggil Digiflazz");
          }

          @Override
          public void onFailure(String error) {}
        });
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
    if (firstBack) {
      firstBack = false;
      back();
    } else super.onBackPressed();
  }
}
