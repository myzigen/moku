package com.mhr.mobile.ui.navcontent.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mhr.mobile.adapter.pager.QiosKategoriPager;
import com.mhr.mobile.databinding.QiosKategoriMenuBinding;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.navcontent.home.kategori.KategoriPager;
import com.mhr.mobile.util.AndroidViews;

public class HomeKategori extends InjectionFragment {
  private QiosKategoriMenuBinding binding;
  private QiosKategoriPager pager;

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = QiosKategoriMenuBinding.inflate(i, v, false);

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View arg0, Bundle arg1) {
    super.onViewCreated(arg0, arg1);
    pager = new QiosKategoriPager(getMainActivity());
    binding.viewpager2.setAdapter(pager);
    binding.viewpager2.setOffscreenPageLimit(pager.getItemCount());
    initViewpager();
  }

  private void initViewpager() {
    new TabLayoutMediator(
            binding.tablayout,
            binding.viewpager2,
            (tab, position) -> {
              switch (position) {
                case 0:
                  tab.setText("Favorite");
                  break;
                case 1:
                  tab.setText("Layanan");
                  break;
                case 2:
                  tab.setText("Konter");
                  break;
                case 3:
                  tab.setText("Lainnya");
                  break;
              }
            })
        .attach();

    AndroidViews.tabTooltip(binding.tablayout);
    binding.dotsIndicator.setupWithViewPager(binding.viewpager2);
  }

  @Override
  public void onDataReload() {
    if (pager != null) {
      // Iterasi melalui semua halaman dan panggil onDataReload jika diperlukan
      for (int i = 0; i < pager.getItemCount(); i++) {
        Fragment fragment = pager.getFragmentAt(i);
        if (fragment instanceof KategoriPager) {
          ((KategoriPager) fragment).onDataReload();
        }
      }
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
