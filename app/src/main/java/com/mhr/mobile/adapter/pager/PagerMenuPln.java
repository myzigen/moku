package com.mhr.mobile.adapter.pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.mhr.mobile.ui.produk.ProdukPLNPasca;

public class PagerMenuPln extends FragmentStateAdapter {

  public PagerMenuPln(FragmentActivity activity) {
    super(activity);
  }

  @Override
  public int getItemCount() {
    return 2;
  }

  @Override
  public Fragment createFragment(int position) {
    switch (position) {
      case 0:
        return new ProdukPLNPasca();
      case 1:
        return new ProdukPLNPasca();
      default:
        return new ProdukPLNPasca();
    }
  }
}
