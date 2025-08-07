package com.mhr.mobile.ui.navcontent.kelola.pelanggan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.api.request.users.RequestKelolaPelanggan;
import com.mhr.mobile.api.response.ResponsePelanggan;
import com.mhr.mobile.databinding.UserListPelangganBinding;
import com.mhr.mobile.eventbus.EventAddPelanggan;
import com.mhr.mobile.ui.inject.InjectionActivity;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class DaftarPelanggan extends InjectionActivity {
  private UserListPelangganBinding binding;
  private PelangganAdapter adapter;
  private List<ResponsePelanggan.Data> mData;

  @Override
  protected String getTitleToolbar() {
    return "Daftar Pelanggan";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = UserListPelangganBinding.inflate(getLayoutInflater());
    initUi();
    return binding.getRoot();
  }

  private void initUi() {
    binding.btnAddPelanggan.setOnClickListener(v -> targetActivity(TambahPelanggan.class));
    binding.incError.retry.setOnClickListener(v -> loadDaftarPelanggan());
    initRecycler();
    if (!cacheViewModel.cachePelanggan) {
      loadDaftarPelanggan();
      cacheViewModel.cachePelanggan = true;
    }
  }

  private void initRecycler() {
    // binding.recyclerview.addItemDecoration(getSpacingItemDecoration(1, 50, true));
    binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    adapter = new PelangganAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnItemClickListener(item -> sendData(item));
  }

  private void sendData(ResponsePelanggan.Data data) {
    Intent i = new Intent(this, TambahPelanggan.class);
    i.putExtra("id", data.getId());
    i.putExtra("nama", data.getNama());
    i.putExtra("nomor", data.getNoHp());
    i.putExtra("edit_mode", true);
    startActivity(i);
  }

  private void loadDaftarPelanggan() {
    binding.incError.root.setVisibility(View.GONE);
    RequestKelolaPelanggan req = RequestKelolaPelanggan.with(this);
    req.token(session.getToken());
    req.aksi("list");
    req.theSamePelanggan(
        new RequestKelolaPelanggan.Callback() {

          @Override
          public void onRequest() {
            // binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onKelola(List<ResponsePelanggan.Data> response) {
            mData = response;
            adapter.updateAdapter(response);
            cekDataKosong();
            // binding.recyclerview.hideShimmerAdapter();
          }

          @Override
          public void onError(String error) {
            // binding.recyclerview.hideShimmerAdapter();
            binding.incError.root.setVisibility(View.VISIBLE);
            binding.incError.infoError.setText(error);
          }
        });
  }

  private void cekDataKosong() {
    binding.incError.retry.setVisibility(View.GONE);
    if (mData == null || mData.isEmpty()) {
      binding.incError.root.setVisibility(View.VISIBLE);
      binding.incError.infoError.setText("Belum Ada Pelanggan");
    } else {
      binding.incError.root.setVisibility(View.GONE);
    }
  }

  private void hapusDariList(String idPelanggan) {
    if (idPelanggan == null) return;
    for (int i = 0; i < mData.size(); i++) {
      ResponsePelanggan.Data pelanggan = mData.get(i);
      if (pelanggan.getId().equals(idPelanggan)) {
        mData.remove(i);
        adapter.notifyItemRemoved(i);
        break;
      }
    }

    cekDataKosong();
  }

  @Override
  public void onResume() {
    super.onResume();
    EventAddPelanggan event = EventBus.getDefault().getStickyEvent(EventAddPelanggan.class);
    if (event != null && event.aksi != null) {
      switch (event.aksi) {
        case "tambah":
        case "edit":
          EventBus.getDefault().removeStickyEvent(event);
          loadDaftarPelanggan();
          break;
        case "hapus":
          EventBus.getDefault().removeStickyEvent(event);
          hapusDariList(event.id);
          break;
      }
    }
  }
}
