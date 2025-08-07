package com.mhr.mobile.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.mhr.mobile.api.listener.CheckNomorListener;
import com.mhr.mobile.api.request.RequestUsers;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.databinding.UserLoginBinding;
import com.mhr.mobile.ui.dialog.DialogMokuAlert;
import com.mhr.mobile.util.QiosPreferences;

public class UserLogin extends AppCompatActivity implements TextWatcher {
  private UserLoginBinding binding;
  private String kirimNomor;
  private QiosPreferences pref;
  private UserSession session;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    binding = UserLoginBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    pref = new QiosPreferences(this);
	pref.remove("dark_mode");
    session = new UserSession(this);
	binding.editext.setShowSoftInputOnFocus(false);
    EditorInfo editorInfo = new EditorInfo();
    InputConnection inputConnection = binding.editext.onCreateInputConnection(editorInfo);
    if (inputConnection != null) {
      binding.keyboard.setInputConnection(inputConnection);
      binding.keyboard.setTargetEditText(binding.editext);
    }
    binding.buttonLogin.setEnabled(false);
    // initEditext(binding.editTextEmail);
    binding.editext.addTextChangedListener(this);
    binding.buttonLogin.setOnClickListener(v -> validate());
  }

  private void validate() {
    String nomor = binding.editext.getText().toString();

    if (nomor.isEmpty()) {
      binding.input.setError("Nomor Tidak Boleh Kosong");
      return;
    }
    if (nomor.length() < 10) {
      binding.input.setError("Nomor tidak valid");
      return;
    }

    // hideKeyboard();

    new AlertDialog.Builder(this)
        .setTitle(nomor)
        .setMessage("Apakah Nomor Sudah Sesuai?")
        .setPositiveButton("Ya, Benar", (arg0, arg1) -> cekNomor(nomor))
        .setNegativeButton("Batal", null)
        .show();
  }

  // * Cek Apakah User Sudah Terdaftar Atau Belum Di Database
  private void cekNomor(String nomor) {
    RequestUsers users = RequestUsers.with(this);
    users.setNomor(nomor);
    users.CheckNomorTerdaftar(
        new CheckNomorListener() {
          @Override
          public void onRequest() {}

          @Override
          public void onCheckNomor(ResponseUsers users) {
            String status = users.getStatus();
            if ("success".equals(status)) {
              boolean hasPin = users.getData().getHashPin();

              String tokenReady = users.getData().getToken();
              String nomorReady = users.getData().getNomor();
              String namaReady = users.getData().getNama();
              String namaTokoReady = users.getData().getNamaToko();
              String alamatTokoReady = users.getData().getAlamatToko();

              ResponseUsers.User sesion =
                  new ResponseUsers.User(
                      tokenReady,
                      namaReady,
                      nomorReady,
                      namaTokoReady,
                      alamatTokoReady,
                      hasPin); // "nama_toko","alamat_toko"
              pref.saveUserSession(sesion);
              loginUlang(tokenReady, nomorReady);
            } else if ("not_found".equals(status)) {
              goToOtp(nomor);
            } else {
              alert("Status tidak dikenali dari server");
            }
          }

          @Override
          public void onFailure(String error) {
            alert("Koneksi gagal");
          }
        });
  }

  private void loginUlang(String token, String nomor) {
    Intent i = new Intent(UserLogin.this, UserPin.class);
    i.putExtra("token", token);
    i.putExtra("nomor", nomor);
    i.putExtra("mode", "login");
    startActivity(i);
    finish();
  }

  private void goToOtp(String nomor) {
    Intent i = new Intent(UserLogin.this, UserOtp.class);
    i.putExtra("nomor", nomor);
    i.putExtra("token", session.getToken());
    i.putExtra("mode", "login");
    startActivity(i);
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    String nomor = s.toString().trim();
    if (nomor.isBlank()) {
      binding.buttonLogin.setEnabled(false);
      binding.input.setError(null);
    } else if (nomor.startsWith("08") && nomor.length() >= 10) {
      binding.buttonLogin.setEnabled(true);
    } else {
      binding.buttonLogin.setEnabled(false);
    }
  }

  private void alert(String message) {
    DialogMokuAlert.with(this).TampilkanPesan("Info", message);
  }
}
