package com.mhr.mobile.adapter.pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.mhr.mobile.ui.produk.prepaid.kuota.KuotaPagerContent;
import java.util.HashMap;

public class PagerMenuData extends FragmentStateAdapter {

  private String[] kategori = {"Data", "Voucher"};
  private final HashMap<Integer, Fragment> fragmentMap = new HashMap<>();

  public PagerMenuData(FragmentActivity activity) {
    super(activity);
  }

  @Override
  public int getItemCount() {
    return kategori.length; // Total jumlah halaman
  }

  @Override
  public Fragment createFragment(int position) {
    if (!fragmentMap.containsKey(position)) {
      Fragment fragment = KuotaPagerContent.newInstance(kategori[position]);
      fragmentMap.put(position, fragment);
    }
    return fragmentMap.get(position);
  }

  public Fragment getFragmentAt(int position) {
    return fragmentMap.get(position); // Mengembalikan fragment dari fragmentMap
  }
}
