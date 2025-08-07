package com.mhr.mobile.ui.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mhr.mobile.adapter.pager.PagerHistory;
import com.mhr.mobile.databinding.NavQiosproTransaksiBinding;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.util.AndroidViews;

public class NavQiosproTransaksi extends InjectionFragment {
  private NavQiosproTransaksiBinding binding;
  private PagerHistory pagerHistory;

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = NavQiosproTransaksiBinding.inflate(getLayoutInflater());
    binding.refresh.setOnClickListener(
        vi -> {
          onDataReload();
        });

    setupViewPager();

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void setupViewPager() {
    pagerHistory = new PagerHistory(getMainActivity());
    binding.viewpager.setAdapter(pagerHistory);
    // binding.viewpager.setOffscreenPageLimit(pagerHistory.getItemCount());
    new TabLayoutMediator(
            binding.tabLayout,
            binding.viewpager,
            (tab, position) -> {
              switch (position) {
                case 0:
                  tab.setText("PRABAYAR");
                  break;
                case 1:
                  tab.setText("PASCA");
                  break;
                case 2:
                  tab.setText("DEPOSIT");
                  break;
                case 3:
                  tab.setText("SEMUA");
                  break;
              }
            })
        .attach();
    AndroidViews.tabTooltip(binding.tabLayout);
  }

  @Override
  public void onDataReload() {
    int fragmentCount = pagerHistory.getItemCount();
    for (int i = 0; i < fragmentCount; i++) {
      Fragment fragment = pagerHistory.getFragment(i);
      // if (fragment != null && fragment.isAdded() && fragment instanceof HistoryTransaksi) {
      //  ((HistoryTransaksi) fragment).initHistoryTransaksi();
      // }
    }
  }
}
