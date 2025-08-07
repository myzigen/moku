package com.mhr.mobile.inquiry.helper;

import android.app.Activity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mhr.mobile.inquiry.request.InquiryRequest;
import com.mhr.mobile.inquiry.response.InquiryResponse;

public class WifiHelper {
  private DatabaseReference db;
  private Activity activity;

  public WifiHelper(Activity activity) {
    this.activity = activity;
    db = FirebaseDatabase.getInstance().getReference("check_username_wifi");
  }

  public void checkCustomerName(String noPelanggan, String codeProduk, WifiCallback callback) {
    callback.onLoading();
    db.child(noPelanggan)
        .get()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful() && task.getResult().exists()) {
                InquiryResponse.WifiData wifiData = task.getResult().getValue(InquiryResponse.WifiData.class);
                if (wifiData != null){
					callback.onDataFound(wifiData);
					fetchFromApiWithoutSaving(noPelanggan,codeProduk,callback);
				} else {
					fetchFromApiAndSaveToFirebase(noPelanggan,codeProduk,callback);
				}
              } else {
                fetchFromApiAndSaveToFirebase(noPelanggan, codeProduk, callback);
              }
            });
  }

  public void fetchFromApiAndSaveToFirebase(
      String noPelanggan, String codeProduk, WifiCallback callback) {
    InquiryRequest request = new InquiryRequest(activity);
    request.setUsername();
    request.setApiKey();
    request.setCodeProduk(codeProduk);
    request.setHp(noPelanggan);
    request.startInquiryRequest(
        new InquiryRequest.InquiryCallback() {
          @Override
          public void onStartLoading() {
            callback.onLoading();
          }

          @Override
          public void onResponse(InquiryResponse response) {
            InquiryResponse.Data data = response.getData();
            callback.onDataApi(data);
            if (data != null && data.getTrName() != null && !data.getTrName().isEmpty()) {
              InquiryResponse.WifiData wifiData =
                  new InquiryResponse.WifiData(data.getHp(), data.getTrName());
              callback.onDataFound(wifiData);
              saveToFirebase(noPelanggan, wifiData, callback);
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onError(errorMessage);
          }
        });
  }

  public void fetchFromApiWithoutSaving(
      String noPelanggan, String codeProduk, WifiCallback callback) {
    InquiryRequest request = new InquiryRequest(activity);
    request.setUsername();
    request.setApiKey();
    request.setCodeProduk(codeProduk);
    request.setHp(noPelanggan);
    request.startInquiryRequest(
        new InquiryRequest.InquiryCallback() {
          @Override
          public void onStartLoading() {
            callback.onLoading();
          }

          @Override
          public void onResponse(InquiryResponse response) {
            InquiryResponse.Data data = response.getData();
            if (data != null) {
              callback.onDataApi(data); // Kirim data lengkap
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onError(errorMessage);
          }
        });
  }

  private void saveToFirebase(
      String noPelanggan, InquiryResponse.WifiData wifiData, WifiCallback callback) {
    db.child(noPelanggan)
        .setValue(wifiData)
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful()) {
                callback.onDataSaved();
              } else {
                callback.onError("Gagal menyimpan data");
              }
            })
        .addOnFailureListener(e -> callback.onError("Error menyimpan data" + e.getMessage()));
  }

  // Interface callback untuk memberikan respon ke UI
  public interface WifiCallback {
    void onLoading();

    void onDataApi(InquiryResponse.Data data);

    void onDataFound(InquiryResponse.WifiData data);

    void onDataSaved();

    void onError(String errorMessage);
  }
}
