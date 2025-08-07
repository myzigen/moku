package com.mhr.mobile.ui.inject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import com.mhr.mobile.interfaces.ConnectivityListener;
import com.mhr.mobile.ui.MainActivity;
import com.mhr.mobile.util.NetworkMonitoringUtil;
import com.mhr.mobile.util.UtilsManager;
import com.mhr.mobile.viewmodel.NetworkViewModel;
import java.util.ArrayList;
import java.util.List;

public abstract class InjectionConnectivity extends InjectionTheme implements ConnectivityListener {
  private List<ConnectivityListener> listener = new ArrayList<>();
  protected NetworkViewModel networkViewModel;
  private Boolean lastNetworkState = null;
  private final Observer<Boolean> activeNetworkStateObserver =
      new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isConnected) {
          if (lastNetworkState == null) {
            // Pertama kali observe, set saja nilai awal, jangan ambil aksi
            lastNetworkState = isConnected;
            return;
          }

          if (!lastNetworkState && isConnected) {
            // Dari offline → online → reload
            onNetworkConnected(true);
          } else if (lastNetworkState && !isConnected) {
            // Dari online → offline → tampilkan snackbar
            onNetworkConnected(false);
          }

          lastNetworkState = isConnected;
        }
      };

  // Inisialisasi ActivityResultLauncher untuk memilih kontak
  protected final ActivityResultLauncher<Intent> contactPickerLauncher =
      registerForActivityResult(
          new ActivityResultContracts.StartActivityForResult(),
          result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
              Uri contactUri = result.getData().getData();
              if (contactUri != null) {
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                try (Cursor cursor =
                    getContentResolver().query(contactUri, projection, null, null, null)) {
                  if (cursor != null && cursor.moveToFirst()) {
                    String phoneNumber =
                        cursor.getString(
                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    // Menghapus karakter "+" "-" dan spasi
                    phoneNumber =
                        phoneNumber.replaceAll("[\\+\\s-]", ""); // Hapus "+", spasi, dan "-"
                    if (phoneNumber.startsWith("62")) {
                      phoneNumber = "0" + phoneNumber.substring(2); // Ubah "62" menjadi "0"
                    }
                    onPhoneNumberPicked(phoneNumber); // Callback method untuk handle nomor
                  }
                }
              }
            } else {
              // Jika tidak ada kontak dipilih, bisa memberi tahu pengguna
              onPhoneNumberPickFailed();
            }
          });

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // getSharedAxis();
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    // Pastikan NetworkMonitoringUtil memonitor status jaringan
    NetworkMonitoringUtil networkMonitoringUtil = new NetworkMonitoringUtil(this);
    // Pastikan ini dipanggil untuk memantau status jaringan
    networkMonitoringUtil.registerNetworkCallbackEvents();
    // Cek status jaringan saat aplikasi dimulai
    networkMonitoringUtil.checkNetworkState();

    // Inisialisasi ViewModel untuk memantau status jaringan
    networkViewModel = NetworkViewModel.getInstance();
    networkViewModel.getNetworkConnectivityStatus().observe(this, activeNetworkStateObserver);
  }

  @Override
  public void onNetworkConnected(boolean isConnected) {}

  @Override
  public void onDataReload() {
    // Override di aktivitas atau fragmen untuk reload data
    for (ConnectivityListener listen : listener) {
      listen.onDataReload();
    }
  }

  // Callback yang menangani nomor telepon yang dipilih
  protected abstract void onPhoneNumberPicked(String phoneNumber);

  // Callback jika tidak ada kontak yang dipilih atau terjadi kegagalan
  protected abstract void onPhoneNumberPickFailed();

  // Definisikan judul toolbar untuk aktivitas
  protected abstract String getTitleToolbar();

  protected void setDrawUnderStatusbar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
      UtilsManager.setAllowDrawUnderStatusBar(getWindow());
    else UtilsManager.setStatusBarTranslucent(getWindow());
  }

  // Menyediakan metode untuk meluncurkan pemilih kontak
  protected void launchContactPicker() {
    Intent intent =
        new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
    contactPickerLauncher.launch(intent);
  }

  public void onFragmentAttached(ConnectivityListener listen) {
    if (listener != null) {
      if (!listener.contains(listen)) {
        listener.add(listen);
      }
    }
  }

  public void onFragmentRemove(ConnectivityListener listen) {
    if (listener != null) {
      listener.remove(listen);
    }
  }

  protected void cekIzinKontak() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
          this, new String[] {Manifest.permission.READ_CONTACTS}, 101);
    } else {
      // izin sudah diberikan, bisa akses kontak
      ambilKontak();
    }
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 101) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        ambilKontak();
      } else {
        Toast.makeText(this, "Izin akses kontak ditolak", Toast.LENGTH_SHORT).show();
      }
    }
  }

  protected void ambilKontak() {}

  public void clearActivity(Class<?> targetActivity) {
    Intent intent = new Intent(this, targetActivity);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();
  }

  protected void back() {
    Intent intent = new Intent(this, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    startActivity(intent);
    finish();
  }
}
