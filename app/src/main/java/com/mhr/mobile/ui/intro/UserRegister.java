package com.mhr.mobile.ui.intro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.api.request.RequestRegister;
import com.mhr.mobile.api.response.ResponseRegister;
import com.mhr.mobile.databinding.UserRegisterBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.FormatUtils;

public class UserRegister extends InjectionActivity {
  private UserRegisterBinding binding;
  private String ambilNomor, normalizeNomor;
  private static final int REQUEST_GALLERY = 1;
  private Uri selectedImageUri;

  @Override
  protected String getTitleToolbar() {
    return "Pendaftaran";
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {
      selectedImageUri = data.getData();
      binding.imageUser.setImageURI(selectedImageUri); // tampilkan preview
    }
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserRegisterBinding.inflate(getLayoutInflater());

    ambilNomor = getAbsIntent("nomor");
    normalizeNomor = FormatUtils.normalizeNomor(ambilNomor);

    binding.btnLanjut.setOnClickListener(v -> initRegister());
    binding.btnCekRefferal.setOnClickListener(v -> checkUpline());
    binding.imageUser.setOnClickListener(v -> openGallery());

    return binding.getRoot();
  }

  private void openGallery() {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), REQUEST_GALLERY);
  }

  private void initRegister() {
    String nama = binding.etIsiNama.getText().toString();
    String namaToko = binding.etNamaToko.getText().toString();
    String referral = binding.etReferral.getText().toString();

    if (nama.isEmpty()) {
      binding.input.setError("Nama Lengkap Harus Di Isi");
      return;
    }

    String token = getAbsIntent("token");

    Intent i = new Intent(UserRegister.this, UserPin.class);
    i.putExtra("mode", "register"); // Tambahkan ini!
    i.putExtra("nomor", normalizeNomor);
    i.putExtra("nama", nama);
    i.putExtra("nama_toko", namaToko);
    i.putExtra("referral", referral);
    i.putExtra("token", token);

    // Kirim URI hanya jika gambar dipilih
    if (selectedImageUri != null) {
      i.putExtra("image_uri", selectedImageUri.toString());
    }

    startActivity(i);
    finish();
  }

  private void checkUpline() {
    String referral = binding.etReferral.getText().toString();

    if (referral.isEmpty()) {
      AndroidViews.showToast("Masukkan Kode Referral", this);
      return;
    }

    RequestRegister request = new RequestRegister(this);
    request.setReferral(referral);
    request.cekReferral(
        new RequestRegister.CallbackRegister() {
          @Override
          public void onRequest() {
            dialog.show(getSupportFragmentManager(), "pin");
          }

          @Override
          public void onRegister(ResponseRegister register) {
            if ("success".equals(register.getStatus())) {
              binding.inputReferral.setHelperText(register.upline_nama);
			  binding.inputReferral.setHelperTextEnabled(true);
            } else {
              binding.inputReferral.setError(register.getMessage());
            }
          }

          @Override
          public void onFailure(String error) {
            dialog.dismiss();
            AndroidViews.showToast("Gagal memeriksa referral", UserRegister.this);
          }
        });
  }
}
