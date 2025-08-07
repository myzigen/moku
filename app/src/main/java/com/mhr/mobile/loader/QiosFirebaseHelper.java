package com.mhr.mobile.loader;

import android.app.Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mhr.mobile.api.response.ResponseHistory;
import com.mhr.mobile.model.RiwayatTransaksi;
import com.mhr.mobile.util.QiosPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QiosFirebaseHelper {
  private static String TAG = QiosFirebaseHelper.class.getSimpleName();
  private DatabaseReference db;
  private String dbName;
  private ValueEventListener eventListener;
  private FirebaseAuth auth, authLogin;
  private FirebaseUser user;
  private String username;
  private Activity activity;
  private String kategori;
  private String nomor;
  private QiosPreferences preferences;

  public QiosFirebaseHelper(Activity activity) {
    this.activity = activity;
  }

  public static QiosFirebaseHelper with(Activity activity) {
    return new QiosFirebaseHelper(activity);
  }

  public QiosFirebaseHelper dbName(String dbName) {
    this.dbName = dbName;
    this.db = FirebaseDatabase.getInstance().getReference(dbName);
    return this;
  }

  public QiosFirebaseHelper dbKategori(String kategori) {
    this.kategori = kategori;
    return this;
  }

  public QiosFirebaseHelper dbNomor(String nomor) {
    this.nomor = nomor;
    return this;
  }
  /*
  public QiosFirebaseHelper SaveToHistory(ResponseHistory h) {
    String refId = h.getDigiflazz().getRefId();
    DatabaseReference d2 = db.child(kategori).child(nomor).child(refId);
    d2.setValue(h).addOnCompleteListener(task -> {});
    d2.child("timestamp").setValue(ServerValue.TIMESTAMP);

    return this;
  }

  public QiosFirebaseHelper SaveTripayHistory(ResponseHistory h) {
    String refId = h.getTripay().getRefId();
    DatabaseReference d2 = db.child(kategori).child(nomor).child(refId);
    d2.setValue(h).addOnCompleteListener(task -> {});
    d2.child("timestamp").setValue(ServerValue.TIMESTAMP);
    return this;
  }
  */

  public void getDataTransaksi(FirebaseCallback callback) {
    DatabaseReference dbKategori = db.child(kategori).child(nomor);

    dbKategori.keepSynced(true);
    callback.onStartRequest();
    dbKategori.addValueEventListener(
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot snapshot) {
            List<ResponseHistory> combineDataList = new ArrayList<>();
            for (DataSnapshot dataSnap : snapshot.getChildren()) {
              ResponseHistory riwayat = dataSnap.getValue(ResponseHistory.class);
              combineDataList.add(riwayat);
            }
            callback.onDataChanged(combineDataList);
          }

          @Override
          public void onCancelled(DatabaseError arg0) {}
        });
  }

  public void getLastTransaksi(String keywoard, LastTransaksiCallback callback) {
    DatabaseReference dbLast = db.child("prabayar");
    dbLast
        .orderByChild("timestamp")
        .limitToLast(10)
        .addListenerForSingleValueEvent(
            new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot snapshot) {
                List<RiwayatTransaksi> list = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                  RiwayatTransaksi last = dataSnapshot.getValue(RiwayatTransaksi.class);
                  if (last != null && last.getKategori() != null) {
                    list.add(last);
                  }
                }

                Collections.sort(list, (a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));

                for (RiwayatTransaksi transaksi : list) {
                  if (transaksi.getKategori().toLowerCase().contains(keywoard.toLowerCase())) {
                    callback.onReceive(transaksi.getCustomerId());
                    return;
                  }
                }
                callback.onReceive(null); // tidak ditemukan
              }

              @Override
              public void onCancelled(DatabaseError arg0) {
                callback.onReceive(null);
              }
            });
  }

  public void statusTransaksi(String refId, FirebaseRealtimeTransaksi realtimeTransaksi) {
    DatabaseReference dbStatus = db.child("prabayar").child(refId).child("status");

    eventListener =
        dbStatus.addValueEventListener(
            new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                  String statusTransaksi = snapshot.getValue(String.class);
                  realtimeTransaksi.onStatusRealtime(statusTransaksi);
                }
              }

              @Override
              public void onCancelled(DatabaseError arg0) {}
            });

    // db.addValueEventListener(eventListener);
  }

  public void removeValueEventListener() {
    if (db != null && eventListener != null) {
      db.removeEventListener(eventListener);
      db = null;
      eventListener = null;
    }
  }

  public interface FirebaseLoginCallback {
    public void onStartLogin();

    public void onDataLogin();

    public void onFailure(String error);
  }

  public interface FirebaseCallback {
    public void onStartRequest();

    public void onDataChanged(List<ResponseHistory> list);

    public void onUpdateStatus(String refId);
  }

  public interface LastTransaksiCallback {
    public void onReceive(String nomor);
  }

  public interface FirebaseRealtimeTransaksi {
    public void onStatusRealtime(String status);
  }
}
