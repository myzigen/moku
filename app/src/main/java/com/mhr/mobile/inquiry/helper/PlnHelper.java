package com.mhr.mobile.inquiry.helper;

import android.app.Activity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mhr.mobile.inquiry.request.InquiryPLNRequest;
import com.mhr.mobile.inquiry.response.InquiryPLNResponse;

public class PlnHelper {
  private DatabaseReference db;
  private Activity activity;

  public PlnHelper(Activity activity) {
    this.activity = activity;
    db = FirebaseDatabase.getInstance().getReference("check_username_pln");
  }

  public void checkCustomerName(String nometer, PlnCallback callback) {
    callback.onLoading();
    db.child(nometer)
        .get()
        .addOnCompleteListener(
            task -> {
              if (task.isSuccessful() && task.getResult().exists()) {
                InquiryPLNResponse.SaveData wifiData =
                    task.getResult().getValue(InquiryPLNResponse.SaveData.class);
                callback.onDataFound(wifiData);
              } else {
                fetchFromApiAndSaveToFirebase(nometer, callback);
              }
            });
  }

  public void fetchFromApiAndSaveToFirebase(String nometer, PlnCallback callback) {
    // callback.onLoading();
    InquiryPLNRequest pLNRequest = new InquiryPLNRequest(activity);
    pLNRequest.setUsername();
    pLNRequest.setApikey();
    pLNRequest.setNometer(nometer);
    pLNRequest.startInquiryPln(
        new InquiryPLNRequest.InquiryCallback() {
          @Override
          public void onStartLoading() {
            callback.onLoading();
          }

          @Override
          public void onResponse(InquiryPLNResponse response) {
            InquiryPLNResponse.Data data = response.getData();

            if (data != null && data.getTrName() != null && !data.getTrName().isEmpty()) {
              InquiryPLNResponse.SaveData saveData =
                  new InquiryPLNResponse.SaveData(data.getTrName(), data.getNometer(),data.getSegmentPower());
              callback.onDataFound(saveData);
              saveToFirebase(nometer, saveData, callback);
            } else {
              String message = data != null ? data.getMessage() : "Nama pengguna tidak ditemukan";
              callback.onError(message);
            }
          }

          @Override
          public void onFailure(String errorMessage) {
            callback.onError(errorMessage);
          }
        });
  }

  private void saveToFirebase(
      String nometer, InquiryPLNResponse.SaveData saveData, PlnCallback callback) {
    db.child(nometer)
        .setValue(saveData)
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
  public interface PlnCallback {
    void onLoading();

    void onDataFound(InquiryPLNResponse.SaveData data);

    void onDataSaved();

    void onError(String errorMessage);
  }
}
