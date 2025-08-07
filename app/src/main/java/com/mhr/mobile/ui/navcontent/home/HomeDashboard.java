package com.mhr.mobile.ui.navcontent.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.mhr.mobile.R;
import com.mhr.mobile.api.listener.UsersDataListener;
import com.mhr.mobile.api.request.RequestUsers;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.databinding.QiosDashboardFragmentBinding;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.navcontent.akun.web.WebInjection;
import com.mhr.mobile.ui.navcontent.history.TransaksiSelesaiEvent;
import com.mhr.mobile.ui.navcontent.home.dashboard.DashboardLevel;
import com.mhr.mobile.ui.navcontent.home.dashboard.DashboardTopup;
import com.mhr.mobile.ui.navcontent.home.dashboard.DashboardTopupEvent;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.util.QiosPreferences;
import com.mhr.mobile.viewmodel.HomeViewModel;
import java.util.Calendar;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeDashboard extends InjectionFragment {
  private QiosDashboardFragmentBinding binding;
  private QiosPreferences preferences;
  private boolean hideSaldo = false;
  private HomeViewModel viewModel;

  @Override
  protected View onCreateQiosFragment(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = QiosDashboardFragmentBinding.inflate(inflater);
    preferences = new QiosPreferences(getMainActivity());
    hideSaldo = preferences.isHiddenSaldo();

    viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

    // Observe saldo dari ViewModel
    viewModel
        .getSaldo()
        .observe(
            getViewLifecycleOwner(),
            saldo -> {
              if (saldo != null) {
                binding.tvSaldo.setText(hideSaldo ? convertToAsterisks(saldo) : saldo);
              } else {
                String currentPref = preferences.getSaldo();
                binding.tvSaldo.setText(currentPref);
              }
            });

    // Set ikon awal
    binding.imgHideSaldo.setImageResource(hideSaldo ? R.drawable.ic_eye_off : R.drawable.eye_on);

    // Tombol show/hide saldo
    binding.imgHideSaldo.setOnClickListener(
        v -> {
          hideSaldo = !hideSaldo;
          preferences.setHiddenSaldo(hideSaldo);
          String saldo = viewModel.getSaldo().getValue();
          if (saldo != null) {
            binding.tvSaldo.setText(hideSaldo ? convertToAsterisks(saldo) : saldo);
          }
          binding.imgHideSaldo.setImageResource(
              hideSaldo ? R.drawable.ic_eye_off : R.drawable.eye_on);
        });

    initRecyclerview();

    // Cek apakah saldo sudah ada, jika belum panggil API
    if (viewModel.getSaldo().getValue() == null) {
      loadSaldo();
    }
    Intent i = new Intent(requireActivity(), WebInjection.class);
    i.putExtra("url", "https://sandbox.duitku.com/payment/demo/demosuccesstransaction.aspx");
    binding.tf.setOnClickListener(v -> getMainActivity().startActivity(i));
    binding.topup.setOnClickListener(v -> getMainActivity().targetActivity(DashboardTopup.class));
    binding.btnLevel.setOnClickListener(
        v -> getMainActivity().targetActivity(DashboardLevel.class));

    return binding.getRoot();
  }

  @Override
  public void onResume() {
    super.onResume();
    checkTimeAndToggleTextView();
  }

  @Override
  public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  public void onTopupSelesai(DashboardTopupEvent event) {
    loadSaldo();
    new Handler(Looper.getMainLooper())
        .postDelayed(
            () -> {
              EventBus.getDefault().removeStickyEvent(event);
            },
            200);
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  public void onTransaksiSelesai(TransaksiSelesaiEvent event) {
    loadSaldo();

    new Handler(Looper.getMainLooper())
        .postDelayed(
            () -> {
              EventBus.getDefault().removeStickyEvent(event);
            },
            200);
  }

  // Tambahkan Ke Riwayat Transaksi
  public void loadSaldo() {
    RequestUsers users = RequestUsers.with(requireActivity());
    users.Token();
    users.Nomor();
    users.GetDataUsers(
        new UsersDataListener() {
          @Override
          public void onRequest() {
            binding.tvSaldo.setText("Rp Memuat");
          }

          @Override
          public void onReceive(ResponseUsers users) {
            if (users != null && users.getData() != null) {
              String balance = FormatUtils.formatRupiah(users.getData().getSaldo());
              preferences.setSaldo(balance);
              viewModel.setSaldo(balance); // Trigger LiveData observer
            } else {
              // Bisa log error atau set saldo 0
              viewModel.setSaldo("Rp0");
            }
          }

          @Override
          public void onFailure(String error) {
            binding.tvSaldo.setText("Rp Gagal");
          }
        });
  }

  private void initRecyclerview() {
    // Inisialisasi recyclerview jika ada fitur tambahan dashboard
    // Jika tidak, bagian ini bisa dihapus atau dikosongkan
  }

  private String convertToAsterisks(String saldo) {
    if (saldo == null || saldo.isEmpty()) return "";
    int rpIndex = saldo.indexOf("Rp");
    if (rpIndex != -1) {
      String prefix = saldo.substring(0, 2); // "Rp"
      String hidden = saldo.substring(2).replaceAll("[\\d.,]", "*");
      return prefix + hidden;
    } else {
      return saldo.replaceAll("[\\d.,]", "*");
    }
  }

  @Override
  public void onDataReload() {
    loadSaldo();
  }

  private void checkTimeAndToggleTextView() {
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY); // 0 - 23
    int minute = calendar.get(Calendar.MINUTE); // 0 - 59
    binding.infoStartCutOff.setSelected(true);
    binding.infoStartCutOff.setText(
        "Transaksi Akan Pending Jika Dilakukan Pada pukul 23:00 hingga 01:00 WIB");
    // Tampilkan hanya antara jam 23:00 sampai 00:59
    if (hour == 23 && minute >= 0 && minute < 120) {
      binding.infoStartCutOff.setVisibility(View.VISIBLE);
    } else {
      binding.infoStartCutOff.setVisibility(View.GONE);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
