package com.mhr.mobile.api.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AppPricelist.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
	public abstract AppPricelistDao pricelistDao();
}


/*
package com.mhr.mobile.api.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AppPricelist.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
	public abstract AppPricelistDao pricelistDao();
}

package com.mhr.mobile.api.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pricelist")
public class AppPricelist {
  @PrimaryKey
  @NonNull
  public String kode_produk;
  public String nama_produk;
  public int harga_produk;
  public String type;
  public String kategori;
}

package com.mhr.mobile.api.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AppPricelistDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(List<AppPricelist> pricelist);
	
	@Query("SELECT * FROM pricelist")
	LiveData<List<AppPricelist>> getAllPricelist();
}

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

package com.mhr.mobile.viewmodel;

import android.app.Activity;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.mhr.mobile.api.room.AppPricelist;
import java.util.List;

public class CacheViewModel extends AndroidViewModel {
  public boolean cacheProduk = false;
  public boolean cacheRiwayat = false;
  public boolean cacheLastNumber = false;
  public boolean cacheDownline = false;
  public boolean cachePelanggan = false;
  private CacheRepository repo;
  
  public CacheViewModel(Application application){
	  super(application);
	  repo = new CacheRepository(application);
  }
  
  public LiveData<List<AppPricelist>> getPricelist(){
	  return repo.getProdukOffline();
  }
  
  public void refreshProduk(Activity activity,String tipe,String kategori){
	  repo.fetchProdukOnline(activity,tipe,kategori);
  }
}


*/