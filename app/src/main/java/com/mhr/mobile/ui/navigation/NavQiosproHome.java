package com.mhr.mobile.ui.navigation;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.mhr.mobile.MyApp;
import com.mhr.mobile.R;
import com.mhr.mobile.api.request.cart.CartRequest;
import com.mhr.mobile.databinding.NavQiosproHomeBinding;
import com.mhr.mobile.ui.content.ContentKeranjang;
import com.mhr.mobile.ui.content.ContentNotification;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.navcontent.home.HomeDashboard;
import com.mhr.mobile.ui.navcontent.home.HomeFlashsale;
import com.mhr.mobile.ui.navcontent.home.HomeKategori;
import com.mhr.mobile.ui.navcontent.home.HomeMarketplace;
import com.mhr.mobile.viewmodel.HomeViewModel;
import com.mhr.mobile.widget.WidgetLayout;
import com.onesignal.OneSignal;
import com.onesignal.inAppMessages.IInAppMessageClickEvent;
import com.onesignal.inAppMessages.IInAppMessageClickListener;

public class NavQiosproHome extends InjectionFragment {
  private NavQiosproHomeBinding binding;
  private boolean isConnected = false;
  private boolean isDataLoaded = false;
  private boolean hasAnimatedMenu = false;
  private HomeViewModel homeViewModel;
  private BadgeDrawable badgeDrawable; // disimpan sebagai field agar tidak lepas

  @Override
  public View onCreateQiosFragment(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = NavQiosproHomeBinding.inflate(getLayoutInflater());
    homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

    setupToolbar();
    OneSignal.getInAppMessages()
        .addClickListener(
            new IInAppMessageClickListener() {
              @Override
              public void onClick(@Nullable IInAppMessageClickEvent event) {
                // Log.v(Tag.LOG_TAG, "INotificationClickListener.inAppMessageClicked");
                // startActivity(new Intent(MainActivity.this, ContentNotification.class));
              }
            });
    initRefresh();
    return binding.getRoot();
  }

  private void initRefresh() {
    binding.refresh.setOnRefreshListener(
        () -> {
          homeViewModel.setIsRefreshing(true);
          onDataReload();
          binding.refresh.postDelayed(() -> homeViewModel.setIsRefreshing(false), 1500);
        });

    homeViewModel
        .getIsRefreshing()
        .observe(
            getViewLifecycleOwner(),
            isRefreshing -> {
              binding.refresh.setRefreshing(isRefreshing);
            });
  }

  private void setupToolbar() {
    ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
    binding.toolbar.setTitle(getActivity().getString(R.string.app_name));
    binding.toolbar.setOnMenuItemClickListener(
        item -> {
          if (item.getItemId() == R.id.action_cart) {
            Intent i = new Intent(requireActivity(), ContentKeranjang.class);
            getMainActivity().abStartActivity(i);
            return true;
          } else if (item.getItemId() == R.id.action_notification) {
            Intent i = new Intent(requireActivity(), ContentNotification.class);
            getMainActivity().abStartActivity(i);
            return true;
          }
          return false;
        });

    WidgetLayout.with(binding.nestedscroll)
        .scrollParallax(binding.viewSlow, binding.appbar, binding.viewFast);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.main_menu, menu);

    if (!hasAnimatedMenu && menu != null) {
      for (int i = 0; i < menu.size(); i++) {
        Drawable drawable = menu.getItem(i).getIcon();
        if (drawable instanceof Animatable) {
          ((Animatable) drawable).start();
        }
      }
      hasAnimatedMenu = true;
    }
    badgeDrawable = BadgeDrawable.create(requireActivity());
    new CartRequest(requireActivity())
        .requestGetCart(
            response -> {
              int count = response.cart.size();
              if (count > 0) {
                badgeDrawable.setNumber(count);
                badgeDrawable.setVisible(true);
              } else {
                badgeDrawable.clearNumber();
                badgeDrawable.setVisible(false);
              }
              BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.toolbar, R.id.action_cart);
            });
  }

  @Override
  public void onDataReload() {
    FragmentManager fm = getChildFragmentManager();

    HomeDashboard homeDashboard = (HomeDashboard) fm.findFragmentById(R.id.fragment_dashboard);
    if (homeDashboard != null) {
      homeDashboard.onDataReload();
    }

    HomeKategori homeKategori = (HomeKategori) fm.findFragmentById(R.id.fragment_menu);
    if (homeKategori != null) {
      homeKategori.onDataReload();
    }

    HomeFlashsale flashsale = (HomeFlashsale) fm.findFragmentById(R.id.fragment_flashsale);
    if (flashsale != null) {
      flashsale.onDataReload();
    }

    HomeMarketplace marketplace = (HomeMarketplace) fm.findFragmentById(R.id.fragment_marketplace);
    if (marketplace != null) {
      marketplace.onDataReload();
    }
  }

  @Override
  public void onResume() {
    super.onResume();

    MyApp.setCartUpdate(
        totalItems -> {
          if (badgeDrawable != null) {
            if (totalItems > 0) {
              badgeDrawable.setNumber(totalItems);
              badgeDrawable.setVisible(true);
            } else {
              badgeDrawable.setVisible(false);
            }
          }
        });

    // Juga update saat resume awal
    /*
       new CartRequest(requireActivity())
           .requestGetCart(
               response -> {
                 int count = response.cart.size();
                 if (badgeDrawable != null) {
                   if (count > 0) {
                     badgeDrawable.setNumber(count);
                     badgeDrawable.setVisible(true);
                   } else {
                     badgeDrawable.setVisible(false);
                   }
                 }
               });
    */
  }

  @Override
  public void onPause() {
    super.onPause();
    MyApp.setCartUpdate(null);
  }
}
