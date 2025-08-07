package com.mhr.mobile.interfaces;

public interface InterfacesMainActivity {
  // Callback yang menangani nomor telepon yang dipilih
  void onPhoneNumberPicked(String phoneNumber);
  // Callback jika tidak ada kontak yang dipilih atau terjadi kegagalan
  void onPhoneNumberPickFailed();
  
  
}
