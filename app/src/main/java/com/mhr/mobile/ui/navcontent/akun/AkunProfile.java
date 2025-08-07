package com.mhr.mobile.ui.navcontent.akun;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mhr.mobile.api.request.RequestLogin;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.databinding.UserEditProfileBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.intro.UserSession;
import com.mhr.mobile.ui.sheet.SheetPIN;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;

public class AkunProfile extends InjectionActivity {
  private UserEditProfileBinding binding;
  private String namaAwal = "";
  private UserSession session;

  @Override
  protected String getTitleToolbar() {
    return "Edit Akun";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserEditProfileBinding.inflate(getLayoutInflater());
    initUi();
	permission();
    return binding.getRoot();
  }

  private void initUi() {

    session = UserSession.with(this);
    namaAwal = session.getNama();

    String nomor = session.getNomor();
    String normalNomor = FormatUtils.denormalizeNomor(nomor);

    // Nama User
    binding.etNamaProfil.setText(namaAwal);
    binding.etNamaProfil.setEnabled(false);
    // Nama Toko

    binding.btnUbahNama.setEnabled(false);
    binding.etNomor.setEnabled(true);
    binding.etNomor.setText(normalNomor);
    binding.input.setEndIconOnClickListener(v -> binding.etNamaProfil.setEnabled(true));
    binding.deleteAkun.setOnClickListener(
        v -> {
          SheetPIN sheet = SheetPIN.newInstance("delete");
          sheet.show(getSupportFragmentManager());
        });
    // Tombol hanya aktif jika nama berubah dan >= 3 karakter
    binding.etNamaProfil.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            String namaBaru = s.toString().trim();
            binding.btnUbahNama.setEnabled(namaBaru.length() >= 3 && !namaBaru.equals(namaAwal));
          }

          @Override
          public void afterTextChanged(Editable s) {}
        });

    binding.btnUbahNama.setOnClickListener(v -> updateNamaProfile());
  }

  private void updateNamaProfile() {
    String token = session.getToken();
    String namaBaru = binding.etNamaProfil.getText().toString().trim();

    if (namaBaru.isEmpty()) {
      AndroidViews.showToast("Tidak boleh kosong", this);
      return;
    }

    if (namaBaru.length() < 3) {
      AndroidViews.showToast("Nama minimal 3 huruf", this);
      return;
    }

    if (namaBaru.equals(namaAwal)) {
      AndroidViews.showToast("Nama tidak berubah", this);
      return;
    }

    binding.btnUbahNama.setEnabled(false); // disable tombol sementara
    RequestLogin requestLogin = new RequestLogin(this);
    requestLogin.setToken(token);
    requestLogin.setUpdateNama(namaBaru);
    requestLogin.setUpdateNamaToko(session.getNamaToko());
    requestLogin.setUpdateAlamatToko(session.getAlamatToko());
    requestLogin.requestUpdate(
        new RequestLogin.Callback() {
          @Override
          public void onUpdateNama() {
            ResponseUsers.User lama = session.getUser();
            ResponseUsers.User baru =
                new ResponseUsers.User(
                    lama.getToken(),
                    namaBaru,
                    lama.getNomor(),
                    lama.getNamaToko(),
                    lama.getAlamatToko(),
                    lama.getHashPin());
            pref.saveUserSession(baru);
            AndroidViews.showToast("Berhasil diupdate", AkunProfile.this);
            binding.btnUbahNama.setEnabled(true);
            setResult(RESULT_OK);
            // onBackPressed();
          }

          @Override
          public void onFailure(String error) {
            AndroidViews.showToast(error, AkunProfile.this);
            binding.btnUbahNama.setEnabled(true);
          }
        });
  }

  private void permission() {
    Dexter.withContext(this)
        .withPermission(Manifest.permission.READ_CONTACTS)
        .withListener(
            new PermissionListener() {
              @Override
              public void onPermissionGranted(PermissionGrantedResponse response) {
                // Izin diberikan, load kontak
              //  loadContacts();
              }

              @Override
              public void onPermissionDenied(PermissionDeniedResponse response) {
                if (response.isPermanentlyDenied()) {
                  // User pilih "Don't ask again"
                  showSettingsDialog();
                } else {
                  AndroidViews.showToast("Ijin Tolak", AkunProfile.this);
                }
              }

              @Override
              public void onPermissionRationaleShouldBeShown(
                  com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
              }
            })
        .check();
  }

  private void showSettingsDialog() {
    new AlertDialog.Builder(this)
        .setTitle("Butuh Izin Kontak")
        .setMessage("Aplikasi membutuhkan akses kontak. Silakan aktifkan di pengaturan.")
        .setPositiveButton(
            "Ke Pengaturan",
            (dialog, which) -> {
              Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
              Uri uri = Uri.fromParts("package", getPackageName(), null);
              intent.setData(uri);
              startActivity(intent);
            })
        .setNegativeButton("Batal", null)
        .show();
  }
  
  @Override 
  public void onResume(){
	  super.onResume();
  }
}
