package com.mhr.mobile.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mhr.mobile.R;
import com.mhr.mobile.api.listener.MokuCallback;
import com.mhr.mobile.api.request.RequestApp;
import com.mhr.mobile.api.response.ResponsePromosi;
import com.mhr.mobile.databinding.ActivityMainBinding;
import com.mhr.mobile.ui.dialog.PopupDialog;
import com.mhr.mobile.ui.inject.InjectionActivity;
import com.mhr.mobile.ui.navigation.BottomNavPagerAdapter;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.widget.viewpager.GestureControlViewPager;

public class MainActivity extends InjectionActivity implements ViewPager.OnPageChangeListener {
  private ActivityMainBinding binding;
  private GestureControlViewPager viewPager;
  private BottomNavPagerAdapter adapter;
  private BottomNavigationView bottomNavigationView;
  private MenuItem prevMenuItem;
  private long confirmBackPress;
  private Toast toast;

  @Override
  public View onCreateQiosView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
    transitionSharedAxis(R.id.root);
    binding = ActivityMainBinding.inflate(layoutInflater, viewGroup, false);
    // ID
    appbar.setVisibility(View.GONE);
    viewPager = binding.viewpager;
    bottomNavigationView = binding.bottomNavigationMenu;
    initViewPager();

    RequestApp request = new RequestApp(this);
    request.popupRequest(
        new MokuCallback<ResponsePromosi>() {

          @Override
          public void onDataLoading() {}

          @Override
          public void onDataValue(ResponsePromosi data) {
            if ("show".equalsIgnoreCase(data.data.status)) {
              if (!MainActivity.this.isFinishing() && !MainActivity.this.isDestroyed()) {
                PopupDialog dialog = new PopupDialog();
                dialog.setImagePopup(data.data.image_url);

                // Pastikan FragmentManager tidak destroyed
                if (getSupportFragmentManager() != null
                    && !getSupportFragmentManager().isDestroyed()) {
                  dialog.show(getSupportFragmentManager(), "Popup");
                }
              }
            }
          }

          @Override
          public void onDataError(String error) {}
        });
    return binding.getRoot();
  }

  private void initViewPager() {
    adapter = new BottomNavPagerAdapter(this, getSupportFragmentManager());
    viewPager.isSwipeGestureEnabled = false;
    viewPager.setAdapter(adapter);
    viewPager.setOffscreenPageLimit(4);
    bottomNavigationView.setItemIconTintList(null);
    bottomNavigationView.setOnNavigationItemSelectedListener(navigationSelectedListener);
    viewPager.addOnPageChangeListener(this);
    hideToltipBottomNav();
  }

  private void hideToltipBottomNav() {
    for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
      bottomNavigationView
          .findViewById(bottomNavigationView.getMenu().getItem(i).getItemId())
          .setOnLongClickListener(
              new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                  return true;
                }
              });
    }
  }

  @Override
  public void onPageScrolled(int arg0, float arg1, int arg2) {}

  @Override
  public void onPageSelected(int position) {
    if (bottomNavigationView != null) {
      if (prevMenuItem != null) {
        prevMenuItem.setChecked(false); // Menonaktifkan item sebelumnya
      } else {
        bottomNavigationView
            .getMenu()
            .getItem(0)
            .setChecked(false); // Menonaktifkan item pertama jika belum ada item sebelumnya
      }
      bottomNavigationView
          .getMenu()
          .getItem(position)
          .setChecked(true); // Menandai item yang dipilih
      prevMenuItem = bottomNavigationView.getMenu().getItem(position); // Menyimpan item yang aktif
    }
  }

  @Override
  public void onPageScrollStateChanged(int arg0) {}

  private BottomNavigationView.OnNavigationItemSelectedListener navigationSelectedListener =
      item -> {
        switch (item.getItemId()) {
          case R.id.nav_home:
            if (viewPager.getCurrentItem() != 0) {
              viewPager.setCurrentItem(0);
            }
            return true;
          case R.id.nav_transaksi:
            if (viewPager.getCurrentItem() != 1) {
              viewPager.setCurrentItem(1);
            }
            return true;
          case R.id.nav_downline:
            if (viewPager.getCurrentItem() != 2) {
              viewPager.setCurrentItem(2);
            }
            return true;
          case R.id.nav_pelanggan:
            if (viewPager.getCurrentItem() != 3) {
              viewPager.setCurrentItem(3);
            }
            return true;
          case R.id.nav_account:
            if (viewPager.getCurrentItem() != 4) {
              viewPager.setCurrentItem(4);
            }
            return true;
        }
        return false;
      };

  public void getCurrentDownline() {
    if (viewPager.getCurrentItem() != 2) {
      viewPager.setCurrentItem(2);
    }
  }

  @Override
  public void onBackPressed() {
    if (confirmBackPress + 2000 > System.currentTimeMillis()) {
      if (toast != null) toast.cancel();
      super.onBackPressed();
      return;
    } else {
      toast = Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT);
      toast.show();
    }
    confirmBackPress = System.currentTimeMillis();
  }

  @Override
  public void onNetworkConnected(boolean isConnected) {
    if (isConnected) {
      onDataReload();
      AndroidViews.showSnackbar(this, "Jaringan dipulihkan");
    } else {
      AndroidViews.showSnackbar(this, "Cie gapunya kuota");
    }
  }

  @Override
  public void onDataReload() {}
}
