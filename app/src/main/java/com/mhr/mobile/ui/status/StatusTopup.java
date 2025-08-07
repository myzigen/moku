package com.mhr.mobile.ui.status;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mhr.mobile.R;
import com.mhr.mobile.databinding.ContentTopupStatusBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidTimes;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import com.mhr.mobile.util.bitmap.SaveBitmap;
import com.mhr.mobile.util.preferences.PrefStatusTransaksi;
import com.mhr.mobile.widget.textview.RoundedBackgroundSpan;
import java.io.IOException;
import java.util.concurrent.Executors;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class StatusTopup extends InjectionActivity {
  private ContentTopupStatusBinding binding;
  private String message = "Transaksi tidak ditemukan";
  private PrefStatusTransaksi savePref;
  private boolean firstBack = true;

  @Override
  protected String getTitleToolbar() {
    return "Rincian";
  }

  @Override
  public View onCreateQiosView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = ContentTopupStatusBinding.inflate(inflater, viewGroup, false);
    savePref = new PrefStatusTransaksi(this);
    appbar.setBackgroundColor(Color.parseColor("#F1F3F7"));
    toolbar.setTitleTextColor(Color.parseColor("#000000"));
    Drawable navIconTint = toolbar.getNavigationIcon();
    navIconTint.setTint(QiosColor.getColor(this, R.color.black));
    binding.infoId.setOnClickListener(this::copyRefId);
    binding.copy.setOnClickListener(this::copyNoRekening);
    binding.infoReference.setOnClickListener(this::copyReference);
    binding.btnSaveQr.setOnClickListener(this::saveQr);
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
    String batasPembayaran = objTopup.optString("batas_pembayaran");
    String serverNow = objTopup.optString("server_now");
    String paymentImage = objTopup.optString("payment_image");
    String paymentUrl = objTopup.optString("topup_url");
    String reference = objTopup.optString("reference");
    String qr = objTopup.optString("qris");

    int admin = objTopup.optInt("biaya_admin");
    int harga = objTopup.optInt("amount");
    int kodeUnik = objTopup.optInt("kode_unik");

    if ("SP".equalsIgnoreCase(paymentMethod)) {
      binding.wrapVa.setVisibility(View.GONE);
      binding.wrapQr.setVisibility(View.VISIBLE);
      if (qr != null) generateQRCode(qr);
    } else if ("NQ".equalsIgnoreCase(paymentMethod)) {
      binding.wrapVa.setVisibility(View.GONE);
      binding.wrapQr.setVisibility(View.VISIBLE);
      if (qr != null) generateQRCode(qr);
    } else {
      binding.wrapVa.setVisibility(View.VISIBLE);
      binding.wrapQr.setVisibility(View.GONE);
    }

    switch (status.toLowerCase()) {
      case "unpaid":
        statusColor(R.drawable.corners_bg_error, R.color.color_failed_front, R.drawable.countdown);
        AndroidTimes.with(this)
            .setText(binding.infoExpired)
            .startBatasTopup(batasPembayaran, serverNow);
        AndroidTimes.with(this)
            .setText(binding.infoExpiredQr)
            .startBatasTopup(batasPembayaran, serverNow);
        break;
      case "failed":
         statusColor(R.drawable.corners_bg_error,R.color.color_failed_front,R.drawable.countdown);
        binding.infoExpired.setText("Kadaluwarsa");
        binding.infoExpiredQr.setText("Kadaluwarsa");
        break;
      case "paid":
        statusColor(R.drawable.corners_bg_success,R.color.color_success_front,R.drawable.success);
        binding.infoExpired.setText("Pembayaran Berhasil");
        binding.infoExpiredQr.setText("Pembayaran Berhasil");
        break;
    }

    String editMethod;
    switch (paymentMethod) {
      case "NC":
        editMethod = "Virtual Account";
        break;
      case "BR":
        editMethod = "Virtual Account";
        break;
      case "BT":
        editMethod = "Virtual Account";
        break;
      case "SA":
        editMethod = "E-Wallet";
        break;
      default:
        editMethod = paymentMethod;
        break;
    }

    if (paymentMethod.equals("SA") || paymentMethod.equals("SP") || paymentMethod.equals("SL")) {
      // ShopeePay dan sejenisnya, gunakan paymentUrl
      binding.wrapNoRekening.setVisibility(View.GONE);
      binding.btnOpenBrowser.setVisibility(View.VISIBLE);
      binding.btnOpenBrowser.setOnClickListener(
          v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
            startActivity(i);
          });
    } else {
      // Metode lainnya: tampilkan nomor rekening / VA
      binding.infoNoRekening.setText(nomor);
    }

    binding.infoMethod.setText(editMethod);
    Glide.with(binding.infoImg).load(paymentImage).into(binding.infoImg);
    Glide.with(binding.imgClientQr)
        .load("https://sandbox.duitku.com/Images/qris/bg-client-01.png")
        .into(binding.imgClientQr);
    binding.infoReference.setText(reference);
    binding.infoTgl.setText(waktu);
    binding.infoQrRef.setText(reference);
    binding.infoBank.setText(paymentName);
    binding.infoPemilik.setText(pemilik);
    binding.infoQrName.setText(pemilik);
    binding.infoKodeUnik.setText(String.valueOf(kodeUnik));
    binding.infoQrKode.setText(String.valueOf(kodeUnik));
    binding.infoBiaya.setText(FormatUtils.formatRupiah(admin));
    String totalAmount = FormatUtils.formatRupiah(harga);
    binding.infoTotal.setText(highlightLastThreeDigits(totalAmount));
    binding.infoTotalQr.setText(highlightLastThreeDigits(totalAmount));
  }

  private void checkStatus() {
    String refId = getAbsIntent("ref_id");
    binding.infoId.setText(refId);
    binding.infoQrId.setText(refId);
    /*
       JSONObject cached = savePref.getStatus(refId);
       if (cached != null) {
         binding.progress.setVisibility(View.GONE);
         infoTopup(cached);
         return;
       }
    */

    OkHttpClient client = new OkHttpClient();
    String url = "https://api.qiospro.my.id/api/status_topup.php?ref_id=" + refId;
    Request request = new Request.Builder().url(url).get().build();

    binding.progress.setIndeterminate(true);

    client
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> AndroidViews.showToast("Gagal koneksi", StatusTopup.this));
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                  String responseBody = response.body().string();

                  try {
                    JSONObject json = new JSONObject(responseBody);

                    if (json.optBoolean("status")) {
                      JSONObject data = json.optJSONObject("data");
                      runOnUiThread(
                          () -> {
                            new Handler(Looper.getMainLooper())
                                .postDelayed(
                                    () -> {
                                      binding.progress.setIndeterminate(false);
                                      binding.progress.setVisibility(View.GONE);
                                      infoTopup(data);
                                      // savePref.saveStatus(data);
                                    },
                                    1000);
                          });
                    } else {
                      runOnUiThread(() -> AndroidViews.showToast(message, StatusTopup.this));
                    }

                  } catch (JSONException e) {
                    e.printStackTrace();
                  }

                } else {
                  runOnUiThread(() -> AndroidViews.showToast("Server Gagal", StatusTopup.this));
                }
              }
            });
  }

  private void saveQr(View view) {
    binding
        .getRoot()
        .post(
            () -> {
              Bitmap receiveBitmap = SaveBitmap.getBipmapFromView(binding.imgQr);
              try {
                SaveBitmap.saveBitmap(this, receiveBitmap, "id");
                Toast.makeText(this, "Gambar Disimpan", Toast.LENGTH_SHORT).show();
              } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show();
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

  private void copyReference(View v) {
    String copySn = binding.infoReference.getText().toString();
    AndroidViews.copyToClipboard(this, copySn, "Reference Di Salin", v);
  }

  private SpannableString highlightLastThreeDigits(String text) {
    SpannableString spannableString = new SpannableString(text);
    String[] words = text.split(" ");
    int start = 0;

    for (String word : words) {
      if (word.matches(".*\\d{3}$")) { // Cek apakah kata mengandung 3 digit angka terakhir
        int end = start + word.length();
        spannableString.setSpan(
            new RoundedBackgroundSpan(
                QiosColor.getColor(this, R.color.status_pending), Color.BLACK, 8),
            end - 3,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      }
      start += word.length() + 1; // +1 karena ada spasi antar kata
    }

    return spannableString;
  }

  private void generateQRCode(String qrData) {
    Executors.newSingleThreadExecutor()
        .execute(
            () -> {
              QRCodeWriter writer = new QRCodeWriter();
              try {
                BitMatrix bitMatrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 512, 512);
                Bitmap bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565);

                for (int x = 0; x < 512; x++) {
                  for (int y = 0; y < 512; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                  }
                }

                runOnUiThread(() -> binding.imgQr.setImageBitmap(bitmap));

              } catch (WriterException e) {
                e.printStackTrace();
              }
            });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    binding = null;
    AndroidTimes.with(this).cancelCountdown();
  }

  @Override
  public void onBackPressed() {
    if (firstBack) {
      firstBack = false;
      back();
    } else {
      super.onBackPressed();
    }
  }

  private void statusColor(int bg, int color, int icon) {
    Drawable success = AndroidViews.getDrawable(this, icon);
    binding.infoExpired.setBackground(AndroidViews.getDrawable(this, bg));
    binding.infoExpired.setCompoundDrawablesWithIntrinsicBounds(success, null, null, null);
    binding.infoExpired.setTextColor(QiosColor.getColor(this, color));
  }
}
