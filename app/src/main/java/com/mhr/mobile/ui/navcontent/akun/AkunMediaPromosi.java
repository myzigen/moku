package com.mhr.mobile.ui.navcontent.akun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.mhr.mobile.databinding.UserMediaPromosiBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class AkunMediaPromosi extends InjectionActivity {
  private UserMediaPromosiBinding binding;
  private String message = "Transaksi tidak ditemukan";

  @Override
  protected String getTitleToolbar() {
    return "Rincian";
  }

  @Override
  public View onCreateQiosView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserMediaPromosiBinding.inflate(inflater, viewGroup, false);
	binding.infoId.setOnClickListener(this::copyRefId);
    binding.copy.setOnClickListener(this::copyNoRekening);

    checkStatus();
    return binding.getRoot();
  }

  private void infoTopup(JSONObject objTopup) {
    String status = objTopup.optString("status");
    String paymentName = objTopup.optString("nama_bank");
    String paymentMethod = objTopup.optString("metode");
    String pemilik = objTopup.optString("pemilik");
    String nomor = objTopup.optString("no_rekening");
    String waktu = objTopup.optString("tanggal");
    String paymentImage = objTopup.optString("payment_image");
    int harga = objTopup.optInt("amount");

    Glide.with(this).load(paymentImage).into(binding.infoImg);
    binding.infoBank.setText(paymentName);
    binding.infoMethod.setText(paymentMethod);
    binding.infoNoRekening.setText(nomor);
    binding.infoExpired.setText(waktu);
    binding.infoTotal.setText(FormatUtils.formatRupiah(harga));
  }

  private void checkStatus() {
    String refId = getAbsIntent("ref_id");
    binding.infoId.setText(refId);

    OkHttpClient client = new OkHttpClient();
    String url = "https://api.qiospro.my.id/api/status_topup.php?ref_id=" + refId;
    Request request = new Request.Builder().url(url).get().build();

    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> AndroidViews.showToast("Gagal koneksi", AkunMediaPromosi.this));
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                  String responseBody = response.body().string();

                  try {
                    JSONObject json = new JSONObject(responseBody);

                    if (json.optBoolean("status")) {
                      JSONObject data = json.optJSONObject("data");
                      // pref.saveStatus(data);
                      runOnUiThread(() -> infoTopup(data));
                    } else {
                      runOnUiThread(() -> AndroidViews.showToast(message, AkunMediaPromosi.this));
                    }

                  } catch (JSONException e) {
                    e.printStackTrace();
                  }

                } else {
                  runOnUiThread(() -> AndroidViews.showToast("Server Gagal", AkunMediaPromosi.this));
                }
              }
            });
  }

  private void copyNoRekening(View v) {
    String copySn = binding.infoNoRekening.getText().toString();
    AndroidViews.copyToClipboard(this, copySn, "Berhasil Di Salin", v);
  }

  private void copyRefId(View v) {
    String copySn = binding.infoId.getText().toString();
    AndroidViews.copyToClipboard(this, copySn, "Berhasil Di Salin", v);
  }
}
