package com.mhr.mobile.ui.produk.sheet;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.mhr.mobile.adapter.DataInternetAdapter;
import com.mhr.mobile.adapter.filter.FilterDataAdapter;
import com.mhr.mobile.adapter.filter.FilterKuotaAdapter;
import com.mhr.mobile.api.request.RequestPricelist;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.databinding.SheetFilterKuotaBinding;
import com.mhr.mobile.ui.inject.InjectionSheetFragment;
import com.mhr.mobile.ui.produk.adapter.ProdukFilterAdapter;
import com.mhr.mobile.ui.produk.helper.HelperFilterKuota;
import com.mhr.mobile.util.AndroidViews;
import com.mhr.mobile.util.preferences.PrefPricelist;
import com.mhr.mobile.widget.helper.HelperUiRecycler;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SheetFilterKuota extends InjectionSheetFragment {
  private SheetFilterKuotaBinding binding;
  private RecyclerView recyclerview;
  private DataInternetAdapter adapter;
  private ProdukFilterAdapter masaAktifAdapter;
  private FilterKuotaAdapter kuotaAdapter;
  private String ARGBrand;
  private String filterByHarga = "";
  private String filterByMasaAktif = "";
  private String filterByKuota = "";
  private FilterApplyListener listener;

  public interface FilterApplyListener {
    void onFilterApply(List<ResponsePricelist> filteredList);
  }

  public void setOnFilterApplyListener(FilterApplyListener listener) {
    this.listener = listener;
  }

  @Override
  protected String getSheetTitle() {
    return "Filter Produk";
  }

  public static SheetFilterKuota newInstance(String code) {
    SheetFilterKuota fragment = new SheetFilterKuota();
    Bundle args = new Bundle();
    args.putString("brand", code);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected View onCreateSheetView(LayoutInflater i, ViewGroup v, Bundle b) {
    binding = SheetFilterKuotaBinding.inflate(getLayoutInflater());
    initialize();
    initUi();
    return binding.getRoot();
  }

  private void initialize() {
    ARGBrand = getArguments().getString("brand");
  }

  // * Hubungkan ini ke class ContentMenuData.java
  public void init(DataInternetAdapter adapter, RecyclerView recycler) {
    this.adapter = adapter;
    this.recyclerview = recycler;
  }

  private void initUi() {
    binding.btnHargaTerendah.setOnClickListener(v -> initHargaFilter("Terendah"));
    binding.btnHargaTertinggi.setOnClickListener(v -> initHargaFilter("Tertinggi"));
    binding.btnTerapkan.setOnClickListener(this::applyFilter);
    binding.btnReset.setOnClickListener(this::resetFilter);
    initRecyclerMasaAktif();
    initRecyclerKuota();
    initFilterMasaAktif();
  }

  private void initRecyclerMasaAktif() {
    masaAktifAdapter = new ProdukFilterAdapter(new ArrayList<>());
    HelperUiRecycler ui = HelperUiRecycler.with(getActivity());
    ui.HorizontalRecycler(binding.recyclerMasaAktif);
    ui.setAdapter(masaAktifAdapter);
    masaAktifAdapter.setOnFilterSelected(filter -> filterByMasaAktif = filter.getMasaAktif());
  }

  private void initRecyclerKuota() {
    List<String> stringKuota = new ArrayList<>();
    stringKuota.add("<1GB");
    stringKuota.add("1-10GB");
    stringKuota.add("10-50GB");
    stringKuota.add("50-100GB");
    stringKuota.add("100GB>");
    kuotaAdapter = new FilterKuotaAdapter(stringKuota);
    HelperUiRecycler ui = HelperUiRecycler.with(getActivity());
    ui.HorizontalRecycler(binding.recyclerKuota);
    ui.setAdapter(kuotaAdapter);
    kuotaAdapter.setOnFilterKuota(filter -> filterByKuota = filter);
  }

  private void initFilterMasaAktif() {
    if (PrefPricelist.isAvailable(requireContext())) {
      List<ResponsePricelist> cached = PrefPricelist.get(requireContext());
      if (cached != null) {
        filterAndSetAdapter(cached);
        return;
      }
    }

    RequestPricelist request = new RequestPricelist(requireActivity());
    request.setType("prabayar");
    request.setKategori("Data");
    request.startExecute(
        new RequestPricelist.Callback() {
          @Override
          public void onRequest() {}

          @Override
          public void onDataChange(List<ResponsePricelist> pricelist) {
            PrefPricelist.save(requireContext(), pricelist); // simpan cache
            filterAndSetAdapter(pricelist);
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
  }

  private void filterAndSetAdapter(List<ResponsePricelist> pricelist) {
    List<ResponsePricelist> filtered = new ArrayList<>();
    for (ResponsePricelist item : pricelist) {
      if (item.getBrand().equalsIgnoreCase(ARGBrand)) {
        filtered.add(item);
      }
    }
	masaAktifAdapter.notifyAdapterMasaAktif(filtered);
  }

  private void initHargaFilter(String filter) {
    filterByHarga = filter;
    int color = Color.BLACK;
    int defaultColor = Color.parseColor("#E9E9E9");
    if (filter.equals("Terendah")) {
      binding.btnHargaTerendah.setStrokeColor(ColorStateList.valueOf(color));
      binding.btnHargaTertinggi.setStrokeColor(ColorStateList.valueOf(defaultColor));
    } else {
      binding.btnHargaTerendah.setStrokeColor(ColorStateList.valueOf(defaultColor));
      binding.btnHargaTertinggi.setStrokeColor(ColorStateList.valueOf(color));
    }
  }

  private void applyFilter(View v) {
    List<ResponsePricelist> itemOriginal = new ArrayList<>(adapter.getOriginalData());
    List<ResponsePricelist> itemFilter = new ArrayList<>(itemOriginal);
    // * Jangan lakukan apapun jika tidak ada filter yg dipilih
    // * Munculkan Toast dan dismiss dialog
    if (noFilterSelected()) {
      AndroidViews.showToast("Tidak ada filter yang dipilih", requireActivity());
      dismiss();
      return;
    }

    // * Terapkan filter berdasarkan harga terendah atau tertinggi
    if (filterByHarga.equals("Terendah")) {
      itemFilter.sort(Comparator.comparingInt(ResponsePricelist::getHargaJual));
    } else if (filterByHarga.equals("Tertinggi")) {
      itemFilter.sort((p1, p2) -> Integer.compare(p2.getHargaJual(), p1.getHargaJual()));
    }

    // * Terapkan filter berdasarkan masa aktif
    if (!filterByMasaAktif.isEmpty()) {
      itemFilter =
          itemFilter.stream()
              .filter(produk -> produk.getMasaAktif().equalsIgnoreCase(filterByMasaAktif))
              .collect(Collectors.toList());
      itemFilter.sort(Comparator.comparingInt(ResponsePricelist::getHargaJual));
    }

    // Terapkan filter berdasarkan kuota
    if (!filterByKuota.isEmpty()) {
      itemFilter =
          itemFilter.stream()
              .filter(produk -> HelperFilterKuota.filterByKuota(produk, filterByKuota))
              .collect(Collectors.toList());
      itemFilter.sort(Comparator.comparingInt(ResponsePricelist::getHargaJual));
    }

    // * Kirimkan listener ke ContentMenuData.java
    adapter.resetSelectedPosition();
    masaAktifAdapter.notifyAdapterMasaAktif(itemFilter);
    recyclerview.scrollToPosition(0);
    if (listener != null) {
      listener.onFilterApply(new ArrayList<>(itemFilter)); // Pastikan salinan data
    }

    dismiss();
  }

  private void resetFilter(View v) {
    adapter.updateData(new ArrayList<>(adapter.getOriginalData()));
    adapter.sortProductListByPrice();
    adapter.resetSelectedPosition();
    recyclerview.scrollToPosition(0);
	dismiss();
  }

  private boolean noFilterSelected() {
    return filterByHarga.isEmpty() && filterByMasaAktif.isEmpty() && filterByKuota.isEmpty();
  }
}
