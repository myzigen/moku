package com.mhr.mobile.ui.inject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.mhr.mobile.databinding.InjectionFragmentBinding;
import com.mhr.mobile.interfaces.ConnectivityListener;
import com.mhr.mobile.ui.MainActivity;
import com.mhr.mobile.ui.intro.UserSession;
import com.mhr.mobile.util.QiosPreferences;
import com.mhr.mobile.viewmodel.CacheViewModel;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;

public abstract class InjectionFragment extends Fragment implements ConnectivityListener {
  private InjectionFragmentBinding binding;
  private ViewGroup fragmentContainer;
  private InjectionConnectivity injectionConnectivity;
  private SpacingItemDecoration spacingItemDecoration;
  private LinearLayoutManager linearLayoutManager;
  private GridLayoutManager gridLayoutManager;
  private View applyPay;
  protected Context safeContext;
  protected MainActivity safeActivity;
  private QiosPreferences pref;
  protected CacheViewModel cacheViewModel;
  private UserSession session;

  public MainActivity getMainActivity() {
    return safeActivity;
  }

  public UserSession getSession() {
    if (session == null && getActivity() != null) {
      session = UserSession.with(getActivity());
    }
    return session;
  }

  public QiosPreferences getPreferences() {
    if (pref == null && getContext() != null) {
      pref = new QiosPreferences(getContext());
    }
    return pref;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    safeContext = context;
    if (context instanceof MainActivity) {
      safeActivity = (MainActivity) context;
    }
    try {
      injectionConnectivity = (InjectionConnectivity) context;
    } catch (ClassCastException e) {
      throw new RuntimeException(
          context.getClass().getSimpleName()
              + " must be an instance of "
              + InjectionConnectivity.class.getSimpleName());
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    injectionConnectivity = null;
    safeContext = null;
    safeActivity = null;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    injectionConnectivity.onFragmentRemove(this);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true); // Agar bisa menampilkan menu di fragment
  }

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = InjectionFragmentBinding.inflate(i, v, false);
    View contentFragment = onCreateQiosFragment(i, v, b);
    binding.fragmentContainer.addView(contentFragment);
    applyPay = binding.pay.containerBottom;
    applyIncludePembayaran(applyPay);

    cacheViewModel = new ViewModelProvider(requireActivity()).get(CacheViewModel.class);

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    injectionConnectivity.onFragmentAttached(this);
  }

  protected abstract View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b);

  protected void applyIncludePembayaran(View includeView) {}

  protected SpacingItemDecoration getSpacingDecoration(int column, int count, boolean spacing) {
    return spacingItemDecoration = new SpacingItemDecoration(column, count, spacing);
  }

  protected LinearLayoutManager getLinearLayoutManager() {
    return linearLayoutManager = new LinearLayoutManager(getActivity());
  }

  protected GridLayoutManager getGridLayoutManager(int columnt) {
    return gridLayoutManager = new GridLayoutManager(getActivity(), columnt);
  }

  protected void goToActivity(Class<?> targetActivity) {
    startActivity(new Intent(getMainActivity(), targetActivity));
  }

  @Override
  public void onNetworkConnected(boolean isConnected) {}

  @Override
  public void onDataReload() {
    // Override di aktivitas atau fragmen untuk reload data
  }
}
