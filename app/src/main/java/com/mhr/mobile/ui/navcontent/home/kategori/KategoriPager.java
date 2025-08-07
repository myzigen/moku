package com.mhr.mobile.ui.navcontent.home.kategori;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhr.mobile.adapter.QiosMenuAdapter;
import com.mhr.mobile.databinding.QiosKategoriPager1Binding;
import com.mhr.mobile.loader.KategoriLoader;
import com.mhr.mobile.model.MenuKategoriModel;
import com.mhr.mobile.ui.inject.InjectionFragment;
import com.mhr.mobile.ui.produk.ProdukNoInput;
import com.mhr.mobile.ui.produk.ProdukPLNPasca;
import com.mhr.mobile.ui.produk.ProdukType;
import com.mhr.mobile.ui.produk.pasca.PascaBpjs;
import com.mhr.mobile.ui.produk.pasca.PascaPLN;
import com.mhr.mobile.ui.produk.pasca.PascaWifi;
import com.mhr.mobile.ui.produk.pasca.ProdukPasca;
import com.mhr.mobile.ui.produk.prepaid.convert.ConvertPaypal;
import com.mhr.mobile.ui.produk.prepaid.kuota.KuotaPager;
import com.mhr.mobile.ui.produk.prepaid.pln.PLNTokenReload;
import com.mhr.mobile.ui.produk.prepaid.pulsa.PulsaReload;
import java.util.ArrayList;
import java.util.List;

public class KategoriPager extends InjectionFragment {
  private QiosKategoriPager1Binding binding;
  private QiosMenuAdapter adapter;
  private KategoriLoader kategoriLoader;
  private Handler handler;
  private List<MenuKategoriModel> mData = new ArrayList<>();
  private String kategori;

  public static KategoriPager newInstance(String kategori) {
    KategoriPager fragment = new KategoriPager();
    Bundle args = new Bundle();
    args.putString("kategori", kategori);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  protected View onCreateQiosFragment(LayoutInflater i, ViewGroup v, Bundle b) {
    getMainActivity().getWindow().setAllowReturnTransitionOverlap(true);
    binding = QiosKategoriPager1Binding.inflate(getLayoutInflater());

    kategori = getArguments().getString("kategori");
    kategoriLoader = new KategoriLoader(kategori);
    binding.recyclerview.setHasFixedSize(true);
    binding.recyclerview.addItemDecoration(getSpacingDecoration(4, 30, false));
    binding.recyclerview.setLayoutManager(getGridLayoutManager(4));

    // Inisialisasi Adapter
    adapter = new QiosMenuAdapter(requireActivity(), new ArrayList<>());
    binding.recyclerview.setAdapter(adapter);

    // Tambahkan listener klik pada item RecyclerView
    adapter.setOnItemClickListener(posisiKlik -> endToDetail(posisiKlik));

    dataIsReady();

    return binding.getRoot();
  }

  private void dataIsReady() {
    if (adapter != null && adapter.getItemCount() > 0) {
      return;
    }
    loadDataKategori();
  }

  private void loadDataKategori() {
    kategoriLoader.applyKategori(
        new KategoriLoader.OnKategoriCallback() {
          @Override
          public void onLoading() {
            binding.recyclerview.showShimmerAdapter(); // Menampilkan shimmer
          }

          @Override
          public void onDataLoaded(List<MenuKategoriModel> kategoriList) {
            mData = kategoriList;
            adapter.updateData(mData);
            binding.recyclerview.hideShimmerAdapter();
          }

          @Override
          public void onError(Exception e) {
            binding.recyclerview.hideShimmerAdapter();
            // Menangani kesalahan, misalnya menampilkan pesan error

          }
        });
  }

  private void endToDetail(int position) {

    String TYPEproduk = "type_produk";
    Intent intent = null;
    if ("kategori".equalsIgnoreCase(kategori)) {
      switch (position) {
        case 0:
          intent = new Intent(requireActivity(), PulsaReload.class);
          intent.putExtra("pulsa", "Pulsa");
          break;
        case 1:
          intent = new Intent(requireActivity(), KuotaPager.class);
          break;
        case 2:
          intent = new Intent(requireActivity(), PLNTokenReload.class);
          break;
        case 3:
          intent = new Intent(requireActivity(), ProdukType.class);
          setExtraOnly(intent, "E-Money", "Dompet Digital");
          break;
        case 4:
          intent = new Intent(requireActivity(), PascaWifi.class);
          intent.putExtra("kategori", "Internet Pascabayar");
          break;
        case 5:
          intent = new Intent(requireActivity(), ProdukType.class);
          intent.putExtra(TYPEproduk, "Masa Aktif");
          intent.putExtra("brand", "Masa Aktif Kartu");
          break;
        case 6:
          intent = new Intent(requireActivity(), ProdukType.class);
          intent.putExtra(TYPEproduk, "paket sms & telpon");
          intent.putExtra("brand", "Paket sms & Telpon");
          break;
        case 7:
          intent = new Intent(requireActivity(), ProdukType.class);
          intent.putExtra(TYPEproduk, "Games");
          intent.putExtra("brand", "Games");
          break;
      }
    } else if ("kategori2".equalsIgnoreCase(kategori)) {
      switch (position) {
        case 0:
          intent = new Intent(requireActivity(), PascaBpjs.class);
          intent.putExtra("kategori", "BPJS KESEHATAN");
          break;
        case 1:
          intent = new Intent(requireActivity(), ProdukNoInput.class);
          intent.putExtra("brand", "Pertamina Gas");
          intent.putExtra("kategori", "Gas");
          break;
        case 2:
          intent = new Intent(requireActivity(), PascaWifi.class);
          intent.putExtra("kategori", "Multifinance");
          break;
        case 3:
          intent = new Intent(requireActivity(), PascaWifi.class);
          intent.putExtra("kategori", "PDAM");
          break;
        case 4:
          intent = new Intent(requireActivity(), PascaWifi.class);
          intent.putExtra("kategori", "PBB");
          break;
        case 5:
          intent = new Intent(requireActivity(), PascaPLN.class);
          break;
        case 6:
          intent = new Intent(requireActivity(), PascaWifi.class);
          intent.putExtra("kategori", "TV PASCABAYAR");
          break;
        case 7:
          intent = new Intent(requireActivity(), ProdukPasca.class);
          intent.putExtra(TYPEproduk, ProdukPasca.ARG_GAS);
          intent.putExtra("brand", "GAS NEGARA");
          break;
      }
    } else if ("kategori3".equalsIgnoreCase(kategori)) {
      switch (position) {
        case 0:
          intent = new Intent(requireActivity(), ProdukType.class);
          intent.putExtra(TYPEproduk, "Aktivasi Perdana");
          intent.putExtra("brand", "Aktivasi Perdana");
          break;
        case 1:
          intent = new Intent(requireActivity(), ProdukType.class);
          intent.putExtra(TYPEproduk, "Aktivasi Voucher");
          intent.putExtra("brand", "Aktivasi Voucher");
          break;
        case 2:
          intent = new Intent(requireActivity(), ProdukType.class);
          intent.putExtra(TYPEproduk, "Voucher");
          intent.putExtra("filter_mode", "voucher_kuota");
          intent.putExtra("brand", "Voucher Kuota");
          break;
        case 3:
          intent = new Intent(requireActivity(), ProdukPasca.class);
          intent.putExtra(TYPEproduk, ProdukPasca.ARG_HP_PASCA);
          intent.putExtra("brand", "HP Pascabayar");
          break;
        case 4:
          intent = new Intent(requireActivity(), ProdukType.class);
          intent.putExtra(TYPEproduk, "Streaming");
          intent.putExtra("brand", "Paket Streaming");
          break;
        case 5:
          break;
        case 6:
          intent = new Intent(requireActivity(), ProdukType.class);
          intent.putExtra(TYPEproduk, "Voucher");
          intent.putExtra("brand", "Voucher");
          intent.putExtra("filter_mode", "voucher_belanja");
          break;
        case 7:
          intent = new Intent(requireActivity(), ProdukType.class);
          intent.putExtra(TYPEproduk, "E-SIM");
          intent.putExtra("brand", "e-SIM");
          break;
      }
    } else if ("kategori4".equalsIgnoreCase(kategori)) {
      switch (position) {
        case 0:
          intent = new Intent(requireActivity(), ProdukPLNPasca.class);
          break;
        case 1:
          intent = new Intent(requireActivity(), ConvertPaypal.class);
          break;
        case 2:
          intent = new Intent(requireActivity(), ProdukNoInput.class);
          intent.putExtra("brand", "Tv Prabayar");
          intent.putExtra("kategori", "TV");
          break;
        case 3:
          break;
        case 4:
          intent = new Intent(requireActivity(), ProdukType.class);
          intent.putExtra(TYPEproduk, "Malaysia Topup");
          intent.putExtra("brand", "Malaysia Topup");
          break;
        case 5:
          intent = new Intent(requireActivity(), ProdukType.class);
          intent.putExtra(TYPEproduk, "Pulsa");
          intent.putExtra("brand", "Pulsa");
          break;
        case 6:
          break;
        case 7:
          break;
      }
    }

    if (intent != null) getMainActivity().abStartActivity(intent);
  }

  @Override
  public void onDataReload() {
    dataIsReady(); // Reload data ketika dibutuhkan
  }

  private void goToActivity(Intent i, Class<?> targetActivity) {
    i = new Intent(requireActivity(), targetActivity);
    startActivity(i);
  }

  private void setExtraOnly(Intent intent, String extra, String brand) {
    String TYPEproduk = "type_produk";
    intent.putExtra(TYPEproduk, extra);
    intent.putExtra("brand", brand);
  }
}
