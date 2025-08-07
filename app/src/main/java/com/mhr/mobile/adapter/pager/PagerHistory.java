package com.mhr.mobile.adapter.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.HashMap;

public class PagerHistory extends FragmentStateAdapter {
  private String[] kategori = {"PRABAYAR", "PASCABAYAR", "DEPOSIT", "SEMUA"};
  private final HashMap<Integer, Fragment> fragmentMap = new HashMap<>();

  public PagerHistory(@NonNull FragmentActivity fragmentActivity) {
    super(fragmentActivity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    if (!fragmentMap.containsKey(position)) {
      // Fragment fragment = HistoryTransaksi.newInstance(kategori[position]);
      // fragmentMap.put(position, fragment);
    }
    return fragmentMap.get(position);
  }

  public Fragment getFragment(int position) {
    return fragmentMap.get(position);
  }

  @Override
  public int getItemCount() {
    return kategori.length;
  }
}
