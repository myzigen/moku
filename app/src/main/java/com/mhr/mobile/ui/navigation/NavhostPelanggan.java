package com.mhr.mobile.ui.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.api.request.users.RequestKelolaPelanggan;
import com.mhr.mobile.api.response.ResponsePelanggan;
import com.mhr.mobile.databinding.NavHostPelangganBinding;
import com.mhr.mobile.eventbus.EventAddPelanggan;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.navcontent.kelola.pelanggan.DaftarPelanggan;
import com.mhr.mobile.ui.navcontent.kelola.pelanggan.DaftarPembeli;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class NavhostPelanggan extends InjectionFragment {
  private NavHostPelangganBinding binding;
  private int lastTotalPelanggan = -1; // penanda agar tidak call ulang
  private boolean isFirstLoad = true;

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = NavHostPelangganBinding.inflate(getLayoutInflater());
	binding.refresh.setOnRefreshListener(this::doRefresh);
    binding.btnViewPelanggan.setOnClickListener(
        view -> getMainActivity().targetActivity(DaftarPelanggan.class));

    binding.btnPembeli.setOnClickListener(
        view -> getMainActivity().targetActivity(DaftarPembeli.class));

    // Ambil sekali saat pertama kali fragment dibuka
    if (isFirstLoad) {
      ambilTotalPelanggan();
      isFirstLoad = false;
    }


    return binding.getRoot();
  }
  
  private void doRefresh(){
	  ambilTotalPelanggan();
	  binding.refresh.setRefreshing(false);
  }

  private void ambilTotalPelanggan() {
    RequestKelolaPelanggan req = RequestKelolaPelanggan.with(requireActivity());
    req.token(getSession().getToken());
    req.aksi("list");
    req.theSamePelanggan(
        new RequestKelolaPelanggan.Callback() {
          @Override
          public void onRequest() {}

          @Override
          public void onKelola(List<ResponsePelanggan.Data> response) {
            int total = response != null ? response.size() : 0;
            // Jangan update kalau jumlahnya tidak berubah
            if (total != lastTotalPelanggan) {
              lastTotalPelanggan = total;
              binding.infoTotalPelanggan.setText(String.valueOf(total));
            }
          }

          @Override
          public void onError(String error) {
            binding.infoTotalPelanggan.setText("0");
          }
        });
  }

  @Override
  public void onResume() {
    super.onResume();

    // Cek apakah ada event perubahan data pelanggan
    EventAddPelanggan event = EventBus.getDefault().getStickyEvent(EventAddPelanggan.class);
    if (event != null) {
      ambilTotalPelanggan(); // hanya ambil kalau ada event
      EventBus.getDefault().removeStickyEvent(event);
    }
  }
}
