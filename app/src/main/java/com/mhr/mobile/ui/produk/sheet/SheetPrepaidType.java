package com.mhr.mobile.ui.produk.sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mhr.mobile.R;
import com.mhr.mobile.adapter.DataInternetAdapter;
import com.mhr.mobile.api.request.RequestPricelist;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.InjectionRecyclerviewBinding;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.util.FormatUtils;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import com.mikelau.shimmerrecyclerviewx.ShimmerRecyclerViewX;
import java.util.ArrayList;
import java.util.List;

public class SheetPrepaidType extends InjectionSheetFragment {
  private InjectionRecyclerviewBinding binding;
  private DataInternetAdapter adapter;
  private String kategori, brand;

  private onSelectedListener listener;

  public interface onSelectedListener {
    void onSelected(ResponsePricelist model);
  }

  public void setOnSelectedLisetener(onSelectedListener listener) {
    this.listener = listener;
  }

  public static SheetPrepaidType newInstance(String kategori, String brand) {
    SheetPrepaidType fragment = new SheetPrepaidType();
    Bundle args = new Bundle();
    args.putString("kategori", kategori);
    args.putString("brand", brand);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = InjectionRecyclerviewBinding.inflate(inflater, viewGroup, false);

    kategori = getArguments().getString("kategori");
    brand = getArguments().getString("brand");

    ui();
    return binding.getRoot();
  }

  private void ui() {
    initRecyclerview();
  }

  private void initRecyclerview() {
    binding.recyclerview.setDemoLayoutManager(ShimmerRecyclerViewX.LayoutMangerType.LINEAR_VERTICAL);
    binding.recyclerview.setDemoLayoutReference(R.layout.shimmer_pricelist);
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(3, 30, true));
    binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireActivity()));
    adapter = new DataInternetAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnDataClickListener(
        item -> {
          if (listener != null) {
            listener.onSelected(item);
            dismiss();
          }
        });

    loadPrepaidType();
  }

  private void loadPrepaidType() {
    RequestPricelist request = new RequestPricelist(requireActivity());
    request.setType("prabayar");
    request.setKategori(kategori);
    request.startExecute(
        new RequestPricelist.Callback() {
          @Override
          public void onRequest() {
            binding.recyclerview.showShimmerAdapter();
          }

          @Override
          public void onDataChange(List<ResponsePricelist> pricelist) {
            List<ResponsePricelist> filtered = new ArrayList<>();
            for (ResponsePricelist item : pricelist) {
              if (item.getBrand().equalsIgnoreCase(brand)) {
                filtered.add(item);
              }
            }
            adapter.setOriginalData(filtered);
            binding.recyclerview.hideShimmerAdapter();
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
  }

  @Override
  public void onStart() {
    super.onStart();
    View view = getView();
    if (view != null) {
      View parent = (View) view.getParent();
      BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(parent);

      // Tinggi toolbar biasanya 56dp
      int toolbarHeightDp = 25;
      float density = getResources().getDisplayMetrics().density;
      int toolbarHeightPx = (int) (toolbarHeightDp * density);

      // Tinggi maksimum = tinggi layar - tinggi toolbar
      int screenHeight = getResources().getDisplayMetrics().heightPixels;
      int maxHeight = screenHeight - toolbarHeightPx;

      // Set tinggi parent layout BottomSheet
      parent.getLayoutParams().height = maxHeight;
      parent.requestLayout();

      // Set agar tidak auto full expand ke atas
      behavior.setPeekHeight(maxHeight);
      behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
  }
}
