package com.mhr.mobile.ui.content;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mhr.mobile.adapter.KontakAdapter;
import com.mhr.mobile.databinding.MenuKontakBinding;
import com.mhr.mobile.model.Kontak;
import com.mhr.mobile.ui.inject.InjectionActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContentMenuKontak extends InjectionActivity {
  public MenuKontakBinding binding;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuKontakBinding.inflate(layoutInflater, viewGroup, false);
    initialize();
    return binding.getRoot();
  }

  private void initialize() {
    binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
    getContacts();
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 1) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // Izin diberikan, dapatkan kontak
        getContacts();
      } else {
        // Izin ditolak, beri penanganan jika perlu
        Log.d("ContentMenuKontak", "Permission denied to read contacts.");
      }
    }
  }

  private void getContacts() {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    executor.execute(
        () -> {
          List<Kontak> contacts = new ArrayList<>();

          // Meminta izin untuk membaca kontak
          if (ContextCompat.checkSelfPermission(
                  ContentMenuKontak.this, Manifest.permission.READ_CONTACTS)
              == PackageManager.PERMISSION_GRANTED) {

            Cursor cursor = null;
            try {
              cursor =
                  getContentResolver()
                      .query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

              if (cursor != null) {
                while (cursor.moveToNext()) {
                  String contactName =
                      cursor.getString(
                          cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                  String contactId =
                      cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                  // Mendapatkan nomor telepon
                  Cursor phoneCursor =
                      getContentResolver()
                          .query(
                              ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                              null,
                              ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                              new String[] {contactId},
                              null);

                  if (phoneCursor != null && phoneCursor.moveToNext()) {
                    String phoneNumber =
                        phoneCursor.getString(
                            phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contacts.add(new Kontak(contactName, phoneNumber, null));
                    phoneCursor.close();
                  }
                }
              }
            } catch (Exception e) {
              Log.e("ContentMenuKontak", "Error retrieving contacts", e);
            } finally {
              if (cursor != null) {
                cursor.close();
              }
            }

            // Logging untuk memeriksa apakah ada kontak yang ditemukan
            Log.d("ContentMenuKontak", "Contacts size: " + contacts.size());

            // Setelah mendapatkan data, update UI di thread utama
            runOnUiThread(
                () -> {
                  if (contacts.isEmpty()) {
                    // Tampilkan pesan kosong atau UI lain jika kontak tidak ditemukan
                    // binding.tvEmpty.setVisibility(View.VISIBLE);
                  } else {
                    KontakAdapter adapter = new KontakAdapter(contacts);
                    binding.recyclerview.setAdapter(adapter);
                  }
                });
          } else {
            // Handle izin yang tidak diberikan
            ActivityCompat.requestPermissions(
                ContentMenuKontak.this, new String[] {Manifest.permission.READ_CONTACTS}, 1);
          }
        });
  }
}
