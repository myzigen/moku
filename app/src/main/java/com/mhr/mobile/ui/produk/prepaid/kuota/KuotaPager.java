package com.mhr.mobile.ui.produk.prepaid.kuota;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mhr.mobile.adapter.pager.PagerMenuData;
import com.mhr.mobile.databinding.MenuDataBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidViews;

public class KuotaPager extends InjectionActivity {
  private MenuDataBinding binding;
  private PagerMenuData adapter;

  @Override
  protected String getTitleToolbar() {
    return "Paket Data";
  }

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuDataBinding.inflate(layoutInflater, viewGroup, false);
    setupViewPager();
    return binding.getRoot();
  }

  private void setupViewPager() {
    adapter = new PagerMenuData(this);
    binding.viewPager2.setAdapter(adapter);
    new TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager2,
            (tab, position) -> {
              switch (position) {
                case 0:
                  tab.setText("Regular");
                  break;
                case 1:
                  tab.setText("Voucher Fisik");
                  break;
              }
            })
        .attach();
    AndroidViews.tabTooltip(binding.tabLayout);
  }
}
