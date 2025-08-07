package com.mhr.mobile.ui.sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.databinding.SheetKalendarBinding;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.util.QiosPreferences;
import java.util.Calendar;

public class SheetKalendar extends InjectionSheetFragment {
  private SheetKalendarBinding binding;
  private QiosPreferences pref;
  private boolean isReminderActive = false;
  private OnTanggalDipilihListener tanggalListener;

  public interface OnTanggalDipilihListener {
    void onTanggalDipilih(String tanggal);
  }

  public void setOnTanggalDipilihListener(OnTanggalDipilihListener listener) {
    this.tanggalListener = listener;
  }

  @Override
  protected String getSheetTitle() {
    return "Tambah Pengingat";
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetKalendarBinding.inflate(getLayoutInflater());
    pref = new QiosPreferences(requireActivity());
    boolean reminderActive = pref.getBoolean("reminder_enabled", false);

    // Listener saat user memilih tanggal
    binding.kalendar.setOnDateSelectedListener(
        (year, month, day) -> {
          Calendar now = Calendar.getInstance();

          Calendar picked = Calendar.getInstance();
          picked.set(Calendar.YEAR, year);
          picked.set(Calendar.MONTH, month);
          picked.set(Calendar.DAY_OF_MONTH, day);
          picked.set(Calendar.SECOND, 0);
          picked.set(Calendar.MILLISECOND, 0);

          // Jika tanggal yang dipilih sudah lewat, jadikan bulan depan
          if (picked.before(now)) {
            picked.add(Calendar.MONTH, 1);
          }

          // Simpan ke preferences
          String tanggalDipilih =
              picked.get(Calendar.DAY_OF_MONTH)
                  + "/"
                  + (picked.get(Calendar.MONTH) + 1)
                  + "/"
                  + picked.get(Calendar.YEAR);
          pref.setString("tanggal_internet", tanggalDipilih);
          if (tanggalListener != null) {
            tanggalListener.onTanggalDipilih(String.valueOf(picked.get(Calendar.DAY_OF_MONTH)));
          }
          // Highlight kalender
          binding.kalendar.setSelectedDate(
              picked.get(Calendar.YEAR),
              picked.get(Calendar.MONTH),
              picked.get(Calendar.DAY_OF_MONTH));
        });

    // Ambil tanggal dari SharedPreferences
    String tanggal = pref.getString("tanggal_internet");
    int day, month, year;

    if (!tanggal.isEmpty() && tanggal.contains("/")) {
      String[] split = tanggal.split("/");
      try {
        day = Integer.parseInt(split[0]);
        month = Integer.parseInt(split[1]) - 1;
        year = Integer.parseInt(split[2]);
      } catch (NumberFormatException e) {
        // Fallback ke tanggal 1 bulan depan
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        day = 1;
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
      }
    } else {
      // Belum ada pilihan → default: tanggal 1 bulan depan
      Calendar cal = Calendar.getInstance();
      cal.add(Calendar.MONTH, 1);
      day = 1;
      month = cal.get(Calendar.MONTH);
      year = cal.get(Calendar.YEAR);
    }

    binding.kalendar.setSelectedDate(year, month, day);
    binding.btnTerapkanReminder.setOnClickListener(v -> dismiss());
    return binding.getRoot();
  }

  public void setReminderActive(boolean active) {
    this.isReminderActive = active;
  }
}
