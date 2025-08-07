package com.mhr.mobile.ui.sheet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mhr.mobile.adapter.WilayahAdapter;
import com.mhr.mobile.api.request.RequestWilayah;
import com.mhr.mobile.databinding.SheetSearchBinding;
import com.mhr.mobile.model.Wilayah;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import java.util.ArrayList;
import java.util.List;

public class SheetWilayah extends InjectionSheetFragment implements TextWatcher {
  private SheetSearchBinding binding;
  private WilayahAdapter adapter;
  private OnNamaWilayahListener listener;

  public interface OnNamaWilayahListener {
    void oWilayah(String kec, String kab, String prov);
  }

  @Override
  protected String getSheetTitle() {
    return "Pilih kecamatan";
  }

  @Override
  protected View onCreateSheetView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
    binding = SheetSearchBinding.inflate(getLayoutInflater());
    recycler();
    return binding.getRoot();
  }

  private void recycler() {
    binding.editext.addTextChangedListener(this);
    binding.recyclerview.addItemDecoration(new SpacingItemDecoration(1, 30, true));
    binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireActivity()));
    adapter = new WilayahAdapter(new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);
    adapter.setOnSelectedListener(
        item -> {
          String kecamatan = item.nama;
          String kab = item.kabupaten;
          String provinsi = item.provinsi;

          listener.oWilayah(kecamatan, kab, provinsi);

          dismiss();
        });
  }

  public void setOnWilayahListener(OnNamaWilayahListener listener) {
    this.listener = listener;
  }

  private void cariWilayah(String key) {
    RequestWilayah wilayah = new RequestWilayah(requireActivity());
    wilayah.setKeywoard(key);
    wilayah.getWilayah(
        new RequestWilayah.Callback() {
          @Override
          public void onWilayah(List<Wilayah.Item> wilayahList) {
            adapter.setData(wilayahList);
          }

          @Override
          public void onError(String error) {}
        });
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    String search = s.toString().trim();
    if (search.isEmpty()) {
      binding.imageNoData.setVisibility(View.VISIBLE);
    } else if (search.length() >= 3) {
      binding.imageNoData.setVisibility(View.GONE);
      cariWilayah(s.toString());
    }
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
