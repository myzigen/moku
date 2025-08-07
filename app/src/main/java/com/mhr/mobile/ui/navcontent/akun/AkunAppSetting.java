package com.mhr.mobile.ui.navcontent.akun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mhr.mobile.databinding.UserSettingAppBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.DeleteCache;

public class AkunAppSetting extends InjectionActivity {
  private UserSettingAppBinding binding;

  @Override
  protected String getTitleToolbar() {
    return "Pengaturan";
  }

  @Override
  public View onCreateQiosView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = UserSettingAppBinding.inflate(inflater, container, false);
    setupUI();
    return binding.getRoot();
  }

  private void setupUI() {
    int currentTheme = pref.getThemeMode();
    binding.btnTheme.setOnClickListener(v -> showThemeDialog(pref.getThemeMode()));
    updateCacheSize();

    binding.btnClearCache.setOnClickListener(
        v -> {
          new MaterialAlertDialogBuilder(this)
              .setTitle("Hapus Cache")
              .setPositiveButton(
                  "Hapus",
                  (dialog, which) -> {
                    DeleteCache.clearCache(this);
                    updateCacheSize(); // Refresh
                  })
              .setNegativeButton("Batal", null)
              .show();
        });
  }

  private void showThemeDialog(int currentTheme) {
    String[] themes = {"Ikuti Sistem", "Terang", "Gelap"};
    int checkedItem;

    switch (currentTheme) {
      case AppCompatDelegate.MODE_NIGHT_NO:
        checkedItem = 1;
        break;
      case AppCompatDelegate.MODE_NIGHT_YES:
        checkedItem = 2;
        break;
      case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
      default:
        checkedItem = 0;
        break;
    }

    new MaterialAlertDialogBuilder(this)
        .setTitle("Pilih Mode Tema")
        .setSingleChoiceItems(themes, checkedItem, null)
        .setPositiveButton(
            "Pilih",
            (dialog, which) -> {
              int selected =
                  ((androidx.appcompat.app.AlertDialog) dialog)
                      .getListView()
                      .getCheckedItemPosition();
              int selectedTheme;

              switch (selected) {
                case 1:
                  selectedTheme = AppCompatDelegate.MODE_NIGHT_NO;
                  break;
                case 2:
                  selectedTheme = AppCompatDelegate.MODE_NIGHT_YES;
                  break;
                case 0:
                default:
                  selectedTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                  break;
              }

              // Simpan dan terapkan
              pref.setThemeMode(selectedTheme);
              AppCompatDelegate.setDefaultNightMode(selectedTheme);
            })
        .setNegativeButton("Batal", (dialog, which) -> {})
        .show();
  }

  private void updateCacheSize() {
    long cacheSize =
        DeleteCache.getDirSize(getCacheDir()) + DeleteCache.getDirSize(getExternalCacheDir());
    String formattedSize = android.text.format.Formatter.formatShortFileSize(this, cacheSize);
    binding.subtitleCache.setText(formattedSize); // Ganti tvCacheSize sesuai ID TextView kamu
  }
}
