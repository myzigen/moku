package com.mhr.mobile.ui.navigation;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.api.listener.UsersDataListener;
import com.mhr.mobile.api.request.RequestUsers;
import com.mhr.mobile.api.response.ResponseUsers;
import com.mhr.mobile.databinding.NavQiosproDownlineBinding;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.navcontent.downline.adapter.DownlineAdapter;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.widget.tooltip.ToolTip;
import com.mhr.mobile.widget.tooltip.ToolTipsManager;
import java.util.ArrayList;
import java.util.List;

public class NavQiosproDownline extends InjectionFragment implements ToolTipsManager.TipListener {
  private NavQiosproDownlineBinding binding;
  private ToolTipsManager manager;
  private DownlineAdapter adapter;

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = NavQiosproDownlineBinding.inflate(getLayoutInflater());
    initUi();

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initRecycler(); // ✅ Panggil setelah cacheViewModel pasti siap
  }

  private void initUi() {
    manager = new ToolTipsManager(this);
    String nama = getSession().getNama();
    if (nama == null) nama = "";
    String inisial = "";
    int maxChar = 20;

    if (nama != null && !nama.isEmpty()) {
      inisial = nama.substring(0, 1).toUpperCase();
    }

    String result = nama.length() > maxChar ? nama.substring(0, maxChar) + "..." : nama;
    binding.initial.setText(inisial);
    binding.infoUpline.setText(result);
    binding.infoReferral.setPaintFlags(
        binding.infoReferral.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    binding.refresh.setOnRefreshListener(this::doRefresh);
  }

  private void initRecycler() {
    binding.recyclerview.setLayoutManager(getLinearLayoutManager());
    adapter = new DownlineAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setClickItemListener(p -> {});

    loadReferral();
  }

  private void doRefresh() {
    loadReferral();
    binding.refresh.setRefreshing(false);
  }

  private void loadReferral() {
    RequestUsers users = RequestUsers.with(requireActivity());
    users.Token();
    users.Nomor();
    users.GetDataUsers(
        new UsersDataListener() {
          @Override
          public void onRequest() {}

          @Override
          public void onReceive(ResponseUsers users) {
            if (users != null && users.getData() != null) {
              List<ResponseUsers.Downline> downlineList = users.getData().getDownline();
              if (downlineList != null && !downlineList.isEmpty()) {
                adapter.updateDownlineAdapter(users.getData().getDownline());
                binding.noDownline.setVisibility(View.GONE);
              } else {
                binding.noDownline.setVisibility(View.VISIBLE);
              }

              binding.infoReferral.setText(users.getData().getReferralCode());
              binding.infoReferral.setOnClickListener(v -> copy(v));
              binding.infoTotalDownline.setText(String.valueOf(users.data.total_downline));
              getPreferences().saveTotalDownline(users.data.total_downline);
            } else {
              // binding.incError.root.setVisibility(View.VISIBLE);
            }
          }

          @Override
          public void onFailure(String error) {}
        });
  }

  private void copy(View v) {
    String copyNomor = binding.infoReferral.getText().toString();
    AndroidViews.copyToClipboard(requireActivity(), copyNomor, "Berhasil Di Salin", v);
    tooltip();
  }

  private void tooltip() {
    ToolTip.Builder builder =
        new ToolTip.Builder(
            requireActivity(),
            binding.infoReferral,
            binding.getRoot(),
            "Berhasil di salin",
            ToolTip.POSITION_BELOW);
    manager.show(builder.build());
    new Handler(Looper.getMainLooper())
        .postDelayed(
            () -> {
              manager.findAndDismiss(binding.infoReferral);
            },
            2000);

    // manager.dismissAll();
  }

  @Override
  public void onTipDismissed(View view, int anchorViewId, boolean byUser) {
    // Log.d(TAG, "tip near anchor view " + anchorViewId + " dismissed");

    // if (anchorViewId == R.id.text_view) {
    // Do something when a tip near view with id "R.id.text_view" has been dismissed
    // }
  }
}
