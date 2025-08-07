package com.mhr.mobile.ui.navcontent.kelola.pelanggan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import com.mhr.mobile.api.request.users.RequestKelolaPelanggan;
import com.mhr.mobile.api.response.ResponsePelanggan;
import com.mhr.mobile.databinding.TambahPelangganBinding;
import com.mhr.mobile.eventbus.EventAddPelanggan;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.sheet.SheetEditText;
import com.mhr.mobile.util.AndroidViews;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class TambahPelanggan extends InjectionActivity implements SheetEditText.onFieldEditedListener {
  private TambahPelangganBinding binding;
  private AdapterFieldPelanggan adapter;
  List<FieldPelangganModel> formList = new ArrayList<>();
  private RequestKelolaPelanggan request;
  private boolean isEditMode = false;
  private String pelangganId = null;

  @Override
  protected String getTitleToolbar() {
    return isEditMode ? getAbsIntent("nama") : "Tambah Pelanggan";
  }

  @Override
  public View onCreateQiosView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = TambahPelangganBinding.inflate(inflater, viewGroup, false);
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    request = RequestKelolaPelanggan.with(this);
    request.token(session.getToken());
    initRecycler();
    terimaData();
    binding.btnDelete.setOnClickListener(v -> konfirmasiHapus());
  }

  private void terimaData() {
    isEditMode = getIntent().getBooleanExtra("edit_mode", false);
    pelangganId = getAbsIntent("id");
    String nama = getAbsIntent("nama");
    String nomor = getAbsIntent("nomor");
    formList.clear();
    formList.add(new FieldPelangganModel("nama", "Nama", nama, "Belum ada nama"));
    formList.add(new FieldPelangganModel("nomor", "Nomor", nomor, "Belum ada nomor"));

    adapter.notifyDataSetChanged();

    toolbar.setTitle(getTitleToolbar());
    if (isEditMode) {
      binding.btnDelete.setVisibility(View.VISIBLE);
    } else {
      binding.btnDelete.setVisibility(View.GONE);
    }
  }

  private void initRecycler() {
    formList.add(new FieldPelangganModel("nama", "Nama", null, "Belum ada nama"));
    formList.add(new FieldPelangganModel("nomor", "Nomor", null, "Belum ada nomor"));
    adapter = new AdapterFieldPelanggan(formList);
    adapter.setOnEditClickListener(
        key -> {
          FieldPelangganModel selected = findByKey(formList, key);
          SheetEditText.newInstance(key, selected.value)
              .show(getSupportFragmentManager(), "edit_" + key);
        });

    binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    binding.recyclerview.setAdapter(adapter);
  }

  @Override
  public void onFieldEdited(String type, String value) {
    for (FieldPelangganModel field : formList) {
      if (field.key.equals(type)) {
        field.value = value;
        break;
      }
    }
    adapter.notifyDataSetChanged(); // Refresh UI
  }

  private void crud(String aksi, String id, String nama, String no) {
    request.token(session.getToken());
    request.aksi(aksi);
    request.id(id);
    request.nama(nama);
    request.noHp(no);

    request.theSamePelanggan(
        new RequestKelolaPelanggan.Callback() {
          @Override
          public void onRequest() {}

          @Override
          public void onKelola(List<ResponsePelanggan.Data> response) {

            if (!isEditMode && response != null && !response.isEmpty()) {
              pelangganId = response.get(0).getId();
              isEditMode = true;
            } else {
              TambahPelanggan.super.onBackPressed();
            }

            EventBus.getDefault().postSticky(new EventAddPelanggan(aksi, pelangganId));
          }

          @Override
          public void onError(String error) {
            AndroidViews.showSnackbar(TambahPelanggan.this, error);
          }
        });
  }

  private FieldPelangganModel findByKey(List<FieldPelangganModel> list, String key) {
    for (FieldPelangganModel item : list) {
      if (item.key.equals(key)) return item;
    }
    return null;
  }

  private String getValue(String key) {
    for (FieldPelangganModel field : formList) {
      if (field.key.equals(key)) {
        return field.value;
      }
    }
    return null;
  }

  private boolean isKosong(String key, String value) {
    for (FieldPelangganModel field : formList) {
      if (field.key.equals(key)) {
        return value == null || value.isEmpty() || value.equalsIgnoreCase(field.hint);
      }
    }
    return true;
  }

  private void konfirmasiHapus() {
    if (pelangganId == null) {
      AndroidViews.showSnackbar(this, "ID pelanggan tidak ditemukan");
      return;
    }

    new AlertDialog.Builder(this)
        .setTitle("Hapus Pelanggan")
        .setMessage("Yakin ingin menghapus pelanggan ini?")
        .setPositiveButton("Hapus", (dialog, which) -> crud("hapus", pelangganId, null, null))
        .setNegativeButton("Batal", null)
        .show();
  }

  @Override
  public void onBackPressed() {
    String nama = getValue("nama");
    String nomor = getValue("nomor");

    boolean isNamaKosong = isKosong("nama", nama);
    boolean isNomorKosong = isKosong("nomor", nomor);

    if (isNamaKosong && isNomorKosong) {
      super.onBackPressed();
      return;
    }

    String aksi = isEditMode ? "edit" : "tambah";
    crud(
        aksi,
        isEditMode ? pelangganId : null,
        isNamaKosong ? null : nama,
        isNomorKosong ? null : nomor);
  }
}
