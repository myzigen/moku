package com.mhr.mobile.ui.intro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.mhr.mobile.R;
import com.mhr.mobile.api.listener.CheckNomorListener;
import com.mhr.mobile.api.request.RequestRegister;
import com.mhr.mobile.api.request.RequestUsers;
import com.mhr.mobile.api.response.ResponseRegister;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.databinding.UserPinBinding;
import com.mhr.mobile.ui.MainActivity;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosColor;
import com.mhr.mobile.util.UtilsManager;
import com.onesignal.OneSignal;
import com.poovam.pinedittextfield.PinField;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/** @return bismillah */
public class UserPin extends InjectionActivity {
  private UserPinBinding binding;
  private String firstPin = null;

  @Override
  protected String getTitleToolbar() {
    if (pref.getPin()) {
      return "Masuk";
    } else {
      return "Pendaftaran";
    }
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserPinBinding.inflate(getLayoutInflater());
    toolbar.setVisibility(View.GONE);
    binding.etPin.setShowSoftInputOnFocus(false);
    EditorInfo editorInfo = new EditorInfo();
    InputConnection inputConnection = binding.etPin.onCreateInputConnection(editorInfo);
    if (inputConnection != null) {
      binding.keyboard.setInputConnection(inputConnection);
      binding.keyboard.setTargetEditText(binding.etPin);
    }
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    if ("login".equals(getAbsIntent("mode"))) {
      binding.title.setText("Selamat datang kembali, " + session.getNama());
      binding.strInputPin.setVisibility(View.VISIBLE);
      binding.bantuan.setVisibility(View.VISIBLE);
      long savedExpired = pref.getPinLockedUntil(session.getNomor());
      long now = System.currentTimeMillis() / 1000;
      if (savedExpired > now) {
        // Masih dalam masa kunci
        int errorColor = QiosColor.getColor(UserPin.this, R.color.status_canceled);
        binding.etPin.setText("");
        binding.etPin.setFieldColor(errorColor);
        binding.strInputPin.setTextColor(errorColor);
        AndroidViews.startCountdownTimer(binding.strInputPin, savedExpired);
        binding.etPin.setEnabled(false);
        binding.keyboard.setButtonEnabled(false);
      } else {
        pref.clearPinLockedUntil(session.getNomor()); // Hapus jika sudah tidak berlaku
        binding.etPin.setEnabled(true);
        binding.keyboard.setButtonEnabled(true);
      }
      binding.btnGantiAkun.setOnClickListener(
          v -> {
            pref.remove("dark_mode");
            Intent i = new Intent(this, UserLogin.class);
            startActivity(i);
            finish();
          });
    } else {
      binding.title.setText("Buat PIN");
      binding.btnGantiAkun.setVisibility(View.GONE);
    }
    applyPinView();
  }

  private void applyPinView() {
    String mode = getIntent().getStringExtra("mode");
    binding.etPin.setOnTextCompleteListener(
        new PinField.OnTextCompleteListener() {
          @Override
          public boolean onTextComplete(String text) {
            if ("login".equals(mode)) {
              cekPin(text);
            } else {
              createPin(text);
            }
            return false;
          }
        });
  }

  private void createPin(String inputPin) {
    if (firstPin == null) {
      firstPin = inputPin;
      binding.title.setText("Konfirmasi PIN");
      binding.etPin.setText("");
    } else {
      if (firstPin.equals(inputPin)) {
        GetPin(inputPin);
      } else {
        firstPin = null;
        AndroidViews.showToast("PIN Salah", UserPin.this);
      }
    }
  }

  // Pertama Kali Membuat Pin
  private void GetPin(String pin) {
    Uri uriFoto = null;
    File fileFoto = null;

    String uriString = getAbsIntent("image_uri");
    if (uriString != null) {
      uriFoto = Uri.parse(uriString);
      fileFoto = getFileFromUri(uriFoto); // file lokal siap kirim
    }
    String nomor = getAbsIntent("nomor");
    String nama = getAbsIntent("nama");
    String namaToko = getAbsIntent("nama_toko");
    String referral = getAbsIntent("referral");
    String normalized = FormatUtils.normalizeNomor(nomor);
    String playerId = OneSignal.getUser().getPushSubscription().getId();

    RequestRegister users = RequestRegister.with(this);
    users.setNomor(normalized);
    users.setNama(nama);
    users.setNamaToko(namaToko);
    users.setPin(pin);
    users.setReferral(referral);
    users.setFoto(fileFoto);
    users.setPlayerId(playerId);

    users.PostRegister(
        new RequestRegister.CallbackRegister() {
          @Override
          public void onRequest() {
            dialog.show(getSupportFragmentManager(), "UserPinDialog");
          }

          @Override
          public void onRegister(ResponseRegister register) {
            if ("success".equals(register.getStatus())) {
              ResponseUsers.User user = new ResponseUsers.User(register.data.token, nama, nomor, namaToko, "", true);
              user.setHashPin(true);
              pref.setPin(true);
              pref.saveLogin(true);
              pref.saveUserSession(user);
              dialog.showSuccess(() -> clearActivity(MainActivity.class));
            } else {
              failed(register.getMessage());
            }
          }

          @Override
          public void onFailure(String error) {
            failed(error);
          }
        });
  }

  private void cekPin(String pin) {
    String token = getIntent().getStringExtra("token");
    String nomor = getIntent().getStringExtra("nomor");

    if (token == null || token.isEmpty()) {
      token = session.getToken();
    }

    RequestUsers users = RequestUsers.with(this);
    users.setToken(token);
    users.setNomor(nomor);
    users.setPin(pin);
    users.ValidatePin(
        new CheckNomorListener() {
          @Override
          public void onRequest() {
            dialog.show(getSupportFragmentManager(), "UsrPinDialog");
          }

          @Override
          public void onCheckNomor(ResponseUsers users) {
            if ("success".equals(users.getStatus())) {
              if (!pref.getPin()) {
                failed("PIN tidak aktif, Silahkan login ulang");
                return;
              }
              pref.saveLogin(true);
              dialog.showSuccess(
                  () -> {
                    targetActivity(MainActivity.class);
                    finish();
                  });
            } else if ("locked".equals(users.getStatus())) {
              long expiredAt = users.getExpiredAt(); // dalam detik (dari server)
              long now = System.currentTimeMillis() / 1000; // detik sekarang
              pref.setPinLockedUntil(session.getNomor(), expiredAt);
              if (expiredAt > now) {
                AndroidViews.startCountdownTimer(binding.strInputPin, users.getExpiredAt());
                failed(users.getMessage());
                binding.etPin.setEnabled(false);
                binding.keyboard.setButtonEnabled(false);
              } else {
                pref.clearPinLockedUntil(session.getNomor());
                binding.etPin.setEnabled(true);
                binding.keyboard.setButtonEnabled(true);
                failed("Silahkan coba lagi");
              }
            } else {
              failed(users.getMessage());
            }
          }

          @Override
          public void onFailure(String error) {
            failed(error);
          }
        });
  }

  private void failed(String message) {
    dialog.dismiss();
    int errorColor = QiosColor.getColor(UserPin.this, R.color.status_canceled);
    binding.etPin.setText("");
    binding.etPin.setFieldColor(errorColor);
    binding.strInputPin.setTextColor(errorColor);
    AndroidViews.animateShake(binding.etPin);
    UtilsManager.vibrateDevice(UserPin.this);
    binding.strInputPin.setText(message);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

  private File getFileFromUri(Uri uri) {
    File file = null;
    try {
      String extension = getContentResolver().getType(uri); // contoh: image/jpeg
      String ext = "jpg"; // default
      if (extension != null) {
        if (extension.contains("png")) ext = "png";
        else if (extension.contains("jpeg") || extension.contains("jpg")) ext = "jpg";
        else if (extension.contains("webp")) ext = "webp";
      }

      InputStream inputStream = getContentResolver().openInputStream(uri);
      String fileName = "foto_user_" + System.currentTimeMillis() + "." + ext;
      file = new File(getCacheDir(), fileName);
      FileOutputStream outputStream = new FileOutputStream(file);
      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) > 0) {
        outputStream.write(buffer, 0, length);
      }
      outputStream.close();
      inputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return file;
  }
}
