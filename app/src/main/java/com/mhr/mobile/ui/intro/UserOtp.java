package com.mhr.mobile.ui.intro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.api.listener.OtpRequestListener;
import com.mhr.mobile.api.listener.OtpVerifyListener;
import com.mhr.mobile.api.request.RequestOtp;
import com.mhr.mobile.api.response.ResponseOtp;
import com.mhr.mobile.databinding.UserOtpBinding;
import com.mhr.mobile.ui.dialog.DialogMokuAlert;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidViews;
import com.poovam.pinedittextfield.PinField;

public class UserOtp extends InjectionActivity implements OtpVerifyListener {
  private UserOtpBinding binding;
  private CountDownTimer timer;
  private String nomor;
  private ProgressDialog progressDialog;

  @Override
  protected String getTitleToolbar() {
    return "Verifikasi";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserOtpBinding.inflate(getLayoutInflater());

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      binding.etOtp.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO);
    }
    nomor = getIntent().getStringExtra("nomor");
    initEditext(binding.etOtp);
    binding.textview.setText("Kami telah mengirimkan kode OTP ke No Whatsapp " + nomor);
    binding.tvKirimUlangOtp.setEnabled(false);

    kirimOtp(nomor);

    binding.etOtp.setOnTextCompleteListener(
        new PinField.OnTextCompleteListener() {
          @Override
          public boolean onTextComplete(String text) {
            verifyOtp(nomor, text);
            return true;
          }
        });

    binding.tvKirimUlangOtp.setOnClickListener(
        v -> {
          binding.tvKirimUlangOtp.setEnabled(false);
          kirimOtp(nomor);
        });

    return binding.getRoot();
  }

  private void kirimOtp(String nomor) {
    showProgress("Mengirim OTP...");
    RequestOtp users = RequestOtp.with(this);
    users.Nomor(nomor);
    users.RequestOtp(
        new OtpRequestListener() {
          @Override
          public void onRequest() {}

          @Override
          public void onReceive() {
            hideProgress();
            long waktuServer = users.getWaktu(); // harus di-set di class RequestUsers
            long waktuSekarang = System.currentTimeMillis() / 1000;
            long sisaDetik = 180 - (waktuSekarang - waktuServer);

            if (sisaDetik > 0) {
              mulaiCountdown(sisaDetik * 1000);
            } else {
              binding.tvKirimUlangOtp.setEnabled(true);
            }
          }

          @Override
          public void onError(String error) {
            hideProgress();
            hideKeyboard();
            AndroidViews.showToast("Gagal mengirim OTP: " + error, UserOtp.this);
            binding.tvKirimUlangOtp.setEnabled(true);
          }
        });
  }

  private void verifyOtp(String nomor, String textOtp) {
    showProgress("Memverifikasi OTP...");
    RequestOtp.with(this).Nomor(nomor).VerifiyOtp(textOtp).RequestVerifyOtp(this);
  }

  private void mulaiCountdown(long millis) {
    if (timer != null) {
      timer.cancel();
    }

    timer =
        new CountDownTimer(millis, 1000) {
          @Override
          public void onTick(long millisUntilFinished) {
            long detik = millisUntilFinished / 1000;
            long menit = detik / 60;
            long sisaDetik = detik % 60;

            binding.tvKirimUlangOtp.setText(
                String.format("Kirim ulang OTP dalam %02d:%02d", menit, sisaDetik));
          }

          @Override
          public void onFinish() {
            binding.tvKirimUlangOtp.setText("Kirim Ulang OTP");
            binding.tvKirimUlangOtp.setEnabled(true);
          }
        };

    timer.start();
  }

  private void showProgress(String message) {
    if (progressDialog == null) {
      progressDialog = new ProgressDialog(this);
      progressDialog.setCancelable(false);
    }
    progressDialog.setMessage(message);
    progressDialog.show();
  }

  private void hideProgress() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (timer != null) {
      timer.cancel();
    }
    hideProgress();
  }

  @Override
  public void onRequest() {}

  @Override
  public void onReceive(ResponseOtp usersOtp) {
    hideProgress();
    hideKeyboard();

    if ("success".equals(usersOtp.status)) {
      if (session.hasPin()) {
        Intent intent = new Intent(UserOtp.this, UserPin.class);
        intent.putExtra("nomor", nomor);
        intent.putExtra("token", getIntent().getStringExtra("token"));
        startActivity(intent);
        finish();
      } else {
        Intent intent = new Intent(UserOtp.this, UserRegister.class);
        intent.putExtra("nomor", nomor);
        intent.putExtra("token", getIntent().getStringExtra("token"));
        startActivity(intent);
        finish();
      }

    } else {
      alert(usersOtp.message);
    }
  }

  @Override
  public void onFailure(String error) {
    alert(error);
    hideProgress();
    hideKeyboard();
  }

  private void alert(String message) {
    DialogMokuAlert.with(this).TampilkanPesan("Warning", message, binding.getRoot());
  }
}
