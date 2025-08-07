package com.mhr.mobile.ui.navigation;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class BottomNavPagerAdapter extends FragmentStatePagerAdapter {

  private final Context context;

  // Cache fragment jika ingin reuse (opsional)
  private Fragment homeFragment;
  private Fragment riwayatFragment;
  private Fragment downlineFragment;
  private Fragment pelangganFragment;
  private Fragment akunFragment;

  public BottomNavPagerAdapter(Context ctx, FragmentManager fm) {
    super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT); // Gunakan behavior baru
    this.context = ctx;
  }

  @Override
  public int getCount() {
    return 5;
  }

  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        if (homeFragment == null) homeFragment = new NavQiosproHome();
        return homeFragment;

      case 1:
        if (riwayatFragment == null) riwayatFragment = new NavHostRiwayat();
        return riwayatFragment;

      case 2:
        if (downlineFragment == null) downlineFragment = new NavQiosproDownline();
        return downlineFragment;

      case 3:
        if (pelangganFragment == null) pelangganFragment = new NavhostPelanggan();
        return pelangganFragment;

      case 4:
        if (akunFragment == null) akunFragment = new NavQiosproAkun();
        return akunFragment;

      default:
        return new Fragment(); // fallback kosong
    }
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return ""; // Tidak digunakan jika tidak pakai tab layout
  }
}
