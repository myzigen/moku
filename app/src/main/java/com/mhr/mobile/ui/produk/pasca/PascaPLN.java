package com.mhr.mobile.ui.produk.pasca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mhr.mobile.adapter.pager.PagerMenuPln;
import com.mhr.mobile.databinding.MenuPlnBinding;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.util.AndroidViews;

public class PascaPLN extends InjectionActivity {
  private MenuPlnBinding binding;
  private PagerMenuPln adapter;

  @Override
  protected String getTitleToolbar() {
    return "Listrik PLN";
  }

  @Override
  public View onCreateQiosView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = MenuPlnBinding.inflate(inflater, viewGroup, false);
    initEditext(binding.editText);
    setupViewPager();
    return binding.getRoot();
  }

  private void setupViewPager() {
    adapter = new PagerMenuPln(this);
    binding.viewPager2.setAdapter(adapter);
    new TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager2,
            (tab, position) -> {
              switch (position) {
                case 0:
                  tab.setText("Tagihan");
                  break;
                case 1:
                  tab.setText("Non Taglis");
                  break;
              }
            })
        .attach();
    AndroidViews.tabTooltip(binding.tabLayout);
  }
}
