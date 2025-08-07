package com.mhr.mobile.ui.produk;

import android.app.Activity;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import com.mhr.mobile.R;
import com.mhr.mobile.api.request.RequestPricelist;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.ui.produk.adapter.ProdukAdapter;
import com.mhr.mobile.ui.produk.adapter.ProdukTypeAdapter;
import com.mhr.mobile.widget.recyclerview.SpacingItemDecoration;
import com.mikelau.shimmerrecyclerviewx.ShimmerRecyclerViewX;
import java.util.ArrayList;
import java.util.List;

public class ProdukRequest {

  private Activity activity;
  private ProdukTypeAdapter adapter;
  private ProdukAdapter produkAdapter;
  private String kategori;
  private View viewLoading;
  private ShimmerRecyclerViewX shimmerRecyclerViewX;

  public ProdukRequest(Activity activity) {
    this.activity = activity;
  }

  public static ProdukRequest with(Activity activity) {
    return new ProdukRequest(activity);
  }

  public ProdukRequest Kategori(String kategori) {
    this.kategori = kategori;
    return this;
  }

  public ProdukRequest Adapter(ProdukAdapter produkAdapter) {
    this.produkAdapter = produkAdapter;
    return this;
  }

  public ProdukRequest setAdapter(ProdukTypeAdapter adapter) {
    this.adapter = adapter;
    return this;
  }

  public ProdukRequest ViewLoading(View loading) {
    this.viewLoading = loading;
    return this;
  }

  public ProdukRequest ViewShimmer(ShimmerRecyclerViewX recyclerViewX) {
    this.shimmerRecyclerViewX = recyclerViewX;
    //this.shimmerRecyclerViewX.setLayoutManager(new GridLayoutManager(activity, 2));
    this.shimmerRecyclerViewX.addItemDecoration(new SpacingItemDecoration(2, 50, true));
    this.shimmerRecyclerViewX.setDemoLayoutReference(R.layout.shimmer_pricelist);
    this.shimmerRecyclerViewX.setAdapter(produkAdapter);
    return this;
  }

  public ProdukRequest RequestProdukBrand(String brand) {
    RequestPricelist request = new RequestPricelist(activity);
    request.setType("prabayar");
    request.setKategori(kategori);
    request.startExecute(
        new RequestPricelist.Callback() {
          @Override
          public void onRequest() {
            shimmerRecyclerViewX.showShimmerAdapter();
          }

          @Override
          public void onDataChange(List<ResponsePricelist> pricelist) {
            List<ResponsePricelist> filtered = new ArrayList<>();
            for (ResponsePricelist item : pricelist) {
              if (item.getBrand().equalsIgnoreCase(brand)) {
                filtered.add(item);
              }
            }
            produkAdapter.perbaruiData(filtered);
            shimmerRecyclerViewX.hideShimmerAdapter();
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
    return this;
  }

  public ProdukRequest RequestProduk() {
    RequestPricelist request = new RequestPricelist(activity);
    request.setType("prabayar");
    request.setKategori(kategori);
    request.startExecute(
        new RequestPricelist.Callback() {
          @Override
          public void onRequest() {
            viewLoading.setVisibility(View.VISIBLE);
          }

          @Override
          public void onDataChange(List<ResponsePricelist> pricelist) {
            adapter.updateProduk(pricelist);
            viewLoading.setVisibility(View.GONE);
          }

          @Override
          public void onFailure(String errorMessage) {}
        });
    return this;
  }
}
