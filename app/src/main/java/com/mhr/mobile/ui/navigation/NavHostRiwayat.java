package com.mhr.mobile.ui.navigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.R;
import com.mhr.mobile.api.request.RequestHistory;
import com.mhr.mobile.api.response.ResponseHistory;
import com.mhr.mobile.databinding.NavHostRiwayatBinding;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.navcontent.history.TransaksiSelesaiEvent;
import com.mhr.mobile.ui.navcontent.history.history.HistoryPagingAdapter;
import com.mhr.mobile.ui.navcontent.home.dashboard.DashboardTopupEvent;
import com.mhr.mobile.ui.status.StatusLayoutCallback;
import com.mhr.mobile.ui.status.StatusTopup;
import com.mhr.mobile.widget.recyclerview.PaginationAdapterCallback;
import com.mhr.mobile.widget.recyclerview.PaginationScrollListener;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NavHostRiwayat extends InjectionFragment implements PaginationAdapterCallback {
  private NavHostRiwayatBinding binding;
  private LinearLayoutManager manager;
  private HistoryPagingAdapter adapter;
  private static final int PAGE_START = 1;
  private static final int TOTAL_PAGE = 10;
  private int currentPage = PAGE_START;
  private boolean isLoading = false;
  private boolean isLastPage = false;

  private OnHistoryLoadedListener historyLoadedListener;

  public interface OnHistoryLoadedListener {
    void onFirstCustomerNoLoaded(String nomor);
  }

  public void setOnHistoryLoadedListener(OnHistoryLoadedListener listener) {
    this.historyLoadedListener = listener;
  }

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = NavHostRiwayatBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initRecycler();
  }

  private void initRecycler() {
    binding.recyclerview.setDemoLayoutReference(R.layout.shimmer_riwayat);
    manager = new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false);
    binding.recyclerview.setLayoutManager(manager);
    binding.recyclerview.setItemAnimator(new DefaultItemAnimator());
    adapter = new HistoryPagingAdapter(getMainActivity());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnHistoryClickListener(
        item -> {
          String kategori = item.getKategori().toLowerCase();
          Intent intent = null;

          switch (kategori) {
            case "prabayar":
            case "pascabayar":
              intent = new Intent(requireActivity(), StatusLayoutCallback.class);
              intent.putExtra("ref_id", item.getRefId());
              intent.putExtra("tanggal", item.getTanggal());
              intent.putExtra("brand", item.getJenis());
              intent.putExtra("nomor", item.getKeterangan());
              intent.putExtra("harga", item.getJumlah());
              intent.putExtra("status", item.getStatus());
              break;
            case "deposit":
              intent = new Intent(requireActivity(), StatusTopup.class);
              intent.putExtra("ref_id", item.getRefId());
              intent.putExtra("brand", item.getJenis());
              break;
          }

          if (intent != null) getMainActivity().abStartActivity(intent);
        });

    // Load More Pagination
    binding.recyclerview.addOnScrollListener(
        new PaginationScrollListener(manager) {
          @Override
          protected void loadMoreItems() {
            isLoading = true;
            currentPage += 1;
            loadNextPage();
          }

          @Override
          public int getTotalPageCount() {
            return TOTAL_PAGE;
          }

          @Override
          public boolean isLastPage() {
            return isLastPage;
          }

          @Override
          public boolean isLoading() {
            return isLoading;
          }
        });

    binding.refresh.setOnRefreshListener(this::doRefresh);
    // binding.clude.retry.setOnClickListener(v -> doRefresh());

    loadFirstPage();
  }

  private void loadFirstPage() {
    currentPage = PAGE_START;
    RequestHistory request = new RequestHistory(requireActivity());
    request.setToken(getSession().getToken());
    request.setPageLimit(currentPage, 10);
    request.setKategori("semua");
    request.requestHistory(
        new RequestHistory.CallbackHistoryTransaksi() {
          @Override
          public void sendRequest() {
            binding.clude.getRoot().setVisibility(View.GONE);
            if (currentPage == 1) {
              binding.recyclerview.showShimmerAdapter();
            }
          }

          @Override
          public void onHistory(List<ResponseHistory.Data> data) {
            adapter.addAll(data);
            if (currentPage == PAGE_START && data.isEmpty()) {
              binding.clude.getRoot().setVisibility(View.VISIBLE);
			  binding.clude.infoEmpty.setText("Belum Ada Riwayat Transaksi");
            } else {
              binding.clude.getRoot().setVisibility(View.GONE);
            }

            if (currentPage == PAGE_START && !data.isEmpty() && historyLoadedListener != null) {
              historyLoadedListener.onFirstCustomerNoLoaded(data.get(0).getKeterangan());
            }

            int totalItemCount = adapter.getData().size();

            if (data.isEmpty() || currentPage >= TOTAL_PAGE) {
              isLastPage = true;
              adapter.setLastPage(true);
              adapter.removeLoadingFooter();

              // ➜ Tambahkan footer hanya jika item ≥ 10
              if (totalItemCount >= 10) {
                adapter.addLoadingFooter(); // ini akan memicu teks “paling jauh”
              }

            } else {
              isLastPage = false;
              adapter.setLastPage(false);
              adapter.addLoadingFooter();
            }

            binding.recyclerview.hideShimmerAdapter();
          }

          @Override
          public void onFailure(String error) {
            binding.recyclerview.hideShimmerAdapter();
            binding.clude.getRoot().setVisibility(View.VISIBLE);
			binding.clude.infoEmpty.setText(error);
          }
        });
  }

  private void loadNextPage() {
    RequestHistory request = new RequestHistory(requireActivity());
    request.setToken(getSession().getToken());
    request.setPageLimit(currentPage, 10);
    request.setKategori("semua");
    request.requestHistory(
        new RequestHistory.CallbackHistoryTransaksi() {
          @Override
          public void sendRequest() {}

          @Override
          public void onHistory(List<ResponseHistory.Data> data) {
            adapter.removeLoadingFooter();
            isLoading = false;

            adapter.addAll(data);

            int totalItemCount = adapter.getData().size();

            if (data.isEmpty() || currentPage >= TOTAL_PAGE) {
              isLastPage = true;
              adapter.setLastPage(true);
              adapter.removeLoadingFooter();

              if (totalItemCount >= 10) {
                adapter.addLoadingFooter(); // hanya tampilkan teks kalau item-nya cukup
              }

            } else {
              isLastPage = false;
              adapter.setLastPage(false);
              adapter.addLoadingFooter();
            }
          }

          @Override
          public void onFailure(String error) {}
        });
  }

  private void doRefresh() {
    currentPage = PAGE_START;
    isLastPage = false;
    isLoading = false;

    adapter.setLastPage(false);
    adapter.clear();
    loadFirstPage();
    binding.refresh.setRefreshing(false);
  }

  private void showErrorView() {}

  @Override
  public void onDataReload() {
    loadFirstPage();
  }

  @Override
  public void onResume() {
    super.onResume();
    
  }

  @Override
  public void onPause() {
    super.onPause();
    
  }

  @Override
  public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  public void onTopupSelesai(DashboardTopupEvent event) {
    doRefresh();
    EventBus.getDefault().removeStickyEvent(event);
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  public void onTransaksiSelesai(TransaksiSelesaiEvent event) {
    doRefresh();
    EventBus.getDefault().removeStickyEvent(event);
  }

  @Override
  public void retryPageLoad() {
    loadNextPage();
  }

  private final BroadcastReceiver notifReceiver =
      new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          String refId = intent.getStringExtra("ref_id");
          String status = intent.getStringExtra("status");

          if (refId != null && status != null) {
            adapter.updateStatus(refId, status);
          }
        }
      };
}
