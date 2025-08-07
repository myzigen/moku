package com.mhr.mobile.loader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mhr.mobile.model.MenuKategoriModel;
import java.util.ArrayList;
import java.util.List;

public class KategoriLoader {

  private DatabaseReference db;

  public KategoriLoader(String kategori) {
    db = FirebaseDatabase.getInstance().getReference(kategori);
  }

  public void applyKategori(OnKategoriCallback callback) {
    callback.onLoading();
	//db.keepSynced(true);
    db.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot snapshot) {
            List<MenuKategoriModel> kategoriList = new ArrayList<>();
            for (DataSnapshot data : snapshot.getChildren()) {
              MenuKategoriModel item = data.getValue(MenuKategoriModel.class);
              if (item != null) {
                kategoriList.add(item);
              }
            }
            callback.onDataLoaded(kategoriList);
          }

          @Override
          public void onCancelled(DatabaseError error) {
            callback.onError(error.toException());
          }
        });
  }

  public interface OnKategoriCallback {
    void onLoading();

    void onDataLoaded(List<MenuKategoriModel> kategoriList);

    void onError(Exception e);
  }
  
}
