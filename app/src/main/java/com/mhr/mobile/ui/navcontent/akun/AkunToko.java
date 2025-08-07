package com.mhr.mobile.ui.navcontent.akun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.api.request.RequestLogin;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.databinding.UserTokoBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.sheet.SheetEditText;
import com.mhr.mobile.util.AndroidViews;

public class AkunToko extends InjectionActivity implements SheetEditText.onFieldEditedListener {
  private UserTokoBinding binding;
  private String jenisEdit = ""; // "nama_toko" atau "alamat_toko"
  private String namaTokoAwal = "";
  private String alamatTokoAwal = "";

  @Override
  protected String getTitleToolbar() {
    return "Atur Toko";
  }

  @Override
  public View onCreateQiosView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserTokoBinding.inflate(inflater, viewGroup, false);

    displayUi();
    return binding.getRoot();
  }

  private void displayUi() {
    namaTokoAwal = session.getNamaToko();
    alamatTokoAwal = session.getAlamatToko();

    if (namaTokoAwal.isEmpty()) {
      binding.infoNamaToko.setText("Belum Diatur");
      binding.infoNamaToko.setTag("default");
    } else {
      binding.infoNamaToko.setText(namaTokoAwal);
      binding.infoNamaToko.setTag(null);
    }

    if (alamatTokoAwal.isEmpty()) {
      binding.infoAlamatToko.setText("Belum Diatur");
      binding.infoAlamatToko.setTag("default");
    } else {
      binding.infoAlamatToko.setText(alamatTokoAwal);
      binding.infoAlamatToko.setTag(null);
    }

    binding.infoNamaToko.setOnClickListener(
        v -> {
          jenisEdit = "nama_toko";
          showDialog("nama_toko");
        });
    binding.infoAlamatToko.setOnClickListener(
        v -> {
          jenisEdit = "alamat_toko";
          showDialog("alamat_toko");
        });
  }

  private void showDialog(String field) {
    String text = "";

    if (field.equals("nama_toko")) {
      text = isDefaultPlaceholder(binding.infoNamaToko) ? "" : binding.infoNamaToko.getText().toString();
    } else if (field.equals("alamat_toko")) {
      text = isDefaultPlaceholder(binding.infoAlamatToko) ? "" : binding.infoAlamatToko.getText().toString();
    }
	
	SheetEditText sheet = SheetEditText.newInstance(field,text);
	sheet.show(getSupportFragmentManager(),"TAG");
  }

  private boolean isDefaultPlaceholder(View view) {
    Object tag = view.getTag();
    return "default".equals(tag);
  }

  @Override
  public void onFieldEdited(String type, String value) {
    if (type.equals("nama_toko")) {
      binding.infoNamaToko.setText(value);
      binding.infoNamaToko.setTag(null);
      editNamaToko(value);
    } else if (type.equals("alamat_toko")) {
      binding.infoAlamatToko.setText(value);
      binding.infoAlamatToko.setTag(null);
      editAlamatToko(value);
    }
  }

  private void editNamaToko(String namaToko) {
    RequestLogin request = new RequestLogin(this);
    request.setToken(session.getToken());
    request.setUpdateNama(session.getNama());
    request.setUpdateNamaToko(namaToko);
    request.setUpdateAlamatToko(session.getAlamatToko());
    request.requestUpdate(
        new RequestLogin.Callback() {
          @Override
          public void onUpdateNama() {
            ResponseUsers.User dataLama = session.getUser();
            ResponseUsers.User dataBaru = new ResponseUsers.User();
            dataBaru.setToken(dataLama.getToken());
            dataBaru.setNama(dataLama.getNama());
            dataBaru.setNomor(dataLama.getNomor());
            dataBaru.setHashPin(dataLama.getHashPin());
            dataBaru.setAlamatToko(dataLama.getAlamatToko());
            //
            dataBaru.setNamaToko(namaToko);
            pref.saveUserSession(dataBaru);
            AndroidViews.showToast("Sukses", AkunToko.this);
          }

          @Override
          public void onFailure(String error) {
            AndroidViews.showToast(error, AkunToko.this);
          }
        });
  }

  private void editAlamatToko(String alamatToko) {
    RequestLogin request = new RequestLogin(this);
    request.setToken(session.getToken());
    request.setUpdateNama(session.getNama());
    request.setUpdateNamaToko(session.getNamaToko());
    request.setUpdateAlamatToko(alamatToko);
    request.requestUpdate(
        new RequestLogin.Callback() {
          @Override
          public void onUpdateNama() {
            ResponseUsers.User dataLama = session.getUser();
            ResponseUsers.User dataBaru =
                new ResponseUsers.User(
                    dataLama.getToken(),
                    dataLama.getNama(),
                    dataLama.getNomor(),
                    dataLama.getNamaToko(),
                    alamatToko,
                    dataLama.getHashPin());

            pref.saveUserSession(dataBaru);
            AndroidViews.showToast("Sukses", AkunToko.this);
          }

          @Override
          public void onFailure(String error) {
            AndroidViews.showToast(error, AkunToko.this);
          }
        });
  }
}
