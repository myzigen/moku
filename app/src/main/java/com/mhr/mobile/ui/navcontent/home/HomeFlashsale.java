package com.mhr.mobile.ui.navcontent.home;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mhr.mobile.api.listener.MokuListCallback;
import com.mhr.mobile.api.request.RequestPricelist;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.LayoutFlashsaleBinding;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.produk.adapter.FlashsaleAdapter;
import com.mhr.mobile.widget.recyclerview.SpacingItemHorizontal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HomeFlashsale extends InjectionFragment {
  private LayoutFlashsaleBinding binding;
  private FlashsaleAdapter adapter;
  private CountDownTimer globalTimer;

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = LayoutFlashsaleBinding.inflate(i, v, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    loadFlashsale();
  }

  private void loadFlashsale() {
    binding.recyclerview.addItemDecoration(new SpacingItemHorizontal(14));
    binding.recyclerview.setLayoutManager(
        new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
    adapter = new FlashsaleAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);

    RequestPricelist data = new RequestPricelist(requireActivity());
    data.flashsaleAktif(
        new MokuListCallback<ResponsePricelist>() {
          @Override
          public void onEvent() {}

          @Override
          public void onDataValue(List<ResponsePricelist> data) {
            if (data.isEmpty()) {
              binding.root.setVisibility(View.GONE);
            } else {
              binding.root.setVisibility(View.VISIBLE);
              adapter.setData(data);

              // Ambil waktu berakhir paling akhir dari seluruh flashsale
              long minEndTimeMillis = getMinEndTime(data);
              startGlobalCountdown(minEndTimeMillis);
            }
          }

          @Override
          public void onError(String error) {
            // showSnackbar(error);
          }
        });
  }

  private long getMinEndTime(List<ResponsePricelist> data) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));

    long now = System.currentTimeMillis();
    long minEndTime = Long.MAX_VALUE;

    for (ResponsePricelist item : data) {
      try {
        Date end = sdf.parse(item.getEndTime());
        if (end != null && end.getTime() > now) {
          minEndTime = Math.min(minEndTime, end.getTime());
        }
      } catch (Exception ignored) {
      }
    }

    return minEndTime;
  }

  private void startGlobalCountdown(long endMillis) {
    if (globalTimer != null) globalTimer.cancel();

    long now = System.currentTimeMillis();
    long diff = endMillis - now;

    if (diff > 0) {
      globalTimer =
          new CountDownTimer(diff, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
              long seconds = millisUntilFinished / 1000;
              long hours = seconds / 3600;
              long minutes = (seconds % 3600) / 60;
              long secs = seconds % 60;

              binding.tvHour.setText(String.format("%02d", hours));
              binding.tvMinute.setText(String.format("%02d", minutes));
              binding.tvSecond.setText(String.format("%02d", secs));
            }

            @Override
            public void onFinish() {
              binding.tvHour.setText("00");
              binding.tvMinute.setText("00");
              binding.tvSecond.setText("00");
            }
          }.start();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
	if (globalTimer != null) globalTimer.cancel();
    binding = null;
  }
  
  @Override
  public void onDataReload() {
	  loadFlashsale();
  }
}
