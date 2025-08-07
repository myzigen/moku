package com.mhr.mobile.viewmodel;

import android.app.Activity;
import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import com.mhr.mobile.api.request.RequestPricelist;
import com.mhr.mobile.api.response.ResponsePricelist;
import com.mhr.mobile.api.room.AppDatabase;
import com.mhr.mobile.api.room.AppPricelist;
import com.mhr.mobile.api.room.AppPricelistDao;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class CacheRepository {
  private AppPricelistDao dao;
  private AppDatabase db;

  public CacheRepository(Application app) {
    db = Room.databaseBuilder(app, AppDatabase.class, "pricelist-db").build();
    dao = db.pricelistDao();
  }

  public LiveData<List<AppPricelist>> getProdukOffline() {
    return dao.getAllPricelist();
  }

  public void fetchProdukOnline(Activity activity, String tipe, String kategori) {
    new RequestPricelist(activity)
        .setType(tipe)
        .setKategori(kategori)
        .startExecute(
            new RequestPricelist.Callback() {
              @Override
              public void onRequest() {}

              @Override
              public void onDataChange(List<ResponsePricelist> pricelist) {
                // Simpan ke Room
                List<AppPricelist> lokal = new ArrayList<>();
                for (ResponsePricelist item : pricelist) {
                  AppPricelist e = new AppPricelist();
                  e.kode_produk = item.getBuyerSkuCode(); // ganti sesuai field asli
                  e.nama_produk = item.getProductName();
                  e.harga_produk = item.getHargaJual();
                  e.type = item.getType();
                  e.kategori = item.getCategory();
                  lokal.add(e);
                }

                Executors.newSingleThreadExecutor().execute(() -> dao.insertAll(lokal));
              }

              @Override
              public void onFailure(String errorMessage) {
                // Gagal online, tapi data Room tetap bisa tampil
              }
            });
  }
}
